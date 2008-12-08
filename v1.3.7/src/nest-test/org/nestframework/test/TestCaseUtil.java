package org.nestframework.test;

import java.util.HashMap;
import java.util.Map;

import org.nestframework.action.ActionProcessHelper;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.utils.NestUtil;


public class TestCaseUtil {
	private static IConfiguration config;
	
	static {
		config = new TestCaseConfiguration();
	}
	
	public static void init() {
		config.init();
	}
	
	public static IConfiguration getConfiguration() {
		return config;
	}
	
	/**
	 * 执行Action，并返回Action实例。
	 * 
	 * @param actionPath action路径，如/user/UserAction.a
	 * @param params 提交的参数
	 * @return ExecuteContext。
	 * @throws Exception 异常。
	 */
	public static ExecuteContext execute(String actionPath, Map<String, String[]> params) throws Exception {
		ExecuteContext context = new ExecuteContext(new Request(), new Response());
		context.setConfig(config)
		   .setParams(params)
		   .setPath(actionPath);
		
		ActionProcessHelper.process(context, config);
		
		return context;
	}
	
	/**
	 * 执行Action，并返回Action实例。
	 * 
	 * @param actionPath action路径，如/user/UserAction.a
	 * @return ExecuteContext。
	 * @throws Exception 异常。
	 */
	public static ExecuteContext execute(String actionPath) throws Exception {
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        String[] path = actionPath.split("\\?");
        if (path.length == 2) {
            actionPath = path[0];
            String[] nameValues = path[1].split("&");
            for (String nameValue: nameValues) {
                if (!NestUtil.isEmpty(nameValue)) {
                    String[] nv = nameValue.split("=");
                    String name = nv[0];
                    String value = "";
                    if (nv.length == 2) {
                        value = nv[1];
                    }
                    String[] values = params.get(name);
                    if (values == null) {
                        values = new String[] {value};
                    } else {
                        String[] newValues = new String[values.length + 1];
                        System.arraycopy(values, 0, newValues, 0, values.length);
                        newValues[values.length] = value;
                        values = newValues;
                    }
                    params.put(name, values);
                }
            }
        }
		return execute(actionPath, params);
	}
	
	public static void setPackageBase(String packageBase) {
		setConfig("base", packageBase);
	}
	
	public static void setConfig(String name, String value) {
		config.getProperties().put(name, value);
	}
}
