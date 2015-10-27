package siri_lite.test;

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
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.apache.http.message.BasicNameValuePair;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.common.XmlStructureFactory;
import siri_lite.discovery.LinesDiscoveryParameters;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.LinesDeliveryStructure;
import uk.org.siri.siri.LinesDiscoveryRequestStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.wsdl.LinesDiscoveryError;

@NoArgsConstructor
public class LinesDiscoveryTest extends AbstractUnit {

	@Getter
	public LinesDiscoveryRequestStructure linesDiscoveryRequest;

	@Test
	@RunAsClient
	public void test() throws Exception {

		initialize();

		// invoke service
		String URL = "http://localhost:8080/siri/2.0.0/lines-discovery.xml";
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair(
				LinesDiscoveryParameters.REQUESTOR_REF, "REQUESTORREF"));
		parameters.add(new BasicNameValuePair(
				LinesDiscoveryParameters.ACCOUNT_ID, "ACCOUNTID"));
		parameters.add(new BasicNameValuePair(
				LinesDiscoveryParameters.ACCOUNT_KEY, "ACCOUNTKEY"));

		String url = Utils.buildURL(URL, parameters);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request().get();
		String value = response.readEntity(String.class);
		Object object = unmarshaller.unmarshal(new StringReader(value));
		response.close();

		// request test
		LinesDiscoveryRequestStructure request = getLinesDiscoveryRequest();
		Assert.assertNotNull(request);
		Assert.assertEquals(request.getRequestorRef().getValue(),
				"REQUESTORREF");
		Assert.assertEquals(request.getAccountId(), "ACCOUNTID");
		Assert.assertEquals(request.getAccountKey(), "ACCOUNTKEY");

		// response test
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertTrue(object instanceof Siri);
		Siri siri = (Siri) object;
		Assert.assertNotNull(siri.getLinesDelivery());

		dispose();

	}

	protected Object createServer() {
		return new Server();
	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Server {

		@WebMethod(operationName = "LinesDiscovery", action = "LinesDiscovery")
		@RequestWrapper(localName = "LinesDiscovery", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsLinesDiscoveryStructure")
		@ResponseWrapper(localName = "LinesDiscoveryResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsLinesDiscoveryAnswerStructure")
		public void linesDiscovery(
				@WebParam(name = "Request", targetNamespace = "") LinesDiscoveryRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<LinesDeliveryStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws LinesDiscoveryError {

			LinesDiscoveryTest.this.linesDiscoveryRequest = request;

			answer.value = factory.createLinesDeliveryStructure();
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
