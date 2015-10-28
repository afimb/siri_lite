package siri_lite.test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.apache.http.message.BasicNameValuePair;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.discovery.StopPointsDiscoveryParameters;

@Log4j
@NoArgsConstructor
public class JSONMarshallerTest extends AbstractUnit {

	@Test
	@RunAsClient
	public void test() throws Exception {

		initialize();

		// invoke service
		String URL = "http://localhost:8080/siri/2.0.0/stoppoints-discovery";
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair(
				StopPointsDiscoveryParameters.REQUESTOR_REF, "REQUESTORREF"));

		String url = Utils.buildURL(URL, parameters);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request().acceptEncoding("application/json")
				.get();

		String value = response.readEntity(String.class);
		log.info("[DSU] entity : " + value);
		response.close();

		// response test
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertEquals(response.getHeaderString(HttpHeaders.CONTENT_TYPE),
				"application/json;charset=UTF-8");

		dispose();
	}

}
