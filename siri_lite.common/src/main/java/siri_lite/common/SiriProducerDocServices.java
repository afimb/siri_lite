package siri_lite.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.w3c.dom.Document;

public class SiriProducerDocServices {

	private Marshaller marshaller;
	private WebTarget target;
	private Client client;

	public void invoke(Object jaxbElement,
			InvocationCallback<Response> handler, DefaultParameters parameters)
			throws ParserConfigurationException, JAXBException, SOAPException,
			IOException {

		// create soap message
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		marshaller.marshal(jaxbElement, document);
		SOAPMessage soap = MessageFactory.newInstance().createMessage();
		soap.getSOAPBody().addDocument(document);

		// invoke web service
		OutputStream buffer = new ByteArrayOutputStream();
		soap.writeTo(buffer);
		Entity<String> entity = Entity.entity(buffer.toString(),
				MediaType.TEXT_XML);
		Invocation invocation = target.request().buildPost(entity);
		invocation.submit(handler);
	}

	public void initialize() throws Exception {
		Configuration configuration = Configuration.getInstance();

		JAXBContext jaxbContext = SiriStructureFactory.getContext();
		marshaller = jaxbContext.createMarshaller();
		// client = ClientBuilder.newClient();
		// client.property("", 100);
		// client.property("", 10);
		ResteasyClientBuilder builder = new ResteasyClientBuilder();
		builder.establishConnectionTimeout(configuration.getTimeout(),
				TimeUnit.MILLISECONDS).socketTimeout(
				configuration.getTimeout(), TimeUnit.MILLISECONDS);

		if (configuration.getProxyHost() != null
				&& !configuration.getProxyHost().isEmpty()
				&& configuration.getProxyPort() != null
				&& configuration.getProxyPort() > 0) {
			builder.defaultProxy(configuration.getProxyHost(),
					configuration.getProxyPort());

			if (configuration.getProxyUser() != null
					&& configuration.getProxyPassword() != null) {
				//
				// context.put(BindingProvider.USERNAME_PROPERTY,
				// configuration.getProxyUser());
				// context.put(BindingProvider.PASSWORD_PROPERTY,
				// configuration.getProxyPassword());
			}
		}

		client = builder.build();
		target = client.target(configuration.getProducerAddress());
	}

	public void dispose() {
		if (client != null) {
			client.close();
			client = null;
		}
		target = null;
	}
}
