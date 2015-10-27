package siri_lite.common;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import uk.org.siri.siri.Siri;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
public abstract class DefaultAsyncHandler<T> implements
		AsyncHandler<SOAPMessage> {

	public static final String GZIP = "; gzip";

	protected AsyncResponse peer;
	protected DefaultParameters parameters;
	protected Configuration configuration;

	protected Class<T> type;

	@Setter
	private SiriProducerDocServicesWrapper service;

	@SuppressWarnings("unchecked")
	public DefaultAsyncHandler(Configuration configuration,
			DefaultParameters parameters, AsyncResponse response) {
		this.parameters = parameters;
		this.configuration = configuration;
		this.peer = response;
		type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleResponse(javax.xml.ws.Response<SOAPMessage> response) {

		Monitor monitor = MonitorFactory
				.start("DefaultAsyncHandler.handleResponse()");
		Response payload = null;
		try {
			SOAPMessage soapMessage = response.get();
			SOAPBody soapBody = soapMessage.getSOAPBody();
			Object value = service.unmarshal(soapBody.getFirstChild());
			log.info(Color.CYAN + "[DSU] result : " + value + Color.NORMAL);
			SiriProducerDocServicesFactory.getInstance().passivate(service);
			if (value != null && type.isInstance(value)) {
				try {
					handleResponse((T) value);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			if (e.getCause().getClass().getName()
					.equalsIgnoreCase("org.apache.cxf.binding.soap.SoapFault")) {
				log.error(e.getMessage(), e);
				payload = Response.status(Status.INTERNAL_SERVER_ERROR).build();
				peer.resume(payload);
				SiriProducerDocServicesFactory.getInstance().passivate(service);
			} else {
				log.error(e.getMessage(), e.getCause());
				payload = Response.status(Status.SERVICE_UNAVAILABLE).build();
				peer.resume(payload);
				SiriProducerDocServicesFactory.getInstance()
						.invalidate(service);
			}
		}

		log.info(Color.YELLOW + "[DSU] " + monitor.stop() + Color.NORMAL);
	}

	protected void resume(Siri siri, Integer maxAge) {
		Object value = siri;

		ResponseBuilder builder = Response.ok(value).type(
				parameters.getEncoding().withCharset(
						StandardCharsets.UTF_8.toString()));
		if (maxAge != 0) {
			CacheControl cache = new CacheControl();
			cache.setMaxAge(maxAge);
			builder.cacheControl(cache);
		}
		Response payload = builder.build();
		peer.resume(payload);
	}

	public abstract void handleResponse(T response);
}