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
import org.nestframework.commons.utils.Encryption;
import org.nestframework.commons.utils.RSA_Encrypt;
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
	private boolean autoCreateUser = false;
	private String encryptType="MD5";

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert.isInstanceOf(BecomSSOAuthenticationToken.class, authentication, "Only BecomSSOAuthenticationToken is supported");
		
		BecomSSOAuthenticationToken auth = (BecomSSOAuthenticationToken) authentication;
		if (auth.getClientKey() != null && auth.getRemoteKey() != null
				&& encrypt(auth.getClientKey() + auth.getLoginName(),auth.getRemoteKey())) {
			UserDetails user = null;
			try {
				user = getUserDetailsService().loadUserByUsername(auth.getLoginName());
			} catch (UsernameNotFoundException e) {
				if (autoCreateUser) {
					user = getBecomSSOUserCreateService().createAndGet(auth.getLoginName());
				} else {
					throw new NoSuchUserException("No such user.", e);
				}
			}
			
			Assert.notNull(user, "Can't get userdetails - a violation of the interface contract");
			
			return new BecomSSOAuthenticationToken(auth.getLoginName(), auth.getTestName(), auth.getRemoteKey(), auth.getClientKey(), user.getAuthorities());
		} else {
			throw new BadCredentialsException("Authentication failed.");
		}
	}
	//校验
	private boolean encrypt(String data,String sign){
		if("MD5".equalsIgnoreCase(encryptType)){
			//MD5方式加密
			return sign.equals(Encryption.computeDigest(data));
		}else{
			//RSA方式加密
			return RSA_Encrypt.verify(data,sign);
		}
	}
	public boolean supports(Class authentication) {
		return BecomSSOAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "userDetailsService must be set");
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

	public void setAutoCreateUser(boolean autoCreateUser) {
		this.autoCreateUser = autoCreateUser;
	}
	public String getEncryptType() {
		return encryptType;
	}
	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

}
