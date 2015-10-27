package siri_lite.test;

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

import lombok.NoArgsConstructor;

import org.apache.http.message.BasicNameValuePair;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.common.SiriException;
import siri_lite.common.SiriStructureFactory;
import siri_lite.discovery.StopPointsDiscoveryParameters;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.StopPointsDeliveryStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.StopPointsDiscoveryError;

@NoArgsConstructor
public class SoapFaultTest extends AbstractUnit {

	@Test
	@RunAsClient
	public void test() throws Exception {

		initialize();

		// invoke service
		String URL = "http://localhost:8080/siri/2.0.0/stoppoints-discovery.xml";
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair(
				StopPointsDiscoveryParameters.REQUESTOR_REF, "REQUESTORREF"));

		String url = Utils.buildURL(URL, parameters);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request().get();
		response.close();

		// response test
		Assert.assertEquals(response.getStatus(), 500);

		dispose();
	}

	@Override
	protected Object createServer() {
		return new Server();
	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Server {

		public final String SERVICE_NOT_AVAILABLE = SiriException.ERROR_CODE.SERVICE_NOT_AVAILABLE_ERROR
				.name();

		@WebMethod(operationName = "StopPointsDiscovery", action = "StopPointsDiscovery")
		@RequestWrapper(localName = "StopPointsDiscovery", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryStructure")
		@ResponseWrapper(localName = "StopPointsDiscoveryResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryAnswerStructure")
		public void stopPointsDiscovery(
				@WebParam(name = "Request", targetNamespace = "") StopPointsDiscoveryRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<StopPointsDeliveryStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws StopPointsDiscoveryError {

			throw new StopPointsDiscoveryError(
					SERVICE_NOT_AVAILABLE,
					SiriStructureFactory
							.createServiceNotAvailableErrorConditionStructure(SERVICE_NOT_AVAILABLE));

		}
	}

}
