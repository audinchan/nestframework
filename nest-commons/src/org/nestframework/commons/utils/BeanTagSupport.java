/**
 * 
 */
package org.nestframework.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.nestframework.addons.spring.Spring;
import org.nestframework.utils.NestUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author audin
 *
 */
public abstract class BeanTagSupport extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
			ServletContext sctx = req.getSession().getServletContext();
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);
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
			return super.doStartTag();
		} catch (Exception e) {
			throw new JspException("Failed to init spring beans.", e);
		}
	}


}
