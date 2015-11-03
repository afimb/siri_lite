package siri_lite.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import lombok.extern.log4j.Log4j;

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
public class InvalidResponseTest extends Arquillian {

	private HttpServer server;

	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment() {
		final EnterpriseArchive result = ShrinkWrap.createFromZipFile(
				EnterpriseArchive.class, new File(
						"../siri/target/siri_lite.ear"));
		return result;
	}

	public void initialize() throws Exception {
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

	public void dispose() throws Exception {
		if (server != null) {
			server.stop();
			server.awaitTermination(5, TimeUnit.SECONDS);
		}
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
			Client client = ClientBuilder.newClient();
			client.register(LoginFilter.class);
			WebTarget target = client.target(url);
			Response response = target.request().get();
			response.close();

			// response test
			Assert.assertEquals(response.getStatus(), 500);
		} finally {
			dispose();
		}
	}
}
