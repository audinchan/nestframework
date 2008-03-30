/**
 * 
 */
package org.nestframework.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * With this annotation, the output with will be translate to JSON format.
 * The default Content-Type is "application/json; charset=UTF-8". You can
 * change it by setting the parameter of JSON annotation.
 * 
 * @author audin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Json {
	String value() default "application/json; charset=UTF-8";
}
