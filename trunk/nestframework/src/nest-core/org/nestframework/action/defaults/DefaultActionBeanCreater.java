package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ActionResolver;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;

/**
 * Default action bean creator.
 * 
 * @author Audin Chan
 */
@Intercept({Stage.CREATE_ACTIONBEAN})
public class DefaultActionBeanCreater implements IActionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultActionBeanCreater.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		String className = context.getConfig().getPackageBase() + context.getPath().replaceAll("\\/", ".");

		context.setActionBean(ActionResolver.resolveAction(className));
		context.setActionClass(context.getActionBean().getClass());

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
