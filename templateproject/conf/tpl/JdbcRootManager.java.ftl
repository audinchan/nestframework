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
	 * ����̬sql.
	 * 
	 * @param params ����.
	 * @param name ��̬Sql������.
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
	 * ���ݶ�̬Sql���ơ��������ҷ�ҳ����.
	 * 
	 * @param <E> ��ҳ�����е�Ԫ�ص�����.
	 * @param querySqlName ��ѯSql������.
	 * @param countSqlName ͳ������Sql������.
	 * @param params ����.
	 * @param pageNo �ڼ�ҳ.
	 * @param pageSize ÿҳ��ʾ����������.
	 * @param rh �д���ӿ�.
	 * @return
	 * @throws Exception �쳣.
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
