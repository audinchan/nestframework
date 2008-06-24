<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd 
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
		">

	<bean id="filterChainProxy"
		class="org.acegisecurity.util.FilterChainProxy">
		<property name="filterInvocationDefinitionSource">
			<value>
				CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
				PATTERN_TYPE_APACHE_ANT
				/**=httpSessionContextIntegrationFilter,logoutFilter,formAuthenticationProcessingFilter,exceptionTranslationFilter,filterSecurityInterceptor
			</value>
			<!--  -->
		</property>
	</bean>

	<bean id="exceptionTranslationFilter"
		class="org.acegisecurity.ui.ExceptionTranslationFilter">
		<property name="authenticationEntryPoint"
			ref="formLoginAuthenticationEntryPoint" />
	</bean>

	<!-- Define filter to handle FORM authentication -->
	<bean id="formAuthenticationProcessingFilter"
		class="${hss_base_package}.webapp.util.UserAuthenticationProcessingFilter">
		<property name="filterProcessesUrl">
			<value>/j_acegi_security_check</value>
		</property>
		<property name="authenticationFailureUrl">
			<value>/login.jsp?error=user_psw_error</value>
		</property>
		<property name="defaultTargetUrl">
			<value>/admin/</value>
		</property>
		<property name="alwaysUseDefaultTargetUrl" value="true"/>
		<property name="authenticationManager"
			ref="authenticationManager" />
		<property name="exceptionMappings">
			<value>
				org.acegisecurity.AuthenticationException=/login.jsp?error=user_psw_error
				org.acegisecurity.concurrent.ConcurrentLoginException=/login.jsp?error=too_many_user_error
				org.springframework.jdbc.CannotGetJdbcConnectionException=/login.jsp?error=db_error
			</value>
		</property>
	</bean>

	<!-- Define realm for FORM login-->
	<bean id="formLoginAuthenticationEntryPoint"
		class="org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint">
		<property name="loginFormUrl">
			<value>/login.jsp</value>
		</property>
		<property name="forceHttps">
			<value>false</value>
		</property>
	</bean>

	<bean id="logoutFilter"
		class="org.acegisecurity.ui.logout.LogoutFilter">
		<constructor-arg value="/login.jsp?logout=true" />
		<constructor-arg>
			<list>
				<bean
					class="org.acegisecurity.ui.logout.SecurityContextLogoutHandler" />
				<bean
					class="org.nestframework.commons.acegi.SessionlogoutHandler" />
			</list>
		</constructor-arg>
	</bean>

	<bean id="authenticationManager"
		class="org.acegisecurity.providers.ProviderManager">
		<property name="providers">
			<list>
				<ref bean="daoAuthenticationProvider" />
			</list>
		</property>
	</bean>

	<bean id="daoAuthenticationProvider"
		class="org.acegisecurity.providers.dao.DaoAuthenticationProvider">
		<property name="userDetailsService"
			ref="userDetailsServiceJdbc" />
		<property name="passwordEncoder" ref="md5PasswordEncoder" />
	</bean>
	
	<bean id="md5PasswordEncoder"
		class="org.acegisecurity.providers.encoding.Md5PasswordEncoder">
	</bean>
	
</beans>