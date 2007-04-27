package org.nestframework.core;

public enum Stage {
	/**
	 * Create action bean.
	 */
	CREATE_ACTIONBEAN,
	
	/**
	 * Initial action bean. In this stage we can set bean fields' value according
	 * to request parameters submitted.
	 */
	INITIAL_ACTIONBEAN,
	
	/**
	 * Do something after the bean created, such as action method detection, 
	 * or plugins initialization for bean(eg. Spring bean injection). Validation
	 * can also be taken in this stage.
	 */
	AFTER_INITIALIZATION,
	
	/**
	 * Invoke action method. These method parameters should be bound automatically:
	 * HttpServletRequest, HttpServletResponse, HttpSession, ServletContext, BeanContext.
	 */
	EXECUTE_ACTION,
	
	/**
	 * After action method invoking. Currently we reflect bean's fields to request attributes
	 * for convenient usage in view(jsp) using jstl or EL.
	 */
	AFTER_EXECUTION,
	
	/**
	 * Controller's duty is done. Next stage is to show the view.
	 */
	HANDLE_VIEW
}
