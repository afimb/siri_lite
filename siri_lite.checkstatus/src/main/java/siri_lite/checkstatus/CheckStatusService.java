package siri_lite.checkstatus;

import java.util.NoSuchElementException;

import javax.ejb.Stateless;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import lombok.extern.log4j.Log4j;
import siri_lite.common.Color;
import siri_lite.common.Configuration;
import siri_lite.common.DefaultAsyncHandler;
import siri_lite.common.DefaultParameters;
import siri_lite.common.RequestStructureFactory;
import siri_lite.common.SiriProducerDocServices;
import siri_lite.common.SiriProducerDocServicesFactory;
import uk.org.siri.siri.CheckStatusRequestStructure;
import uk.org.siri.siri.CheckStatusResponseBodyStructure;
import uk.org.siri.siri.CheckStatusResponseStructure;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.ProducerResponseEndpointStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.wsdl.WsCheckStatusResponseStructure;
import uk.org.siri.wsdl.WsCheckStatusStructure;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
@Stateless
public class CheckStatusService {

	private static ObjectFactory factory = new ObjectFactory();
	private static uk.org.siri.wsdl.ObjectFactory wsFactory = new uk.org.siri.wsdl.ObjectFactory();

	public void checkstatus(MultivaluedMap<String, String> properties,
			AsyncResponse response) {
		Monitor monitor = MonitorFactory
				.start("CheckStatusService.checkstatus()");
		SiriProducerDocServices service = null;
		try {

			Configuration configuration = Configuration.getInstance();

			// validate parameters
			CheckStatusParameters parameters = new CheckStatusParameters();
			parameters.configure(properties);
			parameters.validate();

			// create message
			WsCheckStatusStructure checkstatus = wsFactory
					.createWsCheckStatusStructure();
			CheckStatusRequestStructure request = RequestStructureFactory
					.create(CheckStatusRequestStructure.class, configuration,
							parameters);
			checkstatus.setRequest(request);
			ExtensionsStructure extension = factory.createExtensionsStructure();
			checkstatus.setRequestExtension(extension);
			JAXBElement<WsCheckStatusStructure> jaxbElement = new JAXBElement<WsCheckStatusStructure>(
					new QName("http://wsdl.siri.org.uk", "CheckStatus"),
					WsCheckStatusStructure.class, checkstatus);

			// invoke web service
			service = SiriProducerDocServicesFactory.make();
			CheckStatusHandler handler = new CheckStatusHandler(configuration,
					parameters, response);
			handler.setService(service);
			service.invoke(jaxbElement, handler, parameters);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage(), e);
			Response payload = Response.status(Status.SERVICE_UNAVAILABLE)
					.build();
			response.resume(payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Response payload = Response.status(Status.BAD_REQUEST).build();
			response.resume(payload);
			if (service != null) {
				SiriProducerDocServicesFactory.passivate(service);
				service = null;
			}
		}

		log.info(Color.GREEN + "[DSU] " + monitor.stop() + Color.NORMAL);
	}

	class CheckStatusHandler extends
			DefaultAsyncHandler<WsCheckStatusResponseStructure> {

		public CheckStatusHandler(Configuration configuration,
				DefaultParameters parameters, AsyncResponse response)
				throws JAXBException {
			super(configuration, parameters, response);
		}

		public void handleResponse(WsCheckStatusResponseStructure response) {

			// initialize siri stucture
			Siri siri = factory.createSiri();
			CheckStatusResponseStructure value = factory
					.createCheckStatusResponseStructure();
			siri.setCheckStatusResponse(value);

			ProducerResponseEndpointStructure info = response
					.getCheckStatusAnswerInfo();
			value.setResponseTimestamp(info.getResponseTimestamp());
			value.setProducerRef(info.getProducerRef());
			value.setAddress(info.getAddress());
			value.setResponseMessageIdentifier(info
					.getResponseMessageIdentifier());
			value.setRequestMessageRef(info.getRequestMessageRef());

			CheckStatusResponseBodyStructure answer = response.getAnswer();
			value.setStatus(answer.isStatus());
			value.setErrorCondition(answer.getErrorCondition());
			value.setServiceStartedTime(answer.getServiceStartedTime());

			// resume
			resume(siri, 0);
		}
	}
}
