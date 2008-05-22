/**
 * 
 */
package org.nestframework.commons.fckeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author audin
 *
 */
@SuppressWarnings({"unchecked", "serial"})
public class ResourceFetchServlet extends HttpServlet {
	private String encoding = "utf-8";
	

	@Override
	public void init() throws ServletException {
		String enc = getInitParameter("encoding");
		if (enc != null && enc.trim().length() > 0) {
			encoding = enc.trim();
		}
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String relativePath = req.getRequestURI().replace(req.getSession().getServletContext().getContextPath(), "");
		try {
			relativePath = URLDecoder.decode(relativePath, encoding);
		} catch (UnsupportedEncodingException e) {}
		String realPath = req.getSession().getServletContext().getRealPath(relativePath);
		
		File file = new File(realPath);
		if (file.exists()) {
			ServletOutputStream os = resp.getOutputStream();
			FileInputStream is = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int read;
			while ((read = is.read(buf)) != -1) {
				os.write(buf, 0, read);
			}
			os.flush();
			os.close();
			is.close();
		}
	}

}
