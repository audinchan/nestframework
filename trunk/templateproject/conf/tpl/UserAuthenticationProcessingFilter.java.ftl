package ${hss_base_package}.webapp.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.nestframework.commons.utils.StringUtil;

import ${hss_base_package}.common.Constant;
import ${hss_base_package}.exception.BadRandomCodeException;
/**
 * @author audin
 */
public class UserAuthenticationProcessingFilter extends
    AuthenticationProcessingFilter
{
    @Override
	public Authentication attemptAuthentication(HttpServletRequest request)
		throws org.acegisecurity.AuthenticationException {
	
		// 加入验证码校验代码.
		String checkCode = request.getParameter("j_captcha_response");
		HttpSession session = request.getSession();
	
		if (StringUtil.isEmpty(checkCode)
		 || !checkCode.equalsIgnoreCase((String) session
				 .getAttribute(Constant.KEY_LOGIN_CHECKCODE)))
		{
			throw new BadRandomCodeException("checkCode error");
		}
		//解决中文登录 
		String username = "";
		try {
			username = new String(obtainUsername(request).getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String password = obtainPassword(request);
	     
		 
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					username, password);
		try
		{
			request.getSession().setAttribute(ACEGI_SECURITY_LAST_USERNAME_KEY, username);
		}
		catch (AuthenticationException ex)
		{
			throw ex;
		}
		//验证用户登录信息和用户权限
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
