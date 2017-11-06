package siri_lite.vehicle_monitoring;

import siri_lite.common.Configuration;
import siri_lite.common.RequestStructureFactory;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.VehicleMonitoringRequestStructure;
import uk.org.siri.siri.VehicleMonitoringRequestStructure.MaximumNumberOfCalls;
import uk.org.siri.siri.VehicleRefStructure;

public class VehicleMonitoringRequestStructureFactory extends
		RequestStructureFactory<VehicleMonitoringRequestStructure, VehicleMonitoringParameters> {

	@Override
	protected VehicleMonitoringRequestStructure create(Configuration configuration,
			VehicleMonitoringParameters parameters) {
		VehicleMonitoringRequestStructure request = factory.createVehicleMonitoringRequestStructure();
		populate(request, configuration, parameters);
		return request;
	}

	protected void populate(VehicleMonitoringRequestStructure request, Configuration configuration,
			VehicleMonitoringParameters parameters) {
		request.setRequestTimestamp(parameters.getRequestTimestamp());
		request.setMessageIdentifier(parameters.getMessageIdentifier());
		request.setVersion(configuration.getVersion());

		// VehicleRef
		if (parameters.getVehicleRef() != null) {
			VehicleRefStructure vehicleRef = factory.createVehicleRefStructure();
			vehicleRef.setValue(parameters.getVehicleRef());
			request.setVehicleRef(vehicleRef);
		}

		// LineRef
		if (parameters.getLineRef() != null) {
			LineRefStructure lineRef = factory.createLineRefStructure();
			lineRef.setValue(parameters.getLineRef());
			request.setLineRef(lineRef);
		}

		// MaximumVehicles
		if (parameters.getMaximumVehicles() != null) {
			request.setMaximumVehicles(parameters.getMaximumVehicles());
		}

		// MaximumNumberOfCallsOnwards
		if (parameters.getMaximumNumberOfCallsOnwards() != null) {
			MaximumNumberOfCalls maximumNumberOfCalls = factory
					.createVehicleMonitoringRequestStructureMaximumNumberOfCalls();
			maximumNumberOfCalls.setOnwards(parameters.getMaximumNumberOfCallsOnwards());
			request.setMaximumNumberOfCalls(maximumNumberOfCalls);
		}

	}

	static {
		RequestStructureFactory.register(new VehicleMonitoringRequestStructureFactory());
	}
}
