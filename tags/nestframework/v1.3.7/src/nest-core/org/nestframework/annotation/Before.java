package org.nestframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nestframework.core.Stage;

/**
 * Indicate before which stage(s) will the method be executed.
 * Default stage is EXECUTE_ACTION.
 * 
 * @author audin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Before {
	Stage[] value() default Stage.EXECUTE_ACTION;
}
