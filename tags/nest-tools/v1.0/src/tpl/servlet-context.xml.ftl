<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:aop="http://www.springframework.org/schema/aop"
		xmlns:tx="http://www.springframework.org/schema/tx"
		xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd 
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

<tx:annotation-driven/>

<#if hss_export_demo = false><!--</#if>
	<bean name="/demoAction" scope="request"
		class="${hss_base_package}.webapp.action.DemoAction" autowire="byName">
		<aop:scoped-proxy/>
	</bean>
<#if hss_export_demo = false>--></#if>	
</beans>