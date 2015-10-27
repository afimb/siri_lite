package siri_lite.stop_monitoring;

import java.math.BigInteger;

import javax.validation.ValidationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Data;
import lombok.EqualsAndHashCode;
import siri_lite.common.DefaultParameters;
import siri_lite.common.XmlStructureFactory;
import uk.org.siri.siri.StopVisitTypeEnumeration;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopMonitoringParameters extends DefaultParameters {

	public static final String START_TIME = "StartTime";
	public static final String PREVIEW_INTERVAL = "PreviewInterval";
	public static final String MONITORING_REF = "MonitoringRef";
	public static final String OPERATOR_REF = "OperatorRef";
	public static final String LINE_REF = "LineRef";
	public static final String DESTINATION_REF = "DestinationRef";
	public static final String MAXIMUM_STOP_VISITS = "MaximumStopVisits";
	public static final String MINIMUM_STOP_VISITS_PER_LINE = "MinimumStopVisitsPerLine";
	public static final String MINIMUM_STOP_VISITS_PER_LINE_VIA = "MinimumStopVisitsPerLineVia";
	// public static final String STOPMONITORINGDETAILLEVEL =
	// "StopMonitoringDetailLevel";
	public static final String MAXIMUMNUMBEROFCALLS_ONWARDS = "MaximumNumberOfCalls.Onwards";
	public static final String STOPVISITTYPE = "StopVisitType";

	private XMLGregorianCalendar startTime;
	private Duration previewInterval;
	private String monitoringRef;
	private String operatorRef;
	private String lineRef;
	private String destinationRef;
	private BigInteger maximumStopVisits;
	private BigInteger minimumStopVisitsPerLine;
	private BigInteger minimumStopVisitsPerLineVia;
	// private StopMonitoringDetailEnumeration stopMonitoringDetailLevel;
	private BigInteger maximumNumberOfCallsOnwards;
	private StopVisitTypeEnumeration stopVisitType;

	@Override
	public void configure(MultivaluedMap<String, String> properties)
			throws ValidationException {
		super.configure(properties);
		try {
			if (properties.getFirst(START_TIME) != null) {
				setStartTime(XmlStructureFactory.getTimestamp(properties
						.getFirst(START_TIME)));
			}
			// else {
			// setStartTime(XmlStructureFactory.getTimestamp());
			// }
			String value = properties.getFirst(PREVIEW_INTERVAL);
			if (value != null) {
				setPreviewInterval(XmlStructureFactory.getDuration(value));
			}
			value = properties.getFirst(MONITORING_REF);
			if (value != null) {
				setMonitoringRef(value);
			}
			value = properties.getFirst(OPERATOR_REF);
			if (value != null) {
				setOperatorRef(value);
			}
			value = properties.getFirst(LINE_REF);
			if (value != null) {
				setLineRef(value);
			}
			value = properties.getFirst(DESTINATION_REF);
			if (value != null) {
				setDestinationRef(value);
			}
			value = properties.getFirst(MAXIMUM_STOP_VISITS);
			if (value != null) {
				setMaximumStopVisits(new BigInteger(value));
			}
			value = properties.getFirst(MINIMUM_STOP_VISITS_PER_LINE);
			if (value != null) {
				setMinimumStopVisitsPerLine(new BigInteger(value));
			}
			value = properties.getFirst(MINIMUM_STOP_VISITS_PER_LINE_VIA);
			if (value != null) {
				setMinimumStopVisitsPerLineVia(new BigInteger(value));
			}
			// value = properties.getFirst(STOPMONITORINGDETAILLEVEL);
			// if (value != null) {
			// setStopMonitoringDetailLevel(StopMonitoringDetailEnumeration
			// .fromValue(value));
			// }
			value = properties.getFirst(MAXIMUMNUMBEROFCALLS_ONWARDS);
			if (value != null) {
				setMaximumNumberOfCallsOnwards(new BigInteger(value));
			}
			value = properties.getFirst(STOPVISITTYPE);
			if (value != null) {
				setStopVisitType(StopVisitTypeEnumeration.fromValue(value));
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
}
