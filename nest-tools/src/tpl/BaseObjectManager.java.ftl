package ${hss_service_package}.impl;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

<#assign classbody>
<#if hss_jdk5>
import java.io.Serializable;
<#if !merge_dao>
import ${hss_dao_package}.IBaseDAO;
</#if>
import ${hss_service_package}.IBaseManager;
<#else>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
<#assign valname = hssutil.firstLowerCase(declarationName)>
<#if !merge_dao>
import ${hss_dao_package}.I${declarationName}DAO;
</#if>
import ${pojo.getPackageName()}.${declarationName};
import ${hss_service_package}.I${declarationName}Manager;
</#if>
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.nestframework.commons.hibernate.QueryWrap;
import org.springframework.orm.hibernate3.HibernateCallback;

<#if hss_jdk5>@SuppressWarnings("unchecked")</#if>
public abstract class Base<#if hss_jdk5 = false>${declarationName}</#if>Manager<#if hss_jdk5><T, K extends Serializable></#if> extends RootManager<#if hss_jdk5><T, K></#if> implements I<#if hss_jdk5>Base<#else>${declarationName}</#if>Manager<#if hss_jdk5><T, K></#if> {
<#if hss_jdk5>
  <#if merge_dao>
	private Class<T> entityClass;
	
	public String currentDateSql="getCurrentDateOfDatabase";
	
	public BaseManager() {
		entityClass = getGenericClass(getClass());
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
  <#else>
	public abstract IBaseDAO<T> getDAO();
  </#if>
<#else>
  <#if !merge_dao>
	protected I${declarationName}DAO ${valname}DAO;

	public void set${declarationName}DAO(I${declarationName}DAO dao) {
		this.${valname}DAO = dao;
	}
  </#if>
</#if>

<#if merge_dao>
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
	
	public Date getCurrentDateOfDatabase() {
		Date currentDate = (Date)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {				
				try
				{
					QueryWrap q = new QueryWrap(session);
					Query query = q.getQuery(_("getCurrentDateOfDatabase"));
					return query.uniqueResult();
				}
				catch (Exception ex)
				{
				}
				return null;
			}
		});
		return currentDate;
	}

<#else>
	public List<#if hss_jdk5><T></#if> findAll() {
		return <#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.findAll();
	}

	public List<#if hss_jdk5><T></#if> findByExample(<#if hss_jdk5>T<#else>${declarationName}</#if> instance, int firstResult, int maxResults) {
		return <#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.findByExample(instance, firstResult, maxResults);
	}

	public List<#if hss_jdk5><T></#if> findByExample(<#if hss_jdk5>T<#else>${declarationName}</#if> instance) {
		return <#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.findByExample(instance);
	}

	public <#if hss_jdk5>T<#else>${declarationName}</#if> get(<#if hss_jdk5>K<#else>${pojo.getJavaTypeName(clazz.identifierProperty, false)}</#if> id) {
		return <#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.get(id);
	}

	public void removeById(<#if hss_jdk5>K<#else>${pojo.getJavaTypeName(clazz.identifierProperty, false)}</#if> id) {
		<#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.removeById(id);
	}
	
	public void remove(<#if hss_jdk5>T<#else>${declarationName}</#if> instance) {
		<#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.remove(instance);
	}

	public void save(<#if hss_jdk5>T<#else>${declarationName}</#if> instance) {
		<#if hss_jdk5>getDAO()<#else>${valname}DAO</#if>.save(instance);
	}
</#if>	
}
</#assign>

<#if hss_jdk5 = false>
${pojo.generateImports()}
</#if>
${classbody}