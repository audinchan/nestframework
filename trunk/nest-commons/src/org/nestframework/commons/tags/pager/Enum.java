package org.nestframework.commons.tags.pager;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Enum extends SimpleTagSupport {
    public void doTag() throws JspException, IOException {
        Pager pager = (Pager) findAncestorWithClass(this, Pager.class);
        for(int i=1; i<=pager.getTotal(); i++){
            getJspContext().getOut().write(pager.generateLink(i).append(" ").toString());            
        }
    }

}
