package org.nestframework.test;

import org.nestframework.action.defaults.DefaultActionBeanCreater;
import org.nestframework.action.defaults.DefaultActionBeanSetter;
import org.nestframework.action.defaults.DefaultActionExecutor;
import org.nestframework.action.defaults.DefaultActionMethodFinder;
import org.nestframework.config.IConfiguration;
import org.nestframework.config.RuntimeConfiguration;
import org.nestframework.core.NestContext;

public class TestCaseConfiguration extends RuntimeConfiguration {
	
	public static IConfiguration getInstance() {
		IConfiguration nestConfig = new RuntimeConfiguration();
		nestConfig.addLifecycleHandler(new DefaultActionBeanCreater());
		nestConfig.addLifecycleHandler(new DefaultActionBeanSetter());
		nestConfig.addLifecycleHandler(new DefaultActionMethodFinder());
		nestConfig.addLifecycleHandler(new DefaultActionExecutor());
		nestConfig.addLifecycleHandler(new TestActionBeanGetter());
		nestConfig.addLifecycleHandler(new TestActionForwarder());
		NestContext.setConfig(nestConfig);
		return nestConfig;
	}
}
