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
public class JdbcManagerSupport extends JdbcDaoSupport {
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

	/**
	 * 处理动态sql.
	 * 
	 * @param params
	 *            参数.
	 * @param name
	 *            动态Sql的名称.
	 * @return
	 * @throws Exception
	 */
	protected ISqlElement processSql(Map<String, Object> params, String name)
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

	/**
	 * 根据动态Sql名称、参数查找分页对象.
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param querySqlName
	 *            查询Sql的名称.
	 * @param countSqlName
	 *            统计总数Sql的名称.
	 * @param params
	 *            参数.
	 * @param pageNo
	 *            第几页.
	 * @param pageSize
	 *            每页显示多少条数据.
	 * @param rh
	 *            行处理接口.
	 * @return 分页对象.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> IPage<E> findPage(String querySqlName, String countSqlName,
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

	/**
	 * 根据动态Sql名称、参数查找分页对象.
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param querySqlName
	 *            查询Sql的名称.
	 * @param countSqlName
	 *            统计总数Sql的名称.
	 * @param pageNo
	 *            第几页.
	 * @param pageSize
	 *            每页显示多少条数据.
	 * @param rh
	 *            行处理接口.
	 * @param paramName
	 *            参数名
	 * @param paramValue
	 *            参数值
	 * @return 分页对象.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> IPage<E> findPage(String querySqlName, String countSqlName,
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

	/**
	 * 根据动态Sql名称、参数查找分页对象(单个参数).
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param querySqlName
	 *            查询Sql的名称.
	 * @param countSqlName
	 *            统计总数Sql的名称.
	 * @param pageNo
	 *            第几页.
	 * @param pageSize
	 *            每页显示多少条数据.
	 * @param rh
	 *            行处理接口.
	 * @param paramName
	 *            参数名.
	 * @param paramValue
	 *            参数值.
	 * @return 分页对象.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> IPage<E> findPage(String querySqlName, String countSqlName,
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

	/**
	 * 根据动态Sql名称、参数查找分页对象(没有参数).
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param querySqlName
	 *            查询Sql的名称.
	 * @param countSqlName
	 *            统计总数Sql的名称.
	 * @param pageNo
	 *            第几页.
	 * @param pageSize
	 *            每页显示多少条数据.
	 * @param rh
	 *            行处理接口.
	 * @return 分页对象.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> IPage<E> findPage(String querySqlName, String countSqlName,
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
	
	/**
	 * 根据动态Sql名称、参数查询对象列表.
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param sqlName
	 *            查询Sql的名称.
	 * @param params
	 *            参数.
	 * @param rm
	 *            行数据映射处理器.
	 * @return 对象列表.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> List<E> findList(String sqlName, Map<String, Object> params,
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

	/**
	 * 根据动态Sql名称、参数查询对象列表.
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param sqlName
	 *            查询Sql的名称.
	 * @param rm
	 *            行数据映射处理器.
	 * @param paramName
	 *            参数名.
	 * @param paramValue
	 *            参数值.
	 * @return 对象列表.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> List<E> findList(String sqlName, RowMapper rm,
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

	/**
	 * 根据动态Sql名称、参数查询对象列表(单个参数).
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param sqlName
	 *            查询Sql的名称.
	 * @param rm
	 *            行数据映射处理器.
	 * @param paramName
	 *            参数名.
	 * @param paramValue
	 *            参数值.
	 * @return 对象列表.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> List<E> findList(String sqlName, RowMapper rm,
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

	/**
	 * 根据动态Sql名称、参数查询对象列表(没有参数).
	 * 
	 * @param <E>
	 *            分页对象中的元素的类型.
	 * @param sqlName
	 *            查询Sql的名称.
	 * @param rm
	 *            行数据映射处理器.
	 * @return 对象列表.
	 * @throws Exception
	 *             异常.
	 */
	protected <E> List<E> findList(String sqlName, RowMapper rm) throws Exception {
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
