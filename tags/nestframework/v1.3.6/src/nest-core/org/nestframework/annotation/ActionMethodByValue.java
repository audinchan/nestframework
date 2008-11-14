package org.nestframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate the method be executed is got from a parameter's value.
 * The default parameter is 'actionMethod'.
 * 
 * @author audin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ActionMethodByValue {
	String value() default "actionMethod";
}
