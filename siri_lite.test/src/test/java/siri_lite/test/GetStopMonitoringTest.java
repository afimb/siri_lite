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
import javax.xml.datatype.DatatypeFactory;
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
import siri_lite.stop_monitoring.StopMonitoringParameters;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProducerResponseEndpointStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.StopMonitoringDeliveriesStructure;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.wsdl.StopMonitoringError;
import uk.org.siri.wsdl.WsServiceRequestInfoStructure;

@Log4j
public class GetStopMonitoringTest extends Arquillian {

	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment() {
		final EnterpriseArchive result = ShrinkWrap.createFromZipFile(
				EnterpriseArchive.class, new File(
						"../siri/target/siri_lite.ear"));
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
			String URL = "http://localhost:8080/siri/2.0.0/stop-monitoring.xml";
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.MESSAGE_IDENTIFIER,
					"MESSAGE_IDENTIFIER"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.REQUESTOR_REF, "REQUESTORREF"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.ACCOUNT_ID, "ACCOUNTID"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.ACCOUNT_KEY, "ACCOUNTKEY"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.START_TIME,
					"2015-09-23T17:43:47.715+02:00"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.PREVIEW_INTERVAL,
					"P0Y0M0DT0H12M0.000S"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.MONITORING_REF, "MONITORINGREF"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.OPERATOR_REF, "OPERATORREF"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.LINE_REF, "LINEREF"));
			parameters
					.add(new BasicNameValuePair(
							StopMonitoringParameters.DESTINATION_REF,
							"DESTINATIONREF"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.STOPVISITTYPE, "all"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.MAXIMUM_STOP_VISITS, "1"));
			parameters
					.add(new BasicNameValuePair(
							StopMonitoringParameters.MINIMUM_STOP_VISITS_PER_LINE,
							"2"));
			parameters.add(new BasicNameValuePair(
					StopMonitoringParameters.MINIMUM_STOP_VISITS_PER_LINE_VIA,
					"3"));
			parameters
					.add(new BasicNameValuePair(
							StopMonitoringParameters.MAXIMUMNUMBEROFCALLS_ONWARDS,
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
			StopMonitoringRequestStructure request = (StopMonitoringRequestStructure) service
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
			DatatypeFactory xmlFactory = Utils.getDatatypeFactory();
			Assert.assertEquals(request.getStartTime(), xmlFactory
					.newXMLGregorianCalendar("2015-09-23T17:43:47.715+02:00"));
			Assert.assertEquals(request.getPreviewInterval(),
					xmlFactory.newDuration("P0Y0M0DT0H12M0.000S"));
			Assert.assertEquals(request.getMonitoringRef().getValue(),
					"MONITORINGREF");
			Assert.assertEquals(request.getOperatorRef().getValue(),
					"OPERATORREF");
			Assert.assertEquals(request.getLineRef().getValue(), "LINEREF");
			Assert.assertEquals(request.getDestinationRef().getValue(),
					"DESTINATIONREF");
			Assert.assertEquals(request.getStopVisitTypes().value(), "all");
			Assert.assertTrue(request.getMaximumStopVisits().compareTo(
					BigInteger.valueOf(1)) == 0);
			Assert.assertTrue(request.getMinimumStopVisitsPerLine().compareTo(
					BigInteger.valueOf(2)) == 0);
			Assert.assertTrue(request.getMinimumStopVisitsPerLineVia()
					.compareTo(BigInteger.valueOf(3)) == 0);
			Assert.assertTrue(request.getMaximumNumberOfCalls().getOnwards()
					.compareTo(BigInteger.valueOf(4)) == 0);

			// response test
			Assert.assertTrue(object instanceof Siri);
			Siri siri = (Siri) object;
			Assert.assertNotNull(siri.getServiceDelivery());
			Assert.assertNotNull(siri.getServiceDelivery()
					.getStopMonitoringDelivery());
		} finally {
			dispose();
		}
	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Service extends Server {

		public Service() {
			super();
		}

		@WebMethod(operationName = "GetStopMonitoring", action = "GetStopMonitoring")
		@RequestWrapper(localName = "GetStopMonitoring", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.StopMonitoringRequestStructure")
		@ResponseWrapper(localName = "GetStopMonitoringResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.StopMonitoringAnswerStructure")
		public void getStopMonitoring(
				@WebParam(name = "ServiceRequestInfo", targetNamespace = "") WsServiceRequestInfoStructure serviceRequestInfo,
				@WebParam(name = "Request", targetNamespace = "") StopMonitoringRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "ServiceDeliveryInfo", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ProducerResponseEndpointStructure> serviceDeliveryInfo,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<StopMonitoringDeliveriesStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws StopMonitoringError {

			setRequest(request);
			setServiceRequestInfo(serviceRequestInfo);

			ObjectFactory factory = Utils.getObjectFactory();
			serviceDeliveryInfo.value = factory
					.createProducerResponseEndpointStructure();
			answer.value = factory.createStopMonitoringDeliveriesStructure();
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

			StopMonitoringDeliveryStructure delivery = factory
					.createStopMonitoringDeliveryStructure();
			answer.value.getStopMonitoringDelivery().add(delivery);

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
