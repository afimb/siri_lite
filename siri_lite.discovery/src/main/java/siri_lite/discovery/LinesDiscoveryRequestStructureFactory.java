package siri_lite.discovery;

import siri_lite.common.Configuration;
import siri_lite.common.RequestStructureFactory;
import siri_lite.common.SiriStructureFactory;
import uk.org.siri.siri.LinesDiscoveryRequestStructure;
import uk.org.siri.siri.ParticipantRefStructure;

public class LinesDiscoveryRequestStructureFactory
		extends
		RequestStructureFactory<LinesDiscoveryRequestStructure, LinesDiscoveryParameters> {

	@Override
	protected LinesDiscoveryRequestStructure create(Configuration config,
			LinesDiscoveryParameters parameters) {
		LinesDiscoveryRequestStructure request = factory
				.createLinesDiscoveryRequestStructure();
		populate(request, config, parameters);
		return request;
	}

	protected void populate(LinesDiscoveryRequestStructure request,
			Configuration configuration, LinesDiscoveryParameters parameters) {

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
				.register(new LinesDiscoveryRequestStructureFactory());
	}
}
