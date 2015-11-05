package siri_lite.common;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.w3c.dom.Node;

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

	private Unmarshaller unmarshaller;

	@Setter
	private SiriProducerDocServices service;

	@SuppressWarnings("unchecked")
	public DefaultAsyncHandler(Configuration configuration,
			DefaultParameters parameters, AsyncResponse response)
			throws JAXBException {
		this.parameters = parameters;
		this.configuration = configuration;
		this.peer = response;
		type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		JAXBContext jaxbContext = SiriStructureFactory.getContext();
		unmarshaller = jaxbContext.createUnmarshaller();
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
			Object value = unmarshal(soapBody.getFirstChild());
			log.info(Color.CYAN + "[DSU] result : " + value + Color.NORMAL);
			SiriProducerDocServicesFactory.passivate(service);
			if (value != null && type.isInstance(value)) {
				try {
					handleResponse((T) value);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			SiriProducerDocServicesFactory.invalidate(service);

			if (e.getCause().getClass().getName()
					.equalsIgnoreCase("org.apache.cxf.binding.soap.SoapFault")) {
				payload = Response.status(Status.INTERNAL_SERVER_ERROR).build();
				peer.resume(payload);				
			} else {
				log.error(e.getMessage(), e.getCause());
				payload = Response.status(Status.SERVICE_UNAVAILABLE).build();
				peer.resume(payload);
			}
		}

		log.info(Color.YELLOW + "[DSU] " + monitor.stop() + Color.NORMAL);
	}

	protected Object unmarshal(Node node) throws JAXBException {
		Object object = unmarshaller.unmarshal(node);
		Object result = ((JAXBElement<?>) object).getValue();
		return result;
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