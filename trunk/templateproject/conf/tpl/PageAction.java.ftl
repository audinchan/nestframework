/**
 * 
 */
package ${hss_base_package}.webapp.action;

import org.nestframework.commons.hibernate.IPage;

/**
 * @author audin
 * 
 */
public abstract class PageAction<E> extends BaseAction {
	public int pageSize = 10;

	public int pageNo = 1;
	
	public String orderBy;
	
	public String orderType;

	protected IPage<E> pageObj;

	/**
	 * @return the pageObj
	 */
	public IPage<E> getPageObj() {
		return pageObj;
	}

}
