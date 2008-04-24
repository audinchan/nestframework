/**
 * 
 */
package org.nestframework.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define the XML(or JSON) root element's name.
 * 
 * @author audin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RootElement {
	String value();
}
