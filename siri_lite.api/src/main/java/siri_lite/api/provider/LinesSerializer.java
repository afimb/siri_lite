package siri_lite.api.provider;

import java.io.IOException;
import java.util.List;

import lombok.extern.log4j.Log4j;
import uk.org.siri.siri.AnnotatedStopPointStructure.Lines;
import uk.org.siri.siri.LineDirectionStructure;
import uk.org.siri.siri.LineRefStructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Log4j
public class LinesSerializer extends JsonSerializer<Lines> {

	@Override
	public void serialize(Lines value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		try {
			jgen.writeStartArray();

			List<Object> list = value.getLineRefOrLineDirection();
			for (Object object : list) {
				jgen.writeStartObject();
				if (object instanceof LineRefStructure) {
					LineRefStructure lineRef = (LineRefStructure) object;
					jgen.writeStringField("LineRef", lineRef.getValue());
				} else if (object instanceof LineDirectionStructure) {
					LineDirectionStructure lineDirection = (LineDirectionStructure) object;
					if (lineDirection.getLineRef() != null) {
						jgen.writeStringField("LineRef", lineDirection
								.getLineRef().getValue());
					}
					if (lineDirection.getDirectionRef() != null) {
						jgen.writeStringField("DirectionRef", lineDirection
								.getDirectionRef().getValue());
					}

				}
				jgen.writeEndObject();
			}
			jgen.writeEndArray();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
