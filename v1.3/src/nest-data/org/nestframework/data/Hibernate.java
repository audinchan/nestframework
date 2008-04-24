package org.nestframework.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Whether to convert hibernate collection(handle lazy load).
 * Default is true.
 * 
 * @author audin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Hibernate {
	boolean value() default true;
}
