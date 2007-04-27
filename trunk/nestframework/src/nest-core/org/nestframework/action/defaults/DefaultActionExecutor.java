package org.nestframework.action.defaults;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;

/**
 * 默认的ActionBean执行者。
 * 
 * @author audin
 */
@Intercept({Stage.EXECUTE_ACTION})
public class DefaultActionExecutor implements IActionHandler {

	public boolean process(ExecuteContext context) throws Exception {
		// 调用Context的执行方法。
		context.execute();
		return true;
	}

}
