package org.nestframework.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.nestframework.action.ActionException;
import org.nestframework.utils.NestUtil;

public class PropertiesRuntimeConfiguration extends RuntimeConfiguration {
	public static final String configLocationKey = "configLocation";
	public static final String PROPERTY_ENCODING_KEY = "configEncoding";
	protected String defaultEncoding = "GBK";

	@Override
	public void init() {
		String encoding = getProperties().get(PROPERTY_ENCODING_KEY);
		if (NestUtil.isEmpty(encoding)) {
			encoding = defaultEncoding;
		}
		String configLocation = getProperties().get(configLocationKey);
		InputStream is = getExternalContext().getResourceAsStream(configLocation);
		Properties props = new Properties();
		try {
			NestUtil.load(props, new InputStreamReader(is, encoding));
		} catch (Exception e) {
			throw new ActionException("Failed to load configuration from:" + configLocation);
		}
		for (Object key: props.keySet()) {
			getProperties().put((String)key, props.getProperty((String)key)); 
		}
		
		super.init();
	}

	
}
