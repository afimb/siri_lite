package siri_lite.common;

public class Configuration {

	public static final String DEFAULT_VERSION = "2.0:FR-IDF-2.4";
	public static final String PRODUCER_ADDRESS = "siri_lite.producer.address";
	public static final String VERSION = "siri_lite.version";
	public static final String REQUESTOR_REF = "siri_lite.requestor.ref";
	public static final String DELEGATOR_REF = "siri_lite.delegator.ref";
	public static final String PROXY_HOST = "siri_lite.proxy.host";
	public static final String PROXY_PORT = "siri_lite.proxy.port";
	public static final String PROXY_USER = "siri_lite.proxy.user";
	public static final String PROXY_PASSWORD = "siri_lite.proxy.password";
	public static final String TIMEOUT = "siri_lite.timeout";
	public static final String STOPS_DISCOVERY_MAX_AGE = "siri_lite.stops_discovery.max_age";
	public static final String LINES_DISCOVERY_MAX_AGE = "siri_lite.lines_discovery.max_age";
	public static final String STOP_MONITORING_MAX_AGE = "siri_lite.stop_monitoring.max_age";
	public static final String GENERAL_MESSAGE_MAX_AGE = "siri_lite.general_message.max_age";

	private static Configuration instance;

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public String getProducerAddress() {
		return getProperty(PRODUCER_ADDRESS);
	}

	public String getVersion() {
		String result = DEFAULT_VERSION;
		String value = getProperty(VERSION);
		if (value != null && !value.isEmpty()) {
			result = value.trim();
		}
		return result;
	}

	public String getRequestorRef() {
		return getProperty(REQUESTOR_REF);
	}

	public String getProxyHost() {
		return getProperty(REQUESTOR_REF);
	}

	public Integer getProxyPort() {
		return Integer.valueOf(getProperty(PROXY_PORT));
	}

	public String getProxyUser() {
		return getProperty(PROXY_USER);
	}

	public String getProxyPassword() {
		return getProperty(PROXY_PASSWORD);
	}

	public Integer getTimeout() {
		int result = 3000;
		String value = getProperty(TIMEOUT);
		if (value != null && !value.isEmpty()) {
			result = Integer.valueOf(value);
		}
		return result;
	}

	public Integer getStopsDiscoveryMaxAge() {
		int result = 3600;
		String value = getProperty(STOPS_DISCOVERY_MAX_AGE);
		if (value != null && !value.isEmpty()) {
			result = Integer.valueOf(value);
		}
		return result;
	}

	public Integer getLinesDiscoveryMaxAge() {
		int result = 3600;
		String value = getProperty(LINES_DISCOVERY_MAX_AGE);
		if (value != null && !value.isEmpty()) {
			result = Integer.valueOf(value);
		}
		return result;
	}

	public Integer getStopMonitoringMaxAge() {
		int result = 60;
		String value = getProperty(STOP_MONITORING_MAX_AGE);
		if (value != null && !value.isEmpty()) {
			result = Integer.valueOf(value);
		}
		return result;
	}

	public Integer getGeneralMessageMaxAge() {
		int result = 60;
		String value = getProperty(GENERAL_MESSAGE_MAX_AGE);
		if (value != null && !value.isEmpty()) {
			result = Integer.valueOf(value);
		}
		return result;
	}

	public String getDelegatorRef() {
		String result = "";
		String value = getProperty(DELEGATOR_REF);
		if (value != null && !value.isEmpty()) {
			result = value.trim();
		}
		return result;
	}

	private String getProperty(String key) {
		return System.getProperty(key);
	}
}
