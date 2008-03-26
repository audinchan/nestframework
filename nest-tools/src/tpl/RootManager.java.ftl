package ${hss_service_package}.impl;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

<#if merge_dao>
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
</#if>

import ${hss_service_package}.IRootManager;
<#if merge_dao>
<#if hss_jdk5>
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
</#if>
</#if>

public abstract class RootManager<#if hss_jdk5><T, K extends Serializable></#if><#if merge_dao> extends HibernateDaoSupport</#if> implements IRootManager<#if hss_jdk5><T, K></#if> {
<#if merge_dao>
<#if hss_jdk5>
	public static Class getGenericClass(Class clazz) {
		return getGenericClass(clazz, 0);
	}

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
</#if>
}