package ${hss_service_package};
// Generated ${date} by Hibernate Tools ${version} with nest-tools

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nestframework.commons.hibernate.IPage;

public interface IRootManager<#if hss_jdk5><T, K extends Serializable></#if> {
    /**
     * 根据动态Hql查询分页对象.
     * 
     * @param dqQuery 查询用的动态Hql名称.
     * @param dqCount 计算查询结果集记录数的动态Hql名称.
     * @param paras 查询参数.
     * @param pageNumber 第几页.
     * @param pageSize 每页显示记录数.
     * @return
     */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final Map<String, Object> paras,
			final int pageNumber, final int pageSize);
	
	/**
	 * 根据属性名和属性值查找对象列表.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @return 对象列表.
	 */
	public List<T> findByProperty(String propertyName, Object propertyValue);
	
	/**
	 * 根据属性名和属性值查找单个对象.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @return 对象.
	 */
	public T getByProperty(String propertyName, Object propertyValue);
}
