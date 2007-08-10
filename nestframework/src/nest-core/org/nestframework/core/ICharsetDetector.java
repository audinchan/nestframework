package org.nestframework.core;

import javax.servlet.http.HttpServletRequest;

public interface ICharsetDetector {
	public String detectCharsetFromRequest(HttpServletRequest req);
	public String detectCharsetFromContent(String content);
}
