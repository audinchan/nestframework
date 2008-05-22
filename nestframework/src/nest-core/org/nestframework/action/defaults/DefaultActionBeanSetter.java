package org.nestframework.action.defaults;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ognl.DefaultMemberAccess;
import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.action.FileItem;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.DateFormat;
import org.nestframework.annotation.DateFormatGroup;
import org.nestframework.annotation.Intercept;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.IInitable;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;
import org.nestframework.utils.RequestParamMapTypeConverter;

/**
 * Populate all request parameters as action bean's properties.
 * 
 * Configuration parameters:
 * get.encoding.source: source encoding(iso-8859-1);
 * get.encoding.default: default encoding(GBK);
 * get.encoding.doDecode: whether to do decode(true);
 * get.encoding.forceDefault: whether to force encoding to default encoding(false);
 * 
 * @author audin
 */
@Intercept(Stage.AFTER_INITIALIZATION)
public class DefaultActionBeanSetter implements IActionHandler, IInitable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(DefaultActionBeanSetter.class);
	
	private String sourceEnc = "ISO-8859-1";
	private String defaultDestEnc = "GBK";
	private boolean doDecode = true;
	private boolean forceDefault = false;

	public void init(IConfiguration config) {
		String ges = config.getProperties().get("get.encoding.source");
		if (NestUtil.isNotEmpty(ges)) {
			sourceEnc = NestUtil.trimAll(ges);
		}
		String ged = config.getProperties().get("get.encoding.default");
		if (NestUtil.isNotEmpty(ged)) {
			defaultDestEnc = NestUtil.trimAll(ged);
		}
		if ("true".equalsIgnoreCase(config.getProperties().get("get.encoding.doDecode"))) {
			doDecode = true;
		}
		if ("true".equalsIgnoreCase(config.getProperties().get("get.encoding.forceDefault"))) {
			forceDefault = true;
		}
	}

	public boolean process(ExecuteContext context) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("process(ExecuteContext) - start");
		}
		
		Object bean = context.getActionBean();
        // handle dateformat
        Map<String, String> dfMap = new HashMap<String, String>();
        Collection<Field> fields = NestUtil.getFields(context.getActionClass());
        for (Field f : fields) {
			DateFormat df = f.getAnnotation(DateFormat.class);
			putDateFormatMap(dfMap, f, df);
			DateFormatGroup dfg = f.getAnnotation(DateFormatGroup.class);
			if (dfg != null) {
				for (DateFormat df2: dfg.value()) {
					putDateFormatMap(dfMap, f, df2);
				}
			}
		}
		Map<?, ?> ognlContext = Ognl.createDefaultContext(bean);
		DefaultMemberAccess memberAccess = new DefaultMemberAccess(true);
		Ognl.setMemberAccess(ognlContext, memberAccess);
		Ognl.setTypeConverter(ognlContext, new RequestParamMapTypeConverter());
		Map<String, String[]> params = context.getParams();
        boolean ifDecode = doDecode && "GET".equalsIgnoreCase(context.getRequest().getMethod());
        String encoding = null;

        if (ifDecode) {
        	if (forceDefault) {
        		encoding = defaultDestEnc;
        	} else {
	        	encoding = context.getRequest().getCharacterEncoding();
	        	if (NestUtil.isEmpty(encoding)) {
	        		encoding = defaultDestEnc;
	        	}
        	}
        }
		for (String paramName: params.keySet()) {
			String[] paramValue = params.get(paramName);
            
            if (paramValue != null && ifDecode) {
                for (int i = 0; i < paramValue.length; i++) {
                    paramValue[i] = new String(paramValue[i].getBytes(sourceEnc), encoding);
                }
            }
			
//			String p = paramName;
//			int pos = p.indexOf('.');
//			if (pos != -1) {
//				p = p.substring(0, pos);
//			}
            Object value = paramValue;
			try {
				String df = dfMap.get(paramName);
				if (df != null) {
					value = new SimpleDateFormat(df).parse(paramValue[0]);
				}
				Ognl.setValue(paramName, ognlContext, bean, value);
			} catch (NoSuchPropertyException e) {
				// pass over not match properties
				//logger.error("process(ExecuteContext)", e);
			} catch (Exception e) {
				logger.error("process(ExecuteContext)", e);
			}
		}
		
		// handle files
		for (String fileName: context.getUploadedFiles().keySet()) {
			FileItem[] fileItem = context.getUploadedFiles().get(fileName);
			try {
				Ognl.setValue(fileName, ognlContext, bean, fileItem);
			} catch (NoSuchPropertyException e) {
				//logger.error("process(ExecuteContext)", e);
			} catch (Exception e) {
				logger.error("process(ExecuteContext)", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("process(ExecuteContext) - end");
		}
		return false;
	}

	/**
	 * @param dfMap
	 * @param f
	 * @param df
	 */
	private void putDateFormatMap(Map<String, String> dfMap, Field f,
			DateFormat df) {
		if (df != null) {
			if (NestUtil.isEmpty(df.property())) {
				dfMap.put(f.getName(), df.value());
			} else {
				dfMap.put(f.getName() + '.' + df.property(), df.value());
			}
		}
	}

}
