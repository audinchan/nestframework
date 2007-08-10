package org.nestframework.action;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.oreilly.servlet.MultipartRequest;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
	/**
	 * Constant identifier for the mulipart content type :
	 * 'multipart/form-data'.
	 */
	public static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";

	/** Pattern used to parse useful info out of the IOException cos throws. */
	private static Pattern EXCEPTION_PATTERN = Pattern
			.compile("Posted content length of (\\d*) exceeds limit of (\\d*)");

	/**
	 * max post size configuration's key.
	 */
	public static final String MAX_POST_SIZE = "maxPostSize";

	private static IConfiguration nestConfig = null;

	private static ServletConfig servletConfig = null;

	/**
	 * 上传临时文件目录.
	 */
	private File tempDir = null;

	/**
	 * 上传大小限制. 默认50MB
	 */
	private int maxPostSize = 1024 * 1024 * 50;

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
	@SuppressWarnings("unchecked")
	public void init(ServletConfig config) throws ServletException {
		servletConfig = config;
		nestConfig = RuntimeConfiguration.getInstance();
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
	}

	@SuppressWarnings("unchecked")
	protected void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		IConfiguration config = nestConfig;
		ExecuteContext context = new ExecuteContext(req, res);

		// If we are invoking using <jsp:include/>
		String actionPath = (String) req
				.getAttribute("javax.servlet.include.servlet_path");
		// else
		if (actionPath == null) {
			actionPath = req.getServletPath();
		}

		// 添加配置信息。
		context.setConfig(config).setServletConfig(servletConfig).setPath(
				actionPath).setParams(req.getParameterMap());

		// 处理文件上传.
		if (isMultipart(req)) {
			processMultipart(context, req, res);
		}

		try {
			ActionProcessHelper.process(context, config);
		} catch (Exception e) {
			throw new ServletException("Action dispatch error.", e);
		}

	}

	@SuppressWarnings("unchecked")
	private void processMultipart(ExecuteContext context,
			HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			MultipartRequest mreq = new MultipartRequest(req, tempDir
					.getAbsolutePath(), maxPostSize, req.getCharacterEncoding());

			// handle parameters
			Map<String, String[]> params = new HashMap<String, String[]>();
			Enumeration<String> parameterNames = mreq.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				params.put(paramName, mreq.getParameterValues(paramName));
			}
			context.setParams(params);

			// handle files
			Map<String, FileItem> fileItems = new HashMap<String, FileItem>();
			Enumeration<String> fileNames = mreq.getFileNames();
			while (fileNames.hasMoreElements()) {
				String fileName = fileNames.nextElement();
				fileItems.put(fileName, new FileItem(mreq.getFile(fileName),
						mreq.getContentType(fileName), mreq
								.getOriginalFileName(fileName)));
			}
			context.setUploadedFiles(fileItems);
		} catch (IOException e) {
			Matcher matcher = EXCEPTION_PATTERN.matcher(e.getMessage());

			if (matcher.matches()) {
				throw new FileUploadLimitExceededException(Long
						.parseLong(matcher.group(2)), Long.parseLong(matcher
						.group(1)));
			} else {
				throw e;
			}
		}
	}

	public static boolean isMultipart(HttpServletRequest request) {
		return request.getContentType() != null
				&& request.getContentType().startsWith(MULTIPART_CONTENT_TYPE);
	}
}
