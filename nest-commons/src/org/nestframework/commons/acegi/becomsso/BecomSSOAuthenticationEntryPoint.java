/**
 * 
 */
package org.nestframework.commons.acegi.becomsso;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.AuthenticationEntryPoint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author audin
 *
 */
public class BecomSSOAuthenticationEntryPoint implements
		AuthenticationEntryPoint, InitializingBean {
	
	/**
	 * Becom SSO auth url.
	 */
	private String authUrl;

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(this.authUrl, "authUrl must be specified");
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.ui.AuthenticationEntryPoint#commence(javax.servlet.ServletRequest, javax.servlet.ServletResponse, org.acegisecurity.AuthenticationException)
	 */
	public void commence(ServletRequest request, ServletResponse response,
			AuthenticationException authException) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String thisUrl = AbstractProcessingFilter.obtainFullRequestUrl(req);
		thisUrl = URLEncoder.encode(thisUrl, "UTF-8");
		String key = req.getSession().getId();
		
		String redirectUrl = authUrl + "?auth_url=" + thisUrl + "&auth_key=" + key;
		
		res.sendRedirect(redirectUrl);
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

}
