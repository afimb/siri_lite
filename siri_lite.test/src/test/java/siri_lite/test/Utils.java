package siri_lite.test;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

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
	
}
