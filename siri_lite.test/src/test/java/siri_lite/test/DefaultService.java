package siri_lite.test;

import java.math.BigInteger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import siri_lite.common.XmlStructureFactory;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.ObjectFactory;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.StopPointsDeliveryStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.StopPointsDiscoveryError;

@WebService(name = "SiriWS", targetNamespace = "http://wsdl.siri.org.uk")
public class DefaultService extends Server {

	public DefaultService() {
		super();
	}

	@WebMethod(operationName = "StopPointsDiscovery", action = "StopPointsDiscovery")
	@RequestWrapper(localName = "StopPointsDiscovery", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryStructure")
	@ResponseWrapper(localName = "StopPointsDiscoveryResponse", targetNamespace = "http://wsdl.siri.org.uk", className = "uk.org.siri.wsdl.WsStopPointsDiscoveryAnswerStructure")
	public void stopPointsDiscovery(
			@WebParam(name = "Request", targetNamespace = "") StopPointsDiscoveryRequestStructure request,
			@WebParam(name = "RequestExtension", targetNamespace = "") ExtensionsStructure requestExtension,
			@WebParam(name = "Answer", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<StopPointsDeliveryStructure> answer,
			@WebParam(name = "AnswerExtension", targetNamespace = "", mode = WebParam.Mode.OUT) Holder<ExtensionsStructure> answerExtension)
			throws StopPointsDiscoveryError {

		ObjectFactory factory = Utils.getObjectFactory();
		setRequest(request);

		answer.value = factory.createStopPointsDeliveryStructure();
		answerExtension.value = factory.createExtensionsStructure();
		answer.value.setResponseTimestamp(XmlStructureFactory.getTimestamp());
		answer.value.setVersion(request.getVersion());
		answer.value.setStatus(Boolean.FALSE);
		ServiceDeliveryErrorConditionStructure error = createOtherErrorStructure("");
		answer.value.setErrorCondition(error);
	}

	private ServiceDeliveryErrorConditionStructure createOtherErrorStructure(
			String text) {

		ObjectFactory factory = Utils.getObjectFactory();

		ServiceDeliveryErrorConditionStructure result = factory
				.createServiceDeliveryErrorConditionStructure();
		OtherErrorStructure error = factory.createOtherErrorStructure();
		error.setErrorText(text);
		error.setNumber(BigInteger.valueOf(0));
		result.setOtherError(error);

		return result;
	}
}