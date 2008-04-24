/**
 * 
 */
package org.nestframework.core;

import org.nestframework.config.IConfiguration;

/**
 * Initable interface.
 * 
 * @author audin
 *
 */
public interface IInitable {
	/**
	 * Initial.
	 * @param config Nest configuration.
	 */
	public void init(IConfiguration config);
}
