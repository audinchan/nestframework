/**
 * 
 */
package org.nestframework.commons.becomsso.acegi;

import org.acegisecurity.userdetails.UserDetails;

/**
 * @author audin
 *
 */
public interface IBecomSSOUserCreateService {
	public UserDetails createAndGet(String loginName);
}
