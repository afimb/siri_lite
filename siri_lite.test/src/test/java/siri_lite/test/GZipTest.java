package siri_lite.test;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.bind.Unmarshaller;

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
import siri_lite.discovery.StopPointsDiscoveryParameters;
import uk.org.siri.siri.Siri;

@Log4j
public class GZipTest extends Arquillian {

	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment() {
		final EnterpriseArchive result = ShrinkWrap.createFromZipFile(
				EnterpriseArchive.class, new File(
						"../siri_lite.server/target/siri_lite.ear"));
		return result;
	}

	private Server service = null;

	public void initialize() throws Exception {
		service = new DefaultService();
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
			String URL = "http://localhost:8080/siri/2.0.0/stoppoints-discovery.xml";
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters
					.add(new BasicNameValuePair(
							StopPointsDiscoveryParameters.REQUESTOR_REF,
							"REQUESTORREF"));

			String url = Utils.buildURL(URL, parameters);
			Client client = ClientBuilder.newClient();
			client.register(LoginFilter.class);
			WebTarget target = client.target(url);
			Builder builder = target.request();
			builder.header("compress", "true");
			Response response = builder.get();
			String value = response.readEntity(String.class);
			Unmarshaller unmarshaller = Utils.getJaxbContext()
					.createUnmarshaller();
			Object object = unmarshaller.unmarshal(new StringReader(value));
			response.close();

			// response test
			Assert.assertEquals(
					response.getHeaderString(HttpHeaders.CONTENT_ENCODING),
					"gzip");
			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertTrue(object instanceof Siri);
			Siri siri = (Siri) object;
			Assert.assertNotNull(siri.getStopPointsDelivery());
		} finally {
			dispose();
		}
	}
}
