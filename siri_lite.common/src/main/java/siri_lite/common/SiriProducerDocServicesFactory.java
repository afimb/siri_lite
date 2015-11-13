package siri_lite.common;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class SiriProducerDocServicesFactory {

	private static GenericObjectPool<SiriProducerDocServices> instance;

	public static synchronized SiriProducerDocServices make() {
		SiriProducerDocServices result = null;
		try {
			ObjectPool<SiriProducerDocServices> pool = getInstance();
			result = pool.borrowObject();
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
		return result;
	}

	public static synchronized void passivate(SiriProducerDocServices o) {
		try {
			ObjectPool<SiriProducerDocServices> pool = getInstance();
			pool.returnObject(o);
		} catch (Exception ignored) {
		}
	}

	public static synchronized void invalidate(SiriProducerDocServices o) {
		try {
			ObjectPool<SiriProducerDocServices> pool = getInstance();
			pool.invalidateObject(o);
		} catch (Exception ignored) {
		}
	}

	private static ObjectPool<SiriProducerDocServices> getInstance() {
		if (instance == null) {
			PoolableObjectFactory<SiriProducerDocServices> factory = new BasePoolableObjectFactory<SiriProducerDocServices>() {

				@Override
				public SiriProducerDocServices makeObject() throws Exception {
					SiriProducerDocServices service = new SiriProducerDocServices();
					service.initialize();
					return service;
				}

				@Override
				public void destroyObject(SiriProducerDocServices service)
						throws Exception {
					service.dispose();
				}
			};

			instance = new GenericObjectPool<SiriProducerDocServices>(factory);
			instance.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
			String value = System.getProperty("max.request");
			if (value != null && !value.isEmpty()) {
				int maxActive = Integer.valueOf(value);
				if (maxActive > 0) {
					instance.setMaxActive(maxActive);
					instance.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_FAIL);
				}
			}
		}

		return instance;
	}

}
