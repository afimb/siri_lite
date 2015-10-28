package siri_lite.discovery;

import java.util.NoSuchElementException;

import javax.ejb.Stateless;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import lombok.extern.log4j.Log4j;
import siri_lite.common.Color;
import siri_lite.common.Configuration;
import siri_lite.common.DefaultAsyncHandler;
import siri_lite.common.DefaultParameters;
import siri_lite.common.RequestStructureFactory;
import siri_lite.common.SiriProducerDocServices;
import siri_lite.common.SiriProducerDocServicesFactory;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.LinesDiscoveryRequestStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.Siri;
import uk.org.siri.wsdl.WsLinesDiscoveryAnswerStructure;
import uk.org.siri.wsdl.WsLinesDiscoveryStructure;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
@Stateless
public class LinesDiscoveryService {

	private static ObjectFactory factory = new ObjectFactory();
	private static uk.org.siri.wsdl.ObjectFactory wsFactory = new uk.org.siri.wsdl.ObjectFactory();

	public void linesDiscovery(MultivaluedMap<String, String> properties,
			AsyncResponse response) {
		Monitor monitor = MonitorFactory
				.start("DiscoveryService.linesDiscovery()");
		SiriProducerDocServices service = null;
		try {

			Configuration configuration = Configuration.getInstance();

			// validate parameters
			LinesDiscoveryParameters parameters = new LinesDiscoveryParameters();
			parameters.configure(properties);
			parameters.validate();

			// create message
			WsLinesDiscoveryStructure discovery = wsFactory
					.createWsLinesDiscoveryStructure();
			LinesDiscoveryRequestStructure request = RequestStructureFactory
					.create(LinesDiscoveryRequestStructure.class,
							configuration, parameters);
			discovery.setRequest(request);
			ExtensionsStructure extension = factory.createExtensionsStructure();
			discovery.setRequestExtension(extension);
			JAXBElement<WsLinesDiscoveryStructure> jaxbElement = new JAXBElement<WsLinesDiscoveryStructure>(
					new QName("http://wsdl.siri.org.uk", "LinesDiscovery"),
					WsLinesDiscoveryStructure.class, discovery);

			// invoke web service
			service = SiriProducerDocServicesFactory.make();
			LinesDiscoveryHandler handler = new LinesDiscoveryHandler(
					configuration, parameters, response);
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
				SiriProducerDocServicesFactory.invalidate(service);
				service = null;
			}
		}

		log.info(Color.GREEN + "[DSU] " + monitor.stop() + Color.NORMAL);
	}

	class LinesDiscoveryHandler extends
			DefaultAsyncHandler<WsLinesDiscoveryAnswerStructure> {

		public LinesDiscoveryHandler(Configuration configuration,
				DefaultParameters parameters, AsyncResponse response) {
			super(configuration, parameters, response);
		}

		public void handleResponse(WsLinesDiscoveryAnswerStructure response) {

			// initialize siri stucture
			Siri siri = factory.createSiri();
			siri.setLinesDelivery(response.getAnswer());

			// resume
			resume(siri, configuration.getLinesDiscoveryMaxAge());
		}
	}
}
