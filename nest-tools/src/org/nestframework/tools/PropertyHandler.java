package org.nestframework.tools;

import java.util.Properties;

public class PropertyHandler {
	private String key;
	private Properties properties;

	/**
	 * @return Returns the property.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param property The property to set.
	 */
	public PropertyHandler setKey(String property) {
		this.key = property;
		return this;
	}
	
	
	/**
	 * @return Returns the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties The properties to set.
	 */
	public PropertyHandler setProperties(Properties properties) {
		this.properties = properties;
		return this;
	}

	public boolean asBoolean() {
		return "true".equalsIgnoreCase(properties.getProperty(key));
	}
	
	public boolean asBoolean(String key) {
		return "true".equalsIgnoreCase(properties.getProperty(key));
	}
	
	public String asString() {
		String s = properties.getProperty(key);
		return (null == s) ? "" : s;
	}
	
	public String asString(String key) {
		String s = properties.getProperty(key);
		return (null == s) ? "" : s;
	}
	
	public String asPath() {
		return asString().replaceAll("\\.", "/");
	}
	
	public String asPath(String key) {
		return asString(key).replaceAll("\\.", "/");
	}
}
