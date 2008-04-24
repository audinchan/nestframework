package ${hss_dao_package};
// Generated ${date} by Hibernate Tools ${version} with nest-tools
 
<#assign classbody>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
<#if hss_jdk5>
import ${pojo.getPackageName()}.${declarationName};
</#if>

public interface I${declarationName}DAO extends IBase<#if hss_jdk5 = false>${declarationName}</#if>DAO<#if hss_jdk5><${declarationName}, ${pojo.getJavaTypeName(clazz.identifierProperty, false)}></#if> {

}
</#assign>

${pojo.generateImports()}
${classbody}