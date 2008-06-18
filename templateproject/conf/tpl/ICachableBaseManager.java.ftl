/**
 * 
 */
package ${hss_service_package};

import java.io.Serializable;

/**
 * 可缓存数据的Manager基类接口.
 * 
 * @author audin
 * 
 */
public interface ICachableBaseManager<T, K extends Serializable> {
	/**
	 * 根据实例对象获取标识号.
	 * 
	 * @param instance
	 *            实例对象.
	 * @return 标识号.
	 */
	public K getKey(T instance);

	/**
	 * 删除全部缓存.
	 */
	public void clearCache();

	/**
	 * 删除指定的缓存.
	 * 
	 * @param id
	 *            标识号.
	 */
	public void clearCache(K id);
}
