package org.nestframework.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.nestframework.addons.spring.Spring;
import org.nestframework.utils.NestUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BeanHttpServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
			Collection<Field> fields = NestUtil.getFields(getClass());
			for (Field f : fields) {
				Spring spring = f.getAnnotation(Spring.class);
				if (spring != null) {
					String beanName = spring.value();
					if (NestUtil.isEmpty(beanName)) {
						beanName = f.getName();
					}
					if (!Modifier.isPublic(f.getModifiers())) {
						try {
							f.setAccessible(true);
						} catch (SecurityException e) {

						}
					}
					f.set(this, ctx.getBean(beanName));
				}
			}
		} catch (Exception e) {
			throw new ServletException("Failed to init spring beans.", e);
		}
	}

}
