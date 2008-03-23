package org.nestframework.addons.spring;

import org.nestframework.core.ExecuteContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Spring helper class.
 * 
 * @author audin
 *
 */
public class SpringHelper {
	/**
	 * Get bean from spring context.
	 * 
	 * @param context Execute context.
	 * @param beanName Bean name.
	 * @return Spring bean.
	 * @throws Exception
	 */
	public static Object getBean(ExecuteContext context, String beanName) throws Exception {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(context.getRequest().getSession().getServletContext()).getBean(beanName);
	}
}
