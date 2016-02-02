package siri_lite.checkstatus;

import siri_lite.common.Configuration;
import siri_lite.common.RequestStructureFactory;
import siri_lite.common.SiriStructureFactory;
import uk.org.siri.siri.CheckStatusRequestStructure;
import uk.org.siri.siri.ParticipantRefStructure;

public class CheckStatusRequestStructureFactory
		extends
		RequestStructureFactory<CheckStatusRequestStructure, CheckStatusParameters> {

	@Override
	protected CheckStatusRequestStructure create(Configuration config,
			CheckStatusParameters parameters) {
		CheckStatusRequestStructure request = factory
				.createCheckStatusRequestStructure();
		populate(request, config, parameters);
		return request;
	}

	protected void populate(CheckStatusRequestStructure request,
			Configuration configuration, CheckStatusParameters parameters) {

		request.setRequestTimestamp(parameters.getRequestTimestamp());
		request.setMessageIdentifier(parameters.getMessageIdentifier());
		request.setVersion(configuration.getVersion());

		// RequestorRef
		ParticipantRefStructure requestorRef = SiriStructureFactory
				.createParticipantRef(parameters.getRequestorRef(), null);
		request.setRequestorRef(requestorRef);

		// AccountId
		request.setAccountId(parameters.getAccountId());

		// AccountKey
		request.setAccountKey(parameters.getAccountKey());

	}

	static {
		RequestStructureFactory
				.register(new CheckStatusRequestStructureFactory());
	}
}
