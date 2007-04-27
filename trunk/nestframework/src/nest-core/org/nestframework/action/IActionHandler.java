package org.nestframework.action;

import org.nestframework.core.ExecuteContext;


/**
 * Action 处理器。
 * @author audin
 */
public interface IActionHandler {
	/**
	 * Action处理器.
	 * @param context 上下文。
	 * @return 是否停止后继操作(当上下文支持)。
	 */
	public boolean process(ExecuteContext context) throws Exception;
}
