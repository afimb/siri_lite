package siri_lite.test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import lombok.NoArgsConstructor;

import org.apache.http.message.BasicNameValuePair;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.discovery.StopPointsDiscoveryParameters;
import uk.org.siri.siri.Siri;

@NoArgsConstructor
public class GZipTest extends AbstractUnit {

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
		Builder builder = target.request();
		builder.header(HttpHeaders.ACCEPT_ENCODING, "gzip");
		Response response = builder.get();
		String value = response.readEntity(String.class);
		Object object = unmarshaller.unmarshal(new StringReader(value));
		response.close();

		// response test
		Assert.assertEquals(
				response.getHeaderString(HttpHeaders.CONTENT_ENCODING), "gzip");
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertTrue(object instanceof Siri);
		Siri siri = (Siri) object;
		Assert.assertNotNull(siri.getStopPointsDelivery());

		dispose();
	}

}
