package ${hss_service_package};
// Generated ${date} by Hibernate Tools ${version} with mintgen

<#assign classbody>
<#if hss_jdk5>
import java.io.Serializable;
<#else>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
import ${pojo.getPackageName()}.${declarationName};
</#if>
import java.util.List;

public interface IBase<#if hss_jdk5 = false>${declarationName}</#if>Manager<#if hss_jdk5><T, K extends Serializable></#if> extends IRootManager<#if hss_jdk5><T, K></#if> {
	public <#if hss_jdk5>T<#else>${declarationName}</#if> get(<#if hss_jdk5>K<#else>${pojo.getJavaTypeName(clazz.identifierProperty, false)}</#if> id);
	public void save(<#if hss_jdk5>T<#else>${declarationName}</#if> instance);
	public void remove(<#if hss_jdk5>T<#else>${declarationName}</#if> instance);
	public void removeById(<#if hss_jdk5>K<#else>${pojo.getJavaTypeName(clazz.identifierProperty, false)}</#if> id);
	public List<#if hss_jdk5><T></#if> findAll();
	public List<#if hss_jdk5><T></#if> findByExample(<#if hss_jdk5>T<#else>${declarationName}</#if> instance);
	public List<#if hss_jdk5><T></#if> findByExample(<#if hss_jdk5>T<#else>${declarationName}</#if> instance, int firstResult, int maxResults);
}
</#assign>

<#if hss_jdk5 = false>
${pojo.generateImports()}
</#if>
${classbody}