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
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.WsStopPointsDiscoveryAnswerStructure;
import uk.org.siri.wsdl.WsStopPointsDiscoveryStructure;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
@Stateless
public class StopPointsDiscoveryService {

	private static ObjectFactory factory = new ObjectFactory();
	private static uk.org.siri.wsdl.ObjectFactory wsFactory = new uk.org.siri.wsdl.ObjectFactory();
	private static Configuration configuration = Configuration.getInstance();

	public void stopPointsDiscovery(MultivaluedMap<String, String> properties,
			AsyncResponse response) {
		Monitor monitor = MonitorFactory
				.start("DiscoveryService.stopPointsDiscovery()");
		SiriProducerDocServices service = null;
		try {

			// validate parameters
			StopPointsDiscoveryParameters parameters = new StopPointsDiscoveryParameters();
			parameters.configure(properties);
			parameters.validate();

			// create message
			WsStopPointsDiscoveryStructure discovery = wsFactory
					.createWsStopPointsDiscoveryStructure();
			StopPointsDiscoveryRequestStructure request = RequestStructureFactory
					.create(StopPointsDiscoveryRequestStructure.class,
							configuration, parameters);
			discovery.setRequest(request);
			ExtensionsStructure extension = factory.createExtensionsStructure();
			discovery.setRequestExtension(extension);
			JAXBElement<WsStopPointsDiscoveryStructure> jaxbElement = new JAXBElement<WsStopPointsDiscoveryStructure>(
					new QName("http://wsdl.siri.org.uk", "StopPointsDiscovery"),
					WsStopPointsDiscoveryStructure.class, discovery);

			// invoke web service
			service = SiriProducerDocServicesFactory.make();
			StopPointsDiscoveryHandler handler = new StopPointsDiscoveryHandler(
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

	class StopPointsDiscoveryHandler extends
			DefaultAsyncHandler<WsStopPointsDiscoveryAnswerStructure> {

		public StopPointsDiscoveryHandler(Configuration configuration,
				DefaultParameters parameters, AsyncResponse response) {
			super(configuration, parameters, response);
		}

		public void handleResponse(WsStopPointsDiscoveryAnswerStructure response) {

			// initialize siri stucture
			Siri siri = factory.createSiri();
			siri.setStopPointsDelivery(response.getAnswer());

			// resume
			resume(siri, configuration.getStopsDiscoveryMaxAge());
		}
	}

}
