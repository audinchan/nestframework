package com.becom.demo.webapp.form;

import java.util.Set;

import org.apache.struts.action.ActionForm;

public class UserForm extends ActionForm {
	private Integer userid;
	private String username;
	private String password;
	private Set contacts;
	/**
	 * @return Returns the contacts.
	 */
	public Set getContacts() {
		return contacts;
	}
	/**
	 * @param contacts The contacts to set.
	 */
	public void setContacts(Set contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the userid.
	 */
	public Integer getUserid() {
		return userid;
	}
	/**
	 * @param userid The userid to set.
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
