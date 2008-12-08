/**
 * 
 */
package org.nestframework.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * With this annotation, the output with will be translate to JSON or XML format.
 * The output format is detected by request parameter 'dataType' which name can be
 * changed by setting the parameter paramName of AutoType. 
 * The default Content-Type for JSON is "application/json; charset=UTF-8", for XML
 * is "xml:text/xml; charset=UTF-8". You can
 * change it by setting the parameter contentTypes of AutoType annotation.
 * 
 * @author audin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoType {
	String paramName() default "dataType";
	String[] contentTypes() default {"xml:text/xml; charset=UTF-8", "json:application/json; charset=UTF-8"};
}
