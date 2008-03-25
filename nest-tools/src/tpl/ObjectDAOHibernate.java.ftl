package ${hss_dao_package}.hibernate;
// Generated ${date} by Hibernate Tools ${version} with mintgen

<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
<#if hss_jdk5>
import ${hss_dao_package}.I${declarationName}DAO;
import ${pojo.getPackageName()}.${declarationName};
</#if>

public class ${declarationName}DAOHibernate extends Base<#if hss_jdk5 == false>${declarationName}</#if>DAOHibernate<#if hss_jdk5><${declarationName}, ${pojo.getJavaTypeName(clazz.identifierProperty, false)}> implements I${declarationName}DAO</#if> {

}
