package org.nestframework.test;

import org.nestframework.action.IActionHandler;
import org.nestframework.core.ExecuteContext;

/**
 * 将ActionBean的属性反射到Request中。
 * 
 * @author audin
 */
public class TestActionBeanGetter implements IActionHandler {

	public boolean process(ExecuteContext context) throws Exception {
		return false;
	}

}
