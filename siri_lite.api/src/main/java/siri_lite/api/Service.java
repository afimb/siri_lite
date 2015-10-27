package siri_lite.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import lombok.extern.log4j.Log4j;

import org.jboss.resteasy.annotations.GZIP;

import siri_lite.discovery.LinesDiscoveryService;
import siri_lite.discovery.StopPointsDiscoveryService;
import siri_lite.general_message.GeneralMessageService;
import siri_lite.stop_monitoring.StopMonitoringService;

@Path("/2.0.0")
@RequestScoped
@GZIP
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Log4j
public class Service {

	@Inject
	StopPointsDiscoveryService stopPointsDiscoveryService;

	@Inject
	LinesDiscoveryService linesDiscoveryService;

	@Inject
	StopMonitoringService stopMonitoringService;

	@Inject
	GeneralMessageService generalMessageService;

	@GET
	@Path("/stoppoints-discovery{encoding: (\\z|.json|.xml)}")
	public void stopPointsDiscovery(@Context HttpHeaders headers,
			@Context UriInfo uri, @Suspended final AsyncResponse response) {
		MultivaluedMap<String, String> properties = headers.getRequestHeaders();
		properties.putAll(uri.getPathParameters());
		properties.putAll(uri.getQueryParameters());
		log.info("[DSU] stoppoints-discovery : " + properties);
		stopPointsDiscoveryService.stopPointsDiscovery(properties, response);		
	}

	@GET
	@Path("/lines-discovery{encoding:(\\z|.json|.xml)}")
	public void linesDiscovery(@Context HttpHeaders headers,
			@Context UriInfo uri, @Suspended final AsyncResponse response) {
		MultivaluedMap<String, String> properties = headers.getRequestHeaders();
		properties.putAll(uri.getPathParameters());
		properties.putAll(uri.getQueryParameters());
		log.info("[DSU] stoppoints-discovery : " + properties);
		linesDiscoveryService.linesDiscovery(properties, response);
	}

	@GET
	@Path("/stop-monitoring{encoding: (\\z|.json|.xml)}")
	public void getStopMonitoring(@Context HttpHeaders headers,
			@Context UriInfo uri, @Suspended final AsyncResponse response) {
		MultivaluedMap<String, String> properties = headers.getRequestHeaders();
		properties.putAll(uri.getPathParameters());
		properties.putAll(uri.getQueryParameters());
		log.info("[DSU] stop-monitoring : " + properties);
		stopMonitoringService.getStopMonitoring(properties, response);
	}

	@GET
	@Path("general-message{encoding: (\\z|.json|.xml)}")
	public void getGeneralMessage(@Context HttpHeaders headers,
			@Context UriInfo uri, @Suspended final AsyncResponse response) {
		MultivaluedMap<String, String> properties = headers.getRequestHeaders();
		properties.putAll(uri.getPathParameters());
		properties.putAll(uri.getQueryParameters());
		log.info("[DSU] general-message : " + properties);
		generalMessageService.getGeneralMessage(properties, response);
	}
}
