package siri_lite.discovery;

import java.math.BigDecimal;

import javax.validation.ValidationException;
import javax.ws.rs.core.MultivaluedMap;

import lombok.Data;
import siri_lite.common.DefaultParameters;
import uk.org.siri.siri.StopPointsDetailEnumeration;

@Data
public class StopPointsDiscoveryParameters extends DefaultParameters {

	public static final String BOUNDINGBOXSTRUCTURE_UPPERLEFT_LONGITUDE = "BoundingBoxStructure.UpperLeft.Longitude";
	public static final String BOUNDINGBOXSTRUCTURE_UPPERLEFT_LATITUDE = "BoundingBoxStructure.UpperLeft.Latitude";
	public static final String BOUNDINGBOXSTRUCTURE_LOWERRIGHT_LONGITUDE = "BoundingBoxStructure.LowerRight.Longitude";
	public static final String BOUNDINGBOXSTRUCTURE_LOWERRIGHT_LATITUDE = "BoundingBoxStructure.LowerRight.Latitude";
	public static final String STOPPOINTSDETAILLEVEL = "StopPointsDetailLevel";

	private BigDecimal upperLeftLongitude;
	private BigDecimal upperLeftLatitude;
	private BigDecimal lowerRightLongitude;
	private BigDecimal lowerRightLatitude;
	private StopPointsDetailEnumeration stopPointsDetailLevel;

	@Override
	public void configure(MultivaluedMap<String, String> properties)
			throws ValidationException {
		super.configure(properties);
		try {

			String value = properties
					.getFirst(BOUNDINGBOXSTRUCTURE_UPPERLEFT_LONGITUDE);
			if (value != null) {
				setUpperLeftLongitude(new BigDecimal(value));
			}
			value = properties
					.getFirst(BOUNDINGBOXSTRUCTURE_UPPERLEFT_LATITUDE);
			if (value != null) {
				setUpperLeftLatitude(new BigDecimal(value));
			}
			value = properties
					.getFirst(BOUNDINGBOXSTRUCTURE_LOWERRIGHT_LONGITUDE);
			if (value != null) {
				setLowerRightLongitude(new BigDecimal(value));
			}
			value = properties
					.getFirst(BOUNDINGBOXSTRUCTURE_LOWERRIGHT_LATITUDE);
			if (value != null) {
				setLowerRightLatitude(new BigDecimal(value));
			}
			value = properties.getFirst(STOPPOINTSDETAILLEVEL);
			if (value != null) {
				setStopPointsDetailLevel(StopPointsDetailEnumeration
						.fromValue(value));
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
}
