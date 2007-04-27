package org.nestframework.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nestframework.config.IConfiguration;
import org.nestframework.config.RuntimeConfiguration;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.ServletExternalContextImpl;


@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
	
	private static IConfiguration nestConfig = null;
	private static ServletConfig servletConfig = null; 

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req, res);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(ServletConfig config) throws ServletException {
		servletConfig = config;
		nestConfig = RuntimeConfiguration.getInstance();
		// Load parameters to NestConfig.
		Enumeration<String> parameterNames = config.getInitParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paraName = parameterNames.nextElement();
			nestConfig.getProperties().put(paraName, config.getInitParameter(paraName));
		}
		nestConfig.setExternalContext(new ServletExternalContextImpl(config));
		nestConfig.init();
		servletConfig.getServletContext().setAttribute(Constant.CONFIG_KEY, nestConfig);
	}

	@SuppressWarnings("unchecked")
	protected void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		IConfiguration config = nestConfig;
		ExecuteContext context = new ExecuteContext(req, res);
        
        // If we are invoking using <jsp:include/>
        String actionPath = (String) req.getAttribute("javax.servlet.include.servlet_path");
        // else
        if (actionPath == null) {
            actionPath = req.getServletPath();
        }
		
		// 添加配置信息。
		context.setConfig(config)
			   .setServletConfig(servletConfig)
			   .setPath(actionPath)
			   .setParams(req.getParameterMap());
			
		try {
			ActionProcessHelper.process(context, config);
		} catch (Exception e) {
			throw new ServletException("Action dispatch error.", e);
		}

	}

}
