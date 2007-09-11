package org.nestframework.action.defaults;

import java.lang.reflect.Method;
import java.util.Collection;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.annotation.InterceptBefore;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

@Intercept({Stage.CREATE_ACTIONBEAN,Stage.INITIAL_ACTIONBEAN,Stage.AFTER_INITIALIZATION,Stage.EXECUTE_ACTION,Stage.AFTER_EXECUTION,Stage.HANDLE_VIEW})
public class ActionMethodBeforeInterceptHandler implements IActionHandler, Constant {

	public boolean process(ExecuteContext context) throws Exception {
		Object bean = context.getActionBean();
		if (bean == null) {
			return false;
		}
		Collection<Method> methods = NestUtil.getMethods(context.getActionClass());
		for (Method method : methods) {
			InterceptBefore before = method.getAnnotation(InterceptBefore.class);
			if (before != null) {
				Stage[] stages = before.value();
				for (Stage stage : stages) {
					if (stage == context.getStage()) {
						method.invoke(bean, new Object[] {});
					}
				}
			}
		}
		return false;
	}

}
