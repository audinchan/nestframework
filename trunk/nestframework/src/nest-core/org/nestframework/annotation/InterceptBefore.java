package org.nestframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nestframework.core.Stage;

/**
 * Indicate which stage(s) will the ActionHandler intercept.
 * 
 * @author audin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InterceptBefore {
	Stage[] value();
}
