package siri_lite.test;

import java.nio.charset.Charset;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.org.siri.siri.ObjectFactory;

public abstract class Utils {

	public static String buildURL(String url, List<BasicNameValuePair> values) {
		return url + "?"
				+ URLEncodedUtils.format(values, Charset.forName("UTF-8"))
				+ "&debug";
	}

	public static void sleep(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException ignored) {

		}
	}

	private static ObjectFactory factory = new ObjectFactory();

	public static ObjectFactory getObjectFactory() {
		return factory;
	}

	private static DatatypeFactory xmlFactory;

	static {
		try {
			xmlFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException ignored) {
		}
	}

	public static DatatypeFactory getDatatypeFactory() {
		return xmlFactory;
	}

	private static JAXBContext jaxbContext;

	static {
		try {
			jaxbContext = JAXBContext.newInstance(
					uk.org.siri.siri.ObjectFactory.class,
					uk.org.siri.wsdl.siri.ObjectFactory.class,
					uk.org.siri.wsdl.ObjectFactory.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static JAXBContext getJaxbContext() {
		return jaxbContext;
	}
}
