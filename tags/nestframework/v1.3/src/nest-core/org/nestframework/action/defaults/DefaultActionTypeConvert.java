/**
 * 
 */
package org.nestframework.action.defaults;

import java.util.HashMap;
import java.util.Map;

import org.nestframework.action.ActionException;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.AsType;
import org.nestframework.annotation.Intercept;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.IInitable;
import org.nestframework.core.ITypeConvert;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * Handler action result type convertting.
 * 
 * @author audin
 * 
 */
@Intercept( { Stage.HANDLE_VIEW })
public class DefaultActionTypeConvert implements IActionHandler, IInitable {
	private Map<String, ITypeConvert> convertors = new HashMap<String, ITypeConvert>();

	public void init(IConfiguration config) {
		String currEn = null;
		try {
			String s = config.getProperties().get("typeConvertors");
			String[] ss = NestUtil.trimAll(s).split(",");
			for (String en : ss) {
				currEn = en;
				String[] nv = en.split("=");
				Class<?> clazz = Class.forName(nv[1]);
				convertors.put(nv[0].trim(), (ITypeConvert) clazz.newInstance());
			}
		} catch (Exception e) {
			throw new ActionException("Failed to init type converters, line:" + currEn, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nestframework.action.IActionHandler#process(org.nestframework.core.ExecuteContext)
	 */
	public boolean process(ExecuteContext context) throws Exception {
		AsType at = context.getAction().getAnnotation(AsType.class);
		if (at != null) {
			ITypeConvert tc = convertors.get(at.type());
			if (tc != null) {
				return tc.process(context);
			}
		}
		return false;
	}

}
