/**
 * 
 */
package org.nestframework.core;

import java.lang.annotation.Annotation;

import org.nestframework.action.ActionException;

/**
 * @author audin
 *
 */
public interface IParamAdvisor {
	public Object parseParam(Class<?> clazz, Annotation[] annotation, ExecuteContext ctx) throws ActionException;
}
