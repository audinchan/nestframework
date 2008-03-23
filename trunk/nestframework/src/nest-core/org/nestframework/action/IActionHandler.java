package org.nestframework.action;

import org.nestframework.core.ExecuteContext;


/**
 * Action Handler interface.
 * 
 * @author audin
 */
public interface IActionHandler {
	
	/**
	 * Process action chain.
	 * 
	 * @param context Execute context.
	 * @return if return true then the stage will stop to be handled(when stage supported to be stopped).
	 */
	public boolean process(ExecuteContext context) throws Exception;
}
