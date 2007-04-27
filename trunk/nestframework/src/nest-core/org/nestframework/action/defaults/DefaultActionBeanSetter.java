package org.nestframework.action.defaults;

import java.util.Map;

import ognl.DefaultMemberAccess;
import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.utils.RequestParamMapTypeConverter;

/**
 * 从Request中将提交的参数反射到ActionBean中。
 * @author audin
 */
@Intercept({Stage.INITIAL_ACTIONBEAN})
public class DefaultActionBeanSetter implements IActionHandler {

	public boolean process(ExecuteContext context) throws Exception {
		
		Object bean = context.getActionBean();
		Map ognlContext = Ognl.createDefaultContext(bean);
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
			if (p.indexOf('.') != -1) {
				p = p.substring(0, pos);
			}
			try {
				Ognl.setValue(paramName, ognlContext, bean, paramValue);
			} catch (NoSuchPropertyException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
				
		// 允许其它Setter继续执行。
		return false;
	}

}
