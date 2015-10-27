package siri_lite.common;

import java.util.HashMap;
import java.util.Map;

import uk.org.siri.siri.ObjectFactory;

@SuppressWarnings("rawtypes")
public abstract class RequestStructureFactory<T, P extends DefaultParameters> {

	private static Map<String, RequestStructureFactory> factories = new HashMap<String, RequestStructureFactory>();
	protected static ObjectFactory factory = new ObjectFactory();

	protected abstract T create(Configuration config, P param);

	@SuppressWarnings("unchecked")
	public static final <T, P extends DefaultParameters> T create(
			Class<?> clazz, Configuration configuration, P parameters)
			throws ClassNotFoundException {
		String name = parameters.getClass().getPackage().getName() + "."
				+ clazz.getSimpleName() + "Factory";
		if (!factories.containsKey(name)) {
			Class.forName(name);
			if (!factories.containsKey(name))
				throw new ClassNotFoundException(clazz.getName());
		}
		return (T) ((RequestStructureFactory) factories.get(name)).create(
				configuration, parameters);
	}

	public static final <T, P extends DefaultParameters> void register(
			RequestStructureFactory<T, P> factory) {
		RequestStructureFactory.factories.put(factory.getClass().getName(),
				factory);
	}
}
