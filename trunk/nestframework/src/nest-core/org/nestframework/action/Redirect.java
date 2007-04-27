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
	private boolean local = true;
	
	public Redirect(String target) {
		this.target = target;
	}
	
	public Redirect(String target, Object actionBean) {
		StringBuffer sb = new StringBuffer(target);
		if (target.indexOf('?') == -1) {
			sb.append('?');
		} else {
			sb.append('&');
		}
		Map<String, Object> propertiesMap = NestUtil.getPropertiesMap(actionBean);
		for (Entry<String, Object> entry: propertiesMap.entrySet()) {
			if (entry.getValue() == null) continue;
			String clazz = entry.getValue().getClass().getName();
			if (!clazz.startsWith("java.lang.")) continue;
			if (clazz.equals("java.lang.Class")) continue;
			sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		
		this.target = sb.toString();
	}
	
	public Redirect(String target, Object actionBean, String... propertyNames) {
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
	 * If this is a local redirection. Default is true.
	 * @param local
	 * @return
	 */
	public Redirect setLocal(boolean local) {
		this.local = local;
		return this;
	}
	
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
