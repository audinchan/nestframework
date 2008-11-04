package org.nestframework.commons.acegi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.context.SecurityContextHolder;

/**
 * This is an ACEGI logout helper. There are two parameters:
 * <pre>
 * jscallback: js callback function to use.
 * siteid: site identifier.
 * The callback is used as:
 * jscallback(boolean, siteid);
 * The first parameter is always to true.
 * </pre>
 * @author audin
 *
 */
@SuppressWarnings("serial")
public class JSLogoutServlet extends HttpServlet {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(JSLogoutServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("doGet(HttpServletRequest, HttpServletResponse) - start");
		}

		try {
			String jscallback = req.getParameter("jscallback");
			String siteid = req.getParameter("siteid");
			SecurityContextHolder.clearContext();
			HttpSession session = req.getSession();
			if (session != null) {
				session.invalidate();
			}
			PrintWriter writer = resp.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append(jscallback).append("(true, \"").append(siteid).append("\");");
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (RuntimeException e) {
			logger.error("doGet(HttpServletRequest, HttpServletResponse)", e);

		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("doGet(HttpServletRequest, HttpServletResponse) - end");
		}
	}

}
