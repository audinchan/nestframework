package org.nestframework.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nestframework.action.FileItem;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.config.IConfiguration;
import org.nestframework.localization.ActionMessages;
import org.nestframework.localization.LocalizationUtil;

/**
 * Execute context.
 * 
 * @author audin
 *
 */
public class ExecuteContext {

	/**
	 * Action Path. eg: /user/UserAction.a
	 */
	private String path;

	/**
	 * Action method.
	 */
	private Method action;

	/**
	 * Default action method.
	 */
	private Method defaultAction;

	/**
	 * Action bean.
	 */
	private Object actionBean;

	/**
	 * Action bean class.
	 */
	private Class<?> actionClass;

	/**
	 * Http servlet request.
	 */
	private HttpServletRequest request;

	/**
	 * Http servlet response.
	 */
	private HttpServletResponse response;

	/**
	 * Nest configurations.
	 */
	private IConfiguration config;

	/**
	 * ServletConfig.
	 */
	private ServletConfig servletConfig;

	/**
	 * forward.
	 */
	private Object forward = null;

	/**
	 * Current locale.
	 */
	private Locale locale = null;

	/**
	 * Current stage.
	 */
	private Stage stage = null;

	/**
	 * Nest stage.
	 */
	private Stage nextStage = null;

	/**
	 * Action messages.
	 */
	private ActionMessages messages = new ActionMessages();

	/**
	 * Action errors.
	 */
	private ActionMessages errors = new ActionMessages();

	/**
	 * Cache readable properties.
	 */
	private Set<String> readableProperties = new HashSet<String>();

	/**
	 * Cache writable properties.
	 */
	private Set<String> writableProperties = new HashSet<String>();

	/**
	 * Parameters from http servlet request.
	 */
	private Map<String, String[]> params = new HashMap<String, String[]>();

	/**
	 * Uploaded files.
	 */
	private Map<String, FileItem[]> uploadedFiles = new HashMap<String, FileItem[]>();

	public ExecuteContext() {
	}

	public ExecuteContext(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * Get bean context.
	 * 
	 * @return
	 */
	public BeanContext getBeanContext() {
		BeanContext bc = new BeanContext();
		bc.actionBean = actionBean;
		bc.request = request;
		bc.response = response;
		bc.servletConfig = servletConfig;
		bc.forward = forward;
		bc.locale = locale;

		return bc;
	}

	public String getPath() {
		return path;
	}

	public ExecuteContext setPath(String path) {
		this.path = path;
		return this;
	}

	public Method getAction() {
		return action;
	}

	public ExecuteContext setAction(Method action) {
		this.action = action;
		return this;
	}

	public Method getDefaultAction() {
		return defaultAction;
	}

	public ExecuteContext setDefaultAction(Method defaultAction) {
		this.defaultAction = defaultAction;
		return this;
	}

	public Object getActionBean() {
		return actionBean;
	}

	public ExecuteContext setActionBean(Object actionBean) {
		this.actionBean = actionBean;
		return this;
	}

	public Class<?> getActionClass() {
		return actionClass;
	}

	public void setActionClass(Class<?> actionClass) {
		this.actionClass = actionClass;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public ExecuteContext setRequest(HttpServletRequest request) {
		this.request = request;
		return this;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ExecuteContext setResponse(HttpServletResponse response) {
		this.response = response;
		return this;
	}

	public IConfiguration getConfig() {
		return config;
	}

	public ExecuteContext setConfig(IConfiguration config) {
		this.config = config;
		return this;
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public ExecuteContext setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
		return this;
	}

	public Object getForward() {
		return forward;
	}

	public ExecuteContext setForward(Object forward) {
		this.forward = forward;
		return this;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getNextStage() {
		return nextStage;
	}

	public void setNextStage(Stage currentStage) {
		this.nextStage = currentStage;
	}

	public Set<String> getReadableProperties() {
		return readableProperties;
	}

	public ExecuteContext addReadableProperty(String propertyName) {
		this.readableProperties.add(propertyName);
		return this;
	}

	public Set<String> getWritableProperties() {
		return writableProperties;
	}

	public ExecuteContext addWritableProperty(String propertyName) {
		this.writableProperties.add(propertyName);
		return this;
	}

	public Map<String, String[]> getParams() {
		return params;
	}

	public ExecuteContext setParams(Map<String, String[]> params) {
		this.params = params;
		return this;
	}

	public Map<String, FileItem[]> getUploadedFiles() {
		return uploadedFiles;
	}

	public ExecuteContext setUploadedFiles(Map<String, FileItem[]> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
		return this;
	}

	public ExecuteContext addParam(String name, String[] value) {
		params.put(name, value);
		return this;
	}

	public String getMessage(String key) {
		return LocalizationUtil.getMessage(locale, key);
	}

	public String getMessage(String key, Object... params) {
		return LocalizationUtil.getMessage(locale, key, params);
	}

	public ActionMessages getActionMessages() {
		return messages;
	}

	public ActionMessages getActionErrors() {
		return errors;
	}
	
	public boolean handleException(Exception e) {
		boolean handled = false;
		for (IExceptionHandler handler: config.getExceptionHandlers()) {
			if (handler.execute(e, this)) {
				handled = true;
				break;
			}
		}
		
		return handled;
	}
}
