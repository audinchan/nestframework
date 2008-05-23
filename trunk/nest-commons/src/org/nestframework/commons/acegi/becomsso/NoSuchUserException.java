/**
 * 
 */
package org.nestframework.commons.acegi.becomsso;

import org.acegisecurity.AuthenticationException;

/**
 * @author audin
 *
 */
@SuppressWarnings("serial")
public class NoSuchUserException extends AuthenticationException {

	public NoSuchUserException(String msg, Throwable t) {
		super(msg, t);
		// TODO Auto-generated constructor stub
	}

	public NoSuchUserException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
