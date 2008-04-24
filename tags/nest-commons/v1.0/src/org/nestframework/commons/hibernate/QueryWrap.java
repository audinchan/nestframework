package org.nestframework.commons.hibernate;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.NullableType;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

/**
 * Wrap hibernate query to provide dynamic parameters handler.
 * 
 * @author audin
 */
public class QueryWrap {
	protected static Configuration freeMarkerEngine;
	private static Map<String, NullableType> hibernateTypeMapping = new HashMap<String, NullableType>();
	private Session session;
	private Map<String, Object> namedParams = new HashMap<String, Object>();
	private Map<String, String> appendValues = new HashMap<String, String>();
	private Map<String, String> prependValues = new HashMap<String, String>();
	
	/**
	 * template cache to reduce compile time.
	 */
	private static ConcurrentMap<String, Template> templateCache = new ConcurrentHashMap<String, Template>();
	
	/**
	 * eg. from SomeModel where someAttr = :someAttr[float]
	 */
	static {
		hibernateTypeMapping.put("int", Hibernate.INTEGER);
		hibernateTypeMapping.put("integer", Hibernate.INTEGER);
		hibernateTypeMapping.put("boolean", Hibernate.BOOLEAN);
		hibernateTypeMapping.put("byte", Hibernate.BYTE);
		hibernateTypeMapping.put("date", Hibernate.DATE);
		hibernateTypeMapping.put("long", Hibernate.LONG);
		hibernateTypeMapping.put("short", Hibernate.SHORT);
		hibernateTypeMapping.put("string", Hibernate.STRING);
		hibernateTypeMapping.put("double", Hibernate.DOUBLE);
		hibernateTypeMapping.put("float", Hibernate.FLOAT);
	}
	
    public QueryWrap() {
        
    }
    
	/**
	 * Constructor.
	 * @param session Hibernate session.
	 */
	public QueryWrap(Session session) {
		if (freeMarkerEngine == null) {
			freeMarkerEngine = new Configuration();
			freeMarkerEngine.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		}
		
		this.session = session;
	}
	
    /**
     * Set Hibernate Session.
     * @param session
     */
	public void setSession(Session session)
    {
        this.session = session;
    }

    /**
	 * Set a named parameter.
	 * 
	 * @param name parameter name;
	 * @param value parameter value;
	 * @return self.
	 */
	public QueryWrap setParamter(String name, Object value) {
		namedParams.put(name, value);
		return this;
	}
	
	/**
	 * Set named parameters.
	 * 
	 * @param params parameters map.
	 * @return self.
	 */
	public QueryWrap setParamters(Map<String, Object> params) {
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
			namedParams.put(paramEntry.getKey(), paramEntry.getValue());
		}
		return this;
	}
	
	/**
	 * Append a string to a name's value.
	 * If we use 'like :name' in hql, we should use 
	 * query.setParameter("name", "somevalue%") to search 
	 * records start with 'somevalue'.
	 * 
	 * @param value
	 * @param name
	 * @return
	 */
	public QueryWrap setAppendValue(String value, String name) {
		appendValues.put(name, value);
		return this;
	}
	
	public QueryWrap setAppendValues(String value, String... names) {
		for(String name: names) {
			appendValues.put(name, value);
		}
		return this;
	}
	
	public QueryWrap setPrependValue(String value, String name) {
		prependValues.put(name, value);
		return this;
	}
	
	public QueryWrap setPrependValues(String value, String... names) {
		for(String name: names) {
			prependValues.put(name, value);
		}
		return this;
	}
	
	public QueryWrap setPairAppendValue(String value, String name) {
		appendValues.put(name, value);
		prependValues.put(name, value);
		return this;
	}
	
	public QueryWrap setPairAppendValues(String value, String... names) {
		for(String name: names) {
			appendValues.put(name, value);
			prependValues.put(name, value);
		}
		return this;
	}
	
	/**
	 * Generate dynamic query object.
	 * 
	 * hqlTemplate is written in freemarker template language.
	 * 
	 * @param hqlTemplate hql template.
	 * @return Query.
	 * @throws Exception
	 */
	public Query getQuery(String hqlTemplate) throws Exception {	
		return getQuery(session, hqlTemplate);
	}
    
    public Query getQuery(Session session, String hqlTemplate) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("wraper", this);
        for (Map.Entry<String, Object> paramEntry : namedParams.entrySet()) {
            context.put(paramEntry.getKey(), paramEntry.getValue());
        }
        
        StringWriter out = new StringWriter();
        Template tpl = templateCache.get(hqlTemplate);
        if (tpl == null) {
            tpl = new Template("tpl", new StringReader(hqlTemplate), freeMarkerEngine);
            templateCache.put(hqlTemplate, tpl);
        }
        tpl.process(context, out);
        String hql = out.toString();
        
        // handle like
        // handle like %:username
        Pattern p = Pattern.compile ("(%:([a-zA-Z0-9_]+))");
        Matcher m = p.matcher (hql);
        while (m.find()) {
            setPrependValue("%", m.group(2));
        }
        // handle :username%
        p = Pattern.compile ("(:([a-zA-Z0-9_]+)%)");
        m = p.matcher (hql);
        while (m.find()) {
            setAppendValue("%", m.group(2));
        }
        
        // remove all '%'.
        hql = hql.replaceAll("%", "");
        // end handle like
        
        // handle type define
        // handle :param[type]
        Map<String, NullableType> hbMap = new HashMap<String, NullableType>();
        Map<String, Object> convertedValue = new HashMap<String, Object>();
        p = Pattern.compile ("(:([a-zA-Z0-9_]+)\\[([a-z]+)\\])");
        m = p.matcher (hql);
        while (m.find()) {
            String key = m.group(2);
            String t = m.group(3);
            hbMap.put(key, hibernateTypeMapping.get(t));
            if ("int".equalsIgnoreCase(t) || "integer".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Integer.valueOf((String) namedParams.get(key)));
            } else if ("float".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Float.valueOf((String) namedParams.get(key)));
            } else if ("double".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Double.valueOf((String) namedParams.get(key)));
            } else if ("short".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Short.valueOf((String) namedParams.get(key)));
            } else if ("long".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Long.valueOf((String) namedParams.get(key)));
            } else if ("boolean".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Boolean.valueOf((String) namedParams.get(key)));
            } else if ("byte".equalsIgnoreCase(t)) {
            	convertedValue.put(key, Byte.valueOf((String) namedParams.get(key)));
            }
        }
        if (hbMap.size() > 0) {
        	hql = hql.replaceAll("\\[([a-z]+)\\]", "");
        }
        // end handle type define
        
        Query qry = session.createQuery(hql);
        
        for (Map.Entry<String, Object> paramEntry : namedParams.entrySet()) {
            String name = paramEntry.getKey();
            if (hql.indexOf(':' + name) != -1) {
                Object value = paramEntry.getValue();
                String prepend = prependValues.get(name);
                String append = appendValues.get(name);
                if (prepend != null) {
                    value = prepend + value;
                }
                if (append != null) {
                    value = value + append;
                }
                NullableType ht = hbMap.get(name);
                Object cv = convertedValue.get(name);
                if (ht == null || cv == null) {
                	qry.setParameter(name, value);
                } else {
                	qry.setParameter(name, cv, ht);
                }
            }
        }
        
        return qry;
    }

}
