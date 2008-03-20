package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultActionBeanGetter.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		Object bean = context.getActionBean();
		Map<?, ?> ognlContext = Ognl.createDefaultContext(bean);
		DefaultMemberAccess memberAccess = new DefaultMemberAccess(true);
		Ognl.setMemberAccess(ognlContext, memberAccess);
		
		for (Entry<String, Object> entry: NestUtil.getPropertiesMap(bean, context.getActionClass()).entrySet()) {
			context.getRequest().setAttribute(entry.getKey(), entry.getValue());
		}
		
		// export errors.
		context.getRequest().setAttribute(ERRORS_KEY, context.getActionErrors());

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
