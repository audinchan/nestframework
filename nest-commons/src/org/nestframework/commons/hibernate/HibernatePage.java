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
		//setPageElements(query.setFirstResult((currPageNumber - firstPageNumber) * pageSize).setMaxResults(pageSize + 1).list());
		setPageElements(query.setFirstResult((currPageNumber - firstPageNumber) * pageSize).setMaxResults(pageSize).list());
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
		//setPageElements(query.setFirstResult((currPageNumber - firstPageNumber) * pageSize).setMaxResults(pageSize + 1).list());
		setPageElements(query.setFirstResult((currPageNumber - firstPageNumber) * pageSize).setMaxResults(pageSize).list());
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
		//setPageElements(criteria.setFirstResult((currPageNumber - firstPageNumber) * pageSize).setMaxResults(pageSize + 1).list());
		setPageElements(criteria.setFirstResult((currPageNumber - firstPageNumber) * pageSize).setMaxResults(pageSize).list());
	}
	
	protected void computePage(ScrollableResults sc) {
		sc.last();
		this.totalCount = sc.getRowNumber() + 1;
		computePage();
	}

}
