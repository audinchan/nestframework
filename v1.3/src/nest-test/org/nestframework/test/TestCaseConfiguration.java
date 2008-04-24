package org.nestframework.test;

import org.nestframework.action.defaults.ActionMethodAfterInterceptHandler;
import org.nestframework.action.defaults.ActionMethodBeforeInterceptHandler;
import org.nestframework.action.defaults.DefaultActionBeanCreater;
import org.nestframework.action.defaults.DefaultActionBeanSetter;
import org.nestframework.action.defaults.DefaultActionExecutor;
import org.nestframework.action.defaults.DefaultActionMethodFinder;
import org.nestframework.config.RuntimeConfiguration;

public class TestCaseConfiguration extends RuntimeConfiguration {
	
	@Override
	protected void addDefaultHandlers() {
		addLifecycleHandler(new ActionMethodBeforeInterceptHandler());
		addLifecycleHandler(new DefaultActionBeanCreater());
		addLifecycleHandler(new DefaultActionBeanSetter());
		addLifecycleHandler(new DefaultActionMethodFinder());
		addLifecycleHandler(new DefaultActionExecutor());
		addLifecycleHandler(new TestActionBeanGetter());
		addLifecycleHandler(new TestActionForwarder());
		addLifecycleHandler(new ActionMethodAfterInterceptHandler());
	}
}
