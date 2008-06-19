/**
 * 
 */
package org.nestframework.commons.acegi.becomsso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.logout.LogoutFilter;
import org.acegisecurity.ui.logout.LogoutHandler;
import org.nestframework.commons.utils.EncodeUtil;

/**
 * @author audin
 *
 */
public class BecomSSOLogoutFilter extends LogoutFilter {

	protected String logoutUrl;
	
	public BecomSSOLogoutFilter(String logoutSuccessUrl,
			LogoutHandler[] handlers) {
		super(logoutSuccessUrl, handlers);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (requiresLogout(req, res)) {
        	String ssoLogoutTag = req.getParameter("sso_logout");
    		if (null == ssoLogoutTag) {
    			String thisUrl = req.getRequestURL().toString();
    			
    			if (thisUrl.indexOf('?') != -1) {
    				thisUrl += "&sso_logout=1";
    			} else {
    				thisUrl += "?sso_logout=1";
    			}
    			try {
    				thisUrl = URLEncoder.encode(thisUrl, "UTF-8");
    			} catch (UnsupportedEncodingException e) {}
    			String key = req.getSession().getId();
    			
    			String redirectUrl = logoutUrl + "?authlogout_url=" + thisUrl + "&authlogout_key=" + key;
    			
    			res.sendRedirect(redirectUrl);
    			return;
    		} else {
    			String remoteKey = req.getParameter("authlogout_key");
    			if (EncodeUtil.md5(req.getSession().getId()).toUpperCase().equals(remoteKey)) {
    				// successfull logout
    				super.doFilter(request, response, chain);
    				return;
    			} else {
    				String url = AbstractProcessingFilter.obtainFullRequestUrl(req);
    				res.sendRedirect(url);
    				return;
    			}
    		}
        }
		
        chain.doFilter(request, response);
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

}
