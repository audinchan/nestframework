<?xml version="1.0" encoding="utf-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd"
	default-autowire="byName" default-lazy-init="true">
	
	<!-- extend managers -->
	
	<!-- Excel Export manager -->
	<bean id="excelExportSupport"
		class="${hss_base_package}.service.ext.impl.ExcelExportSupportImpl" autowire="byName">
	</bean>
	<!-- Upload Photo manager -->
	<bean id="uploadPhotoManagerExt"
		class="${hss_base_package}.service.ext.impl.UploadPhotoManagerExt" autowire="byName">
	</bean>
</beans>
