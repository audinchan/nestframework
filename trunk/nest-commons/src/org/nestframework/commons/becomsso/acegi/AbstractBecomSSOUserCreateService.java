/**
 * 
 */
package org.nestframework.commons.becomsso.acegi;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author audin
 *
 */
public abstract class AbstractBecomSSOUserCreateService implements
		IBecomSSOUserCreateService, InitializingBean {
	
	private UserDetailsService userDetailsService;

	/* (non-Javadoc)
	 * @see org.nestframework.commons.becomsso.acegi.IBecomSSOUserCreateService#createAndGet(java.lang.String)
	 */
	public UserDetails createAndGet(String loginName) {
		createUser(loginName);
		return getUserDetailsService().loadUserByUsername(loginName);
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Create user within your application.
	 * 
	 * @param loginName LoginName.
	 */
	protected abstract void createUser(String loginName);

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "userDetailsService must be set");
	}
}
