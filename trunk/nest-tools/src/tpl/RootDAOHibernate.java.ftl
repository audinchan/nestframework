package ${hss_dao_package}.hibernate;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ${hss_dao_package}.IRootDAO;
<#if hss_jdk5>
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
</#if>

public abstract class RootDAOHibernate<#if hss_jdk5><T, K extends Serializable></#if> extends HibernateDaoSupport implements IRootDAO<#if hss_jdk5><T, K></#if> {
<#if hss_jdk5>
	/**
	 * Locates the first generic declaration on a class.
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or <code>null</code> if cannot
	 *         be determined
	 */
	public static Class getGenericClass(Class clazz) {
		return getGenericClass(clazz, 0);
	}

	/**
	 * Locates generic declaration by index on a class.
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 */
	public static Class getGenericClass(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length >= (index - 1))) {
				return (Class) params[index];
			}
		}
		return null;
	}
</#if>
}
