package ${hss_service_package}.impl;
// Generated ${date} by Hibernate Tools ${version} with nest-tools

import ${hss_service_package}.IDemoManager;

/**
 * @author austin
 *
 */<#if hss_jdk5>
 @SuppressWarnings("unchecked")</#if>
public class DemoManager extends RootManager implements IDemoManager {

	/* (non-Javadoc)
	 * @see ${hss_service_package}.IDemoManager#getMsg()
	 */
	public String getMsg() {
		// TODO Auto-generated method stub
		return "This is spring manager bean.";
	}

}
