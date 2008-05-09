package org.nestframework.action.defaults;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.action.DownloadItem;
import org.nestframework.action.IActionHandler;
import org.nestframework.action.Redirect;
import org.nestframework.annotation.Intercept;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;

/**
 * Default forward handler.
 * <p>
 * If forward start with http:// or ftp:// etc. then is a redirection.
 * If forward start with "!" then is a redirection.
 * </p>
 * 
 * @author audin
 */
@Intercept({Stage.HANDLE_VIEW})
public class DefaultActionForwarder implements IActionHandler {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory
			.getLog(DefaultActionForwarder.class);

	public boolean process(ExecuteContext context) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - start");
		}

		Object rs = context.getForward();
		String forward = null;
		boolean redirect = false;
		boolean isLocal = true;
		boolean sizeSet = false;
		if (rs instanceof String) {
			forward = (String) context.getForward();
		} else if (rs instanceof Redirect) {
			Redirect r = (Redirect) context.getForward();
			forward = r.getTarget();
			redirect = true;
			isLocal = r.isLocal();
		} 
		// handle download
		else if (rs instanceof DownloadItem) {
			DownloadItem dt = (DownloadItem) rs;
			if (dt.getFile() != null) {
				rs = dt.getFile();
			} else if (dt.getInputStream() != null) {
				rs = dt.getInputStream();
			} else if (dt.getData() != null) {
				rs = dt.getData();
			}
			if (dt.getSize() != -1) {
				context.getResponse().setContentLength((int)dt.getSize());
				sizeSet = true;
			}
			if (dt.getContentType() != null) {
				context.getResponse().setContentType("application/octet-stream");
			}
		}
		// handle download
		if (rs instanceof InputStream
				|| rs instanceof byte[]
				|| rs instanceof File) {
			String ct = context.getResponse().getContentType();
			if (ct == null) {
				context.getResponse().setContentType("application/octet-stream");
			}
			
			ServletOutputStream os = context.getResponse().getOutputStream();

			if (rs instanceof InputStream
					|| rs instanceof File) {
				InputStream is = null;
				if (rs instanceof InputStream) {
					is = (InputStream) rs;
				} else {
					File file = (File) rs;
					if (!sizeSet) {
						context.getResponse().setContentLength((int) file.length());
						sizeSet = true;
					}
					is = new FileInputStream(file);
				}
				if (is != null) {
					byte[] buf = new byte[1024];
					int readLen = 0;
					while ((readLen = is.read(buf)) != -1) {
						os.write(buf, 0, readLen);
					}
					is.close();
				}
			} else if (rs instanceof byte[]) {
				byte[] data = (byte[]) context.getForward();
				if (!sizeSet) {
					context.getResponse().setContentLength(data.length);
					sizeSet = true;
				}
				os.write(data);
			}
			
			os.flush();
			os.close();

			return true;
		}
		
		if (forward == null) {
			if (log.isDebugEnabled()) {
				log.debug("process(ExecuteContext) - end");
			}
			return false;
		} else if (!redirect && Pattern.matches("^[a-z]+:\\/\\/.*", forward.toLowerCase())) {
			redirect = true;
			isLocal = false;
		} else if (forward.startsWith("!")) {
			redirect = true;
			isLocal = true;
			forward = forward.substring(1);
		}
		
		if (redirect) {
			if (isLocal && forward.startsWith("/")) {
				forward = context.getRequest().getContextPath() + forward;
			}
			context.getResponse().sendRedirect(forward);
		} else {
			context.getRequest().getRequestDispatcher(forward).forward(context.getRequest(), context.getResponse());
		}

		if (log.isDebugEnabled()) {
			log.debug("process(ExecuteContext) - end");
		}
		return false;
	}

}
