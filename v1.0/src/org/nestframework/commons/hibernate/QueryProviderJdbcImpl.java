package org.nestframework.commons.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 * Get hibernate dynamic query from database using jdbc.
 * 
 * @author audin
 */
public class QueryProviderJdbcImpl extends SimpleJdbcDaoSupport implements IQueryProvider {
	private String sql = "select hql from query where name = ?";

	/**
	 * The sql to fetch query string from database.
	 * @param sql
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getQuery(String name) {
		return getSimpleJdbcTemplate().queryForObject(sql, new ParameterizedRowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		
		}, name);
	}

}
