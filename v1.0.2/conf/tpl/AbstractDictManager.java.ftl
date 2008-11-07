/**
 * 
 */
package ${hss_service_package}.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author audin
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @param <D> 字典类型的属性类型
 */
public abstract class AbstractDictManager<T, K extends Serializable, D extends Serializable> extends CachableBaseManager<T, K> {
	
	private Map<D, List<T>> listCache = Collections.synchronizedMap(new HashMap<D, List<T>>());
	
	/**
	 * 获取表示字典类型的属性名.
	 * @return
	 */
	protected abstract String getDictTypePropertyName();

	@Override
	public void clearCache() {
		super.clearCache();
		listCache.clear();
	}
	
	/**
	 * 根据字典类型获取字典对象列表.
	 * 
	 * @param dictType 字典类型.
	 * @param useCache 是否使用缓存.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getDictsByType(D dictType, boolean useCache) {
		List<T> list = null;
		if (useCache) {
			list = listCache.get(dictType);
		}
		if (list == null) {
			list = getHibernateTemplate().find(
				"from " + getGenericClass(getClass()).getSimpleName() + " where "
						+ getDictTypePropertyName() + "=?", dictType);
			listCache.put(dictType, list);
		}
		
		return list;
	}
	
	/**
	 * 根据字典类型获取字典对象列表(不使用缓存).
	 * 
	 * @param dictType 字典类型.
	 * @return
	 */
	public List<T> getDictsByType(D dictType) {
		return getDictsByType(dictType, false);
	}
}
