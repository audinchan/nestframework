package com.becom.demo.webapp.action;
// Generated ${date} by Hibernate Tools ${version} with mintgen

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.becom.demo.model.User;
import com.becom.demo.service.IUserManager;
import com.becom.demo.util.StringUtil;
import com.becom.demo.webapp.form.UserForm;

public class UserAction extends DispatchAction {
	private IUserManager userManager;

	/**
	 * @param userManager The userManager to set.
	 */
	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		UserForm f = (UserForm) form;
		if (null != f.getUserid()) {
			User user = userManager.getUser(f.getUserid());
			BeanUtils.copyProperties(f, user);
		}
		return mapping.findForward("edit");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		UserForm f = (UserForm) form;
		User user = new User();
		BeanUtils.copyProperties(user, f);
		if (f.getUserid() != null && f.getUserid().intValue() == 0) {
			user.setUserid(null);
		}
		userManager.saveUser(user);
		BeanUtils.copyProperties(f, user);
		return mapping.findForward("edit");
	}

}
