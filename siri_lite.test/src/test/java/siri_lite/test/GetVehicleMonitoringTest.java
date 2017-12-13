package siri_lite.test;

import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import lombok.extern.log4j.Log4j;

import org.apache.http.message.BasicNameValuePair;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.common.Color;
import siri_lite.common.XmlStructureFactory;
import siri_lite.vehicle_monitoring.VehicleMonitoringParameters;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProducerResponseEndpointStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.VehicleMonitoringDeliveriesStructure;
import uk.org.siri.siri.VehicleMonitoringDeliveryStructure;
import uk.org.siri.siri.VehicleMonitoringRequestStructure;
import uk.org.siri.wsdl.VehicleMonitoringError;
import uk.org.siri.wsdl.WsServiceRequestInfoStructure;

@Log4j
public class GetVehicleMonitoringTest extends Arquillian {

	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment() {
		final EnterpriseArchive result = ShrinkWrap.createFromZipFile(
				EnterpriseArchive.class, new File(
						"../siri_lite.server/target/siri_lite.ear"));
		return result;
	}

	private Server service = null;

	public void initialize() throws Exception {
		service = new Service();
		service.initialize();
	}

	public void dispose() throws Exception {
		service.dispose();
	}

	@Test
	@RunAsClient
	public void test() throws Exception {

		log.info(Color.YELLOW + "[DSU] execute test : "
				+ this.getClass().getSimpleName() + Color.NORMAL);
		try {
			initialize();
			// invoke service
			String URL = "http://localhost:8080/siri/2.0.0/vehicle-monitoring.xml";
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair(
					VehicleMonitoringParameters.MESSAGE_IDENTIFIER,
					"MESSAGE_IDENTIFIER"));
			parameters.add(new BasicNameValuePair(
					VehicleMonitoringParameters.REQUESTOR_REF, "REQUESTORREF"));
			parameters.add(new BasicNameValuePair(
					VehicleMonitoringParameters.ACCOUNT_ID, "ACCOUNTID"));
			parameters.add(new BasicNameValuePair(
					VehicleMonitoringParameters.ACCOUNT_KEY, "ACCOUNTKEY"));
			parameters.add(new BasicNameValuePair(
					VehicleMonitoringParameters.LINE_REF, "LINEREF"));
			parameters.add(new BasicNameValuePair(
					VehicleMonitoringParameters.MAXIMUM_VEHICLES, "1"));
			parameters
					.add(new BasicNameValuePair(
							VehicleMonitoringParameters.MAXIMUMNUMBEROFCALLS_ONWARDS,
							"4"));

			String url = Utils.buildURL(URL, parameters) + "&debug";
			Client client = ClientBuilder.newClient();
			client.register(LoginFilter.class);
			WebTarget target = client.target(url);
			Response response = target.request().get();
			String value = response.readEntity(String.class);
			Unmarshaller unmarshaller = Utils.getJaxbContext()
					.createUnmarshaller();
			Object object = unmarshaller.unmarshal(new StringReader(value));
			response.close();
			Assert.assertEquals(response.getStatus(), 200);

			// request test
			VehicleMonitoringRequestStructure request = (VehicleMonitoringRequestStructure) service
					.getRequest();
			WsServiceRequestInfoStructure serviceRequestInfo = (WsServiceRequestInfoStructure) service
					.getServiceRequestInfo();
			Assert.assertNotNull(serviceRequestInfo);
			Assert.assertEquals(
					serviceRequestInfo.getDelegatorRef().getValue(),
					"SIRI_LITE");
			Assert.assertEquals(request.getVersion(), "2.0:FR-IDF-2.4");
			Assert.assertEquals(request.getMessageIdentifier().getValue(),
					"MESSAGE_IDENTIFIER");
			Assert.assertEquals(
					serviceRequestInfo.getRequestorRef().getValue(),
					"REQUESTORREF");
			Assert.assertEquals(serviceRequestInfo.getAccountId(), "ACCOUNTID");
			Assert.assertEquals(serviceRequestInfo.getAccountKey(),
					"ACCOUNTKEY");
			Assert.assertNotNull(request);
			Assert.assertEquals(request.getLineRef().getValue(), "LINEREF");
			Assert.assertTrue(request.getMaximumVehicles().compareTo(
					BigInteger.valueOf(1)) == 0);
			Assert.assertTrue(request.getMaximumNumberOfCalls().getOnwards()
					.compareTo(BigInteger.valueOf(4)) == 0);

			// response test
			Assert.assertTrue(object instanceof Siri);
			Siri siri = (Siri) object;
			Assert.assertNotNull(siri.getServiceDelivery());
			Assert.assertNotNull(siri.getServiceDelivery()
					.getVehicleMonitoringDelivery());
		} finally {
			dispose();
		}
	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Service extends Server {

		public Service() {
			super();
		}

		@WebMethod(operationName = "GetVehicleMonitoring", action = "GetVehicleMonitoring")
		@RequestWrapper(localName = "GetVehicleMonitoring", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.VehicleMonitoringRequestStructure")
		@ResponseWrapper(localName = "GetVehicleMonitoringResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.VehicleMonitoringAnswerStructure")
		public void getVehicleMonitoring(
				@WebParam(name = "ServiceRequestInfo", targetNamespace = "") WsServiceRequestInfoStructure serviceRequestInfo,
				@WebParam(name = "Request", targetNamespace = "") VehicleMonitoringRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "ServiceDeliveryInfo", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ProducerResponseEndpointStructure> serviceDeliveryInfo,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<VehicleMonitoringDeliveriesStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws VehicleMonitoringError {

			setRequest(request);
			setServiceRequestInfo(serviceRequestInfo);

			ObjectFactory factory = Utils.getObjectFactory();
			serviceDeliveryInfo.value = factory
					.createProducerResponseEndpointStructure();
			answer.value = factory.createVehicleMonitoringDeliveriesStructure();
			answerExtension.value = factory.createExtensionsStructure();

			serviceDeliveryInfo.value.setResponseTimestamp(XmlStructureFactory
					.getTimestamp());
			ParticipantRefStructure participantRef = factory
					.createParticipantRefStructure();
			participantRef.setValue("PARTICIPANTREF");
			serviceDeliveryInfo.value.setProducerRef(participantRef);
			serviceDeliveryInfo.value.setAddress("ADDRESS");
			MessageQualifierStructure messageQualifier = factory
					.createMessageQualifierStructure();
			messageQualifier.setValue("MESSAGEQUALIFIER");
			serviceDeliveryInfo.value
					.setResponseMessageIdentifier(messageQualifier);
			MessageRefStructure messageRef = factory
					.createMessageRefStructure();
			messageRef.setValue("MESSAGEREF");
			serviceDeliveryInfo.value.setRequestMessageRef(messageRef);

			VehicleMonitoringDeliveryStructure delivery = factory
					.createVehicleMonitoringDeliveryStructure();
			answer.value.getVehicleMonitoringDelivery().add(delivery);

			delivery.setResponseTimestamp(XmlStructureFactory.getTimestamp());
			delivery.setVersion(request.getVersion());
			delivery.setStatus(Boolean.FALSE);
			delivery.setVersion(request.getVersion());
			ServiceDeliveryErrorConditionStructure error = createOtherErrorStructure("");
			delivery.setErrorCondition(error);

		}

		private ServiceDeliveryErrorConditionStructure createOtherErrorStructure(
				String text) {
			ObjectFactory factory = Utils.getObjectFactory();

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
