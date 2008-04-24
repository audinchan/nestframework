package org.nestframework.commons.hibernate;


/**
 * This is a simple query provider wrapper to make things 
 * easy when you want to swith between providers.
 * @author audin
 */
public class QueryProviderWrap implements IQueryProvider {
	private IQueryProvider queryProvider;

	public void setQueryProvider(IQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
	}

	public String getQuery(String name) {
		return queryProvider.getQuery(name);
	}

}
