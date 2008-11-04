/**
 * 
 */
package org.nestframework.commons.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author audin
 * 
 */
@SuppressWarnings("unchecked")
public abstract class HibernateManagerSupport<T, K extends Serializable>
		extends HibernateDaoSupport {
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(HibernateManagerSupport.class);
	
	/**
	 * Whether to enable query cache. Default is enabled.
	 */
	private boolean queryCacheEnabled = true;

	/**
	 * Whether to enable query cache. Default is enabled.
	 * 
	 * @param queryCacheEnabled
	 */
	public void setQueryCacheEnabled(boolean queryCacheEnabled) {
		this.queryCacheEnabled = queryCacheEnabled;
	}

	public static Class getGenericClass(Class clazz) {
		if (logger.isDebugEnabled()) {
			logger.debug("getGenericClass(Class) - start");
		}

		Class returnClass = getGenericClass(clazz, 0);
		if (logger.isDebugEnabled()) {
			logger.debug("getGenericClass(Class) - end");
		}
		return returnClass;
	}

	public static Class getGenericClass(Class clazz, int index) {
		if (logger.isDebugEnabled()) {
			logger.debug("getGenericClass(Class, int) - start");
		}

		Type genType = clazz.getGenericSuperclass();

		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length >= (index - 1))) {
				Class returnClass = (Class) params[index];
				if (logger.isDebugEnabled()) {
					logger.debug("getGenericClass(Class, int) - end");
				}
				return returnClass;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getGenericClass(Class, int) - end");
		}
		return null;
	}

	/**
	 * Dynamic HQL provider.
	 */
	protected IQueryProvider queryProvider;

	public void setQueryProvider(IQueryProvider queryProvider) {
		if (logger.isDebugEnabled()) {
			logger.debug("setQueryProvider(IQueryProvider) - start");
		}

		this.queryProvider = queryProvider;

		if (logger.isDebugEnabled()) {
			logger.debug("setQueryProvider(IQueryProvider) - end");
		}
	}

	/**
	 * Get dynamic HQL template.
	 * 
	 * @param name
	 *            HQL's name.
	 * @return HQL template.
	 */
	protected String _(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("_(String) - start, name=" + name);
		}

		String returnString = queryProvider.getQuery(name);
		if (logger.isDebugEnabled()) {
			logger.debug("_(String) - end");
		}
		return returnString;
	}

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
	protected IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final Map<String, Object> paras,
			final int pageNumber, final int pageSize) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, Map<String,Object>, int, int) - start, query=" + dqQuery + ",count=" + dqCount);
		}

		IPage<T> returnIPage = (IPage<T>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session ss)
							throws HibernateException, SQLException {
						if (logger.isDebugEnabled()) {
							logger.debug("doInHibernate(Session) - start");
						}

						try {
							QueryWrap q = new QueryWrap(ss);
							if (paras != null) {
								q.setParamters(paras);
							}
							Object returnObject = new HibernatePage<T>(
									q.getQuery(_(dqQuery)).setCacheable(queryCacheEnabled),
									q
											.getQuery(dqCount != null ? _(dqCount)
													: ("select count(*) " + _(dqCount))),
									pageNumber, pageSize);
							if (logger.isDebugEnabled()) {
								logger.debug("doInHibernate(Session) - end");
							}
							return returnObject;
						} catch (Exception e) {
							logger.error("doInHibernate(Session)", e);

							Object returnObject = new EmptyPage<T>(pageSize);
							if (logger.isDebugEnabled()) {
								logger.debug("doInHibernate(Session) - end");
							}
							return returnObject;
						}
					}

				});
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, Map<String,Object>, int, int) - end");
		}
		return returnIPage;
	}

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
	protected IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, String paraName, Object paraValue,
			final int pageNumber, final int pageSize) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, String, Object, int, int) - start");
		}

		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(paraName, paraValue);
		IPage<T> returnIPage = findByDynamicQuery(dqQuery, dqCount, paras,
				pageNumber, pageSize);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, String, Object, int, int) - end");
		}
		return returnIPage;
	}
	
	/**
	 * 根据动态Hql查询分页对象(没有参数).
     * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param dqCount 计算查询结果集记录数的动态Hql名称.
	 * @param pageNumber 第几页.
	 * @param pageSize 每页显示记录数.
	 * @return 分页对象.
	 */
	protected IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount,
			final int pageNumber, final int pageSize) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, int, int) - start");
		}

		IPage<T> returnIPage = findByDynamicQuery(dqQuery, dqCount, null,
				pageNumber, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("findByDynamicQuery(String, String, int, int) - end");
		}
		return returnIPage;
	}

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
	protected IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, String[] paraName, Object[] paraValue,
			final int pageNumber, final int pageSize) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, String[], Object[], int, int) - start");
		}

		Map<String, Object> paras = new HashMap<String, Object>();
		for (int i = 0; i < paraName.length; i++) {
			paras.put(paraName[i], paraValue[i]);
		}
		IPage<T> returnIPage = findByDynamicQuery(dqQuery, dqCount, paras,
				pageNumber, pageSize);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findByDynamicQuery(String, String, String[], Object[], int, int) - end");
		}
		return returnIPage;
	}

    /**
     * 根据动态Hql查询对象列表.
	 * 
     * @param dqQuery 查询用的动态Hql名称.
     * @param paraName 参数名.
     * @param paraValue 参数值.
     * @return 对象列表.
     */
	protected List<T> findListByDynamicQuery(final String dqQuery,
			String[] paraName, Object[] paraValue) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, String[], Object[]) - start");
		}

		Map<String, Object> paras = new HashMap<String, Object>();
		for (int i = 0; i < paraName.length; i++) {
			paras.put(paraName[i], paraValue[i]);
		}
		List<T> returnList = findListByDynamicQuery(dqQuery, paras, null, null);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, String[], Object[]) - end");
		}
		return returnList;
	}

    /**
     * 根据动态Hql查询对象列表(单个参数).
	 * 
     * @param dqQuery 查询用的动态Hql名称.
     * @param paraName 参数名.
     * @param paraValue 参数值.
     * @return 对象列表.
     */
	protected List<T> findListByDynamicQuery(final String dqQuery,
			String paraName, Object paraValue) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, String, Object) - start");
		}

		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(paraName, paraValue);
		List<T> returnList = findListByDynamicQuery(dqQuery, paras, null, null);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, String, Object) - end");
		}
		return returnList;
	}
	
    /**
     * 根据动态Hql查询对象列表(没有参数).
	 * 
     * @param dqQuery 查询用的动态Hql名称.
     * @return 对象列表.
     */
	protected List<T> findListByDynamicQuery(final String dqQuery) {
		if (logger.isDebugEnabled()) {
			logger.debug("findListByDynamicQuery(String) - start");
		}

		List<T> returnList = findListByDynamicQuery(dqQuery, null, null, null);
		if (logger.isDebugEnabled()) {
			logger.debug("findListByDynamicQuery(String) - end");
		}
		return returnList;
	}
	
	/**
	 * 根据动态Hql查询对象列表.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param paras 查询参数.
	 * @return 对象列表.
	 */
	protected List<T> findListByDynamicQuery(final String dqQuery,
			final Map<String, Object> paras) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, Map<String,Object>) - start");
		}

		List<T> returnList = findListByDynamicQuery(dqQuery, paras, null, null);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, Map<String,Object>) - end");
		}
		return returnList;
	}

	/**
	 * 根据动态Hql查询对象列表.
	 * 
	 * @param dqQuery 查询用的动态Hql名称.
	 * @param paras 查询参数.
	 * @param firstResult 从第几条记录开始取数据(第一条记录是从0开始计数的).
	 * @param maxResults 最多取几条记录.
	 * @return 对象列表.
	 */
	protected List<T> findListByDynamicQuery(final String dqQuery,
			final Map<String, Object> paras, final Integer firstResult,
			final Integer maxResults) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, Map<String,Object>, Integer, Integer) - start, query=" + dqQuery);
		}

		List<T> returnList = (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session ss)
							throws HibernateException, SQLException {
						if (logger.isDebugEnabled()) {
							logger.debug("doInHibernate(Session) - start");
						}

						try {
							QueryWrap q = new QueryWrap(ss);
							q.setParamters(paras);
							Query qry = q.getQuery(_(dqQuery));
							if (firstResult != null) {
								qry.setFirstResult(firstResult);
							}
							if (maxResults != null) {
								qry.setMaxResults(maxResults);
							}
							Object returnObject = qry.setCacheable(queryCacheEnabled).list();
							if (logger.isDebugEnabled()) {
								logger.debug("doInHibernate(Session) - end");
							}
							return returnObject;
						} catch (Exception e) {
							logger.error("doInHibernate(Session)", e);

							Object returnObject = new ArrayList<T>();
							if (logger.isDebugEnabled()) {
								logger.debug("doInHibernate(Session) - end");
							}
							return returnObject;
						}
					}

				});
		if (logger.isDebugEnabled()) {
			logger
					.debug("findListByDynamicQuery(String, Map<String,Object>, Integer, Integer) - end");
		}
		return returnList;
	}
	
	/**
	 * 根据属性名和属性值查找对象列表.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @param maxResult 最多返回多少条记录. 小于等于0则表示不限制.
	 * @return 对象列表.
	 */
	protected List<T> findByProperty(String propertyName, final Object propertyValue, final int maxResult) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByProperty(String, Object, int) - start, propName=" + propertyName + ",propValue=" + propertyValue + ",max=" + maxResult);
		}

		final String hql = "from " + getGenericClass(getClass()).getSimpleName()
		+ " where " + propertyName + "=?";
		
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
		
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createQuery(hql)
					.setParameter(0, propertyValue)
					.setCacheable(queryCacheEnabled);
				
				if (maxResult > 0) {
					query.setMaxResults(maxResult);
				}
				
				return query.list();
			}
		
		});

		if (logger.isDebugEnabled()) {
			logger.debug("findByProperty(String, Object, int) - end");
		}
		return list;
	}

	/**
	 * 根据属性名和属性值查找对象列表.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @return 对象列表.
	 */
	protected List<T> findByProperty(String propertyName, final Object propertyValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByProperty(String, Object) - start");
		}

		List list = findByProperty(propertyName, propertyValue, 0);
		
		if (logger.isDebugEnabled()) {
			logger.debug("findByProperty(String, Object) - end");
		}
		return list;
	}

	/**
	 * 根据属性名和属性值查找单个对象.
	 * 
	 * @param propertyName 属性名.
	 * @param propertyValue 属性值.
	 * @return 对象.
	 */
	protected T getByProperty(String propertyName, Object propertyValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByProperty(String, Object) - start");
		}

		List<T> list = findByProperty(propertyName, propertyValue, 1);
		if (list != null && list.size() > 0) {
			T returnT = list.get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("getByProperty(String, Object) - end");
			}
			return returnT;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getByProperty(String, Object) - end");
			}
			return null;
		}
	}
}
