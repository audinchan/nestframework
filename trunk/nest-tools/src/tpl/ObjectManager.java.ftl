package ${hss_service_package}.impl;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

<#assign declarationName = pojo.importType(pojo.getDeclarationName())>

<#if hss_jdk5>
<#assign valname = hssutil.firstLowerCase(declarationName)>
<#if !merge_dao>
import ${hss_dao_package}.IBaseDAO;
import ${hss_dao_package}.I${declarationName}DAO;
</#if>
import ${pojo.getPackageName()}.${declarationName};
<#if pojo.identifierProperty.composite>
import ${pojo.getPackageName()}.${declarationName}Id;
</#if>
import ${hss_service_package}.I${declarationName}Manager;
</#if>

public class ${declarationName}Manager extends Base<#if hss_jdk5 = false>${declarationName}</#if>Manager<#if hss_jdk5><${declarationName}, ${pojo.getJavaTypeName(clazz.identifierProperty, false)}> implements I${declarationName}Manager</#if> {

<#if hss_jdk5>
  <#if !merge_dao>
	private I${declarationName}DAO ${valname}DAO;
	/**
	 * @param ${valname}DAO the ${valname}DAO to set
	 */
	public void set${declarationName}DAO(I${declarationName}DAO ${valname}DAO) {
		this.${valname}DAO = ${valname}DAO;
	}
	/* (non-Javadoc)
	 * @see ${hss_service_package}.impl.BaseManager#getDAO()
	 */
	@Override
	public IBaseDAO<${declarationName}> getDAO() {
		return ${valname}DAO;
	}
  </#if>
</#if>

}
