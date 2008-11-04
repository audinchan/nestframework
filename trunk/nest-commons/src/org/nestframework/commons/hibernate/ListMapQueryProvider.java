package org.nestframework.commons.hibernate;

import java.util.List;

import org.nestframework.commons.hibernate.IQueryProvider;

public class ListMapQueryProvider implements IQueryProvider {
	private List<IQueryProvider> providers;

	public void setProviders(List<IQueryProvider> providers) {
		this.providers = providers;
	}

	public String getQuery(String queryName) {
		for (IQueryProvider p : providers) {
			String q = p.getQuery(queryName);
			if (q != null) {
				return q;
			}
		}
		return null;
	}
}