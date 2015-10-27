package siri_lite.common;

import lombok.Getter;
import lombok.ToString;

@ToString
public class SiriException extends java.lang.Exception {

	private static final long serialVersionUID = 5584260623278264158L;

	public static final int OFFSET = 0;

	public static enum ERROR_CODE {
		UNAPPROVED_KEY_ACCESS_ERROR, UNKNOWN_PARTICIPANT_ERROR, UNKNOWN_ENDPOINT_ERROR, ENDPOINT_DENIED_ACCESS_ERROR, ENDPOINT_NOT_AVAILABLE_ACCESS_ERROR, SERVICE_NOT_AVAILABLE_ERROR, CAPABILITY_NOT_SUPPORTED_ERROR, ACCESS_NOT_ALLOWED_ERROR, INVALID_DATA_REFERENCES_ERROR, BEYOND_DATA_HORIZON, NO_INFO_FOR_TOPIC_ERROR, PARAMETER_SIGNORED_ERROR, UNKNOWN_EXTENSIONS_ERROR, ALLOWED_RESOURCE_USAGE_EXCEEDED_ERROR, UNKNOWN_SUBSCRIBER_ERROR, UNKNOWN_SUBSCRIPTION_ERROR, OTHER_ERROR,
	}

	@Getter
	protected String[] value = {};

	@Getter
	protected ERROR_CODE code;

	@Getter
	protected int ordinal;

	@Getter
	protected String name;

	protected SiriException(String message) {
		this(ERROR_CODE.OTHER_ERROR, message, new String[] {});
	}

	public SiriException(ERROR_CODE code, String message) {
		this(code, message, new String[] {});
	}

	public SiriException(ERROR_CODE code, String message, String value) {
		this(code, message, new String[] { value });
	}

	public SiriException(ERROR_CODE code, String message, String[] value) {
		super(message);
		this.value = value;
		this.code = code;
		this.ordinal = code.ordinal();
		this.name = code.name();
	}

}
