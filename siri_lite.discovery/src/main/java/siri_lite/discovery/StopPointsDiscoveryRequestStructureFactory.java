package siri_lite.discovery;

import siri_lite.common.Configuration;
import siri_lite.common.RequestStructureFactory;
import siri_lite.common.SiriStructureFactory;
import uk.org.siri.siri.BoundingBoxStructure;
import uk.org.siri.siri.LocationStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;

public class StopPointsDiscoveryRequestStructureFactory
		extends
		RequestStructureFactory<StopPointsDiscoveryRequestStructure, StopPointsDiscoveryParameters> {

	@Override
	protected StopPointsDiscoveryRequestStructure create(Configuration config,
			StopPointsDiscoveryParameters param) {
		StopPointsDiscoveryRequestStructure request = factory
				.createStopPointsDiscoveryRequestStructure();
		populate(request, config, param);
		return request;
	}

	protected void populate(StopPointsDiscoveryRequestStructure request,
			Configuration configuration,
			StopPointsDiscoveryParameters parameters) {

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

		// BoundingBoxStructure.UpperLeft.Longitude
		// BoundingBoxStructure.UpperLeft.Latitude
		// BoundingBoxStructure.LowerRight.Longitude
		// BoundingBoxStructure.LowerRight.Latitude
		if (parameters.getUpperLeftLongitude() != null
				&& parameters.getUpperLeftLatitude() != null
				&& parameters.getLowerRightLongitude() != null
				&& parameters.getLowerRightLatitude() != null) {
			BoundingBoxStructure boundingBox = factory
					.createBoundingBoxStructure();
			LocationStructure upperLeft = factory.createLocationStructure();
			upperLeft.setLongitude(parameters.getUpperLeftLongitude());
			upperLeft.setLatitude(parameters.getUpperLeftLatitude());
			LocationStructure lowerRigh = factory.createLocationStructure();
			lowerRigh.setLongitude(parameters.getLowerRightLongitude());
			lowerRigh.setLatitude(parameters.getLowerRightLatitude());
			boundingBox.setUpperLeft(upperLeft);
			boundingBox.setLowerRight(lowerRigh);
			request.setBoundingBox(boundingBox);

		}

		// StopPointsDetailLevel
		if (parameters.getStopPointsDetailLevel() != null) {
			request.setStopPointsDetailLevel(parameters
					.getStopPointsDetailLevel());
		}
	}

	static {
		RequestStructureFactory
				.register(new StopPointsDiscoveryRequestStructureFactory());
	}
}
