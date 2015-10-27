package siri_lite.common;

public class SiriProducerDocServicesFactory {
	private static final CommonsObjectPoolFactory factory = new CommonsObjectPoolFactory();

	public static CommonsObjectPoolFactory getInstance() {
		return factory;
	}

}
