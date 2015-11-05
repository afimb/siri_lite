package siri_lite.common;

import java.util.NoSuchElementException;

import lombok.extern.log4j.Log4j;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

@Log4j
public class SiriProducerDocServicesFactory {

	private static GenericObjectPool<SiriProducerDocServices> instance;

	public static SiriProducerDocServices make() {
		SiriProducerDocServices result = null;
		try {
			ObjectPool<SiriProducerDocServices> pool = getInstance();
			result = pool.borrowObject();
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
		return result;
	}

	public static void passivate(SiriProducerDocServices o) {
		try {
			ObjectPool<SiriProducerDocServices> pool = getInstance();
			pool.returnObject(o);
		} catch (Exception ignored) {
		}
	}

	public static void invalidate(SiriProducerDocServices o) {
		try {
			ObjectPool<SiriProducerDocServices> pool = getInstance();
			pool.invalidateObject(o);
		} catch (Exception ignored) {
		}
	}

	private static synchronized ObjectPool<SiriProducerDocServices> getInstance() {
		if (instance == null) {
			PoolableObjectFactory<SiriProducerDocServices> factory = new BasePoolableObjectFactory<SiriProducerDocServices>() {

				@Override
				public SiriProducerDocServices makeObject() throws Exception {
					return new SiriProducerDocServices();
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
