package org.nestframework.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nestframework.action.FileItem;
import org.nestframework.config.IConfiguration;
import org.nestframework.localization.ActionMessages;
import org.nestframework.localization.LocalizationUtil;


public class ExecuteContext {
	
	/**
	 * Action Path.
	 * eg: /user/UserAction.a
	 */
	private String path;
	
	/**
	 * 执行的目标方法。
	 */
	private Method action;
	
	/**
	 * 默认执行方法。
	 */
	private Method defaultAction;

	/**
	 * 执行对象。
	 */
	private Object actionBean;

	/**
	 * HttpRequest对象。
	 */
	private HttpServletRequest request;

	/**
	 * HttpResponse对象。
	 */
	private HttpServletResponse response;
	
	/**
	 * 配置信息。
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
	
	private Locale locale = null;
	
	private Stage stage = null;
	
	private Stage nextStage = null;
	
	private ActionMessages messages = new ActionMessages();
	
	private ActionMessages errors = new ActionMessages();
	
	private Set<String> readableProperties = new HashSet<String>();
	
	private Set<String> writableProperties = new HashSet<String>();
	
	/**
	 * 提交的参数.
	 */
	private Map<String, String[]> params = new HashMap<String, String[]>();
	
	/**
	 * 上传的文件.
	 */
	private Map<String, FileItem> uploadedFiles = new HashMap<String, FileItem>(); 

	public ExecuteContext() {
	}
	
	public ExecuteContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * 执行Action。
	 * @return Forward。
	 * @throws Exception 异常对象。
	 */
	public Object execute() throws Exception {
		List<Object> paras = new ArrayList<Object>();
		Class<?>[] paraTypes = action.getParameterTypes();
		// 自动猜测Action参数类型
		for (Class<?> clazz : paraTypes) {
			if (clazz.equals(HttpServletRequest.class)) {
				paras.add(request);
			} else if (clazz.equals(HttpServletResponse.class)) {
				paras.add(response);
			} else if (clazz.equals(HttpSession.class)) {
				paras.add(request.getSession(true));
			} else if (clazz.equals(BeanContext.class)) {
				paras.add(getBeanContext());
			} else if (clazz.equals(ActionMessages.class)) {
				paras.add(errors);
			} else if (clazz.equals(this.getClass())) {
				paras.add(this);
			}
		}
		forward = action.invoke(actionBean, (Object[])paras.toArray(new Object[] {}));
		return forward;
	}
	
	/**
	 * 获取Bean Context。
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

	public ExecuteContext setForward(String forward) {
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
	
	public Map<String, FileItem> getUploadedFiles() {
		return uploadedFiles;
	}

	public ExecuteContext setUploadedFiles(Map<String, FileItem> uploadedFiles) {
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
}
