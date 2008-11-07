package org.nestframework.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.nestframework.core.IExternalContext;

public class FileExternalContextImpl implements IExternalContext {
	protected File base;

	public FileExternalContextImpl(File base) {
		this.base = base;
	}
	
	public URL getResource(String path) {
		try {
			File f = new File(path);
			if (f.exists() && f.isFile()) {
				return f.toURI().toURL();
			}
			f = new File(base, path);
			if (f.exists() && f.isFile()) {
				return f.toURI().toURL();
			}
			return Thread.currentThread().getContextClassLoader().getResource(path);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getResourceAsStream(String path) {
		try {
			File f = new File(path);
			if (f.exists() && f.isFile()) {
				return new FileInputStream(f);
			}
			f = new File(base, path);
			if (f.exists() && f.isFile()) {
				return new FileInputStream(f);
			}
			return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
