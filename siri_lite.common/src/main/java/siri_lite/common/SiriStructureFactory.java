package siri_lite.common;

import java.math.BigInteger;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import uk.org.siri.siri.ErrorConditionStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.wsdl.WsServiceRequestInfoStructure;

public class SiriStructureFactory {

	private static JAXBContext jaxbContext;

	private static ObjectFactory factory = new ObjectFactory();

	static {
		try {
			jaxbContext = JAXBContext.newInstance(
					uk.org.siri.siri.ObjectFactory.class,
					uk.org.siri.wsdl.siri.ObjectFactory.class,
					uk.org.siri.wsdl.ObjectFactory.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static JAXBContext getContext() {
		return jaxbContext;
	}

	public static final WsServiceRequestInfoStructure createWsServiceRequestInfoStructure(
			Configuration config) {
		WsServiceRequestInfoStructure result = new WsServiceRequestInfoStructure();
		ParticipantRefStructure requestorRef = SiriStructureFactory
				.createParticipantRef(config.getRequestorRef(), null);
		result.setRequestorRef(requestorRef);
		result.setMessageIdentifier(SiriStructureFactory
				.createMessageIdentifier());
		return result;
	}

	public static ErrorConditionStructure createOtherErrorConditionStructure(
			String text) {
		ErrorConditionStructure result = factory
				.createErrorConditionStructure();
		OtherErrorStructure error = factory.createOtherErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setOtherError(error);
		return result;
	}

	public static MessageQualifierStructure createMessageIdentifier() {
		return createMessageIdentifier(null);
	}

	public static MessageQualifierStructure createMessageIdentifier(String value) {
		MessageQualifierStructure result = factory
				.createMessageQualifierStructure();
		result.setValue((value != null) ? value : UUID.randomUUID().toString());
		return result;
	}

	public static ParticipantRefStructure createParticipantRef(String domain,
			String name) {
		ParticipantRefStructure result = factory
				.createParticipantRefStructure();
		if (name == null || name.isEmpty()) {
			result.setValue(domain);
		} else {
			result.setValue(domain + ":" + name);
		}
		return result;
	}

}
