/**
 * 
 */
package org.nestframework.core;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Multipart Handler.
 * 
 * @author audin
 *
 */
public interface IMultipartHandler {
	
	/**
	 * Handle multipart content.
	 * 
	 * @param context Execute context.
	 * @param tmpDir temp directry.
	 * @param maxPostSize Max post size allowed.
	 * @param req request.
	 * @param res response象.
	 * @throws ServletException Servlet exception.
	 * @throws IOException IO exception.
	 */
	public void processMultipart(ExecuteContext context,
			File tmpDir, int maxPostSize,
			HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
}
