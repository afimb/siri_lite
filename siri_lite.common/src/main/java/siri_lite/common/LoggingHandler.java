package siri_lite.common;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import lombok.extern.log4j.Log4j;

@Log4j
public class LoggingHandler implements SOAPHandler<SOAPMessageContext>, Color {

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log(Color.RED, context.getMessage());
		return true;
	}

	@Override
	public boolean handleMessage(final SOAPMessageContext context) {
		Boolean outbound = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			log(Color.CYAN, context.getMessage());
		} else {
			log(Color.MAGENTA, context.getMessage());
		}
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	private void log(String color, SOAPMessage message) {
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(color.getBytes());
			message.writeTo(out);
			out.write(SETCOLOR_NORMAL.getBytes());
			System.out.println(out.toString());
		} catch (Exception ignored) {
			log.error(ignored.getMessage(), ignored);
		}
	}

}
