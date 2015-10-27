package siri_lite.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;

public class CommonsObjectPoolFactory {

	private final Map<Class<?>, ObjectPool<?>> FACTORIES = new HashMap<Class<?>, ObjectPool<?>>();

	public <T> T make(Class<T> clazz) {
		try {
			ObjectPool<T> pool = getInstance(clazz);
			T object = pool.borrowObject();
			return object;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void passivate(T o) {
		Class<T> clazz = (Class<T>) o.getClass();
		ObjectPool<T> pool = getInstance(clazz);
		try {
			pool.returnObject(o);
		} catch (Exception ignored) {
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void invalidate(T o) {
		Class<T> clazz = (Class<T>) o.getClass();
		ObjectPool<T> pool = getInstance(clazz);
		try {
			pool.invalidateObject(o);
		} catch (Exception ignored) {
		}
	}

	@SuppressWarnings("rawtypes")
	private synchronized <T> ObjectPool<T> getInstance(final Class<T> key) {
		if (!FACTORIES.containsKey(key)) {

			PoolableObjectFactory<T> factory = new BasePoolableObjectFactory<T>() {

				@Override
				public void passivateObject(T obj) throws Exception {
					doPassivate(obj);
				}

				@Override
				public T makeObject() throws Exception {
					return key.newInstance();
				}

			};
			ObjectPool<T> pool = new StackObjectPool<T>(factory);

			FACTORIES.put(key, pool);

		}

		return (ObjectPool) FACTORIES.get(key);

	}

	protected <T> void doPassivate(T bean) throws Exception {

	}
}
