package org.nestframework.core;

import org.nestframework.config.IConfiguration;

/**
 * Nest context.
 * 
 * @author audin
 *
 */
public class NestContext {
	
	/**
	 * Nest configuration.
	 */
	private static IConfiguration config;
	
	/**
	 * Set nest configuration.
	 */
	public static void setConfig(IConfiguration config) {
		NestContext.config = config;
	}
	
	/**
	 * Get nest configuration.
	 * @return
	 */
	public static IConfiguration getConfig() {
		return config;
	}
}
