package ${hss_base_package}.webapp.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.nestframework.commons.utils.StringUtil;

import ${hss_base_package}.common.Constant;


public class UserAuthenticationSessionObjectFilter implements Filter {
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		if (null == request.getSession().getAttribute(Constant.KEY_LOGIN_USER)) {
			SecurityContext ctx = SecurityContextHolder.getContext();
			if (ctx != null) {
				Authentication auth = ctx.getAuthentication();
				if (auth != null && auth.isAuthenticated()) {
					String authName = auth.getName();
					if (authName != null) {
						//验证通过的处理
				        //request.getSession().setAttribute(Constant.KEY_LOGIN_USER, dto);
					}
				}
			}
		}
		fc.doFilter(req, res);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
