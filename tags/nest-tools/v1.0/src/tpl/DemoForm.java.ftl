package ${hss_base_package}.webapp.form;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

import org.apache.struts.action.ActionForm;

/**
 * @author austin
 *
 */
public class DemoForm extends ActionForm {

	/**
	 * svuid.
	 */
	private static final long serialVersionUID = -3159775038085099816L;
	
	/**
	 * your name.
	 */
	private String name;
	
	/**
	 * msg to show.
	 */
	private String msg;

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the msg.
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg The msg to set.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
