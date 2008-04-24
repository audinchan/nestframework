/**
 * 
 */
package org.nestframework.core;

/**
 * Action method result type convertor.
 * 
 * @author audin
 *
 */
public interface ITypeConvert {
	/**
	 * process type converting.
	 * 
	 * @param context Execute context.
	 * @return If success return <code>true</code>, else return <code>false</code>
	 * @throws Exception Exception.
	 */
	public boolean process(ExecuteContext context) throws Exception;
}
