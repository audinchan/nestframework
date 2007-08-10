package org.nestframework.addons.spring;

import org.nestframework.core.ExecuteContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class SpringHelper {
	public static Object getBean(ExecuteContext context, String beanName) throws Exception {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(context.getRequest().getSession().getServletContext()).getBean(beanName);
	}
}
