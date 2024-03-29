package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.ActionMethodByValue;
import org.nestframework.annotation.DefaultAction;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;

/**
 * Find action method to be executed.
 * 
 * @author audin
 */
@Intercept( { Stage.AFTER_INITIALIZATION })
public class DefaultActionMethodFinder implements IActionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultActionMethodFinder.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		/*
		 * Descentdant class is early processed.
		 */
		// Action method.
		Method actionMethod = null;
		
		// Default Action method.
		Method defaultActionMethod = null;
		
		// parameters.
		Set<String> paramNames = context.getParams().keySet();
		Class<?> clazz = context.getActionClass();
		
		ActionMethodByValue ambv = context.getActionBean().getClass().getAnnotation(ActionMethodByValue.class);
		
		while (clazz != null
				&& (defaultActionMethod == null || actionMethod == null)) {
			for (Method m1 : clazz.getDeclaredMethods()) {
				if (!Modifier.isPublic(m1.getModifiers())) {
					continue;
				}

				if (defaultActionMethod == null) {
					DefaultAction defaultAction = m1
							.getAnnotation(DefaultAction.class);
					if (defaultAction != null) {
						defaultActionMethod = m1;
					}
				}

				if (actionMethod == null) {
					if (ambv != null) {
						if (m1.getName().equals(ambv.value())) {
							actionMethod = m1;
						}
					}
					else if (paramNames.contains(m1.getName())) {
						actionMethod = m1;
					}
				}
			}

			clazz = clazz.getSuperclass();
		}

		if (actionMethod == null) {
			actionMethod = defaultActionMethod;
		}

		context.setAction(actionMethod);
		context.setDefaultAction(defaultActionMethod);

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}
}
