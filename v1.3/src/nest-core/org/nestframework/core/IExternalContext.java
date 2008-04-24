package org.nestframework.core;

import java.io.InputStream;
import java.net.URL;

/**
 * External context.
 * 
 * @author audin
 *
 */
public interface IExternalContext {
	public URL getResource(String path);
	public InputStream getResourceAsStream(String path);
}
