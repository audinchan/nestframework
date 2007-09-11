/**
 * 
 */
package org.nestframework.addons.spring;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;
import org.springframework.context.ApplicationContext;

/**
 * @author audin
 *
 */
@Intercept({Stage.CREATE_ACTIONBEAN})
public class SpringBeanCreator implements IActionHandler {
	private ApplicationContext ctx;
	
	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	
	private Object getBean(ExecuteContext context, String beanName) throws Exception {
		return ctx != null ? ctx.getBean(beanName) : SpringHelper.getBean(context, beanName);
	}

	/* (non-Javadoc)
	 * @see org.nestframework.action.IActionHandler#process(org.nestframework.core.ExecuteContext)
	 */
	public boolean process(ExecuteContext context) throws Exception {
		String className = context.getConfig().getPackageBase() + context.getPath().replaceAll("\\/", ".");
		int pos = className.lastIndexOf('.');
		Class<?> class1;
		try {
			class1 = Class.forName(className.substring(0, pos));
		} catch (ClassNotFoundException e) {
			class1 = Class.forName(className.substring(0, 1).toUpperCase() + className.substring(1, pos));
		}
		Spring s = class1.getAnnotation(Spring.class);
		if (s != null) {
			String beanName = s.value();
			if (NestUtil.isEmpty(beanName)) {
				beanName = class1.getSimpleName();
				beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
			}
			
			context.setActionBean(getBean(context, beanName));
			return true;
		}
		return false;
	}

}
