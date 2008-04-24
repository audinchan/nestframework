package org.nestframework.localization;

import java.io.Serializable;

/**
 * Representing a message.
 * 
 * @author audin
 */
@SuppressWarnings("serial")
public class ActionMessage implements Serializable {
	private String msg;
	private boolean resource = true;
	private Object[] values = null;

	public ActionMessage(String msg) {
		this.msg = msg;
	}
	
	public ActionMessage(String msg, boolean resource) {
		this.msg = msg;
		this.resource = resource;
	}
	
	public ActionMessage(String msg, Object... values) {
		this.msg = msg;
		this.values = values;
	}
	
	public ActionMessage(String msg, boolean resource, Object... values) {
		this.msg = msg;
		this.resource = resource;
		this.values = values;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isResource() {
		return resource;
	}

	public void setResource(boolean resource) {
		this.resource = resource;
	}

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}
}
