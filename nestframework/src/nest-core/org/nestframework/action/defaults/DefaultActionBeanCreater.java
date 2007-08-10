package org.nestframework.action.defaults;

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

	public boolean process(ExecuteContext context) throws Exception {
		String className = context.getConfig().getPackageBase() + context.getPath().replaceAll("\\/", ".");
		
		// trim ".a" at last part of path.
//		int pos = className.lastIndexOf('.');
//		className = className.substring(0, pos);
//		context.setActionBean(Class.forName(className).newInstance());
		
		context.setActionBean(ActionResolver.resolveAction(className));
		
		// action bean is created, so break this stage's process.
		return true;
	}

}
