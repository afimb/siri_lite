package siri_lite.test;

import java.io.File;
import java.math.BigInteger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;

import siri_lite.common.Color;
import siri_lite.common.XmlStructureFactory;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.StopPointsDeliveryStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.StopPointsDiscoveryError;

@Log4j
public abstract class AbstractUnit extends Arquillian {

	protected Endpoint endpoint;

	protected Unmarshaller unmarshaller;

	protected ObjectFactory factory = new ObjectFactory();

	protected static DatatypeFactory xmlFactory;

	static {
		try {
			xmlFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException ignored) {
		}
	}

	public AbstractUnit() {
		super();
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment() {
		final EnterpriseArchive result = ShrinkWrap.createFromZipFile(
				EnterpriseArchive.class, new File("../siri/target/siri_lite.ear"));
		return result;
	}

	@Getter
	protected StopPointsDiscoveryRequestStructure stopPointsDiscoveryRequest;

	protected void initialize() throws Exception {
//		log.info(Color.YELLOW + "[DSU] ---------------> TEST : "
//				+ this.getClass().getSimpleName() + Color.NORMAL);
		Object server = createServer();
		if (server != null) {

			Object implementor = createServer();
			String address = "http://localhost:20080/siri";

			endpoint = Endpoint.create(implementor);
			endpoint.publish(address);

		}
		unmarshaller = JAXBContext.newInstance(
				uk.org.siri.wsdl.ObjectFactory.class,
				uk.org.siri.siri.ObjectFactory.class).createUnmarshaller();
	}

	protected void dispose() throws Exception {
		if (endpoint != null) {
			endpoint.stop();
		}
	}

	protected Object createServer() {
		return new Server();
	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Server {

		@WebMethod(operationName = "StopPointsDiscovery", action = "StopPointsDiscovery")
		@RequestWrapper(localName = "StopPointsDiscovery", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryStructure")
		@ResponseWrapper(localName = "StopPointsDiscoveryResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryAnswerStructure")
		public void stopPointsDiscovery(
				@WebParam(name = "Request", targetNamespace = "") StopPointsDiscoveryRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<StopPointsDeliveryStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws StopPointsDiscoveryError {

			AbstractUnit.this.stopPointsDiscoveryRequest = request;

			answer.value = factory.createStopPointsDeliveryStructure();
			answerExtension.value = factory.createExtensionsStructure();
			answer.value.setResponseTimestamp(XmlStructureFactory
					.getTimestamp());
			answer.value.setVersion(request.getVersion());
			answer.value.setStatus(Boolean.FALSE);
			ServiceDeliveryErrorConditionStructure error = createOtherErrorStructure("");
			answer.value.setErrorCondition(error);
		}

		public ServiceDeliveryErrorConditionStructure createOtherErrorStructure(
				String text) {
			ServiceDeliveryErrorConditionStructure result = factory
					.createServiceDeliveryErrorConditionStructure();
			OtherErrorStructure error = factory.createOtherErrorStructure();
			error.setErrorText(text);
			error.setNumber(BigInteger.valueOf(0));
			result.setOtherError(error);

			return result;
		}
	}

}
