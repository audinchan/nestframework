<?xml version="1.0" encoding="GBK"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd 
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
		">

	<!-- 定义一系列要使用的安全过滤器 captchaValidationProcessingFilter,channelProcessingFilter,-->
	<bean id="filterChainProxy"
		class="org.acegisecurity.util.FilterChainProxy">
		<property name="filterInvocationDefinitionSource">
			<value>
				CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
				PATTERN_TYPE_APACHE_ANT
				/**=httpSessionContextIntegrationFilter,userAuthenticationSessionObjectFilter,ssoLogoutFilter,ssoAuthenticationProcessingFilter,exceptionTranslationFilter,filterSecurityInterceptor
			</value>
			<!--  -->
		</property>
	</bean>

	<bean id="exceptionTranslationFilter"
		class="org.acegisecurity.ui.ExceptionTranslationFilter">
		<property name="authenticationEntryPoint"
			ref="ssoAuthenticationEntryPoint" />
		
	</bean>

	<!-- 处理用户登录后的session对象 -->
	<bean id="userAuthenticationSessionObjectFilter" 
		class="${hss_base_package}.webapp.util.UserAuthenticationSessionObjectFilter">
	</bean>

	<bean id="authenticationManager"
		class="org.acegisecurity.providers.ProviderManager">
		<property name="providers">
			<list>
				<ref bean="ssoAuthenticationProvider" />
			</list>
		</property>
	</bean>
	
	<!-- BecomSSO -->
	
	<bean id="ssoAuthenticationProcessingFilter"
		class="org.nestframework.commons.acegi.becomsso.BecomSSOProcessingFilter">
		<!-- org.nestframework.commons.acegi.becomsso.BecomSSOLogoutFilter-->
		<property name="filterProcessesUrl">
			<value>/j_acegi_security_check</value>
		</property>
		<property name="authenticationFailureUrl">
			<value>/login.jsp?error=user_psw_error</value>
		</property>
		<property name="defaultTargetUrl">
			<value>/</value>
		</property>
		<property name="authenticationManager"
			ref="authenticationManager" />
		<property name="authenticationEntryPoint" ref="ssoAuthenticationEntryPoint"/>
		<property name="exceptionMappings">
			<value>
				org.nestframework.commons.acegi.becomsso.NoSuchUserException=/login.jsp?error=no_such_user_error
				org.acegisecurity.AuthenticationException=/login.jsp?error=user_psw_error
				org.acegisecurity.concurrent.ConcurrentLoginException=/login.jsp?error=too_many_user_error
				org.springframework.jdbc.CannotGetJdbcConnectionException=/login.jsp?error=db_error
			</value>
		</property>
	</bean>
	
	<bean id="ssoLogoutFilter"
		class="${hss_base_package}.webapp.util.SSOLogoutFilter">
		<!-- org.nestframework.commons.acegi.becomsso.BecomSSOLogoutFilter-->
		<!-- 退出后指向的 URL -->
		<constructor-arg value="/" />
		<constructor-arg>
			<list>
				<bean
					class="org.acegisecurity.ui.logout.SecurityContextLogoutHandler" />
				<!-- 解决退出后再登录出现ClassCastException异常 -->
				<bean
					class="org.nestframework.commons.acegi.SessionlogoutHandler" />
			</list>
		</constructor-arg>
		<property name="logoutUrl">
			<value>$[sso.logoutUrl]</value>
		</property>
	</bean>

	<bean id="ssoAuthenticationEntryPoint"
		class="org.nestframework.commons.acegi.becomsso.BecomSSOAuthenticationEntryPoint">
		<property name="authUrl">
			<value>$[sso.authUrl]</value>
		</property>
	</bean>
	
	<bean id="ssoAuthenticationProvider"
		class="org.nestframework.commons.acegi.becomsso.BecomSSOAuthenticationProvider">
		<property name="userDetailsService"
			ref="userDetailsServiceJdbc" />
		<property name="autoCreateUser" value="false" />
	</bean>
</beans>