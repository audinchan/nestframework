/**
 * 
 */
package ${hss_service_package}.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ${hss_service_package}.ICachableBaseManager;

/**
 * @author audin
 * 
 */
public abstract class CachableBaseManager<T, K extends Serializable> extends
		BaseManager<T, K> implements ICachableBaseManager<T, K> {
	private Map<K, T> cache = Collections.synchronizedMap(new HashMap<K, T>());

	@Override
	public T get(K id) {
		T t = cache.get(id);
		if (t == null) {
			t = super.get(id);
			cache.put(id, t);
		}
		return t;
	}

	@Override
	public void remove(T instance) {
		cache.remove(getKey(instance));
		super.remove(instance);
	}

	@Override
	public void removeById(K id) {
		cache.remove(id);
		super.removeById(id);
	}

	@Override
	public void save(T instance) {
		super.save(instance);
		cache.put(getKey(instance), instance);
	}

	public void clearCache() {
		cache.clear();
	}

	public void clearCache(K id) {
		cache.remove(id);
	}

}
