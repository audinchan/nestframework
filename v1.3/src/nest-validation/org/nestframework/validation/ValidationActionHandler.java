package org.nestframework.validation;

import javax.servlet.ServletException;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.localization.ActionMessages;
import org.nestframework.utils.NestUtil;

@Intercept({Stage.BEFORE_EXECUTION})
public class ValidationActionHandler implements IActionHandler, Constant {
	
	public static final String VALIDATE_FROM_KEY = "__nestframework_errors_from";
	
	public boolean process(ExecuteContext context) throws Exception {
		
		try {
			new CommonsValidator().validate(context);
		} catch (ValidateFailedException e) {
			
		}
		
		ActionMessages msgs = context.getActionErrors();
		
		// add action messages to the request scope.
		if (msgs.size() > 0) {
			// auto export by getter.
//			context.getRequest().setAttribute(ERRORS_KEY, msgs);
			
			String[] from = context.getParams().get(VALIDATE_FROM_KEY);
			if (from == null || from.length == 0 || NestUtil.isEmpty(from[0])) {
				throw new ServletException("No validate_from found. Put <nest:from/> tag in your form.");
			}
			context.setForward(from[0]);
			
			// When validation failed, the process chain should go to after_excution stage.
			context.setNextStage(Stage.AFTER_EXECUTION);
			
			// break current stage.
			return true;
		}
		
		return false;
	}

}
