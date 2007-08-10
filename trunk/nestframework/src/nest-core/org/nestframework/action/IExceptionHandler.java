package org.nestframework.action;

import org.nestframework.core.ExecuteContext;


public interface IExceptionHandler {

	/**
	 * 异常处理。
	 * @param e 异常对象。
	 * @param context 上下文。
	 * @return 重定向路径。
	 * @throws ActionException Servlet异常。
	 */
	public boolean execute(Exception e, ExecuteContext context)
			throws ActionException;
}
