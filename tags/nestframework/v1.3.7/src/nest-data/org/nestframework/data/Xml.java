/**
 * 
 */
package org.nestframework.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * With this annotation, the output with will be translate to XML format.
 * The default Content-Type is "text/xml; charset=UTF-8". You can
 * change it by setting the parameter of Xml annotation.
 * 
 * @author audin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Xml {
	String value() default "text/xml; charset=UTF-8";
}
