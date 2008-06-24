/**
 * 
 */
package org.nestframework.commons.hibernate;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

/**
 * Dynamic SQL processer, supportting Freemarker based syntax.
 * 
 * @author austin
 */
public class DynamicSqlUtil {
	protected static Configuration freeMarkerEngine = new Configuration();
	private static ConcurrentMap<String, Template> templateCache = new ConcurrentHashMap<String, Template>();
	
	static {
		freeMarkerEngine.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
	}
	
	/**
	 * process dynamic Sql.
	 * 
	 * @param params named parameters.
	 * @param sqlTemplate dynamic sql(based on Freemarker syntax).
	 * @return Sql result.
	 * @throws Exception exception.
	 */
	public static ISqlElement processSql(Map<String, Object> params, String sqlTemplate) throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
            context.put(paramEntry.getKey(), paramEntry.getValue());
        }
		StringWriter out = new StringWriter();
        Template tpl = templateCache.get(sqlTemplate);
        if (tpl == null) {
            tpl = new Template("tpl", new StringReader(sqlTemplate), freeMarkerEngine);
            templateCache.put(sqlTemplate, tpl);
        }
        
        tpl.process(context, out);
        String sql = out.toString();
        
        // holder for avaliable parameters.
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        List<Object> paramNamesList = new ArrayList<Object>();
        
        Pattern p = Pattern.compile ("(:([a-zA-Z0-9_]+))");
        Matcher m = p.matcher (sql);
        while (m.find()) {
        	String name = m.group(2);
        	paramsMap.put(name, params.get(name));
        	//paramValuesList.add(params.get(name));
        	paramNamesList.add(name);
        }
          Object[] paramValues = new Object[paramNamesList.size()];
        // handle like
        // handle like %:username
        p = Pattern.compile ("(%:([a-zA-Z0-9_]+))");
        m = p.matcher (sql);
        while (m.find()) {
        	String key = m.group(2);
        	Object v = paramsMap.get(key);
        	paramsMap.put(key, "%" + v);
        }
        // handle :username%
        p = Pattern.compile ("(:([a-zA-Z0-9_]+)%)");
        m = p.matcher (sql);
        while (m.find()) {
        	String key = m.group(2);
        	Object v = paramsMap.get(key);
        	paramsMap.put(key, v + "%" );
        }
        for(int i=0; i<paramNamesList.size(); i++)
        {
        	paramValues[i] = paramsMap.get(paramNamesList.get(i));
        }
        // remove all '%'.
        sql = sql.replaceAll("%", "");
        // end handle like
        
        // replace all named params with ?
        sql = sql.replaceAll(":([a-zA-Z0-9_]+)", "?");
        
        
		SqlElementImpl s = new SqlElementImpl();
		s.setParams(paramValues);
		s.setParamsMap(paramsMap);
		s.setSql(sql);
		
		return s;
	}
}
