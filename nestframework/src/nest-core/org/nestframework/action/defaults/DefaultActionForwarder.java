package org.nestframework.action.defaults;

import java.util.regex.Pattern;

import org.nestframework.action.IActionHandler;
import org.nestframework.action.Redirect;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;

/**
 * Default forward handler.
 * @author audin
 */
@Intercept({Stage.HANDLE_VIEW})
public class DefaultActionForwarder implements IActionHandler {

	public boolean process(ExecuteContext context) throws Exception {
		String forward = null;
		boolean redirect = false;
		boolean isLocal = true;
		if (context.getForward() instanceof String) {
			forward = (String) context.getForward();
		} else if (context.getForward() instanceof Redirect) {
			Redirect r = (Redirect) context.getForward();
			forward = r.getTarget();
			redirect = true;
			isLocal = r.isLocal();
		}
		
		if (forward == null) {
			return false;
		} else if (!redirect && Pattern.matches("^[a-z]+:\\/\\/.*", forward.toLowerCase())) {
			redirect = true;
		} else if (forward.startsWith("!")) {
			redirect = true;
			isLocal = true;
			forward = forward.substring(1);
		}
		
//		if (!redirect && !forward.startsWith("/")) {
//			forward = '/'
//					+ context.getActionBean().getClass().getName().substring(
//							context.getConfig().getPackageBase().length() + 1,
//							context.getActionBean().getClass().getName()
//									.lastIndexOf('.')) + "/" + forward + ".jsp";
//		}
		if (redirect) {
			if (isLocal && forward.startsWith("/")) {
				forward = context.getRequest().getServletPath() + forward;
			}
			context.getResponse().sendRedirect(forward);
		} else {
			context.getRequest().getRequestDispatcher(forward).forward(context.getRequest(), context.getResponse());
		}
		return true;
	}

}
