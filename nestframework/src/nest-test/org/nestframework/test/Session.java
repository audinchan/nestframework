package org.nestframework.test;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class Session implements HttpSession {
	
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	private ServletContext servletContext;

	public Session() {
		
	}
	
	public Session(ServletContext ctx) {
		servletContext = ctx;
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public void invalidate() {
		// TODO Auto-generated method stub
		attributes.clear();
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public void removeValue(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		attributes.put(name, value);
	}

	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}

}
