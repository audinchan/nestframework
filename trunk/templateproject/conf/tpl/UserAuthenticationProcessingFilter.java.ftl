package ${hss_base_package}.webapp.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

//import com.becom.rock.model.User;
//import com.becom.rock.service.IUserManager;

/**
 * @author audin
 */
public class UserAuthenticationProcessingFilter extends
    AuthenticationProcessingFilter
{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(UserAuthenticationProcessingFilter.class);
    
//    private IUserManager userManager;
    
    /*
     * 认证通过后将用户对象从数据库中取出来设置到Session中去。
     * (non-Javadoc)
     * @see org.acegisecurity.ui.AbstractProcessingFilter#onSuccessfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.Authentication)
     */
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("onSuccessfulAuthentication(HttpServletRequest, HttpServletResponse, Authentication) - start");
		}

		UserDetails ud = (UserDetails) authResult.getPrincipal();
//		User user = userManager.get(ud.getUsername());
//        request.getSession().setAttribute("sessionUser",
//        		user);

		if (logger.isDebugEnabled()) {
			logger
					.debug("onSuccessfulAuthentication(HttpServletRequest, HttpServletResponse, Authentication) - end");
		}
	}


	/**
	 * @param userManager the userManager to set
	 */
//	public void setUserManager(IUserManager userManager) {
//		this.userManager = userManager;
//	}


	/*
	 * 确保必要的属性已经设置。
	 * (non-Javadoc)
	 * @see org.acegisecurity.ui.AbstractProcessingFilter#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
//		Assert.notNull(this.userManager, "userManager must be set");
	}
	
	
}
