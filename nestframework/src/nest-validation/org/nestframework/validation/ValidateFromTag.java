package org.nestframework.validation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This tag is used to output a hidden field to tell the
 * validation framework which view it is comming from.
 * 
 * @author audin
 */
@SuppressWarnings("serial")
public class ValidateFromTag extends TagSupport {
	private String from;
	private boolean xhtml = true;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean isXhtml() {
		return xhtml;
	}

	public void setXhtml(boolean xhtml) {
		this.xhtml = xhtml;
	}

	@Override
	public int doEndTag() throws JspException {
		if (from == null || from.trim().length() == 0) {
			HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
			from = req.getServletPath();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<input type='hidden' name='")
			.append(ValidationActionHandler.VALIDATE_FROM_KEY)
			.append("' value='")
			.append(from)
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
