package org.nestframework.test;

import org.nestframework.action.IActionHandler;
import org.nestframework.core.ExecuteContext;

/**
 * 用于单元测试的Action重定向执行者。
 * 
 * @author audin
 */
public class TestActionForwarder implements IActionHandler {

	public boolean process(ExecuteContext context) throws Exception {
		return true;
	}

}
