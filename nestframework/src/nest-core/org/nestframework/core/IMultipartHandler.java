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
 * @author audin
 *
 */
public interface IMultipartHandler {
	public void processMultipart(ExecuteContext context,
			File tmpDir, int maxPostSize,
			HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
}
