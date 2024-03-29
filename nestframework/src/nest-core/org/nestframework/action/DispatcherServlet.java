package org.nestframework.action;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nestframework.config.IConfiguration;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.ServletExternalContextImpl;
import org.nestframework.utils.NestUtil;

@SuppressWarnings({"serial", "unchecked"})
public class DispatcherServlet extends HttpServlet {
	/**
	 * Constant identifier for the mulipart content type :
	 * 'multipart/form-data'.
	 */
	public static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";

	/**
	 * max post size configuration's key.
	 */
	public static final String MAX_POST_SIZE = "maxPostSize";

	private static IConfiguration nestConfig = null;

	private static ServletConfig servletConfig = null;

	/**
	 * upload temp dir.
	 */
	private static File tempDir = null;

	/**
	 * upload limit(bytes), default is50MB.
	 */
	private static int maxPostSize = 1024 * 1024 * 50;
	
	private static volatile boolean initFinished = false;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		servletConfig = config;
		String configClass = config.getInitParameter("configClass");
		if (NestUtil.isEmpty(configClass)) {
			configClass = "org.nestframework.config.RuntimeConfiguration";
		} else {
			configClass = NestUtil.trimAll(configClass);
		}
		
		try {
			nestConfig = (IConfiguration)Class.forName(configClass).newInstance();
		} catch (Exception e) {
			throw new ServletException("Failed to init class: " + configClass, e);
		}
		// Load parameters to NestConfig.
		Enumeration<String> parameterNames = config.getInitParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paraName = parameterNames.nextElement();
			nestConfig.getProperties().put(paraName,
					config.getInitParameter(paraName));
		}
		nestConfig.setExternalContext(new ServletExternalContextImpl(config));
		nestConfig.init();
		servletConfig.getServletContext().setAttribute(Constant.CONFIG_KEY,
				nestConfig);

		File dir = (File) config.getServletContext().getAttribute(
				"javax.servlet.context.tempdir");
		if (dir != null) {
			tempDir = dir;
		} else {
			tempDir = new File(System.getProperty("java.io.tmpdir"))
					.getAbsoluteFile();
		}

		String maxPostSizeValue = nestConfig.getProperties().get(MAX_POST_SIZE);
		if (maxPostSizeValue != null && maxPostSizeValue.trim().length() > 0) {
			maxPostSize = Integer.parseInt(maxPostSizeValue.trim());
		}
		
		initFinished = true;
	}

	@SuppressWarnings("unchecked")
	protected void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		if (!initFinished) {
			throw new ServletException("Initilization not finished. Refresh later.");
		}
		
		IConfiguration config = nestConfig;
		ExecuteContext context = new ExecuteContext(req, res);

		// If action is invoked using <jsp:include/>
		String actionPath = (String) req
				.getAttribute("javax.servlet.include.servlet_path");
		// else
		if (actionPath == null) {
			actionPath = req.getServletPath();
		}

		// init context.
		context.setConfig(config)
			.setServletConfig(servletConfig)
			.setPath(actionPath)
			.setParams(req.getParameterMap());

		// handle upload
		boolean exceptionHandled = false;
		if (isMultipart(req)) {
			try {
				config.getMultipartHandler().processMultipart(context, tempDir,
					maxPostSize, req, res);
			} catch (Exception e) {
				exceptionHandled = context.handleException(e);
			}
		}

		try {
			if (!exceptionHandled) {
				ActionProcessHelper.process(context, config);
			}
		} catch (Exception e) {
			throw new ServletException("Action dispatch error.", e);
		}

	}

	public static boolean isMultipart(HttpServletRequest request) {
		return request.getContentType() != null
				&& request.getContentType().startsWith(MULTIPART_CONTENT_TYPE);
	}
}
