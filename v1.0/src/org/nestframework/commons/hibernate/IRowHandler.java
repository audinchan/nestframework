package org.nestframework.commons.hibernate;

import java.sql.ResultSet;

public interface IRowHandler<E> {
	public E handleRow(ResultSet rs);
}
