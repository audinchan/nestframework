package org.nestframework.action.defaults;


import java.util.Map;
import java.util.Map.Entry;

import ognl.DefaultMemberAccess;
import ognl.Ognl;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * 将ActionBean的属性反射到Request中。
 * 
 * @author audin
 */
@Intercept({Stage.AFTER_EXECUTION})
public class DefaultActionBeanGetter implements IActionHandler, Constant {

	public boolean process(ExecuteContext context) throws Exception {
		Object bean = context.getActionBean();
		Map ognlContext = Ognl.createDefaultContext(bean);
		DefaultMemberAccess memberAccess = new DefaultMemberAccess(true);
		Ognl.setMemberAccess(ognlContext, memberAccess);
		
		for (Entry<String, Object> entry: NestUtil.getPropertiesMap(bean).entrySet()) {
			context.getRequest().setAttribute(entry.getKey(), entry.getValue());
		}
		
		// export errors.
		context.getRequest().setAttribute(ERRORS_KEY, context.getActionErrors());
		
//		for (String propertyName: context.getReadableProperties()) {
//			context.getRequest().setAttribute(propertyName, Ognl.getValue(propertyName, ognlContext, bean));
//		}
		
//		// get all properties with accessors.
//		BeanInfo info = Introspector.getBeanInfo(bean.getClass());
//		PropertyDescriptor[] props = info.getPropertyDescriptors();
//		for (PropertyDescriptor d : props) {
//			context.getRequest().setAttribute(d.getName(), Ognl.getValue(d.getName(), ognlContext, bean));
//		}
//		
//		// get all public properties.
//		Collection<Field> fields = NestUtil.getFields(bean.getClass());
//		for (Field f : fields) {
//			if (Modifier.isPublic(f.getModifiers())) {
//				context.getRequest().setAttribute(f.getName(), Ognl.getValue(f.getName(), ognlContext, bean));
//			}
//		}
		
//		for (String propertyName: context.getReadableProperties()) {
//			context.getRequest().setAttribute(propertyName, PropertyUtils.getProperty(context.getActionBean(), propertyName));
//		}
		return false;
	}

}
