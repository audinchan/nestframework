package org.nestframework.commons.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IHibernateManager<T, K extends Serializable> {

	/**
	 * 根据动态Hql查询分页对象.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param dqCount 计算查询结果集记录数的动态Hql名称，如果为null，则自动使用dqQuery并在之前添加“select count(*) ”.
	 * @param paras 查询参数，如果是null，则作为没有参数处理.
	 * @param pageNumber 第几页.
	 * @param pageSize 每页显示记录数.
	 * @return 分页对象.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final Map<String, Object> paras,
			final int pageNumber, final int pageSize);

	/**
	 * 根据动态Hql查询分页对象(单个参数).
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param dqCount 计算查询结果集记录数的动态Hql名称.
	 * @param paraName 参数名.
	 * @param paraValue 参数值.
	 * @param pageNumber 第几页.
	 * @param pageSize 每页显示记录数.
	 * @return 分页对象.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, String paraName, Object paraValue,
			final int pageNumber, final int pageSize);

	/**
	 * 根据动态Hql查询分页对象(没有参数).
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param dqCount 计算查询结果集记录数的动态Hql名称.
	 * @param pageNumber 第几页.
	 * @param pageSize 每页显示记录数.
	 * @return 分页对象.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final int pageNumber, final int pageSize);

	/**
	 * 根据动态Hql查询分页对象.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param dqCount 计算查询结果集记录数的动态Hql名称.
	 * @param paraName 参数名.
	 * @param paraValue 参数值.
	 * @param pageNumber 第几页.
	 * @param pageSize 每页显示记录数.
	 * @return 分页对象.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, String[] paraName, Object[] paraValue,
			final int pageNumber, final int pageSize);

	/**
	 * 根据动态Hql查询对象列表.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param paraName 参数名.
	 * @param paraValue 参数值.
	 * @return 对象列表.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			String[] paraName, Object[] paraValue);

	/**
	 * 根据动态Hql查询对象列表(单个参数).
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param paraName 参数名.
	 * @param paraValue 参数值.
	 * @return 对象列表.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			String paraName, Object paraValue);

	/**
	 * 根据动态Hql查询对象列表(没有参数).
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @return 对象列表.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery);

	/**
	 * 根据动态Hql查询对象列表.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param paras 查询参数.
	 * @return 对象列表.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			final Map<String, Object> paras);

	/**
	 * 根据动态Hql查询对象列表.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param paras 查询参数.
	 * @param firstResult 从第几条记录开始取数据(第一条记录是从0开始计数的).
	 * @param maxResults 最多取几条记录.
	 * @return 对象列表.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			final Map<String, Object> paras, final Integer firstResult,
			final Integer maxResults);

	/**
	 * 根据属性名和属性值查找对象列表.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @param maxResult 最多返回多少条记录. 小于等于0则表示不限制.
	 * @return 对象列表.
	 */
	public List<T> findByProperty(String propertyName,
			final Object propertyValue, final int maxResult);

	/**
	 * 根据属性名和属性值查找对象列表.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @return 对象列表.
	 */
	public List<T> findByProperty(String propertyName,
			final Object propertyValue);

	/**
	 * 根据属性名和属性值查找单个对象.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @return 对象.
	 */
	public T getByProperty(String propertyName, Object propertyValue);

	/**
	 * 根据主键更新属性.
	 * 
	 * @param keyName 主键名称.
	 * @param keyValue 主键值.
	 * @param propertyName 属性名称.
	 * @param propertyValue 属性值.
	 */
	public void updateProperty(String keyName, K keyValue, String propertyName,
			Object propertyValue);

}