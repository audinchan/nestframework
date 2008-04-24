package ${hss_dao_package}.hibernate;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

<#assign classbody>
<#if hss_jdk5>
import java.io.Serializable;
import ${hss_dao_package}.IBaseDAO;
<#else>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
import ${pojo.getPackageName()}.${declarationName};
import ${hss_dao_package}.I${declarationName}DAO;
</#if>
import java.util.List;

<#if hss_jdk5>@SuppressWarnings("unchecked")</#if>
public abstract class Base<#if hss_jdk5 = false>${declarationName}</#if>DAOHibernate<#if hss_jdk5><T, K extends Serializable></#if> extends RootDAOHibernate<#if hss_jdk5><T, K></#if> implements I<#if hss_jdk5 = false>${declarationName}<#else>Base</#if>DAO<#if hss_jdk5><T, K></#if> {
<#if hss_jdk5>
	private Class<T> entityClass;
	
	public BaseDAOHibernate() {
		entityClass = getGenericClass(getClass());
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
</#if>	
	public void save(<#if hss_jdk5>T<#else>${declarationName}</#if> instance) {
		getHibernateTemplate().saveOrUpdate(instance);
	}

	public List<#if hss_jdk5><T></#if> findAll() {
		return getHibernateTemplate().loadAll(<#if hss_jdk5>entityClass<#else>${declarationName}.class</#if>);
	}

	public <#if hss_jdk5>T<#else>${declarationName}</#if> get(<#if hss_jdk5>K<#else>${pojo.getJavaTypeName(clazz.identifierProperty, false)}</#if> id) {
		return (<#if hss_jdk5>T<#else>${declarationName}</#if>) getHibernateTemplate().get(<#if hss_jdk5>entityClass<#else>${declarationName}.class</#if>, id);
	}
	
	public void remove(<#if hss_jdk5>T<#else>${declarationName}</#if> instance) {
		getHibernateTemplate().delete(instance);
	}

	public void removeById(<#if hss_jdk5>K<#else>${pojo.getJavaTypeName(clazz.identifierProperty, false)}</#if> id) {
		getHibernateTemplate().delete(get(id));
	}

	public List<#if hss_jdk5><T></#if> findByExample(<#if hss_jdk5>T<#else>${declarationName}</#if> instance) {
		return getHibernateTemplate().findByExample(instance);
	}

	public List<#if hss_jdk5><T></#if> findByExample(<#if hss_jdk5>T<#else>${declarationName}</#if> instance, int firstResult, int maxResults) {
		return getHibernateTemplate().findByExample(instance, firstResult,
				maxResults);
	}
}
</#assign>

<#if hss_jdk5 = false>
${pojo.generateImports()}
</#if>
${classbody}