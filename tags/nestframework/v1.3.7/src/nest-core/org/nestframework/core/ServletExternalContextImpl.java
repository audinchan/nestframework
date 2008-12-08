package org.nestframework.core;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;

/**
 * Implements IExternalContext within servlet environment.
 * 
 * @author audin
 *
 */
public class ServletExternalContextImpl implements IExternalContext {
	protected ServletConfig servletConfig;

	public ServletExternalContextImpl(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}
	
	public URL getResource(String path) {
		try {
			return servletConfig.getServletContext().getResource(path);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getResourceAsStream(String path) {
		return servletConfig.getServletContext().getResourceAsStream(path);
	}

}
