package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * 默认的ActionBean执行者。
 * 
 * @author audin
 */
@Intercept( { Stage.EXECUTE_ACTION })
public class DefaultActionExecutor implements IActionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultActionExecutor.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		// 调用Context的执行方法。
		context.setForward(NestUtil.execMethod(context.getAction(), context
				.getActionBean(), context));
		// context.execute();

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
