package org.nestframework.commons.security.csrf;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This tag is used to output a hidden field to generate CSRF Token.
 * 
 * @author audin
 */
@SuppressWarnings("serial")
public class CsrfTokenTag extends TagSupport {
	public static final String TOKEN_FIELD_NAME = "org.nestframework.commons.security.csrf.CsrfTokenTag";
	
	private boolean xhtml = true;

	public boolean isXhtml() {
		return xhtml;
	}

	public void setXhtml(boolean xhtml) {
		this.xhtml = xhtml;
	}

	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		String token = CsrfUtil.genToken(req);
		StringBuffer sb = new StringBuffer();
		sb.append("<input type='hidden' name='")
			.append(TOKEN_FIELD_NAME)
			.append("' value='")
			.append(token)
			.append("'");
		if (xhtml) {
			sb.append("/");
		}
		sb.append(">");
		try {
			pageContext.getOut().println(sb.toString());
		} catch (IOException e) {
			throw new JspException("Error when output from tag.", e);
		}
		return EVAL_PAGE;
	}

}
