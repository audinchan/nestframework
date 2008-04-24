package org.nestframework.action;

import org.nestframework.core.ExecuteContext;

/**
 * Action exception handler interface.
 * 
 * @author audin
 *
 */
public interface IExceptionHandler {

	/**
	 * handle action exception.
	 * 
	 * @param e Exception.
	 * @param context Execute context.
	 * @return if <code>true</code> then stop to execute the left exception handlers. 
	 * @throws ActionException Exception.
	 */
	public boolean execute(Exception e, ExecuteContext context)
			throws ActionException;
}
