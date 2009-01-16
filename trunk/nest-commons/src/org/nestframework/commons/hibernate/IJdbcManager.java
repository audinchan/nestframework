package org.nestframework.commons.hibernate;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface IJdbcManager {

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
	public ISqlElement processSql(Map<String, Object> params, String name)
			throws Exception;

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
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			Map<String, Object> params, final int pageNo, final int pageSize,
			final IRowHandler<E> rh) throws Exception;

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
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh,
			String[] paramName, Object... paramValue) throws Exception;

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
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh,
			String paramName, Object paramValue) throws Exception;

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
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh)
			throws Exception;

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
	public <E> List<E> findList(String sqlName, Map<String, Object> params,
			RowMapper rm) throws Exception;

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
	public <E> List<E> findList(String sqlName, RowMapper rm,
			String[] paramName, Object... paramValue) throws Exception;

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
	public <E> List<E> findList(String sqlName, RowMapper rm, String paramName,
			Object paramValue) throws Exception;

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
	public <E> List<E> findList(String sqlName, RowMapper rm) throws Exception;

}