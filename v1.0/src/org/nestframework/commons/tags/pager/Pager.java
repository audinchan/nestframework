package org.nestframework.commons.tags.pager;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Pager extends SimpleTagSupport {
    private int total;
    private String param;
    private int current;
    private String action;
    private String styleClass;
    private boolean ajax = false;

    public enum LinkType {
        Previous, First, Next, Last
    }

    

    protected StringBuffer generateLink(JspFragment body, LinkType linkType) throws IOException, JspException {
        StringWriter sw = new StringWriter();
        body.invoke(sw);
        int toPage = 0;
        switch (linkType) {
            case Previous:
                if (current > 1) {
                    toPage = current - 1;
                } else {
                    return sw.getBuffer();
                }
                break;
            case First:
                toPage = 1;
                break;
            case Next:
                if (current < total) {
                    toPage = current + 1;
                } else {
                    return sw.getBuffer();
                }
                break;
            case Last:
                toPage = total;
                break;
        }
        return generateLink(toPage, sw.getBuffer());
    }

    protected <T> StringBuffer generateLink(int pageNo, T linkText) throws IOException, JspException {
        StringBuffer result = new StringBuffer("<a href=\"");

        // 设置href
        if (ajax) {
            result.append("javascript:void(0)\"").append(" onclick=\"");
            result.append(action.replaceAll("PLACEHOLDER", param+"="+pageNo));
            result.append(";return false;");
        } else {
            result.append(action);
            char connector = '?';
            if (action.indexOf('?') != -1) {
                connector = '&';
            }
            result.append(connector).append(param).append("=").append(pageNo);
        }
        result.append("\"");
        // 设置styleClass
        if (styleClass != null) {
            result.append(" class=\"").append(styleClass).append("\"");
        }
        result.append(">").append(linkText).append("</a>");
        return result;
    }

    protected StringBuffer generateLink(int pageNo) throws IOException, JspException {
        return generateLink(pageNo, pageNo);
    }

    public void doTag() throws JspException, IOException {
        getJspBody().invoke(null);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }
}
