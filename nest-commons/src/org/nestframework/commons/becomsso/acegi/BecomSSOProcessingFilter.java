/**
 * 
 */
package org.nestframework.commons.becomsso.acegi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.AuthenticationEntryPoint;
import org.springframework.util.Assert;

/**
 * @author audin
 * 
 */
public class BecomSSOProcessingFilter extends AbstractProcessingFilter {

	public static final String BECOM_SSO_LAST_USERNAME_KEY = "BECOM_SSO_LAST_USERNAME_KEY";

	private AuthenticationEntryPoint authenticationEntryPoint;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.ui.AbstractProcessingFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request)
			throws AuthenticationException {
		String loginName = request.getParameter("becom_auth_username");
		String testName = request.getParameter("becom_authtest_username");
		String remoteKey = request.getParameter("becom_auth_key");
		String clientKey = request.getSession().getId();

		BecomSSOAuthenticationToken authRequest = new BecomSSOAuthenticationToken(
				loginName, testName, remoteKey, clientKey);

		authRequest.setDetails(authenticationDetailsSource
				.buildDetails((HttpServletRequest) request));

		Authentication authentication = getAuthenticationManager()
				.authenticate(authRequest);

		request.getSession().setAttribute(BECOM_SSO_LAST_USERNAME_KEY,
				loginName);
		
		afterAuth(request, loginName);

		return authentication;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.ui.AbstractProcessingFilter#getDefaultFilterProcessesUrl()
	 */
	@Override
	public String getDefaultFilterProcessesUrl() {
		return "/j_acegi_becomsso_security_check";
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException {
		if (failed instanceof BadCredentialsException) {
			try {
				authenticationEntryPoint.commence(request, response, failed);
			} catch (ServletException e) {
				throw new IOException("Auth redirect failed.", e);
			}
		} else {
			super.unsuccessfulAuthentication(request, response, failed);
		}
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		return null != request.getParameter("becom_auth_key");
	}

	@Override
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String failureUrl) throws IOException {
		try {
			authenticationEntryPoint.commence(request, response, null);
		} catch (ServletException e) {
			throw new IOException("Auth redirect failed.", e);
		}
	}

	public void setAuthenticationEntryPoint(
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	protected void afterAuth(HttpServletRequest request, String loginName) {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(this.authenticationEntryPoint, "authenticationEntryPoint must be set");
	};

}
