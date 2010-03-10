/**
 * 
 */
package org.nestframework.commons.security.csrf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author audin
 * 
 */
public class CsrfVerifyFilter implements Filter {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(CsrfVerifyFilter.class);
	private String redirect = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch)
			throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");
		}

		try {
			CsrfUtil.validateToken((HttpServletRequest) req);
		} catch (CsrfVerifyException e) {
			if (redirect != null) {
				HttpServletResponse response = (HttpServletResponse) res;
				response.sendRedirect(redirect);
			} else {
				throw e;
			}
		}

		ch.doFilter(req, res);

		if (logger.isDebugEnabled()) {
			logger
					.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig conf) throws ServletException {
		redirect = conf.getInitParameter("redirect");
		String configFile = conf.getInitParameter("config");
		if (configFile == null) {
			configFile = "csrf_config.properties";
		}
		CsrfUtil.init(conf.getServletContext().getResourceAsStream(configFile));
	}

}
