/**
 * 
 */
package org.nestframework.commons.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author audin
 * 
 */
@SuppressWarnings("unchecked")
public class JdbcManagerSupport extends JdbcDaoSupport implements IJdbcManager {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(JdbcManagerSupport.class);

	protected IQueryProvider queryProvider;

	/**
	 * @param queryProvider
	 *            the queryProvider to set
	 */
	public void setQueryProvider(IQueryProvider queryProvider) {
		if (logger.isDebugEnabled()) {
			logger.debug("setQueryProvider(IQueryProvider) - start");
		}

		this.queryProvider = queryProvider;

		if (logger.isDebugEnabled()) {
			logger.debug("setQueryProvider(IQueryProvider) - end");
		}
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#processSql(java.util.Map, java.lang.String)
	 */
	public ISqlElement processSql(Map<String, Object> params, String name)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("processSql(Map<String,Object>, String) - start, name=" + name);
		}

		ISqlElement rs = DynamicSqlUtil.processSql(params, queryProvider
				.getQuery(name));

		if (logger.isDebugEnabled()) {
			logger.debug("processSql(Map<String,Object>, String) - end");
		}
		return rs;
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findPage(java.lang.String, java.lang.String, java.util.Map, int, int, org.nestframework.commons.hibernate.IRowHandler)
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			Map<String, Object> params, final int pageNo, final int pageSize,
			final IRowHandler<E> rh) throws Exception {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, Map<String,Object>, int, int, IRowHandler<E>) - start");
		}

		final ISqlElement seQuery = processSql(params, querySqlName);
		final ISqlElement seCount = processSql(params, countSqlName);
		final int count = getJdbcTemplate().queryForInt(seCount.getSql(),
				seCount.getParams());
		IPage<E> returnIPage = (IPage<E>) getJdbcTemplate().query(
				seQuery.getSql(), seQuery.getParams(),
				new ResultSetExtractor() {

					public Object extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (logger.isDebugEnabled()) {
							logger.debug("extractData(ResultSet) - start");
						}

						Object returnObject = new JdbcPage<E>(rs, count,
								pageNo, pageSize, rh);
						if (logger.isDebugEnabled()) {
							logger.debug("extractData(ResultSet) - end");
						}
						return returnObject;
					}

				});
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, Map<String,Object>, int, int, IRowHandler<E>) - end");
		}
		return returnIPage;
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findPage(java.lang.String, java.lang.String, int, int, org.nestframework.commons.hibernate.IRowHandler, java.lang.String[], java.lang.Object)
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh,
			String[] paramName, Object... paramValue) throws Exception {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, int, int, IRowHandler<E>, String[], Object) - start");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		for (int i = 0; i < paramName.length; i++) {
			params.put(paramName[i], paramValue[i]);
		}

		IPage<E> returnIPage = findPage(querySqlName, countSqlName, params,
				pageNo, pageSize, rh);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, int, int, IRowHandler<E>, String[], Object) - end");
		}
		return returnIPage;
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findPage(java.lang.String, java.lang.String, int, int, org.nestframework.commons.hibernate.IRowHandler, java.lang.String, java.lang.Object)
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh,
			String paramName, Object paramValue) throws Exception {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, int, int, IRowHandler<E>, String, Object) - start");
		}

		IPage<E> returnIPage = findPage(querySqlName, countSqlName, pageNo,
				pageSize, rh, new String[] { paramName }, paramValue);
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, int, int, IRowHandler<E>, String, Object) - end");
		}
		return returnIPage;
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findPage(java.lang.String, java.lang.String, int, int, org.nestframework.commons.hibernate.IRowHandler)
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh) throws Exception {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, int, int, IRowHandler<E>) - start");
		}

		IPage<E> returnIPage = findPage(querySqlName, countSqlName, pageNo,
				pageSize, rh, new String[] {}, "");
		if (logger.isDebugEnabled()) {
			logger
					.debug("findPage(String, String, int, int, IRowHandler<E>) - end");
		}
		return returnIPage;
	}
	
	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findList(java.lang.String, java.util.Map, org.springframework.jdbc.core.RowMapper)
	 */
	public <E> List<E> findList(String sqlName, Map<String, Object> params,
			RowMapper rm) throws Exception {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findList(String, Map<String,Object>, RowMapper) - start");
		}

		final ISqlElement se = processSql(params, sqlName);
		if (rm == null) {
			List<E> returnList = getJdbcTemplate().queryForList(se.getSql(),
					se.getParams());
			if (logger.isDebugEnabled()) {
				logger
						.debug("findList(String, Map<String,Object>, RowMapper) - end");
			}
			return returnList;
		} else {
			List<E> returnList = getJdbcTemplate().query(se.getSql(),
					se.getParams(), rm);
			if (logger.isDebugEnabled()) {
				logger
						.debug("findList(String, Map<String,Object>, RowMapper) - end");
			}
			return returnList;
		}
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findList(java.lang.String, org.springframework.jdbc.core.RowMapper, java.lang.String[], java.lang.Object)
	 */
	public <E> List<E> findList(String sqlName, RowMapper rm,
			String[] paramName, Object... paramValue) throws Exception {
		if (logger.isDebugEnabled()) {
			logger
					.debug("findList(String, RowMapper, String[], Object) - start");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		for (int i = 0; i < paramName.length; i++) {
			params.put(paramName[i], paramValue[i]);
		}

		List<E> returnList = findList(sqlName, params, rm);
		if (logger.isDebugEnabled()) {
			logger.debug("findList(String, RowMapper, String[], Object) - end");
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findList(java.lang.String, org.springframework.jdbc.core.RowMapper, java.lang.String, java.lang.Object)
	 */
	public <E> List<E> findList(String sqlName, RowMapper rm,
			String paramName, Object paramValue) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("findList(String, RowMapper, String, Object) - start");
		}

		List<E> returnList = findList(sqlName, rm, new String[] { paramName },
				paramValue);
		if (logger.isDebugEnabled()) {
			logger.debug("findList(String, RowMapper, String, Object) - end");
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.nestframework.commons.hibernate.IJdbcManager#findList(java.lang.String, org.springframework.jdbc.core.RowMapper)
	 */
	public <E> List<E> findList(String sqlName, RowMapper rm) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("findList(String, RowMapper) - start");
		}

		List<E> returnList = findList(sqlName, rm, new String[] {}, "");
		if (logger.isDebugEnabled()) {
			logger.debug("findList(String, RowMapper) - end");
		}
		return returnList;
	}	
	
}
