/**
 * 
 */
package ${hss_base_package}.webapp.action;

import javax.servlet.http.HttpServletRequest;

/**
 * @author audin
 *
 */
public abstract class BaseAction {
	
	public String message;
	
	public String error;
	/**
	 * 获取登录用户的Session对象.
	 * 
	 * @param req
	 * @return
	 */
//	public User getLoginUser(HttpServletRequest req) {
//		return (User) req.getSession().getAttribute("sessionUser");
//	}
}
