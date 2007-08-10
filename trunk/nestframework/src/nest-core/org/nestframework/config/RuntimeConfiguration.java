package org.nestframework.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nestframework.action.IActionHandler;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.action.defaults.ActionMethodAfterInterceptHandler;
import org.nestframework.action.defaults.DefaultActionBeanCreater;
import org.nestframework.action.defaults.DefaultActionBeanGetter;
import org.nestframework.action.defaults.DefaultActionBeanSetter;
import org.nestframework.action.defaults.DefaultActionExecutor;
import org.nestframework.action.defaults.DefaultActionForwarder;
import org.nestframework.action.defaults.DefaultActionMethodFinder;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.IExternalContext;
import org.nestframework.core.NestContext;
import org.nestframework.core.Stage;
import org.nestframework.core.StageHandler;
import org.nestframework.localization.LocalizationUtil;
import org.nestframework.utils.NestUtil;


public class RuntimeConfiguration implements IConfiguration {
	
	private Map<Stage, StageHandler> handlers = new LinkedHashMap<Stage, StageHandler>();
	
	private List<IExceptionHandler> exceptionHandlers = new ArrayList<IExceptionHandler>();

	private String packageBase = null;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	private IExternalContext externalContext;
		
	public static IConfiguration getInstance() {
		IConfiguration nestConfig = new RuntimeConfiguration();
		// after intercept
		nestConfig.addLifecycleHandler(new ActionMethodAfterInterceptHandler());
		nestConfig.addLifecycleHandler(new DefaultActionBeanCreater());
		nestConfig.addLifecycleHandler(new DefaultActionBeanSetter());
		nestConfig.addLifecycleHandler(new DefaultActionMethodFinder());
		nestConfig.addLifecycleHandler(new DefaultActionExecutor());
		nestConfig.addLifecycleHandler(new DefaultActionBeanGetter());
		nestConfig.addLifecycleHandler(new DefaultActionForwarder());
		NestContext.setConfig(nestConfig);
		return nestConfig;
	}
	
	public IConfiguration addLifecycleHandler(Stage stage, IActionHandler handler) {
		StageHandler s = handlers.get(stage);
		if (s == null) {
			s = new StageHandler(stage);
			handlers.put(stage, s);
		}
		s.addHandler(handler);
		
		return this;
	}

	public IConfiguration addLifecycleHandler(IActionHandler handler) {
		Intercept intercept = handler.getClass().getAnnotation(Intercept.class);
		if (intercept != null) {
			Stage[] stages = intercept.value();
			for (Stage stage : stages) {
				addLifecycleHandler(stage, handler);
			}
		}
		return this;
	}

	public StageHandler getStageHandler(Stage stage) {
		StageHandler s = handlers.get(stage);
		if (s == null) {
			s = new StageHandler(stage);
		}
		return s;
	}

	public IConfiguration addExceptionHandler(IExceptionHandler handler) {
		this.exceptionHandlers.add(handler);
		return this;
	}

	public List<IExceptionHandler> getExceptionHandlers() {
		return exceptionHandlers;
	}

	public String getPackageBase() {
		return packageBase;
	}

	public IConfiguration setPackageBase(String packageBase) {
		this.packageBase = packageBase;
		return this;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public IConfiguration setExternalContext(IExternalContext context) {
		this.externalContext = context;
		return this;
	}

	public IExternalContext getExternalContext() {
		return externalContext;
	}

	public void init() {
		if (NestUtil.isEmpty(properties.get("base"))) {
			throw new RuntimeException("Action base must be specified.");
		}
		
		// localization
		String resourceFile = properties.get("resourceFile");
		if (!NestUtil.isEmpty(resourceFile)) {
			LocalizationUtil.setResource(resourceFile);
		}
		
		setPackageBase(properties.get("base"));
		String ehs = properties.get("exceptionHandlers");
		if (ehs != null) {
			String[] ehClasses = ehs.split(",");
			for (String clazz: ehClasses) {
				try {
					Object object = Class.forName(clazz).newInstance();
					IExceptionHandler exceptionHandler = (IExceptionHandler) object;
					
					// 添加异常处理类。
					addExceptionHandler(exceptionHandler);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		String handlersName = properties.get("actionHandlers");
		String[] handlers = handlersName.split(",");
		for (String hName : handlers) {
			try {
				Object handler = Class.forName(hName.trim()).newInstance();
				addLifecycleHandler((IActionHandler) handler);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
