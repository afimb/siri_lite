package siri_lite.test;

import javax.xml.ws.Endpoint;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
class Server {

	private Endpoint endpoint;

	@Getter
	@Setter
	private Object request;

	@Getter
	@Setter
	private Object serviceRequestInfo;

	protected void initialize() throws Exception {
		// log.info(Color.YELLOW + "[DSU] ---------------> TEST : "
		// + this.getClass().getSimpleName() + Color.NORMAL);

		String address = "http://localhost:20080/siri";
		endpoint = Endpoint.create(this);
		endpoint.publish(address);
	}

	protected void dispose() throws Exception {
		if (endpoint != null) {
			endpoint.stop();
			Utils.sleep(1000);
		}
	}

}
