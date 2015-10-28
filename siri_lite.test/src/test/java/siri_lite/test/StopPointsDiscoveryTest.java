package siri_lite.test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.Unmarshaller;

import lombok.NoArgsConstructor;

import org.apache.http.message.BasicNameValuePair;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.discovery.StopPointsDiscoveryParameters;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;

@NoArgsConstructor
public class StopPointsDiscoveryTest extends AbstractUnit {

	@Test
	@RunAsClient
	public void test() throws Exception {

		initialize();

		// invoke service
		String URL = "http://localhost:8080/siri/2.0.0/stoppoints-discovery.xml";
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair(
				StopPointsDiscoveryParameters.REQUESTOR_REF, "REQUESTORREF"));

		parameters.add(new BasicNameValuePair(
				StopPointsDiscoveryParameters.ACCOUNT_ID, "ACCOUNTID"));
		parameters.add(new BasicNameValuePair(
				StopPointsDiscoveryParameters.ACCOUNT_KEY, "ACCOUNTKEY"));
		parameters
				.add(new BasicNameValuePair(
						StopPointsDiscoveryParameters.BOUNDINGBOXSTRUCTURE_UPPERLEFT_LONGITUDE,
						"1.0"));
		parameters
				.add(new BasicNameValuePair(
						StopPointsDiscoveryParameters.BOUNDINGBOXSTRUCTURE_UPPERLEFT_LATITUDE,
						"2.0"));
		parameters
				.add(new BasicNameValuePair(
						StopPointsDiscoveryParameters.BOUNDINGBOXSTRUCTURE_LOWERRIGHT_LONGITUDE,
						"3.0"));
		parameters
				.add(new BasicNameValuePair(
						StopPointsDiscoveryParameters.BOUNDINGBOXSTRUCTURE_LOWERRIGHT_LATITUDE,
						"4.0"));
		parameters.add(new BasicNameValuePair(
				StopPointsDiscoveryParameters.STOPPOINTSDETAILLEVEL, "full"));

		String url = Utils.buildURL(URL, parameters);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request().get();
		String value = response.readEntity(String.class);
		Unmarshaller unmarshaller = getContext().createUnmarshaller();
		Object object = unmarshaller.unmarshal(new StringReader(value));
		response.close();

		// request test
		StopPointsDiscoveryRequestStructure request = getStopPointsDiscoveryRequest();
		Assert.assertNotNull(request);
		Assert.assertEquals(request.getRequestorRef().getValue(),
				"REQUESTORREF");
		Assert.assertEquals(request.getAccountId(), "ACCOUNTID");
		Assert.assertEquals(request.getAccountKey(), "ACCOUNTKEY");
		Assert.assertTrue(request.getBoundingBox().getUpperLeft()
				.getLongitude().compareTo(new BigDecimal(1.0)) == 0);
		Assert.assertTrue(request.getBoundingBox().getUpperLeft().getLatitude()
				.compareTo(new BigDecimal(2.0)) == 0);
		Assert.assertTrue(request.getBoundingBox().getLowerRight()
				.getLongitude().compareTo(new BigDecimal(3.0)) == 0);
		Assert.assertTrue(request.getBoundingBox().getLowerRight()
				.getLatitude().compareTo(new BigDecimal(4.0)) == 0);
		Assert.assertEquals(request.getStopPointsDetailLevel().value(), "full");

		// response test
		Assert.assertEquals(response.getStatus(), 200);
		Assert.assertTrue(object instanceof Siri);
		Siri siri = (Siri) object;
		Assert.assertNotNull(siri.getStopPointsDelivery());

		dispose();

	}

}
