package org.nestframework.commons.tags.pager;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Next extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        Pager pager = (Pager) findAncestorWithClass(this, Pager.class);
        JspFragment body = getJspBody();
        if(pager.getCurrent() < pager.getTotal()){
            getJspContext().getOut().write(pager.generateLink(body, Pager.LinkType.Next).toString());
        } else {
            body.invoke(null);
        }
    }

}
