package siri_lite.common;

import java.math.BigInteger;
import java.util.Locale;
import java.util.UUID;

import uk.org.siri.siri.AccessNotAllowedErrorStructure;
import uk.org.siri.siri.ErrorConditionStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.NaturalLanguageStringStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.ServiceDeliveryStructure;
import uk.org.siri.siri.ServiceNotAvailableErrorStructure;
import uk.org.siri.siri.SubscriptionQualifierStructure;
import uk.org.siri.wsdl.WsServiceRequestInfoStructure;

public class SiriStructureFactory {
	private static ObjectFactory factory = new ObjectFactory();

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

	public static final ServiceDeliveryErrorConditionStructure createServiceNotAvailableServiceDeliveryErrorConditionStructure(
			String text) {
		ServiceDeliveryErrorConditionStructure result = factory
				.createServiceDeliveryErrorConditionStructure();
		ServiceNotAvailableErrorStructure error = factory
				.createServiceNotAvailableErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setServiceNotAvailableError(error);
		return result;
	}

	public static final ServiceDeliveryErrorConditionStructure createAccessNotAllowedServiceDeliveryErrorConditionStructure(
			String text) {
		ServiceDeliveryErrorConditionStructure result = factory
				.createServiceDeliveryErrorConditionStructure();
		AccessNotAllowedErrorStructure error = factory
				.createAccessNotAllowedErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setAccessNotAllowedError(error);
		return result;
	}

	public static final ServiceDeliveryErrorConditionStructure createOtherServiceDeliveryErrorConditionStructure(
			String text) {
		ServiceDeliveryErrorConditionStructure result = factory
				.createServiceDeliveryErrorConditionStructure();
		OtherErrorStructure error = factory.createOtherErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setOtherError(error);
		return result;
	}

	public static final ErrorConditionStructure createServiceNotAvailableErrorConditionStructure(
			String text) {
		ErrorConditionStructure result = factory
				.createErrorConditionStructure();
		ServiceNotAvailableErrorStructure error = factory
				.createServiceNotAvailableErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setServiceNotAvailableError(error);
		return result;
	}

	public static final ErrorConditionStructure createAccessNotAllowedErrorConditionStructure(
			String text) {
		ErrorConditionStructure result = factory
				.createErrorConditionStructure();
		AccessNotAllowedErrorStructure error = factory
				.createAccessNotAllowedErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setAccessNotAllowedError(error);
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

	public static final ServiceDeliveryStructure getDefaultServiceDeliveryStructure(
			SiriException.ERROR_CODE code) {
		ServiceDeliveryStructure result = factory.createServiceDelivery();
		ServiceDeliveryStructure.ErrorCondition value = factory
				.createServiceDeliveryStructureErrorCondition();
		OtherErrorStructure error = factory.createOtherErrorStructure();
		error.setErrorText(code.name());
		error.setNumber(BigInteger.valueOf(0));
		value.setOtherError(error);
		result.setErrorCondition(value);
		return result;
	}

	public static SubscriptionQualifierStructure createSubscriptionQualifier(
			String value) {
		SubscriptionQualifierStructure result = factory
				.createSubscriptionQualifierStructure();
		result.setValue(value);
		return result;
	}

	public static MessageRefStructure createMessageRefStructure(String value) {
		MessageRefStructure result = factory.createMessageRefStructure();
		result.setValue(value);
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

	public static NaturalLanguageStringStructure createNaturalLanguageStringStructure(
			String text) {
		return createNaturalLanguageStringStructure(text, Locale.getDefault());
	}

	public static NaturalLanguageStringStructure createNaturalLanguageStringStructure(
			String text, Locale locale) {
		NaturalLanguageStringStructure result = factory
				.createNaturalLanguageStringStructure();
		result.setValue(text);
		// result.setLang(locale.getLanguage());
		return result;
	}

}
