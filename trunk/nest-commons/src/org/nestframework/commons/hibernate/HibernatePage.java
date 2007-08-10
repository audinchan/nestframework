package org.nestframework.commons.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

@SuppressWarnings("unchecked")
public class HibernatePage<E> extends AbstractPage<E> {
	
	/**
	 * Hibernate page.<br>
	 * 
	 * use scoll to compute total results.
	 * 
	 * @param query Hibernate Query.
	 * @param pageNumber current page number.
	 * @param pageSize page size.
	 */
	public HibernatePage(Query query, int pageNumber, int pageSize) {
		super(pageNumber, pageSize);
		computePage(query.scroll());
		this.pageElements = query.setFirstResult((currPageNumber - startPage) * pageSize).setMaxResults(pageSize + 1).list();
	}

	/**
	 * Hibernate page.<br>
	 * 
	 * Use specified query to count total results. This is more 
	 * efficient then using scoll.
	 *  
	 * @param query search Query.
	 * @param countQuery count Query.
	 * @param pageNumber current page number.
	 * @param pageSize page size.
	 */
	public HibernatePage(Query query, Query countQuery, int pageNumber, int pageSize) {
		super(pageNumber, pageSize);
		this.totalCount = ((Long) countQuery.uniqueResult()).intValue();
		computePage();
		this.pageElements = query.setFirstResult((currPageNumber - startPage) * pageSize).setMaxResults(pageSize + 1).list();
	}

	/**
	 * Hibernate page.<br>
	 * 
	 * use scoll to compute total results.
	 * 
	 * @param criteria Criteria.
	 * @param pageNumber current page number.
	 * @param pageSize page size.
	 */
	public HibernatePage(Criteria criteria, int pageNumber, int pageSize) {
		super(pageNumber, pageSize);
		computePage(criteria.scroll());
		this.pageElements = criteria.setFirstResult((currPageNumber - startPage) * pageSize).setMaxResults(pageSize + 1).list();
	}
	
	protected void computePage(ScrollableResults sc) {
		sc.last();
		this.totalCount = sc.getRowNumber() + 1;
		computePage();
	}

}
