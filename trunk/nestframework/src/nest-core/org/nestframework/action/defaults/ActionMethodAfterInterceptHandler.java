package org.nestframework.action.defaults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.After;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

@Intercept( { Stage.CREATE_ACTIONBEAN, Stage.INITIAL_ACTIONBEAN,
		Stage.AFTER_INITIALIZATION, Stage.EXECUTE_ACTION,
		Stage.AFTER_EXECUTION, Stage.HANDLE_VIEW })
public class ActionMethodAfterInterceptHandler implements IActionHandler,
		Constant {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(ActionMethodAfterInterceptHandler.class);

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
			After after = method.getAnnotation(After.class);
			if (after != null) {
				Stage[] stages = after.value();
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
