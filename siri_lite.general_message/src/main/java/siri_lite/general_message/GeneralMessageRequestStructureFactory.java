package siri_lite.general_message;

import javax.xml.bind.JAXBElement;

import siri_lite.common.Configuration;
import siri_lite.common.RequestStructureFactory;
import uk.org.siri.siri.DestinationRefStructure;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.GroupOfLinesRefStructure;
import uk.org.siri.siri.InfoChannelRefStructure;
import uk.org.siri.siri.JourneyPatternRefStructure;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.RouteRefStructure;
import uk.org.siri.siri.StopPointRefStructure;
import uk.org.siri.wsdl.siri.IDFGeneralMessageRequestFilterStructure;

public class GeneralMessageRequestStructureFactory extends
		RequestStructureFactory<GeneralMessageRequestStructure, GeneralMessageParameters> {

	protected static uk.org.siri.wsdl.siri.ObjectFactory idfFactory = new uk.org.siri.wsdl.siri.ObjectFactory();

	@Override
	protected GeneralMessageRequestStructure create(Configuration configuration, GeneralMessageParameters parameters) {
		GeneralMessageRequestStructure request = factory.createGeneralMessageRequestStructure();
		populate(request, configuration, parameters);
		return request;
	}

	protected void populate(GeneralMessageRequestStructure request, Configuration configuration,
			GeneralMessageParameters parameters) {

		request.setRequestTimestamp(parameters.getRequestTimestamp());
		request.setMessageIdentifier(parameters.getMessageIdentifier());
		request.setVersion(configuration.getVersion());

		// language
		if (parameters.getLanguage() != null) {
			request.setLanguage(parameters.getLanguage());
		}

		// InfoChannelRef
		if (parameters.getInfoChannelRef() != null) {
			for (String value : parameters.getInfoChannelRef()) {
				InfoChannelRefStructure infoChannelRef = factory.createInfoChannelRefStructure();
				infoChannelRef.setValue(value);
				request.getInfoChannelRef().add(infoChannelRef);
			}
		}

		// Filter
		IDFGeneralMessageRequestFilterStructure extension = idfFactory.createIDFGeneralMessageRequestFilterStructure();
		JAXBElement<IDFGeneralMessageRequestFilterStructure> element = idfFactory
				.createIDFGeneralMessageRequestFilter(extension);

		boolean hasFilter = false;
		if (parameters.getLineRef() != null) {
			hasFilter = true;
			for (String item : parameters.getLineRef()) {
				LineRefStructure lineRef = factory.createLineRefStructure();
				lineRef.setValue(item);
				extension.getLineRef().add(lineRef);
			}
		} else if (parameters.getStopPointRef() != null) {
			hasFilter = true;
			for (String item : parameters.getStopPointRef()) {
				StopPointRefStructure stopPointRef = factory.createStopPointRefStructure();
				stopPointRef.setValue(item);
				extension.getStopPointRef().add(stopPointRef);
			}
		} else if (parameters.getRouteRef() != null) {
			hasFilter = true;
			for (String item : parameters.getRouteRef()) {
				RouteRefStructure routeRef = factory.createRouteRefStructure();
				routeRef.setValue(item);
				extension.getRouteRef().add(routeRef);
			}
		} else if (parameters.getDestinationRef() != null) {
			hasFilter = true;
			for (String item : parameters.getDestinationRef()) {
				DestinationRefStructure destinationRef = factory.createDestinationRefStructure();
				destinationRef.setValue(item);
				extension.getDestinationRef().add(destinationRef);
			}
		} else if (parameters.getJourneyPatternRef() != null) {
			hasFilter = true;
			for (String item : parameters.getJourneyPatternRef()) {
				JourneyPatternRefStructure journeyPatternRef = factory.createJourneyPatternRefStructure();
				journeyPatternRef.setValue(item);
				extension.getJourneyPatternRef().add(journeyPatternRef);
			}
		} else if (parameters.getGroupeOfLinesRef() != null) {
			hasFilter = true;
			for (String item : parameters.getGroupeOfLinesRef()) {
				GroupOfLinesRefStructure groupOfLinesRef = factory.createGroupOfLinesRefStructure();
				groupOfLinesRef.setValue(item);
				extension.getGroupOfLinesRef().add(groupOfLinesRef);
			}
		}

		if (hasFilter) {
			ExtensionsStructure extensions = factory.createExtensionsStructure();
			extensions.getAny().add(element);
			request.setExtensions(extensions);
		}
	}

	public enum FilterType {
		None, LineRef, StopRef, RouteRef, JourneyPatternRef
	}

	static {
		RequestStructureFactory.register(new GeneralMessageRequestStructureFactory());

	}
}
