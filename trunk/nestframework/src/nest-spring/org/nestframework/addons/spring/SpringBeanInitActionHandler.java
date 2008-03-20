package org.nestframework.addons.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;
import org.springframework.context.ApplicationContext;

@Intercept( { Stage.AFTER_INITIALIZATION })
public class SpringBeanInitActionHandler implements IActionHandler {

	private ApplicationContext ctx;

	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	public boolean process(ExecuteContext context) throws Exception {
		Object bean = context.getActionBean();
		Collection<Method> methods = NestUtil.getMethods(context
				.getActionClass());
		for (Method m : methods) {
			Spring s = m.getAnnotation(Spring.class);
			if (s == null) {
				continue;
			}
			String beanName = s.value();
			if (NestUtil.isEmpty(beanName)) {
				beanName = NestUtil.methodToPropertyName(m);
			}
			if (!Modifier.isPublic(m.getModifiers())) {
				try {
					m.setAccessible(true);
				} catch (SecurityException e) {

				}
			}
			m.invoke(bean, ctx != null ? ctx.getBean(beanName) : SpringHelper
					.getBean(context, beanName));
		}

		Collection<Field> fields = NestUtil.getFields(context.getActionClass());
		for (Field f : fields) {
			Spring spring = f.getAnnotation(Spring.class);
			if (spring != null) {
				String beanName = spring.value();
				if ("".equals(beanName)) {
					beanName = f.getName();
				}
				if (!Modifier.isPublic(f.getModifiers())) {
					try {
						f.setAccessible(true);
					} catch (SecurityException e) {

					}
				}
				f.set(context.getActionBean(), ctx != null ? ctx
						.getBean(beanName) : SpringHelper.getBean(context,
						beanName));
			}
		}

		return false;
	}

}
