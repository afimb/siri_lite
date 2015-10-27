package siri_lite.general_message;

import java.util.List;

import javax.validation.ValidationException;
import javax.ws.rs.core.MultivaluedMap;

import lombok.Data;
import siri_lite.common.DefaultParameters;

@Data
public class GeneralMessageParameters extends DefaultParameters {

	public static final String LANGUAGE = "language";
	public static final String INFO_CHANNEL_REF = "InfoChannelRef";
	public static final String LINE_REF = "LineRef";
	public static final String STOP_POINT_REF = "StopPointRef";
	public static final String ROUTE_REF = "RouteRef";
	public static final String DESTINATION_REF = "DestinationRef";
	public static final String JOURNEY_PATTERN_REF = "JourneyPatternRef";
	public static final String GROUPE_OF_LINES_REF = "GroupeOfLinesRef";

	private String language;
	private List<String> infoChannelRef;
	private List<String> lineRef;
	private List<String> stopPointRef;
	private List<String> routeRef;
	private List<String> destinationRef;
	private List<String> journeyPatternRef;
	private List<String> groupeOfLinesRef;

	@Override
	public void configure(MultivaluedMap<String, String> properties)
			throws ValidationException {
		super.configure(properties);
		try {
			String value = properties.getFirst(LANGUAGE);
			if (value != null) {
				setLanguage(value);
			}
			List<String> infoChannelRef = properties.get(INFO_CHANNEL_REF);
			if (infoChannelRef != null) {
				setInfoChannelRef(infoChannelRef);
			}
			List<String> lineRef = properties.get(LINE_REF);
			if (lineRef != null) {
				setLineRef(lineRef);
			}
			List<String> stopPointRef = properties.get(STOP_POINT_REF);
			if (stopPointRef != null) {
				setStopPointRef(stopPointRef);
			}
			List<String> routeRef = properties.get(ROUTE_REF);
			if (routeRef != null) {
				setRouteRef(routeRef);
			}
			List<String> destinationRef = properties.get(DESTINATION_REF);
			if (destinationRef != null) {
				setDestinationRef(destinationRef);
			}
			List<String> journeyPatternRef = properties
					.get(JOURNEY_PATTERN_REF);
			if (journeyPatternRef != null) {
				setJourneyPatternRef(journeyPatternRef);
			}
			List<String> groupeOfLinesRef = properties.get(GROUPE_OF_LINES_REF);
			if (groupeOfLinesRef != null) {
				setGroupeOfLinesRef(groupeOfLinesRef);
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

}
