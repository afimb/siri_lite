package siri_lite.checkstatus;

import javax.validation.ValidationException;
import javax.ws.rs.core.MultivaluedMap;

import lombok.Data;
import siri_lite.common.DefaultParameters;

@Data
public class CheckStatusParameters extends DefaultParameters {

	@Override
	public void configure(MultivaluedMap<String, String> properties)
			throws ValidationException {
		super.configure(properties);
	}
}
