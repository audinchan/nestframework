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
import org.nestframework.commons.utils.Encryption;
import org.nestframework.commons.utils.RSA_Encrypt;

/**
 * @author audin
 *
 */
public class BecomSSOLogoutFilter extends LogoutFilter {

	protected String logoutUrl;
	protected boolean deleteSession = true;
	private String encryptType="MD5";

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
    			String redirectUrl = (String)req.getSession().getAttribute("redirectUrl");
    			if (encrypt(req.getSession().getId(),remoteKey)) {
    				// successfull logout
    				if (deleteSession) {
    					req.getSession().invalidate();
    				}
    				if(redirectUrl==null){
    					super.doFilter(request, response, chain);
    				}else{
    					res.sendRedirect(redirectUrl);
    				}
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

	public void setDeleteSession(boolean deleteSession) {
		this.deleteSession = deleteSession;
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

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}
}
