package siri_lite.vehicle_monitoring;

import java.math.BigInteger;

import javax.validation.ValidationException;
import javax.ws.rs.core.MultivaluedMap;

import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_lite.common.DefaultParameters;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleMonitoringParameters extends DefaultParameters {

	public static final String VEHICLE_REF = "VehicleRef";
	public static final String LINE_REF = "LineRef";
	public static final String MAXIMUM_VEHICLES = "MaximumVehicles";
	public static final String MAXIMUMNUMBEROFCALLS_ONWARDS = "MaximumNumberOfCalls.Onwards";

	private String vehicleRef;
	private String lineRef;
	private BigInteger maximumVehicles;
	private BigInteger maximumNumberOfCallsOnwards;

	@Override
	public void configure(MultivaluedMap<String, String> properties) throws ValidationException {
		super.configure(properties);
		try {
			String value = properties.getFirst(VEHICLE_REF);
			if (value != null) {
				setVehicleRef(value);
			} else {
				value = properties.getFirst(LINE_REF);
				if (value != null) {
					setLineRef(value);
				}
			}
			value = properties.getFirst(MAXIMUM_VEHICLES);
			if (value != null) {
				setMaximumVehicles(new BigInteger(value));
			}
			value = properties.getFirst(MAXIMUMNUMBEROFCALLS_ONWARDS);
			if (value != null) {
				setMaximumNumberOfCallsOnwards(new BigInteger(value));
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
}
