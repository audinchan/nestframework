package org.nestframework.validation;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.nestframework.core.Constant;
import org.nestframework.localization.ActionMessage;
import org.nestframework.localization.ActionMessages;
import org.nestframework.localization.LocalizationUtil;
import org.nestframework.utils.NestUtil;

@SuppressWarnings("serial")
public class ErrorDisplayTag extends TagSupport implements Constant {
	/**
	 * Which error to display.
	 */
	private String of;
	private String style;
	private String styleClass;
	private boolean xhtml = true;

	public String getOf() {
		return of;
	}

	public void setOf(String of) {
		this.of = of;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public boolean isXhtml() {
		return xhtml;
	}

	public void setXhtml(boolean xhtml) {
		this.xhtml = xhtml;
	}

	@Override
	public int doEndTag() throws JspException {
		ActionMessages msgs = (ActionMessages) pageContext.getRequest().getAttribute(ERRORS_KEY);
		if (msgs == null || msgs.size() == 0) {
			return EVAL_PAGE;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span");
		if (!NestUtil.isEmpty(style)) {
			sb.append(" style=\"").append(style).append("\"");
		}
		if (!NestUtil.isEmpty(styleClass)) {
			sb.append(" class=\"").append(styleClass).append("\"");
		}
		sb.append(">");
		
		if (!NestUtil.isEmpty(of)) {
			displayErrors(msgs.get(of), sb);
		} else {
			displayErrors(msgs.get(), sb);
		}
		
		sb.append("</span>");
		
		try {
			pageContext.getOut().println(sb.toString());
		} catch (IOException e) {
			throw new JspException("Error when output errors tag.", e);
		}
		
		return EVAL_PAGE;
	}
	
	private void displayErrors(Iterator<ActionMessage> msgs, StringBuffer sb) {
		Locale locale = (Locale) ((HttpServletRequest) pageContext.getRequest())
				.getSession().getAttribute(LOCALE_KEY);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		while (msgs.hasNext()) {
			ActionMessage msg = msgs.next();
			String message = msg.getMsg();
			if (msg.isResource()) {
				if (msg.getValues() != null && msg.getValues().length > 0) {
					message = LocalizationUtil.getMessage(locale, message, msg
						.getValues());
				} else {
					message = LocalizationUtil.getMessage(locale, message);
				}
			}
			sb.append(message);
			if (msgs.hasNext()) {
				sb.append(xhtml ? "<br/>" : "<br>");
			}
		}
	}
}
