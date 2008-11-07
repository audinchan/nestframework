package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.nestframework.action.ActionException;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.core.ExecuteContext;

/**
 * Default action exception handler.
 * 
 * @author audin
 */
public class DefaultExceptionHandler implements IExceptionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultExceptionHandler.class);

	public boolean execute(Exception e, ExecuteContext context)
			throws ActionException {
		if (log.isDebugEnabled()) {
			log.debug("execute(Exception, ExecuteContext) - start");
		}

		throw new ActionException("Nest Exception.", e);
	}

}
