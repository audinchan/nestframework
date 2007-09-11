package org.nestframework.action.defaults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.DefaultAction;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * Find action method to be executed.
 * 
 * @author audin
 */
@Intercept({Stage.INITIAL_ACTIONBEAN})
public class DefaultActionMethodFinder implements IActionHandler {

	public boolean process(ExecuteContext context) throws Exception {
		Collection<Method> methods = NestUtil.getMethods(context.getActionClass());
		Method actionMethod = null;
		Method defaultActionMethod = null;
		for (Method m : methods) {
			if (!Modifier.isPublic(m.getModifiers())) {
				continue;
			}
			DefaultAction defaultAction = m.getAnnotation(DefaultAction.class);
			if (defaultAction != null) {
				defaultActionMethod = m; 
				continue;
			} else if (context.getParams().keySet().contains(m.getName())) {
				actionMethod = m;
				continue;
			}
		}
		if (actionMethod == null) {
			actionMethod = defaultActionMethod;
		}
		
		context.setAction(actionMethod);
		context.setDefaultAction(defaultActionMethod);
		
		return false;
	}
}
