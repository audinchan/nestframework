package org.nestframework.action;

@SuppressWarnings("serial")
/**
 * Action exception.
 * 
 * @author audin.
 */
public class ActionException extends RuntimeException {

	public ActionException() {
	}

	public ActionException(String message) {
		super(message);
	}

	public ActionException(Throwable cause) {
		super(cause);
	}

	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

}
