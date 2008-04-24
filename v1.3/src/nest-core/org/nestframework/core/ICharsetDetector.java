package org.nestframework.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for request charset detecting.
 * 
 * @author audin
 *
 */
public interface ICharsetDetector {
	
	/**
	 * Detect charset from request.
	 * 
	 * @param req Http servlet request.
	 * @return
	 */
	public String detectCharsetFromRequest(HttpServletRequest req);
	
	/**
	 * Detec charset from content.
	 * 
	 * @param content Content.
	 * @return
	 */
	public String detectCharsetFromContent(String content);
}
