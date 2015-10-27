package siri_lite.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import lombok.NoArgsConstructor;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import siri_lite.discovery.StopPointsDiscoveryParameters;

@NoArgsConstructor
public class InvalidResponseTest extends AbstractUnit {

	private HttpServer server;

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
	protected void initialize() throws Exception {
		server = ServerBootstrap.bootstrap().setListenerPort(20080)
				.registerHandler("*", new HttpRequestHandler() {

					public void handle(HttpRequest request,
							HttpResponse response, HttpContext context)
							throws HttpException, IOException {
						response.setStatusCode(HttpStatus.SC_OK);
						response.setEntity(new StringEntity(
								"some important message",
								ContentType.TEXT_PLAIN));
					}

				}).create();
		server.start();
	}

	@Override
	protected void dispose() throws Exception {
		if (server != null) {
			server.shutdown(5, TimeUnit.SECONDS);
		}
	}

}
