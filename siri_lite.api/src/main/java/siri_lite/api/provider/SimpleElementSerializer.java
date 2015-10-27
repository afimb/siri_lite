package siri_lite.api.provider;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Log4j
@AllArgsConstructor
public class SimpleElementSerializer extends JsonSerializer<Object> {

	private String name;

	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		try {
			String result = BeanUtils.getProperty(value, name);
			jgen.writeString(result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
