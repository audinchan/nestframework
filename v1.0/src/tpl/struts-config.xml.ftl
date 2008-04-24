<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts-config PUBLIC "-//Apache Software Foundation//DTD Struts Configuration 1.2//EN" "http://jakarta.apache.org/struts/dtds/struts-config_1_2.dtd">
<struts-config>
	<form-beans>
		<form-bean name="demoForm" type="${hss_base_package}.webapp.form.DemoForm" />
	</form-beans>
	<global-exceptions />
	<global-forwards />
	<action-mappings>
		<action input="/demo.jsp" name="demoForm" path="/demoAction"
			scope="request"
			type="org.springframework.web.struts.DelegatingActionProxy">
		</action>
	</action-mappings>

	<plug-in
		className="org.springframework.web.struts.ContextLoaderPlugIn">
		<set-property property="contextConfigLocation"
			value="/WEB-INF/servlet-context.xml" />
	</plug-in>
</struts-config>
