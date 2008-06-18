package ${hss_service_package}.impl;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

<#if merge_dao>
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.nestframework.commons.hibernate.EmptyPage;
import org.nestframework.commons.hibernate.HibernatePage;
import org.nestframework.commons.hibernate.IPage;
import org.nestframework.commons.hibernate.IQueryProvider;
import org.nestframework.commons.hibernate.QueryWrap;
</#if>

import ${hss_service_package}.IRootManager;
<#if merge_dao>
<#if hss_jdk5>
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Map;
</#if>
</#if>

@SuppressWarnings("unchecked")
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
    
    /**
     * Dynamic HQL provider.
     */
    protected IQueryProvider queryProvider;

    public void setQueryProvider(IQueryProvider queryProvider)
    {
        this.queryProvider = queryProvider;
    }
    
    /**
     * Get dynamic HQL template.
     * @param name HQL's name.
     * @return HQL template.
     */
    protected String _(String name) {
        return queryProvider.getQuery(name);
    }
    
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
    protected IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final Map<String, Object> paras,
			final int pageNumber, final int pageSize) {
		return (IPage<T>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session ss)
							throws HibernateException, SQLException {
						try {
							QueryWrap q = new QueryWrap(ss);
							q.setParamters(paras);
							return new HibernatePage<T>(q.getQuery(_(dqQuery)),
									q.getQuery(_(dqCount)), pageNumber,
									pageSize);
						} catch (Exception e) {
							return new EmptyPage<T>(pageSize);
						}
					}

				});
	}
</#if>
}