package siri_lite.api.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.extern.log4j.Log4j;
import uk.org.siri.siri.AdviceRefStructure;
import uk.org.siri.siri.AnnotatedStopPointStructure.Lines;
import uk.org.siri.siri.BlockRefStructure;
import uk.org.siri.siri.CapabilityRefStructure;
import uk.org.siri.siri.ClearDownRefStructure;
import uk.org.siri.siri.ConnectionLinkRefStructure;
import uk.org.siri.siri.CourseOfJourneyRefStructure;
import uk.org.siri.siri.DataFrameRefStructure;
import uk.org.siri.siri.DatedVehicleJourneyRefStructure;
import uk.org.siri.siri.DestinationRefStructure;
import uk.org.siri.siri.DirectionRefStructure;
import uk.org.siri.siri.DriverRefStructure;
import uk.org.siri.siri.EntryQualifierStructure;
import uk.org.siri.siri.ErrorDescriptionStructure;
import uk.org.siri.siri.FacilityRefStructure;
import uk.org.siri.siri.FeatureRefStructure;
import uk.org.siri.siri.GroupOfLinesRefStructure;
import uk.org.siri.siri.InfoChannelRefStructure;
import uk.org.siri.siri.InfoMessageRefStructure;
import uk.org.siri.siri.InterchangeRefStructure;
import uk.org.siri.siri.ItemRefStructure;
import uk.org.siri.siri.JourneyPartRefStructure;
import uk.org.siri.siri.JourneyPatternRefStructure;
import uk.org.siri.siri.JourneyPlaceRefStructure;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MonitoringRefStructure;
import uk.org.siri.siri.NetworkRefStructure;
import uk.org.siri.siri.OperatorRefStructure;
import uk.org.siri.siri.OrganisationRefStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProductCategoryRefStructure;
import uk.org.siri.siri.QuayRefStructure;
import uk.org.siri.siri.RouteLinkRefStructure;
import uk.org.siri.siri.RouteRefStructure;
import uk.org.siri.siri.SectionRefStructure;
import uk.org.siri.siri.ServiceFeatureRefStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.SituationSimpleRefStructure;
import uk.org.siri.siri.StopAreaRefStructure;
import uk.org.siri.siri.StopPointRefStructure;
import uk.org.siri.siri.SubscriptionFilterRefStructure;
import uk.org.siri.siri.SubscriptionFilterStructure;
import uk.org.siri.siri.SubscriptionQualifierStructure;
import uk.org.siri.siri.TrainNumberRefStructure;
import uk.org.siri.siri.TrainPartRefStructure;
import uk.org.siri.siri.VehicleFeatureRefStructure;
import uk.org.siri.siri.VehicleJourneyRefStructure;
import uk.org.siri.siri.VehicleMonitoringRefStructure;
import uk.org.siri.siri.VehicleRefStructure;
import uk.org.siri.siri.VersionRefStructure;
import uk.org.siri.siri.ZoneRefStructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Log4j
public class JAXBSerializer implements MessageBodyWriter<Siri> {

	private static final Class<?>[] classes = { AdviceRefStructure.class,
			BlockRefStructure.class, CapabilityRefStructure.class,
			ClearDownRefStructure.class, ConnectionLinkRefStructure.class,
			CourseOfJourneyRefStructure.class, DataFrameRefStructure.class,
			DatedVehicleJourneyRefStructure.class,
			DestinationRefStructure.class, DirectionRefStructure.class,
			DriverRefStructure.class, EntryQualifierStructure.class,
			ErrorDescriptionStructure.class, FacilityRefStructure.class,
			FeatureRefStructure.class, GroupOfLinesRefStructure.class,
			InfoChannelRefStructure.class, InfoMessageRefStructure.class,
			InterchangeRefStructure.class, ItemRefStructure.class,
			JourneyPartRefStructure.class, JourneyPatternRefStructure.class,
			JourneyPlaceRefStructure.class, LineRefStructure.class,
			MessageQualifierStructure.class, MonitoringRefStructure.class,
			NetworkRefStructure.class, OperatorRefStructure.class,
			OrganisationRefStructure.class, ParticipantRefStructure.class,
			ProductCategoryRefStructure.class, QuayRefStructure.class,
			RouteLinkRefStructure.class, RouteRefStructure.class,
			SectionRefStructure.class, ServiceFeatureRefStructure.class,
			SituationSimpleRefStructure.class, StopAreaRefStructure.class,
			StopPointRefStructure.class, SubscriptionFilterRefStructure.class,
			SubscriptionFilterStructure.class,
			SubscriptionQualifierStructure.class,
			TrainNumberRefStructure.class, TrainPartRefStructure.class,
			VehicleFeatureRefStructure.class, VehicleJourneyRefStructure.class,
			VehicleMonitoringRefStructure.class, VehicleRefStructure.class,
			VersionRefStructure.class, ZoneRefStructure.class, };

	@Override
	public boolean isWriteable(Class type, Type generic,
			Annotation[] annotations, MediaType media) {
		return type.isAnnotationPresent(XmlRootElement.class);
	}

	@Override
	public long getSize(Siri target, Class type, Type generic,
			Annotation[] annotations, MediaType media) {
		return -1;
	}

	@Override
	public void writeTo(Siri target, Class type, Type generic,
			Annotation[] annotations, MediaType media, MultivaluedMap headers,
			OutputStream out) throws IOException, WebApplicationException {
		writeToWithJackson(target, type, generic, annotations, media, headers,
				out);
	}

	private void writeToWithJackson(Siri target, Class type, Type generic,
			Annotation[] annotations, MediaType media, MultivaluedMap headers,
			OutputStream out) throws IOException, WebApplicationException {
		try {

			ObjectMapper mapper = new ObjectMapper();
			AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
					TypeFactory.defaultInstance());
			mapper.setAnnotationIntrospector(introspector);
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

			SimpleModule module = new SimpleModule();
			module.addSerializer(Lines.class, new LinesSerializer());
			SimpleElementSerializer serializer = new SimpleElementSerializer(
					"value");
			for (int i = 0; i < classes.length; i++) {
				module.addSerializer(classes[i], serializer);
			}

			mapper.registerModule(module);

			ObjectWriter writer = mapper.writer();
			writer.writeValue(out, target);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
