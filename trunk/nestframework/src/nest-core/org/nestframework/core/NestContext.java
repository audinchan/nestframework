package org.nestframework.core;

import org.nestframework.config.IConfiguration;

public class NestContext {
	private static IConfiguration config;
	
	public static void setConfig(IConfiguration config) {
		NestContext.config = config;
	}
	
	public static IConfiguration getConfig() {
		return config;
	}
}
