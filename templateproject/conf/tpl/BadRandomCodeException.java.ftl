/**
 * 
 */
package ${hss_base_package}.exception;

import org.acegisecurity.AuthenticationException;

/**
 * —È÷§¬Î“Ï≥£.
 *
 */
public class BadRandomCodeException extends AuthenticationException
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1249626825918195824L;

    /**
     * BadRandomCodeException
     * @param msg
     * @param t
     */
    public BadRandomCodeException(String msg, Throwable t)
    {
        super(msg, t);
    }

    /**
     * BadRandomCodeException
     * @param msg
     */
    public BadRandomCodeException(String msg)
    {
        super(msg);
    }
    
}
