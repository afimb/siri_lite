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
import javax.xml.bind.JAXBElement;
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
import siri_lite.general_message.GeneralMessageParameters;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.GeneralMessageDeliveriesStructure;
import uk.org.siri.siri.GeneralMessageDeliveryStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.InfoChannelRefStructure;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProducerResponseEndpointStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.wsdl.GeneralMessageError;
import uk.org.siri.wsdl.WsServiceRequestInfoStructure;
import uk.org.siri.wsdl.siri.IDFGeneralMessageRequestFilterStructure;

@Log4j
public class GetGeneralMessageTest extends Arquillian {

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

		log.info(Color.BLUE + "[DSU] execute test : "
				+ this.getClass().getSimpleName() + Color.NORMAL);
		try {
			initialize();
			// invoke service
			String URL = "http://localhost:8080/siri/2.0.0/general-message.xml";
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.MESSAGE_IDENTIFIER,
					"MESSAGE_IDENTIFIER"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.REQUESTOR_REF, "REQUESTORREF"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.ACCOUNT_ID, "ACCOUNTID"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.ACCOUNT_KEY, "ACCOUNTKEY"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.LANGUAGE, "fr"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.INFO_CHANNEL_REF, "Information"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.INFO_CHANNEL_REF, "Perturbation"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.LINE_REF, "LINEREF.1"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.LINE_REF, "LINEREF.2"));
			parameters.add(new BasicNameValuePair(
					GeneralMessageParameters.LINE_REF, "LINEREF.3"));

			String url = Utils.buildURL(URL, parameters);
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
			GeneralMessageRequestStructure request = (GeneralMessageRequestStructure) service
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
			Assert.assertEquals(
					serviceRequestInfo.getDelegatorRef().getValue(),
					"SIRI_LITE");
			Assert.assertEquals(serviceRequestInfo.getAccountId(), "ACCOUNTID");
			Assert.assertEquals(serviceRequestInfo.getAccountKey(),
					"ACCOUNTKEY");
			Assert.assertNotNull(request);
			Assert.assertEquals(request.getLanguage(), "fr");
			List<InfoChannelRefStructure> infoChannels = request
					.getInfoChannelRef();
			Assert.assertNotNull(infoChannels);
			Assert.assertTrue(infoChannels.size() == 2);
			Assert.assertEquals(infoChannels.get(0).getValue(), "Information");
			Assert.assertEquals(infoChannels.get(1).getValue(), "Perturbation");
			ExtensionsStructure extension = request.getExtensions();
			Assert.assertNotNull(extension);

			List<Object> list = extension.getAny();
			for (Object element : list) {
				if (element instanceof JAXBElement) {
					IDFGeneralMessageRequestFilterStructure filters = (IDFGeneralMessageRequestFilterStructure) ((JAXBElement) element)
							.getValue();
					List<LineRefStructure> lineRefs = filters.getLineRef();
					Assert.assertNotNull(infoChannels);
					Assert.assertTrue(lineRefs.size() == 3);
					Assert.assertEquals(lineRefs.get(0).getValue(), "LINEREF.1");
					Assert.assertEquals(lineRefs.get(1).getValue(), "LINEREF.2");
					Assert.assertEquals(lineRefs.get(2).getValue(), "LINEREF.3");
				}
			}

			// response test
			Assert.assertTrue(object instanceof Siri);
			Siri siri = (Siri) object;
			Assert.assertNotNull(siri.getServiceDelivery());
			Assert.assertNotNull(siri.getServiceDelivery()
					.getGeneralMessageDelivery());

		} finally {
			dispose();
		}

	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Service extends Server {

		public Service() {
			super();
		}

		@WebMethod(operationName = "GetGeneralMessage", action = "GetGeneralMessage")
		@RequestWrapper(localName = "GetGeneralMessage", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.GeneralMessageRequestStructure")
		@ResponseWrapper(localName = "GetGeneralMessageResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.GeneralMessageAnswerStructure")
		public void getGeneralMessage(
				@WebParam(name = "ServiceRequestInfo", targetNamespace = "") WsServiceRequestInfoStructure serviceRequestInfo,
				@WebParam(name = "Request", targetNamespace = "") GeneralMessageRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "ServiceDeliveryInfo", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ProducerResponseEndpointStructure> serviceDeliveryInfo,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<GeneralMessageDeliveriesStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws GeneralMessageError {

			setRequest(request);
			setServiceRequestInfo(serviceRequestInfo);

			ObjectFactory factory = Utils.getObjectFactory();

			serviceDeliveryInfo.value = factory
					.createProducerResponseEndpointStructure();
			answer.value = factory.createGeneralMessageDeliveriesStructure();
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

			GeneralMessageDeliveryStructure delivery = factory
					.createGeneralMessageDeliveryStructure();
			answer.value.getGeneralMessageDelivery().add(delivery);

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
