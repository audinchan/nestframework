package ${hss_service_package}.ext.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.nestframework.commons.hibernate.DynamicSqlUtil;
import org.nestframework.commons.hibernate.IPage;
import org.nestframework.commons.hibernate.IQueryProvider;
import org.nestframework.commons.hibernate.IRowHandler;
import org.nestframework.commons.hibernate.ISqlElement;
import org.nestframework.commons.hibernate.JdbcPage;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

@SuppressWarnings("unchecked")
public class JdbcRootManager extends JdbcDaoSupport {
	protected IQueryProvider queryProvider;

	/**
	 * @param queryProvider
	 *            the queryProvider to set
	 */
	public void setQueryProvider(IQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
	}

	/**
	 * 处理动态sql.
	 * 
	 * @param params 参数.
	 * @param name 动态Sql的名称.
	 * @return
	 * @throws Exception
	 */
	protected ISqlElement processSql(Map<String, Object> params, String name)
			throws Exception {
		ISqlElement rs = DynamicSqlUtil.processSql(params, queryProvider
				.getQuery(name));

		return rs;
	}
	
	/**
	 * 根据动态Sql名称、参数查找分页对象.
	 * 
	 * @param <E> 分页对象中的元素的类型.
	 * @param querySqlName 查询Sql的名称.
	 * @param countSqlName 统计总数Sql的名称.
	 * @param params 参数.
	 * @param pageNo 第几页.
	 * @param pageSize 每页显示多少条数据.
	 * @param rh 行处理接口.
	 * @return
	 * @throws Exception 异常.
	 */
	protected <E> IPage<E> find(String querySqlName, String countSqlName, Map<String, Object> params, final int pageNo, final int pageSize, final IRowHandler<E> rh) throws Exception {
		final ISqlElement seQuery = processSql(params, querySqlName);
		final ISqlElement seCount = processSql(params, countSqlName);
		final int count = getJdbcTemplate().queryForInt(seCount.getSql(), seCount.getParams());
		return (IPage<E>) getJdbcTemplate().query(seQuery.getSql(), new ResultSetExtractor() {
		
			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				return new JdbcPage<E>(rs, count, pageNo, pageSize, rh);
			}
		
		});
	}
}
