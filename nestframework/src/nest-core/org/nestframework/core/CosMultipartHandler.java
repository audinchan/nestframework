/**
 * 
 */
package org.nestframework.core;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nestframework.action.FileItem;
import org.nestframework.action.FileUploadLimitExceededException;

import com.oreilly.servlet.MultipartRequest;

/**
 * @author audin
 *
 */
@SuppressWarnings("unchecked")
public class CosMultipartHandler implements IMultipartHandler {
	
	/** Pattern used to parse useful info out of the IOException cos throws. */
	private static Pattern EXCEPTION_PATTERN = Pattern
			.compile("Posted content length of (\\d*) exceeds limit of (\\d*)");
	
	/* (non-Javadoc)
	 * @see org.nestframework.core.IMultipartHandler#processMultipart(org.nestframework.core.ExecuteContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processMultipart(ExecuteContext context,
			File tempDir, int maxPostSize,
			HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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

}
