/**
 * 
 */
package org.nestframework.commons.acegi.becomsso;

import java.io.Serializable;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.AbstractAuthenticationToken;

/**
 * @author audin
 * 
 */
@SuppressWarnings("serial")
public class BecomSSOAuthenticationToken extends AbstractAuthenticationToken
		implements Serializable {
	
	private final String loginName;
	private final String remoteKey;
	private final String testName;
	private final String clientKey;

	public BecomSSOAuthenticationToken(final String loginName, final String testName, final String remoteKey, final String clientKey) {
		super(null);
		this.loginName = loginName;
		this.testName = testName;
		this.remoteKey = remoteKey;
		this.clientKey = clientKey;
		
		setAuthenticated(false);
	}
	
	public BecomSSOAuthenticationToken(final String loginName, final String testName, final String remoteKey, final String clientKey, GrantedAuthority[] authorities) {
		super(authorities);
		this.loginName = loginName;
		this.testName = testName;
		this.remoteKey = remoteKey;
		this.clientKey = clientKey;
		
		setAuthenticated(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.Authentication#getCredentials()
	 */
	public Object getCredentials() {
		return loginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.Authentication#getPrincipal()
	 */
	public Object getPrincipal() {
		return remoteKey;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getTestName() {
		return testName;
	}

	public String getRemoteKey() {
		return remoteKey;
	}

	public String getClientKey() {
		return clientKey;
	}
}
