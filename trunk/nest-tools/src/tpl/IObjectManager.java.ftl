package ${hss_service_package};
// Generated ${date} by Hibernate Tools ${version} with mintgen

<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
<#if hss_jdk5>
import ${pojo.getPackageName()}.${declarationName};
</#if>


public interface I${declarationName}Manager extends IBase<#if hss_jdk5 = false>${declarationName}</#if>Manager<#if hss_jdk5><${declarationName}, ${pojo.getJavaTypeName(clazz.identifierProperty, false)}></#if> {
}
