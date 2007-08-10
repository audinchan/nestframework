package org.nestframework.action.defaults;

import org.nestframework.action.ActionException;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.core.ExecuteContext;

/**
 * 默认的异常处理器。
 * 
 * @author audin
 */
public class DefaultExceptionHandler implements IExceptionHandler {

	public boolean execute(Exception e, ExecuteContext context)
			throws ActionException {
		throw new ActionException("Nest Exception.", e);
	}

}
