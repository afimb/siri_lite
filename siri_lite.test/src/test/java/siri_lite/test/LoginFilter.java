package siri_lite.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;

import siri_lite.common.Color;

@Provider
@Log4j
public class LoginFilter implements ClientRequestFilter, ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext context) throws IOException {
		log.info(Color.BLUE + "- Client Request -----------------------------"
				+ Color.NORMAL);
		log.info(Color.BLUE
				+ String.format("> %s: %s", new Object[] { context.getMethod(),
						context.getUri().toASCIIString() }) + Color.NORMAL);
		MultivaluedMap<String, Object> headers = context.getHeaders();
		if (!headers.containsKey("compress")) {
			headers.remove(HttpHeaders.ACCEPT_ENCODING);
		}
		for (String key : headers.keySet()) {
			List<Object> values = headers.get(key);
			log.info(Color.BLUE
					+ String.format("> %s: %s", new Object[] { key, values })
					+ Color.NORMAL);

		}

		log.info(Color.BLUE + "----------------------------------------------"
				+ Color.NORMAL);
	}

	@Override
	public void filter(ClientRequestContext requestContext,
			ClientResponseContext responseContext) throws IOException {

		log.info(Color.BLUE + "- Client Response -----------------------------"
				+ Color.NORMAL);
		log.info(Color.BLUE
				+ String.format("< %s", responseContext.getStatus())
				+ Color.NORMAL);
		MultivaluedMap<String, String> headers = responseContext.getHeaders();
		for (String key : headers.keySet()) {
			List<String> values = headers.get(key);
			log.info(Color.BLUE
					+ String.format("< %s: %s", new Object[] { key, values })
					+ Color.NORMAL);
		}
		log.info(Color.BLUE + "----------------------------------------------"
				+ Color.NORMAL);
		BufferedInputStream in = new BufferedInputStream(
				responseContext.getEntityStream());
		in.mark(8 * 1024);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		in.reset();
		responseContext.setEntityStream(in);
		log.info(Color.BLUE + "[DSU] entity : " + out.toString() + Color.NORMAL);
	}
}
