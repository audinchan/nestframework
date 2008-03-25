package org.nestframework.action.defaults;

import java.util.Map;

import ognl.DefaultMemberAccess;
import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.action.FileItem;
import org.nestframework.action.IActionHandler;
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
			
			String p = paramName;
			int pos = p.indexOf('.');
			if (pos != -1) {
				p = p.substring(0, pos);
			}
			try {
				Ognl.setValue(paramName, ognlContext, bean, paramValue);
			} catch (NoSuchPropertyException e) {
				// pass over not match properties
				//logger.error("process(ExecuteContext)", e);
			} catch (Exception e) {
				logger.error("process(ExecuteContext)", e);
			}
		}
		
		// handle files
		for (String fileName: context.getUploadedFiles().keySet()) {
			FileItem fileItem = context.getUploadedFiles().get(fileName);
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

}
