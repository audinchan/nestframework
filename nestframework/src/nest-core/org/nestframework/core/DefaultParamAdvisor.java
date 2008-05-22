/**
 * 
 */
package org.nestframework.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nestframework.action.ActionException;
import org.nestframework.localization.ActionMessages;

/**
 * @author audin
 *
 */
public class DefaultParamAdvisor implements IParamAdvisor {

	/* (non-Javadoc)
	 * @see org.nestframework.core.IParamAdvisor#parseParam(java.lang.Class, org.nestframework.core.ExecuteContext)
	 */
	public Object parseParam(Class<?> clazz, Annotation[] annotation, ExecuteContext ctx) throws ActionException {
		Object rt = null;
		if (clazz.equals(HttpServletRequest.class)) {
			rt = ctx.getRequest();
		} else if (clazz.equals(HttpServletResponse.class)) {
			rt = ctx.getResponse();
		} else if (clazz.equals(HttpSession.class)) {
			rt = ctx.getRequest().getSession(true);
		} else if (clazz.equals(BeanContext.class)) {
			rt = ctx.getBeanContext();
		} else if (clazz.equals(IExternalContext.class)) {
			rt = ctx.getConfig().getExternalContext();
		} else if (clazz.equals(ActionMessages.class)) {
			rt = ctx.getActionErrors();
		} else if (clazz.equals(PrintWriter.class)) {
			try {
				rt = ctx.getResponse().getWriter();
			} catch (IOException e) {
				throw new ActionException("Can't get PrintWriter from request.", e);
			}
		} else if (clazz.equals(ExecuteContext.class)) {
			rt = ctx;
		}
		return rt;
	}

}
