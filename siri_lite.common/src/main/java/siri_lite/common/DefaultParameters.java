package siri_lite.common;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import uk.org.siri.siri.MessageQualifierStructure;

@Log4j
@Data
public abstract class DefaultParameters {

	public static final String DEBUG = "debug";
	public static final String VERSION = "version";
	public static final String ENCODING = "encoding";
	public static final String REQUESTOR_REF = "RequestorRef";
	public static final String ACCOUNT_ID = "AccountId";
	public static final String ACCOUNT_KEY = "AccountKey";

	public static final String MESSAGE_IDENTIFIER = "MessageIdentifier";

	private Boolean debug;
	private XMLGregorianCalendar requestTimestamp;
	private MessageQualifierStructure messageIdentifier;
	private String version;
	private MediaType encoding = MediaType.APPLICATION_JSON_TYPE;
	private String requestorRef;
	private String accountId;
	private String accountKey;

	public void validate() throws ValidationException {

	}

	public void configure(MultivaluedMap<String, String> properties)
			throws ValidationException {
		try {

			setDebug(properties.getFirst(DEBUG) != null);
			setRequestTimestamp(XmlStructureFactory.getTimestamp());
			setMessageIdentifier(SiriStructureFactory
					.createMessageIdentifier(properties
							.getFirst(MESSAGE_IDENTIFIER)));
			setVersion(properties.getFirst(VERSION));
			String encoding = properties.getFirst(ENCODING);
			if (encoding != null && !encoding.isEmpty()) {
				setEncoding(MediaType.valueOf("application/"
						+ encoding.trim().substring(1)));
			}
			setRequestorRef(properties.getFirst(REQUESTOR_REF));
			setAccountId(properties.getFirst(ACCOUNT_ID));
			setAccountKey(properties.getFirst(ACCOUNT_KEY));

		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
}
