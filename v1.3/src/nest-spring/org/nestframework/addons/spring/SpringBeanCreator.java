/**
 * 
 */
package org.nestframework.addons.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(SpringBeanCreator.class);

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
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		try {
			if ("true".equalsIgnoreCase(context.getConfig().getProperties().get("spring.creator.all"))) {
				String beanName = context.getPath();
				beanName = beanName.substring(beanName.lastIndexOf('/') + 1, beanName.lastIndexOf('.'));
				if (! NestUtil.isEmpty(beanName)) {
					beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
					context.setActionBean(getBean(context, beanName));
					context.setActionClass(context.getActionBean().getClass());

					if (log.isDebugEnabled()) {
						log.debug("process(ExecuteContext) - end");
					}
					return true;
				}
			}
			String className = context.getConfig().getPackageBase() + context.getPath().replaceAll("\\/", ".");
			int pos = className.lastIndexOf('.');
			Class<?> clazz;
			try {
				clazz = Class.forName(className.substring(0, pos));
			} catch (ClassNotFoundException e) {
				log.error("process(ExecuteContext)", e);

				clazz = Class.forName(className.substring(0, 1).toUpperCase() + className.substring(1, pos));
			}
			Spring s = clazz.getAnnotation(Spring.class);
			if (s != null) {
				String beanName = s.value();
				if (NestUtil.isEmpty(beanName)) {
					beanName = clazz.getSimpleName();
					beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
				}
				
				context.setActionBean(getBean(context, beanName));
				context.setActionClass(context.getActionBean().getClass());

				if (log.isDebugEnabled()) {
					log.debug("process(ExecuteContext) - end");
				}
				return true;
			}
		} catch (Exception e) {
			log.error("process(ExecuteContext)", e);
		}

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
