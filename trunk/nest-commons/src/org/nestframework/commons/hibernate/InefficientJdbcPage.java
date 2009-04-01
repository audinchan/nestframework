package org.nestframework.commons.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InefficientJdbcPage<E> extends AbstractPage<E> {

	/**
	 * JDBC page.<br>
	 * 
	 * Use scoll to count total results.
	 * 
	 * @param rs Jdbc ResultSet.
	 * @param pageNumber current page number.
	 * @param pageSize page size.
	 * @param rh Row data handler
	 */
	public InefficientJdbcPage(ResultSet rs, int pageNumber, int pageSize, IRowHandler<E> rh) {
		super(pageNumber, pageSize);
		try {
			rs.last();
			totalCount = rs.getRow();
			getElementsFromResultSet(rs, rh);
		} catch (SQLException e) {
			throw new RuntimeException("JDBC page exception.", e);
		}
	}

	/**
	 * JDBC page.<br>
	 * 
	 * Use scoll to find specified page data.
	 * 
	 * @param rs Jdbc ResultSet.
	 * @param totalCount total 
	 * @param pageNumber current page number.
	 * @param pageSize page size.
	 * @param rh Row data handler.
	 */
	public InefficientJdbcPage(ResultSet rs, int totalCount, int pageNumber, int pageSize, IRowHandler<E> rh) {
		super(pageNumber, pageSize);
		try {
			this.totalCount = totalCount;
			getElementsFromResultSet(rs, rh);
		} catch (SQLException e) {
			throw new RuntimeException("JDBC page exception.", e);
		}
	}
	
	protected void getElementsFromResultSet(ResultSet rs, IRowHandler<E> rh) throws SQLException {
		computePage();
		int offset = (currPageNumber - firstPageNumber) * pageSize;
		if (offset > 0) {
			for (int i = 0; i < offset; i++) {
				rs.next();
			}
		}
		int count = 0;
		while (rs.next() && count < pageSize) {
			pageElements.add(rh.handleRow(rs));
			count++;
		}
	}

}
