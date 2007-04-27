package org.nestframework.core;

import java.io.InputStream;
import java.net.URL;

public interface IExternalContext {
	public URL getResource(String path);
	public InputStream getResourceAsStream(String path);
}
