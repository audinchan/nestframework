package org.nestframework.core;

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nestframework.localization.LocalizationUtil;

/**
 * ActionBean的上下文。
 * 
 * @author audin
 */
public class BeanContext {
	/**
	 * 执行对象。
	 */
	protected Object actionBean;

	/**
	 * HttpRequest对象。
	 */
	protected HttpServletRequest request;

	/**
	 * HttpResponse对象。
	 */
	protected HttpServletResponse response;
	
	/**
	 * ServletConfig.
	 */
	protected ServletConfig servletConfig;
	
	protected Locale locale = null;
	
	/**
	 * forward.
	 */
	protected Object forward = null;

	public Object getActionBean() {
		return actionBean;
	}

	public Object getForward() {
		return forward;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public Locale getLocale() {
		return locale;
	}
	
	public String getLocaleMessage(String key) {
		return LocalizationUtil.getMessage(locale, key);
	}
	
	public String getLocaleMessage(String key, Object... params) {
		return LocalizationUtil.getMessage(locale, key, params);
	}
}
