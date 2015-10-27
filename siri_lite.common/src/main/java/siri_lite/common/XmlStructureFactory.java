package siri_lite.common;

import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

public class XmlStructureFactory {
	private static DatatypeFactory factory;
	static {
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException ignored) {
		}
	}

	public static Duration getDuration(long durationInMilliSeconds) {
		return factory.newDuration(durationInMilliSeconds);
	}

	public static Duration getDuration(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		return factory.newDuration(value);
	}

	public static long getTimeInMillis(Duration duration) {
		Calendar calandar = GregorianCalendar.getInstance();
		long t1 = calandar.getTimeInMillis();
		duration.addTo(calandar);
		long t2 = calandar.getTimeInMillis();
		return t2 - t1;
	}

	public static long getStartTime() {
		return ManagementFactory.getRuntimeMXBean().getStartTime();
	}

	public static XMLGregorianCalendar getServiceStartedTime() {
		return getTimestamp(getStartTime());
	}

	public static XMLGregorianCalendar getTimestamp() {
		return getTimestamp(System.currentTimeMillis());
	}

	public static XMLGregorianCalendar getTimestamp(Date date) {
		return getTimestamp(date.getTime());
	}

	public static XMLGregorianCalendar getTimestamp(long date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(date);
		return getTimestamp(calendar);
	}

	public static XMLGregorianCalendar getTimestamp(GregorianCalendar value) {
		if (value == null) {
			return null;
		}
		return factory.newXMLGregorianCalendar(value);
	}

	public static XMLGregorianCalendar getTimestamp(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		return factory.newXMLGregorianCalendar(value);

	}

}
