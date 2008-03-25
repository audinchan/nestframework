package org.nestframework.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.action.ActionException;
import org.nestframework.action.IActionHandler;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.action.defaults.ActionMethodAfterInterceptHandler;
import org.nestframework.action.defaults.ActionMethodBeforeInterceptHandler;
import org.nestframework.action.defaults.DefaultActionBeanCreater;
import org.nestframework.action.defaults.DefaultActionBeanGetter;
import org.nestframework.action.defaults.DefaultActionBeanSetter;
import org.nestframework.action.defaults.DefaultActionExecutor;
import org.nestframework.action.defaults.DefaultActionForwarder;
import org.nestframework.action.defaults.DefaultActionMethodFinder;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.IExternalContext;
import org.nestframework.core.IInitable;
import org.nestframework.core.IMultipartHandler;
import org.nestframework.core.NestContext;
import org.nestframework.core.Stage;
import org.nestframework.core.StageHandler;
import org.nestframework.localization.LocalizationUtil;
import org.nestframework.utils.NestUtil;

public class RuntimeConfiguration implements IConfiguration {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(RuntimeConfiguration.class);

	private Map<Stage, StageHandler> handlers = new LinkedHashMap<Stage, StageHandler>();

	private List<IExceptionHandler> exceptionHandlers = new ArrayList<IExceptionHandler>();

	private String packageBase = null;

	private Map<String, String> properties = new HashMap<String, String>();

	private IExternalContext externalContext;

	private IMultipartHandler multipartHandler;

	public static IConfiguration getInstance() {
		if (log.isDebugEnabled()) {
			log.debug("getInstance() - start");
		}

		IConfiguration nestConfig = new RuntimeConfiguration();
		NestContext.setConfig(nestConfig);

		if (log.isDebugEnabled()) {
			log.debug("getInstance() - end");
		}
		return nestConfig;
	}

	protected void addDefaultHandlers() {
		addLifecycleHandler(new ActionMethodBeforeInterceptHandler());
		addLifecycleHandler(new DefaultActionBeanCreater());
		addLifecycleHandler(new DefaultActionBeanSetter());
		addLifecycleHandler(new DefaultActionMethodFinder());
		addLifecycleHandler(new DefaultActionExecutor());
		addLifecycleHandler(new DefaultActionBeanGetter());
		addLifecycleHandler(new DefaultActionForwarder());
		addLifecycleHandler(new ActionMethodAfterInterceptHandler());
	}

	protected IConfiguration addLifecycleHandler(Stage stage,
			IActionHandler handler) {
		if (log.isDebugEnabled()) {
			log.debug("addLifecycleHandler(Stage, IActionHandler) - start");
		}

		StageHandler s = handlers.get(stage);
		if (s == null) {
			s = new StageHandler(stage, true);
			handlers.put(stage, s);
		}
		s.addHandler(handler);

		if (log.isDebugEnabled()) {
			log.debug("addLifecycleHandler(Stage, IActionHandler) - end");
		}
		return this;
	}

	public IConfiguration addLifecycleHandler(IActionHandler handler) {
		if (log.isDebugEnabled()) {
			log.debug("addLifecycleHandler(IActionHandler) - start");
		}
		
		// init
		if (handler instanceof IInitable) {
			((IInitable) handler).init(this);
		}

		Intercept intercept = handler.getClass().getAnnotation(Intercept.class);
		if (intercept != null) {
			Stage[] stages = intercept.value();
			for (Stage stage : stages) {
				addLifecycleHandler(stage, handler);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("addLifecycleHandler(IActionHandler) - end");
		}
		return this;
	}

	public StageHandler getStageHandler(Stage stage) {
		StageHandler s = handlers.get(stage);
		if (s == null) {
			s = new StageHandler(stage, true);
		}
		return s;
	}

	public IConfiguration addExceptionHandler(IExceptionHandler handler) {
		// init
		if (handler instanceof IInitable) {
			((IInitable) handler).init(this);
		}
		
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

	public IMultipartHandler getMultipartHandler() {
		return multipartHandler;
	}

	public void init() {
		if (log.isDebugEnabled()) {
			log.debug("init() - start");
		}

		if (NestUtil.isEmpty(properties.get("base"))) {
			throw new RuntimeException("Action base must be specified.");
		}

		// localization
		String resourceFile = properties.get("resourceFile");
		if (!NestUtil.isEmpty(resourceFile)) {
			LocalizationUtil.setResource(resourceFile);
		}

		setPackageBase(properties.get("base"));
		String exceptionHandlers = properties.get("exceptionHandlers");
		if (NestUtil.isNotEmpty(exceptionHandlers)) {
			String[] handlerClasses = NestUtil.trimAll(exceptionHandlers).split(",");
			for (String clazz : handlerClasses) {
				try {
					Object handler = Class.forName(clazz.trim()).newInstance();
					// add exception handler.
					addExceptionHandler((IExceptionHandler) handler);
				} catch (Exception e) {
					log.error("init()", e);
					throw new ActionException("Failed to add exception handler, class=" + handlerClasses, e);
				}
			}
		}

		String actionHandlers = properties.get("actionHandlers");
		if (NestUtil.isNotEmpty(actionHandlers)) {
			String[] handlerClasses = NestUtil.trimAll(actionHandlers).split(",");
			for (String clazz : handlerClasses) {
				try {
					Object handler = Class.forName(clazz.trim()).newInstance();
					// add action handler.
					addLifecycleHandler((IActionHandler) handler);
				} catch (Exception e) {
					log.error("init()", e);
					throw new ActionException("Failed to add action handler, class=" + clazz, e);
				}
			}
		}

		// multipart handler
		String mh = properties.get("multipartHandler");
		if (NestUtil.isEmpty(mh)) {
			mh = "org.nestframework.core.CosMultipartHandler";
		}
		try {
			multipartHandler = (IMultipartHandler) Class.forName(mh.trim())
					.newInstance();
		} catch (Exception e) {
			throw new ActionException("Failed to add multipart handler, class="
					+ mh, e);
		}

		// add default handlers.
		// default handler is at tail of handler chain.
		addDefaultHandlers();

		if (log.isDebugEnabled()) {
			log.debug("init() - end");
		}
	}
}