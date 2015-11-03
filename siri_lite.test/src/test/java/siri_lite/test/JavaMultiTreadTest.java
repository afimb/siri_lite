package siri_lite.test;

import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
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
import siri_lite.discovery.StopPointsDiscoveryParameters;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.StopPointsDeliveryStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.StopPointsDiscoveryError;

@Log4j
public class JavaMultiTreadTest extends Arquillian {

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
			String URL = "http://localhost:8080/siri/2.0.0/stoppoints-discovery.xml";
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters
					.add(new BasicNameValuePair(
							StopPointsDiscoveryParameters.REQUESTOR_REF,
							"REQUESTORREF"));
			String url = Utils.buildURL(URL, parameters);
			Client c1 = ClientBuilder.newClient();
			c1.register(LoginFilter.class);
			Future<Response> r1 = c1.target(url + "&MessageIdentifier=1")
					.request().async().get();

			Utils.sleep(200);
			Client c2 = ClientBuilder.newClient();
			c2.register(LoginFilter.class);
			Future<Response> r2 = c2.target(url + "&MessageIdentifier=2")
					.request().async().get();
			while (!(r1.isDone() && r2.isDone())) {
				Utils.sleep(100);
			}

			XMLGregorianCalendar calendar1 = getResponseTimestamp(r1);
			log.info("[DSU] calendar1 " + calendar1);

			XMLGregorianCalendar calendar2 = getResponseTimestamp(r2);
			log.info("[DSU] calendar2 " + calendar2);

			// response test
			Assert.assertTrue((calendar1.compare(calendar2) > 0));
		} finally {
			dispose();
		}
	}

	private XMLGregorianCalendar getResponseTimestamp(Future<Response> future)
			throws Exception {

		Response response = future.get();
		String entity = response.readEntity(String.class);
		Unmarshaller unmarshaller = Utils.getJaxbContext().createUnmarshaller();
		Object object = unmarshaller.unmarshal(new StringReader(entity));
		response.close();

		return ((Siri) object).getStopPointsDelivery().getResponseTimestamp();

	}

	@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
	public class Service extends Server {

		public Service() {
			super();
		}

		@WebMethod(operationName = "StopPointsDiscovery", action = "StopPointsDiscovery")
		@RequestWrapper(localName = "StopPointsDiscovery", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryStructure")
		@ResponseWrapper(localName = "StopPointsDiscoveryResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryAnswerStructure")
		public void stopPointsDiscovery(
				@WebParam(name = "Request", targetNamespace = "") StopPointsDiscoveryRequestStructure request,
				@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
				@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<StopPointsDeliveryStructure> answer,
				@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
				throws StopPointsDiscoveryError {
			setRequest(request);

			int value = Integer.parseInt(request.getMessageIdentifier()
					.getValue());
			if (value % 2 == 1) {
				Utils.sleep(1500);
			}
			ObjectFactory factory = Utils.getObjectFactory();

			answer.value = factory.createStopPointsDeliveryStructure();
			answerExtension.value = factory.createExtensionsStructure();
			answer.value.setResponseTimestamp(XmlStructureFactory
					.getTimestamp());
			answer.value.setVersion(request.getVersion());
			answer.value.setStatus(Boolean.FALSE);

			ServiceDeliveryErrorConditionStructure error = createOtherErrorStructure(
					"MessageIdentifier = "
							+ request.getMessageIdentifier().getValue(), value);
			answer.value.setErrorCondition(error);
		}

		private ServiceDeliveryErrorConditionStructure createOtherErrorStructure(
				String text, Integer number) {

			ObjectFactory factory = Utils.getObjectFactory();

			ServiceDeliveryErrorConditionStructure result = factory
					.createServiceDeliveryErrorConditionStructure();
			OtherErrorStructure error = factory.createOtherErrorStructure();
			error.setErrorText(text);
			error.setNumber(BigInteger.valueOf(number));
			result.setOtherError(error);

			return result;
		}
	}
}
