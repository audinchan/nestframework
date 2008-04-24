package org.nestframework.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Validate {
	
	/**
	 * Which validation rules will be applied.
	 * @return
	 */
	String[] type() default {};
	
	/**
	 * The field's name. Default is the Field name of java bean.
	 * @return
	 */
	String fieldName() default "";
	
	/**
	 * Indicate whether we should do server side validation.
	 * @return
	 */
	boolean server() default true;
	
	/**
	 * Indicate whether we should do client side validation.
	 * @return
	 */
	boolean client() default true;
	
	/**
	 * Indicate which action methods should be applied this validation rule.
	 * If not specified, this validation rule is applied to all action methods.
	 * @return
	 */
	String[] on() default {};
	
	/**
	 * Indicate which action methods should not be applied this validation rule.
	 * @return
	 */
	String[] except() default {};
	
	/**
	 * When labelFromResource is false, then 'label' is used as the Field dispaly name.
	 * Else,
	 * 'label' is used as a key to fetch message from the resource file.
	 * If not defined, the key is used as 'actionBean's name.Field's name.label'.
	 * @return
	 */
	String label() default "";
	
	/**
	 * Indicate whether to get message from resource file.
	 * @return
	 */
	boolean labelFromResource() default true;
	
	/**
	 * When validate failed, 'msg' is used to display some message to users.
	 * If not defined, then use validator's msg.
	 * @return
	 */
	String msg() default "";
	
	/**
	 * Indicate whether to use msg to display directly or use it as key to fetch
	 * message from resource file. Default is to fetch from resource file.
	 * @return
	 */
	boolean msgFromResource() default true;
	
	/**
	 * Var is used to implement complex validation.
	 * @return
	 */
	Param[] params() default {};
}
