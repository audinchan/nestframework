package org.nestframework.validation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.Var;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ActionResolver;
import org.nestframework.core.Constant;
import org.nestframework.localization.LocalizationUtil;
import org.nestframework.utils.NestUtil;

@SuppressWarnings("serial")
public class JavascriptValidatorTag extends BodyTagSupport implements Constant {
	
	protected static final String HTML_BEGIN_COMMENT = "\n<!-- Begin \n";
	protected static final String HTML_END_COMMENT = "//End --> \n";
	protected static String lineEnd = System.getProperty("line.separator");
	
	/**
	 * Wether to generate global validation functions.
	 */
	protected boolean global = false;
	
	/**
	 * If need to generate dynamic functions, for which action.
	 */
	protected String action = "";
	
	/**
	 * form name.
	 */
	protected String form = "";
	
	/**
	 * For which methods to validate.
	 */
	protected String on = "";
	
	
	@Override
	public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(renderJavascript());

        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_BODY_AGAIN;
	}


	@Override
	public void release() {
		global = false;
		action = "";
		on = "";
	}
	
	protected String renderJavascript() throws JspException {
		StringBuffer results = new StringBuffer();
		ValidatorResources validatorResources = CommonsValidator.getValidatorResources();
		
		if (validatorResources == null) {
			throw new JspException("No validator resources found.");
		}
		
		Locale locale = (Locale) pageContext.getSession().getAttribute(LOCALE_KEY);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		
		// begin javascript tag
		if (!NestUtil.isEmpty(action)) {
			results.append("<script type=\"text/javascript\">").append(lineEnd);
			
			// There are some methods to be validated.
			if (!NestUtil.isEmpty(on)) {
				try {
					results.append(getDynamicJavascript(validatorResources, locale));
				} catch (Exception e) {
					throw new JspException("Failed to generate dynamic javascript.", e);
				}
			}
		}
		
		if (global) {
			results.append(getGlobalJavascript(validatorResources));
		}
		
		// end javascript tag
		if (!NestUtil.isEmpty(action)) {
			results.append(lineEnd).append("</script>").append(lineEnd);
		}
		
		return results.toString();
	}
	
	private String getDynamicJavascript(ValidatorResources resources, Locale locale) throws Exception {
		StringBuffer sb = new StringBuffer(lineEnd);
		
		IConfiguration config = (IConfiguration) pageContext.getServletContext().getAttribute(CONFIG_KEY);
		
		String className = config.getPackageBase() + action.replaceAll("\\/", ".");
		Object actionBean = ActionResolver.resolveAction(className);
		
		if (actionBean == null) {
			throw new Exception("No such action: " + action);
		}
		
		String packageBase = config.getPackageBase();
		int baseLen = packageBase.length();
		String labelBase = actionBean.getClass().getName().substring(baseLen + 1);
		
		sb.append("var ").append(form).append("_action=\"\";").append(lineEnd);
		
		sb.append("function validate_").append(form).append("(form) {").append(lineEnd)
			.append("\tvar result=1;").append(lineEnd);
		
		Collection<Field> fields = NestUtil.getFields(actionBean.getClass());
		Set<ValidatorAction> actions = new HashSet<ValidatorAction>();
		String[] methods = on.split("\\s*,\\s*");
		for (String method: methods) {
			sb.append("\tif (").append(form).append("_action==\"").append(method).append("\") {").append(lineEnd);
			
			actions.clear();
			for (Field f: fields) {
				List<Validate> vas = new ArrayList<Validate>();
				Validate validation = f.getAnnotation(Validate.class);
				if (validation != null && validation.client()) {
					vas.add(validation);
				}
				Validations validations = f.getAnnotation(Validations.class);
				if (validations != null) {
					for (Validate v : validations.value()) {
                        if (v.client()) {
                            vas.add(v);
                        }
					}
				}
				for (Validate va : vas) {
					if (!isInStrings(va.except(), method)
							&& (va.on().length == 0 || isInStrings(va.on(), method))) {
						for (String depend: va.type()) {
							ValidatorAction validatorAction = resources.getValidatorAction(depend);
							actions.add(validatorAction);
						}
					}
				}
			}
			
			StringBuffer validateMethods = new StringBuffer();
			
			for (ValidatorAction va: actions) {
				
				int jscriptVar = 0;
				String functionName = null;

	            if (va.getJsFunctionName() != null
	                && va.getJsFunctionName().length() > 0) {
	                functionName = va.getJsFunctionName();
	            } else {
	                functionName = va.getName();
	            }
	            String[] funcs = functionName.split("\\s*,\\s*");
				String jsFunctionName = va.getJsFunctionName();
				String callbackName = va.getName();
				if (funcs.length == 2) {
					jsFunctionName = funcs[0];
					callbackName = funcs[1];
				}
	            
	            sb.append("\t\t").append(form).append("_").append(callbackName).append("=function(){").append(lineEnd);
	            
	            for (Field f: fields) {
	            	List<Validate> vas = new ArrayList<Validate>();
					Validate validation = f.getAnnotation(Validate.class);
					if (validation != null) {
						vas.add(validation);
					}
					Validations validations = f.getAnnotation(Validations.class);
					if (validations != null) {
						for (Validate v : validations.value()) {
							vas.add(v);
						}
					}
					for (Validate v : vas) {
						if (!isInStrings(v.except(), method)
								&& (v.on().length == 0 || isInStrings(v.on(), method))) {
							String fieldName = v.fieldName();
							if (NestUtil.isEmpty(fieldName)) {
								fieldName = f.getName();
							}
							String label = v.label();
							if (NestUtil.isEmpty(label) && v.labelFromResource()) {
								label = labelBase + "." + fieldName + ".label";
							}
							if (v.labelFromResource()) {
								try {
									label = LocalizationUtil.getMessage(locale, label);
								} catch (MissingResourceException e) {

								}
							}
							String msg = v.msg();
							
							boolean msgFormResource = v.msgFromResource();
							Param[] params = v.params();
							Object[] msgParams = new String[params.length + 1];
							msgParams[0] = label;
							// msgParams[1] = value;
							for (int i = 0; i < params.length; i++) {
								msgParams[i + 1] = params[i].value();
							}
							if (msgFormResource) {
								if (NestUtil.isEmpty(msg)) {
									msg = va.getMsg();
								}
								msg = LocalizationUtil.getMessage(locale, msg, msgParams);
							}
							for (String depend: v.type()) {
								if (depend.equals(va.getName())) {
									
									if (NestUtil.isEmpty(va.getJsFunctionName())) {
										continue;
									}
									
									if (validateMethods.length() > 0) {
										validateMethods.append(" && ");
									}
									validateMethods.append(jsFunctionName).append("(form)");
									
									sb.append("\t\t\tthis.a").append(jscriptVar++).append("=new Array(\"");
									if (NestUtil.isEmpty(v.fieldName())) {
										sb.append(f.getName());
									} else {
										sb.append(v.fieldName());
									}
									sb.append("\",\"").append(escapeQuotes(msg)).append("\", ");
									sb.append("new Function (\"varName\", \"");
									
									for (Param p: params) {
										String varName = p.name();
										String varValue = p.value();
										String jsType = p.jsType();
										
										String varValueEscaped = escapeJavascript(varValue);
										if (NestUtil.isEmpty(jsType)) {
											Class cl = f.getClass();
											if (cl == boolean.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == byte.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == char.class) {
												jsType = Var.JSTYPE_STRING;
											}
											else if (cl == double.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == float.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == int.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == long.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == short.class) {
												jsType = Var.JSTYPE_INT;
											}
											else if (cl == String.class) {
												jsType = Var.JSTYPE_STRING;
											}
											else {
												jsType = Var.JSTYPE_STRING;
											}
										}
										sb.append("this.").append(varName);
										if (Var.JSTYPE_INT.equalsIgnoreCase(jsType)) {
											sb.append("=").append(varValueEscaped).append(";");
										} else if (Var.JSTYPE_REGEXP.equalsIgnoreCase(jsType) || "mask".equalsIgnoreCase(varName)) {
											sb.append("=/").append(varValueEscaped).append("/;");
										} else {
											sb.append("='").append(varValueEscaped).append("';");
										}
										
									} // end for params
									sb.append(" return this[varName];\"));").append(lineEnd);
								}
							}
						}
					}
	            }
	            
	            sb.append("\t\t}").append(lineEnd);
			}
			
			sb.append("\t\tresult = ").append(validateMethods.toString()).append(";").append(lineEnd);
			sb.append("\t}").append(lineEnd);
		}
		
		sb.append("\treturn (result == 1);").append(lineEnd).append("}").append(lineEnd);
		
		sb.append(lineEnd).append("document.body.onclick = function(e) {\r\n" + 
				"	var elem = e ? e.target : window.event.srcElement;\r\n" + 
				"	if (elem.type==\"submit\" || elem.type==\"image\") {\r\n" + 
				"		var a = elem.form.getAttributeNode(\"name\").value + \"_action=\'\" + elem.name + \"\';\";\r\n" + 
				"		eval(a);\r\n" + 
				"	}\r\n" + 
				"}");
		
		return sb.toString();
	}
		
	private static boolean isInStrings(String[] strings, String string) {
		for (String s : strings) {
			if (s.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
	private String escapeQuotes(String in)
    {
        if (in == null || in.indexOf("\"") == -1)
        {
            return in;
        }
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(in, "\"", true);

        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();
            if (token.equals("\""))
            {
                buffer.append("\\");
            }
            buffer.append(token);
        }

        return buffer.toString();
    }
	
	private String escapeJavascript(String str)
    {
        if (str == null)
        {
            return null;
        }
        int length = str.length();
        if (length == 0)
        {
            return str;
        }

        // guess at how many chars we'll be adding...
        StringBuffer out = new StringBuffer(length + 4);
        // run through the string escaping sensitive chars
        for (int i=0; i < length; i++)
        {
            char c = str.charAt(i);
            if (c == '"'  ||
                c == '\'' ||
                c == '\\' || 
                c == '\n' || 
                c == '\r')
            {
                out.append('\\');
            }
            out.append(c);
        }
        return out.toString();
    }
	
    protected String getGlobalJavascript(ValidatorResources resources) {
        StringBuffer sb = new StringBuffer(lineEnd);

        Iterator actions = resources.getValidatorActions().values().iterator();
        while (actions.hasNext()) {
            ValidatorAction va = (ValidatorAction) actions.next();
            if (va != null) {
                String javascript = va.getJavascript();
                if (javascript != null && javascript.length() > 0) {
                    sb.append(javascript).append(lineEnd);
                }
            }
        }

        return sb.toString();
    }
			

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public String getOn() {
		return on;
	}


	public void setOn(String on) {
		this.on = on;
	}
}
