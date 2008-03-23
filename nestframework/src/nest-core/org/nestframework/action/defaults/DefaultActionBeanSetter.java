package org.nestframework.action.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

import ognl.DefaultMemberAccess;
import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.nestframework.action.FileItem;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.RequestParamMapTypeConverter;

/**
 * populate all request parameters as action bean's properties.
 * 
 * @author audin
 */
@Intercept(Stage.INITIAL_ACTIONBEAN)
public class DefaultActionBeanSetter implements IActionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(DefaultActionBeanSetter.class);

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
//        boolean doDecode = "GET".equalsIgnoreCase(context.getRequest().getMethod());
//        String encoding = context.getRequest().getCharacterEncoding();
		for (String paramName: params.keySet()) {
			String[] paramValue = params.get(paramName);
            
//            if (paramValue != null && doDecode) {
//                for (int i = 0; i < paramValue.length; i++) {
//                    paramValue[i] = new String(paramValue[i].getBytes("ISO8859-1"), encoding);
//                }
//            }
			
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
