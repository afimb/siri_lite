package siri_lite.stop_monitoring;

import siri_lite.common.Configuration;
import siri_lite.common.RequestStructureFactory;
import uk.org.siri.siri.DestinationRefStructure;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.MonitoringRefStructure;
import uk.org.siri.siri.OperatorRefStructure;
import uk.org.siri.siri.StopMonitoringFilterStructure.MaximumNumberOfCalls;
import uk.org.siri.siri.StopMonitoringRequestStructure;

public class StopMonitoringRequestStructureFactory
		extends
		RequestStructureFactory<StopMonitoringRequestStructure, StopMonitoringParameters> {

	@Override
	protected StopMonitoringRequestStructure create(
			Configuration configuration, StopMonitoringParameters parameters) {
		StopMonitoringRequestStructure request = factory
				.createStopMonitoringRequestStructure();
		populate(request, configuration, (StopMonitoringParameters) parameters);
		return request;
	}

	protected void populate(StopMonitoringRequestStructure request,
			Configuration configuration, StopMonitoringParameters parameters) {

		request.setRequestTimestamp(parameters.getRequestTimestamp());
		request.setMessageIdentifier(parameters.getMessageIdentifier());
		request.setVersion(configuration.getVersion());

		// StartTime
		if (parameters.getStartTime() != null) {
			request.setStartTime(parameters.getStartTime());
		}

		// PreviewInterval
		if (parameters.getPreviewInterval() != null) {
			request.setPreviewInterval(parameters.getPreviewInterval());
		}

		// MonitoringRef
		MonitoringRefStructure monitoringRef = factory
				.createMonitoringRefStructure();
		monitoringRef.setValue(parameters.getMonitoringRef());
		request.setMonitoringRef(monitoringRef);

		// OperatorRef
		if (parameters.getOperatorRef() != null) {
			OperatorRefStructure operatorRef = factory
					.createOperatorRefStructure();
			operatorRef.setValue(parameters.getOperatorRef());
			request.setOperatorRef(operatorRef);
		}

		// LineRef
		if (parameters.getLineRef() != null) {
			LineRefStructure lineRef = factory.createLineRefStructure();
			lineRef.setValue(parameters.getLineRef());
			request.setLineRef(lineRef);
		}

		// DestinationRef
		if (parameters.getDestinationRef() != null) {
			DestinationRefStructure destinationRef = factory
					.createDestinationRefStructure();
			destinationRef.setValue(parameters.getDestinationRef());
			request.setDestinationRef(destinationRef);
		}

		// StopVisitTypes
		if (parameters.getStopVisitType() != null) {
			request.setStopVisitTypes(parameters.getStopVisitType());
		}

		// MaximumStopVisits
		if (parameters.getMaximumStopVisits() != null) {
			request.setMaximumStopVisits(parameters.getMaximumStopVisits());
		}

		// MinimumStopVisitsPerLine
		if (parameters.getMinimumStopVisitsPerLine() != null) {
			request.setMinimumStopVisitsPerLine(parameters
					.getMinimumStopVisitsPerLine());
		}

		// MinimumStopVisitsPerLineVia
		if (parameters.getMinimumStopVisitsPerLineVia() != null) {
			request.setMinimumStopVisitsPerLineVia(parameters
					.getMinimumStopVisitsPerLineVia());
		}

		// StopMonitoringDetailLevel
		// if (parameters.getStopMonitoringDetailLevel() != null) {
		// request.setStopMonitoringDetailLevel(parameters
		// .getStopMonitoringDetailLevel());
		// }

		// MaximumNumberOfCallsOnwards 
		if (parameters.getMaximumNumberOfCallsOnwards() != null) {
			MaximumNumberOfCalls maximumNumberOfCalls = factory
					.createStopMonitoringFilterStructureMaximumNumberOfCalls();
			maximumNumberOfCalls.setOnwards(parameters
					.getMaximumNumberOfCallsOnwards());
			request.setMaximumNumberOfCalls(maximumNumberOfCalls);
		}

	}

	static {
		RequestStructureFactory
				.register(new StopMonitoringRequestStructureFactory());
	}
}
