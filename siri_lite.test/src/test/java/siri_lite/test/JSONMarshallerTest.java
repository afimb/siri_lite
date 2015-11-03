package siri_lite.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

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

@Log4j
public class JSONMarshallerTest extends Arquillian {

	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment() {
		final EnterpriseArchive result = ShrinkWrap.createFromZipFile(
				EnterpriseArchive.class, new File(
						"../siri/target/siri_lite.ear"));
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

		log.info(Color.BLUE + "[DSU] execute test : "
				+ this.getClass().getSimpleName() + Color.NORMAL);
		try {
			initialize();
			// invoke service
			String URL = "http://localhost:8080/siri/2.0.0/stoppoints-discovery";
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters
					.add(new BasicNameValuePair(
							StopPointsDiscoveryParameters.REQUESTOR_REF,
							"REQUESTORREF"));

			String url = Utils.buildURL(URL, parameters);
			Client client = ClientBuilder.newClient();
			client.register(LoginFilter.class);
			WebTarget target = client.target(url);
			Response response = target.request()
					.acceptEncoding("application/json").get();

			String value = response.readEntity(String.class);
			log.info("[DSU] entity : " + value);
			response.close();

			// response test
			Assert.assertEquals(response.getStatus(), 200);
			Assert.assertEquals(
					response.getHeaderString(HttpHeaders.CONTENT_TYPE),
					"application/json;charset=UTF-8");
		} finally {
			dispose();
		}
	}

}
