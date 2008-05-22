/**
 * 
 */
package org.nestframework.commons.acegi.becomsso;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.nestframework.commons.utils.EncodeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author audin
 * 
 */
@SuppressWarnings("unchecked")
public class BecomSSOAuthenticationProvider implements AuthenticationProvider,
		InitializingBean {
	private UserDetailsService userDetailsService;
	private IBecomSSOUserCreateService becomSSOUserCreateService;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert.isInstanceOf(BecomSSOAuthenticationToken.class, authentication, "Only BecomSSOAuthenticationToken is supported");
		
		BecomSSOAuthenticationToken auth = (BecomSSOAuthenticationToken) authentication;
		if (auth.getClientKey() != null && auth.getRemoteKey() != null
				&& EncodeUtil.md5(auth.getClientKey() + auth.getLoginName()).toUpperCase().equals(auth.getRemoteKey())) {
			UserDetails user = null;
			try {
				user = getUserDetailsService().loadUserByUsername(auth.getLoginName());
			} catch (UsernameNotFoundException e) {
				user = getBecomSSOUserCreateService().createAndGet(auth.getLoginName());
			}
			
			Assert.notNull(user, "Can't get userdetails - a violation of the interface contract");
			
			return new BecomSSOAuthenticationToken(auth.getLoginName(), auth.getTestName(), auth.getRemoteKey(), auth.getClientKey(), user.getAuthorities());
		} else {
			throw new BadCredentialsException("Authentication failed.");
		}
	}

	public boolean supports(Class authentication) {
		return BecomSSOAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "userDetailsService must be set");
		Assert.notNull(this.becomSSOUserCreateService, "becomSSOUserCreateService must be set");
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public IBecomSSOUserCreateService getBecomSSOUserCreateService() {
		return becomSSOUserCreateService;
	}

	public void setBecomSSOUserCreateService(
			IBecomSSOUserCreateService becomSSOUserCreateService) {
		this.becomSSOUserCreateService = becomSSOUserCreateService;
	}

}
