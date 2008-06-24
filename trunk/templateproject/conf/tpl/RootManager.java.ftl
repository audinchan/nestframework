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
import java.util.List;
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
    
    public IPage<T> findByDynamicQuery(final String dqQuery,
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
    
    public List<T> findByProperty(String propertyName, Object propertyValue) {
    	List list = getHibernateTemplate().find("from " + getGenericClass(getClass()).getSimpleName() + " where " + propertyName + "=?", propertyValue);
    	return list;
    }
    
    public T getByProperty(String propertyName, Object propertyValue) {
    	List<T> list = findByProperty(propertyName, propertyValue);
    	if (list != null && list.size() > 0) {
    		return list.get(0);
    	} else {
    		return null;
    	}
    }
</#if>
}