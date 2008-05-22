/**
 * 
 */
package org.nestframework.commons.acegi.becomsso;

import org.acegisecurity.userdetails.UserDetails;

/**
 * @author audin
 *
 */
public interface IBecomSSOUserCreateService {
	public UserDetails createAndGet(String loginName);
}
