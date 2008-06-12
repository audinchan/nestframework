package ${hss_service_package};
// Generated ${date} by Hibernate Tools ${version} with nest-tools

<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
<#if hss_jdk5>
import ${pojo.getPackageName()}.${declarationName};
<#if pojo.identifierProperty.composite>
import ${pojo.getPackageName()}.${declarationName}Id;
</#if>
</#if>


public interface I${declarationName}Manager extends IBase<#if hss_jdk5 = false>${declarationName}</#if>Manager<#if hss_jdk5><${declarationName}, ${pojo.getJavaTypeName(clazz.identifierProperty, false)}></#if> {
}

