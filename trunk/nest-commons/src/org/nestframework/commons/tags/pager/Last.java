package org.nestframework.commons.tags.pager;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Last extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        Pager pager = (Pager) findAncestorWithClass(this, Pager.class);
        JspFragment body = getJspBody();
        getJspContext().getOut().write(pager.generateLink(body, Pager.LinkType.Last).toString());
    }

}
