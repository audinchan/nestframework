package ${hss_base_package};

import org.nestframework.addons.spring.SpringBeanInitActionHandler;
import org.nestframework.test.TestCaseUtil;

public abstract class BaseActionTestCase extends BaseTestCase {
	<#if hss_jdk5>@Override</#if>
	protected void onSetUpBeforeTransaction() throws Exception {
		TestCaseUtil.setPackageBase("${hss_base_package}.webapp.action");
		TestCaseUtil.init();
		// For we used spring, so we should add this handler to configuration.
		TestCaseUtil.getConfiguration().addLifecycleHandler(new SpringBeanInitActionHandler(applicationContext));
	}
}
