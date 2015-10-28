package siri_lite.common;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;

import lombok.extern.log4j.Log4j;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import uk.org.siri.siri.ObjectFactory;

@Log4j
public class SiriProducerDocServices extends Service {

	public static final String HTTP_PROXY_PORT = "http.proxyPort";
	public static final String HTTP_PROXY_HOST = "http.proxyHost";
	public static final String CONNECTION_TIMEOUT = "javax.xml.ws.client.connectionTimeout";
	public static final String RECEIVE_TIMEOUT = "javax.xml.ws.client.receiveTimeout";
	public static final URL wsdlLocation = ObjectFactory.class.getClassLoader()
			.getResource("wsdl/siri_wsProducer-Document.wsdl");
	public static final QName serviceQName = new QName(
			"http://wsdl.siri.org.uk", "SiriProducerDocServices");
	public static final QName portQName = new QName("http://wsdl.siri.org.uk",
			"SiriWSPort");

	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	private Dispatch<SOAPMessage> dispatch;

	public SiriProducerDocServices() throws JAXBException {
		super(wsdlLocation, serviceQName);
		initialize();
	}

	public void invoke(Object jaxbElement, AsyncHandler<SOAPMessage> handler,
			DefaultParameters parameters) throws ParserConfigurationException,
			JAXBException, SOAPException {

		// create soap message
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		marshaller.marshal(jaxbElement, document);
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		soapMessage.getSOAPBody().addDocument(document);

		// invoke web service
		try {
			Binding binding = dispatch.getBinding();
			List<Handler> list = binding.getHandlerChain();
			if (parameters.getDebug()) {
				list.add(new LoggingHandler());
				binding.setHandlerChain(list);
			} else {
				list.clear();
				binding.setHandlerChain(list);
			}
			Future<?> result = dispatch.invokeAsync(soapMessage, handler);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void initialize() throws JAXBException {

		Configuration configuration = Configuration.getInstance();
		JAXBContext jaxbContext = SiriStructureFactory.getContext();
		marshaller = jaxbContext.createMarshaller();
		unmarshaller = jaxbContext.createUnmarshaller();

		dispatch = createDispatch(portQName, SOAPMessage.class,
				Service.Mode.MESSAGE);
		Map<String, Object> context = dispatch.getRequestContext();
		context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				configuration.getProducerAddress());
		context.put(CONNECTION_TIMEOUT, configuration.getTimeout().toString());
		context.put(RECEIVE_TIMEOUT, configuration.getTimeout().toString());
		context.put("org.jboss.ws.timeout", configuration.getTimeout()
				.toString());

		if (configuration.getProxyHost() != null
				&& !configuration.getProxyHost().isEmpty()
				&& configuration.getProxyPort() != null
				&& configuration.getProxyPort() > 0) {
			context.put(HTTP_PROXY_HOST, configuration.getProxyHost());
			context.put(HTTP_PROXY_PORT, configuration.getProxyPort());
			if (configuration.getProxyUser() != null
					&& configuration.getProxyPassword() != null) {
				context.put(BindingProvider.USERNAME_PROPERTY,
						configuration.getProxyUser());
				context.put(BindingProvider.PASSWORD_PROPERTY,
						configuration.getProxyPassword());
			}
		}

	}

	public Object unmarshal(Node node) throws JAXBException {
		Object object = unmarshaller.unmarshal(node);
		Object result = ((JAXBElement<?>) object).getValue();
		return result;
	}
}
