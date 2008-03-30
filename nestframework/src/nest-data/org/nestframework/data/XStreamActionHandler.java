/**
 * 
 */
package org.nestframework.data;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.nestframework.action.ActionException;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.IInitable;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * @author audin
 * config parameters:
 * xstream.drivers
 * xstream.aliases
 */
@Intercept(Stage.AFTER_EXECUTION)
public class XStreamActionHandler implements IInitable, IActionHandler {
	private Map<String, HierarchicalStreamDriver> drivers = new HashMap<String, HierarchicalStreamDriver>();
	private Map<String, Class<?>> aliases = new HashMap<String, Class<?>>();
	private boolean defaultConvertHibernate = true;
	/* (non-Javadoc)
	 * @see org.nestframework.core.IInitable#init(org.nestframework.config.IConfiguration)
	 */
	public void init(IConfiguration config) {
		try {
			drivers.put("json", new JettisonMappedXmlDriver());
			String xdrivers = NestUtil.trimAll(config.getProperties().get("xstream.drivers"));
			if (NestUtil.isNotEmpty(xdrivers)) {
				String[] ds = xdrivers.split(",");
				for (String d : ds) {
					String[] dd = d.split("=");
					drivers.put(dd[0].trim(), (HierarchicalStreamDriver)Class.forName(dd[1].trim()).newInstance());
				}
			}
			
			String xaliases = NestUtil.trimAll(config.getProperties().get("xstream.aliases"));
			if (NestUtil.isNotEmpty(xaliases)) {
				String[] as = xaliases.split(",");
				for (String a : as) {
					String[] aa = a.split("=");
					aliases.put(aa[0].trim(), Class.forName(aa[1].trim()));
				}
			}
			
			defaultConvertHibernate = "true".equalsIgnoreCase(NestUtil.trimAll(config.getProperties().get("xstream.convertHibernate")));
		} catch (Exception e) {
			throw new ActionException("Failed to init XStreamActionHandler", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.nestframework.action.IActionHandler#process(org.nestframework.core.ExecuteContext)
	 */
	public boolean process(ExecuteContext context) throws Exception {
		boolean doHandle = false;
		Xml xml = context.getAction().getAnnotation(Xml.class);
		Json json = context.getAction().getAnnotation(Json.class);
		AutoType autoType = context.getAction().getAnnotation(AutoType.class);
		
		String contentType = "text/xml";
		String dataType = "xml";
		
		if (xml != null) {
			contentType = xml.value();
			dataType = "xml";
			doHandle = true;
		} else if (json != null) {
			contentType = json.value();
			dataType = "json";
			doHandle = true;
		} else if (autoType != null) {
			String paramName = autoType.paramName();
			dataType = context.getRequest().getParameter(paramName);
			for (String ct : autoType.contentTypes()) {
				String[] cts = ct.split(":");
				if (dataType.equalsIgnoreCase(cts[0])) {
					contentType = cts[1];
					break;
				}
			}
			doHandle = true;
		}
		
		if (doHandle) {
			XStream xs = null;
			HierarchicalStreamDriver driver = drivers.get(dataType);
			if (driver == null) {
				xs = new XStream();
			} else {
				xs = new XStream(driver);
			}
			
			RootElement rootElement = context.getAction().getAnnotation(RootElement.class);
			if (rootElement != null && rootElement.value().trim().length() > 0) {
				xs.alias(rootElement.value().trim(), context.getForward().getClass());
			}
			
			for (String name: aliases.keySet()) {
				xs.alias(name, aliases.get(name));
			}
			
			Alias alias = context.getAction().getAnnotation(Alias.class);
			if (alias != null && alias.value() != null) {
				for (String aliasStr : alias.value()) {
					String[] as = aliasStr.split("=");
					xs.alias(as[0], Class.forName(as[1]));
				}
			}
			
			Hibernate hibernate = context.getAction().getAnnotation(Hibernate.class);
			
			boolean convertHibernate = false;
			if (defaultConvertHibernate && (null == hibernate || hibernate.value())) {
				convertHibernate = true;
			} else if (!defaultConvertHibernate && (null != hibernate && hibernate.value())) {
				convertHibernate = true;
			}
			
			if (convertHibernate) {
				// handle hibernate collections
				xs.registerConverter(new HibernateCollectionConverter(xs.getConverterLookup()));
			}
			
			context.getResponse().setContentType(contentType);
//			String result = xs.toXML(context.getForward());
			PrintWriter writer = context.getResponse().getWriter();
//			writer.write(result);
			xs.toXML(context.getForward(), writer);
			writer.close();
			
			return true;
		} else {
			return false;
		}
	}

}
