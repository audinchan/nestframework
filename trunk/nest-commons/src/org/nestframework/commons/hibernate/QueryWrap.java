package org.nestframework.commons.hibernate;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;

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
	private Session session;
	private Map<String, Object> namedParams = new HashMap<String, Object>();
	private Map<String, String> appendValues = new HashMap<String, String>();
	private Map<String, String> prependValues = new HashMap<String, String>();
	
	/**
	 * template cache to reduce compile time.
	 */
	private static ConcurrentMap<String, Template> templateCache = new ConcurrentHashMap<String, Template>();
	
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
                qry.setParameter(name, value);
            }
        }
        
        return qry;
    }

}
