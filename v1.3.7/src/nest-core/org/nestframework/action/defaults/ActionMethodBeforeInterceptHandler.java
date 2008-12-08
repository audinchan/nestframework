package org.nestframework.action.defaults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Before;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * A stage handler which is invoked when after some stages.
 * 
 * @author audin.
 *
 */
@Intercept( { Stage.CREATE_ACTIONBEAN, Stage.INITIAL_ACTIONBEAN,
		Stage.AFTER_INITIALIZATION, Stage.EXECUTE_ACTION,
		Stage.AFTER_EXECUTION, Stage.HANDLE_VIEW })
public class ActionMethodBeforeInterceptHandler implements IActionHandler,
		Constant {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(ActionMethodBeforeInterceptHandler.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		Object bean = context.getActionBean();
		if (bean == null) {
			if (log.isDebugEnabled()) {
				log.debug("process(ExecuteContext) - end");
			}
			return false;
		}
		Collection<Method> methods = NestUtil.getMethods(context
				.getActionClass());
		for (Method method : methods) {
			boolean exec = false;
			Before before = method
					.getAnnotation(Before.class);
			if (before != null) {
				Stage[] stages = before.value();
				for (Stage stage : stages) {
					if (stage == context.getStage()) {
						exec = true;
						break;
					}
				}
			}

			if (exec) {
				if (!Modifier.isPublic(method.getModifiers())) {
					method.setAccessible(true);
				}
				NestUtil.execMethod(method, bean, context);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
