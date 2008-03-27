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
 *
 */
@Intercept(Stage.AFTER_EXECUTION)
public class XStreamActionHandler implements IInitable, IActionHandler {
	private Map<String, HierarchicalStreamDriver> drivers = new HashMap<String, HierarchicalStreamDriver>();
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
					drivers.put(dd[0], (HierarchicalStreamDriver)Class.forName(dd[1]).newInstance());
				}
			}
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
			
			Alias alias = context.getAction().getAnnotation(Alias.class);
			if (alias != null && alias.value() != null) {
				for (String aliasStr : alias.value()) {
					String[] as = aliasStr.split("=");
					xs.alias(as[0], Class.forName(as[1]));
				}
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
