/**
 * 
 */
package org.nestframework.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author audin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoType {
	String paramName() default "dataType";
	String[] contentTypes() default {"xml:text/xml; charset=UTF-8", "json:text/plain; charset=UTF-8"};
}
