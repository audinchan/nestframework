package org.nestframework.action;

import java.util.Map;
import java.util.Map.Entry;

import org.nestframework.utils.NestUtil;

/**
 * Describe a redirection forward.
 * 
 * @author audin
 */
public class Redirect {
	
	/**
	 * target location.
	 */
	private String target;
	
	/**
	 * Indicate redirection whether is in the same application or not.
	 */
	private boolean local = true;
	
	/**
	 * Create a redirection forward.
	 * 
	 * @param target Redirection location.
	 */
	public Redirect(String target) {
		this.target = target;
	}
	
	/**
	 * Create a redirection forward.
	 * 
	 * @param target Redirection location.
	 * @param isInSameApplication If <code>true</code> then the redirection is in the same application.
	 */
	public Redirect(String target, boolean isInSameApplication) {
		this.target = target;
		local = isInSameApplication;
	}
	
	/**
	 * Create a redirection forward.
	 * 
	 * @param target Redirection location.
	 * @param actionBean Action bean.
	 * @param propertyNames Which bean properties are populated.
	 */
	public Redirect(String target, Object actionBean, String... propertyNames) {
		 this(target, true, actionBean, propertyNames);
	}
	
	/**
	 * Create a redirection forward.
	 * 
	 * @param target Redirection location.
	 * @param isInSameApplication If <code>true</code> then the redirection is in the same application.
	 * @param actionBean Action bean.
	 * @param propertyNames Which bean properties are populated.
	 */
	public Redirect(String target, boolean isInSameApplication, Object actionBean, String... propertyNames) {
		StringBuffer sb = new StringBuffer(target);
		if (target.indexOf('?') == -1) {
			sb.append('?');
		} else {
			sb.append('&');
		}
		Map<String, Object> propertiesMap = NestUtil.getPropertiesMap(actionBean, propertyNames);
		for (Entry<String, Object> entry: propertiesMap.entrySet()) {
			if (entry.getValue() == null) continue;
			sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		
		this.target = sb.toString();
	}

	public String getTarget() {	
		return target;
	}

	public Redirect setTarget(String target) {
		this.target = target;
		return this;
	}
	
	/**
	 * Set the redirection as the same application or not. 
	 * 
	 * @param isInSameApplication If <code>true</code> then the redirection is in the same application.
	 * @return self.
	 */
	public Redirect setLocal(boolean isInSameApplication) {
		this.local = isInSameApplication;
		return this;
	}
	
	/**
	 * Indicate whether is in the same application or not.
	 * 
	 * @return <code>true</code> means is in the same application.
	 */
	public boolean isLocal() {
		return local;
	}
	
	/**
	 * append parameter to target url.
	 * @param name
	 * @param value
	 * @return
	 */
	public Redirect appendParameter(String name, Object value) {
		StringBuffer sb = new StringBuffer(target);
		if (target.indexOf('?') == -1) {
			sb.append('?');
		} else if (target.charAt(target.length() - 1) != '&') {
			sb.append('&');
		}
		sb.append(name).append('=').append(value);
		target = sb.toString();
		return this;
	}
}
