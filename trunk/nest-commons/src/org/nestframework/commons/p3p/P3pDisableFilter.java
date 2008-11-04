/**
 * 
 */
package org.nestframework.commons.p3p;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author audin
 *
 */
public class P3pDisableFilter implements Filter {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(P3pDisableFilter.class);

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain ch) throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");
		}

		HttpServletResponse response = (HttpServletResponse) res;
		response.addHeader("P3P", "CP=CAO PSA OUR");
		if (logger.isDebugEnabled()) {
			logger
					.debug("add header: P3P='CP=CAO PSA OUR'");
		}
		ch.doFilter(req, res);

		if (logger.isDebugEnabled()) {
			logger
					.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig conf) throws ServletException {

	}

}
