package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Pattern;

import org.nestframework.action.IActionHandler;
import org.nestframework.action.Redirect;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;

/**
 * Default forward handler.
 * <p>
 * If forward start with http:// or ftp:// etc. then is a redirection.
 * If forward start with "!" then is a redirection.
 * </p>
 * 
 * @author audin
 */
@Intercept({Stage.HANDLE_VIEW})
public class DefaultActionForwarder implements IActionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultActionForwarder.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

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
			if (log.isDebugEnabled()) {
				log.debug("process(ExecuteContext) - end");
			}
			return false;
		} else if (!redirect && Pattern.matches("^[a-z]+:\\/\\/.*", forward.toLowerCase())) {
			redirect = true;
			isLocal = false;
		} else if (forward.startsWith("!")) {
			redirect = true;
			isLocal = true;
			forward = forward.substring(1);
		}
		
		if (redirect) {
			if (isLocal && forward.startsWith("/")) {
				forward = context.getRequest().getServletPath() + forward;
			}
			context.getResponse().sendRedirect(forward);
		} else {
			context.getRequest().getRequestDispatcher(forward).forward(context.getRequest(), context.getResponse());
		}

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
