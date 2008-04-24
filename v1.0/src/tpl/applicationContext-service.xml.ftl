<#ftl strip_whitespace="no" strip_text="no">
<?xml version="1.0" encoding="utf-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd"
	default-autowire="byName" default-lazy-init="true"><#t>
<#t>
<#t>

  <tx:advice id="txAdvice" transaction-manager="transactionManager">

    <!-- the transactional semantics... -->
    <tx:attributes>

      <!-- all methods starting with 'get' are read-only -->
      <tx:method name="get*" read-only="true"/>
      <tx:method name="find*" read-only="true"/>

      <!-- other methods use the default transaction settings (see below) -->
      <tx:method name="*"/>
    </tx:attributes>
  </tx:advice>

	<aop:config>
		<aop:pointcut id="serviceMethods" expression="execution(* ${hss_service_package}..*(..))"/>
		<aop:advisor advice-ref="txAdvice" pointcut-ref="serviceMethods"/>
	</aop:config>

	<!-- Hibernate SessionFactory -->
	<bean id="sessionFactory"
	<#if hss_ejb3>
		class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">
	<#else>
		class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
	</#if>
		<property name="dataSource" ref="dataSource" />
	<#if hss_ejb3>
		<property name="annotatedClasses">
	<#else>
		<property name="mappingResources">
	</#if>
			<list>
<#list hssutil.getClassMappings(cfg) as element>
			<#if hss_ejb3>
				<value>${element.className}</value>
			<#else>
				<value>${hssutil.getMappingFileResource(element)}</value>
			</#if>
</#list>
			</list>
		</property>
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">$[hibernate.dialect]</prop>
				<prop key="hibernate.cache.provider_class">org.hibernate.cache.EhCacheProvider</prop>
				<prop key="hibernate.cache.use_query_cache">true</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.bytecode.use_reflection_optimizer">true</prop>
				<prop key="hibernate.hbm2ddl.auto">$[hibernate.hbm2ddl.auto]</prop>
			</props>
		</property>
	</bean>

	<!-- Transaction manager for a single Hibernate SessionFactory
		(alternative to JTA) -->
	<bean id="transactionManager"
		class="${hss_conf_transactionManager}">
		<property name="sessionFactory" ref="sessionFactory" />
	</bean>

	<!-- managers -->

<#if hss_export_demo>	
	<bean id="demoManager" class="${hss_service_package}.impl.DemoManager" autowire="byName">
    </bean>
    
</#if>

<#list hssutil.getClassMappings(cfg) as element>
    <bean id="${hssutil.toBeanName(element)}Manager" class="${hss_service_package}.impl.${hssutil.getDeclarationName(element)}Manager" autowire="byName">
    </bean>
    
</#list>
</beans>
