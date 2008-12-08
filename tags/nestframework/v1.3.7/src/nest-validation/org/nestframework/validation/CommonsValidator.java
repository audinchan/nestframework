package org.nestframework.validation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.IExternalContext;
import org.nestframework.utils.NestUtil;
import org.xml.sax.SAXException;

public class CommonsValidator {
	public static final String CONFIG_KEY = "validateResources";

	private static ValidatorResources resources = null;

	protected static Map<String, Class<?>> standardTypes;
	static {
		standardTypes = new HashMap<String, Class<?>>();
		standardTypes.put("boolean", boolean.class);
		standardTypes.put("byte", byte.class);
		standardTypes.put("char", char.class);
		standardTypes.put("double", double.class);
		standardTypes.put("float", float.class);
		standardTypes.put("int", int.class);
		standardTypes.put("long", long.class);
		standardTypes.put("short", short.class);
		standardTypes.put("java.lang.String", String.class);
	}
	
	protected static Map<String, Class<?>> validatorClasses = new HashMap<String, Class<?>>();

	// cache validator methods
	protected static Map<String, Method> validatorMethods = new HashMap<String, Method>();

	// cache validation field names for specified method.
	// method name is full stlyle. eg. com.demo.action.User.doSave.
	// filed's name is short style. eg. user.name, selectedId.
	protected static Map<String, List<String>> fieldsCache = new HashMap<String, List<String>>();

	public synchronized static ValidatorResources getValidatorResources(IConfiguration conf) {
		if (resources != null) {
			return resources;
		}
//		IConfiguration conf = NestContext.getConfig();
		IExternalContext ec = conf.getExternalContext();
		String validateResources = NestUtil.trimAll(conf.getProperties()
				.get("validateResources"));
		if (NestUtil.isEmpty(validateResources)) {
			validateResources = "/WEB-INF/validator-rules.xml";
		}
		String[] vrArray = validateResources.split(",");
		URL[] vrUrls = new URL[vrArray.length];
		for (int i = 0; i < vrArray.length; i++) {
			vrUrls[i] = ec.getResource(vrArray[i].trim());
		}
		try {
			resources = new ValidatorResources(vrUrls);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load validator resources: "
					+ validateResources, e);
		} catch (SAXException e) {
			throw new RuntimeException("Unable to load validator resources: "
					+ validateResources, e);
		}
		return resources;
	}

	public boolean validate(ExecuteContext context)
			throws ValidateFailedException {
		//Object bean = context.getActionBean();
		Validate beanValidation = context.getActionClass().getAnnotation(Validate.class);
		if (beanValidation == null || !beanValidation.server()) {
			return true;
		}
		if (context.getLocale() == null) {
			context.setLocale(Locale.getDefault());
		}

		String packageBase = context.getConfig().getPackageBase();
		int baseLen = packageBase.length();
		String labelBase = context.getActionClass().getName().substring(baseLen + 1);

		String fullMethodName = context.getActionClass().getName() + "."
				+ context.getAction().getName();
		List<String> cachedFields = fieldsCache.get(fullMethodName);
		Collection<Field> fields = NestUtil.getFields(context.getActionClass());
		if (cachedFields == null) {
			cachedFields = new ArrayList<String>();
			fieldsCache.put(fullMethodName, cachedFields);
			for (Field f : fields) {
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
				for (Validate va : vas) {
					if (!isInStrings(va.except(), context.getAction().getName())
							&& (va.on().length == 0 || isInStrings(va.on(),
									context.getAction().getName()))) {
						cachedFields.add(f.getName());
					}
				}
			}
		}

		// 

		for (Field f : fields) {
			if (!cachedFields.contains(f.getName())) {
				continue;
			}

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
			for (Validate va : vas) {
				String fieldName = va.fieldName();
				if (NestUtil.isEmpty(fieldName)) {
					fieldName = f.getName();
				}
				String[] values = context.getParams().get(fieldName);
				if (values == null) {
					values = new String[1];
					values[0] = null;
				}
				String label = va.label();
				if (NestUtil.isEmpty(label) && va.labelFromResource()) {
					label = labelBase + "." + fieldName + ".label";
				}
				if (va.labelFromResource()) {
					try {
						label = context.getMessage(label);
					} catch (MissingResourceException e) {

					}
				}
				String msg = va.msg();
				boolean msgFormResource = va.msgFromResource();
				Param[] params = va.params();
				for (String value : values) {
					for (String validateType : va.type()) {
						validate(context, fullMethodName, fieldName, label,
								msg, msgFormResource, params, value,
								validateType);
					}
				}
			}
		}
		return false;
	}

	private void validate(ExecuteContext context, String fullMethodName,
			String fieldName, String label, String msg,
			boolean msgFormResource, Param[] params, String value,
			String validateType) throws ValidateFailedException {
		try {
			ValidatorAction validatorAction = getValidatorResources(context.getConfig())
					.getValidatorAction(validateType);
			String dependsString = validatorAction.getDepends();
			if (!NestUtil.isEmpty(dependsString)) {
				String[] depends = dependsString.split(",");
				for (String depend : depends) {
					validate(context, fullMethodName, fieldName, label, msg,
							msgFormResource, params, value, depend.trim());
				}
			}
			Class<?> validatorClass = loadValidatorClass(validatorAction);
			Class<?>[] paramClasses = loadMethodParamClasses(validatorAction);
			Object[] paramValues = loadMethodParamValues(validatorAction,
					paramClasses, value, params);
			Method validatorMethod = loadValidatorMethod(validatorAction,
					validatorClass, paramClasses);
			Object validator = null;
			Boolean r = Boolean.TRUE;
			if (Modifier.isStatic(validatorMethod.getModifiers())) {
				r = (Boolean) validatorMethod.invoke(validatorClass,
						paramValues);
			} else {
				validator = validatorClass.newInstance();
				r = (Boolean) validatorMethod.invoke(validator, paramValues);
			}
			if (Boolean.FALSE.equals(r)) {
				// validation failed.
				String newMsg = msg;
				if (NestUtil.isEmpty(newMsg)) {
					newMsg = validatorAction.getMsg();
					msgFormResource = true;
				}
				Object[] msgParams = new String[params.length + 1];
				msgParams[0] = label;
				// msgParams[1] = value;
				for (int i = 0; i < params.length; i++) {
					msgParams[i + 1] = params[i].value();
				}
				context.getActionErrors().add(fieldName, newMsg,
						msgFormResource, msgParams);
				throw new ValidateFailedException();
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(context.getMessage(
					"commonsValidator.exception", new Object[] {
							fullMethodName, fieldName, value, validateType }));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(context.getMessage(
					"commonsValidator.exception", new Object[] {
							fullMethodName, fieldName, value, validateType }));
		} catch (InstantiationException e) {
			throw new RuntimeException(context.getMessage(
					"commonsValidator.exception", new Object[] {
							fullMethodName, fieldName, value, validateType }));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(context.getMessage(
					"commonsValidator.exception", new Object[] {
							fullMethodName, fieldName, value, validateType }));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(context.getMessage(
					"commonsValidator.exception", new Object[] {
							fullMethodName, fieldName, value, validateType }));
		} catch (InvocationTargetException e) {
			throw new RuntimeException(context.getMessage(
					"commonsValidator.exception", new Object[] {
							fullMethodName, fieldName, value, validateType }));
		}
	}

	/**
	 * Validate the value is not empty.
	 * 
	 * @param value
	 *            value to be validated.
	 * @return true if the value is not empty, else false.
	 */
	public static boolean validateRequired(String value) {
		return !GenericValidator.isBlankOrNull(value);
	}

	/**
	 * <p>
	 * Loads the commons validator class containing the target rule.
	 * </p>
	 * 
	 * @param validatorAction
	 *            the validator rules config bean
	 * @return the class having the target validation method
	 * 
	 * @throws ClassNotFoundException
	 *             if the specified class cannot be found
	 * @throws InstantiationException
	 *             if a new instance cannot be instantiated
	 * @throws IllegalAccessException
	 *             if there is no public constructor
	 */
	protected Class<?> loadValidatorClass(ValidatorAction validatorAction)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		Class<?> clazz = validatorClasses.get(validatorAction.getClassname());
		if (clazz != null) {
			return clazz;
		}
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}
		assert classLoader != null;

		// find the target class having the validator rule
		Class<?> validatorClass = classLoader.loadClass(validatorAction
				.getClassname());
		
		validatorClasses.put(validatorAction.getClassname(), validatorClass);

		return validatorClass;
	}

	/**
	 * <p>
	 * Loads the <code>Method</code> of the <code>validatorClass</code>
	 * having using definitions from the <code>validatorAction</code> bean.
	 * </p>
	 * 
	 * @param validatorAction
	 *            The config info bean of the target rule.
	 * @param validatorClass
	 *            The class having the validation method.
	 * @param methodParamClasses
	 *            The method formal parameter class signature.
	 * @return target commons validator method to invoke.
	 * @throws NoSuchMethodException
	 *             if the specified method cannot be found
	 */
	protected Method loadValidatorMethod(ValidatorAction validatorAction,
			Class<?> validatorClass, Class<?>[] methodParamClasses)
			throws NoSuchMethodException {
		// find the method on the target validator rule class
		Method validatorMethod = validatorClass.getMethod(validatorAction
				.getMethod(), methodParamClasses);
		return validatorMethod;
	}

	/**
	 * <p>
	 * Returns an array of class types corresponding to the the target
	 * validation rules method signature. The params are configured by the
	 * <code>validator</code>'s <code>methodParams</code> attribute.
	 * </p>
	 * 
	 * @param validationAction
	 *            the validators configuration bean populated from the XML file.
	 * @return an array of class types for the formal parameter list.
	 * @throws ClassNotFoundException
	 */
	protected Class<?>[] loadMethodParamClasses(ValidatorAction validationAction)
			throws ClassNotFoundException {

		List<String> tmp = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(validationAction
				.getMethodParams(), ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			if (token.length() > 0) {
				tmp.add(token);
			}
		}

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}
		assert classLoader != null;

		Class<?>[] parameterClasses = new Class[tmp.size()];
		for (int i = 0; i < tmp.size(); i++) {
			String className = tmp.get(i);
			if (standardTypes.containsKey(className)) {
				parameterClasses[i] = (Class<?>) standardTypes.get(className);
			} else {
				parameterClasses[i] = classLoader.loadClass(className);
			}
		}

		return parameterClasses;
	}

	/**
	 * <p>
	 * Loads an array of method parameter values corresponding to the formal
	 * parameter of the target validator's method.
	 * <p>
	 * 
	 * @param validatorAction
	 *            <code>ValidatorAction</code> configuration bean.
	 * @param methodParamClasses
	 *            <code>Class[]</code> of the parameters of the target method.
	 * @return An array of object valuse for each method parameter.
	 */
	protected Object[] loadMethodParamValues(ValidatorAction validatorAction,
			Class<?>[] methodParamClasses, Object value, Param[] params) {
		Object[] target = new Object[methodParamClasses.length];
		
		Object[] values = new Object[params.length + 1];
		values[0] = value;
		for (int i = 0; i < params.length; i++) {
			values[i+1] = params[i].value();
		}

		for (int i = 0; i < methodParamClasses.length; i++) {
			if (values[i] == null) {
				target[i] = null;
			} else {
				target[i] = convert(values[i], methodParamClasses[i]);
			}
		}

		return target;
	}

	// ----------------------------------------------------- Static
	// Initialization

	/**
	 * <p>
	 * A utility method that converts an object to an instance of a given class,
	 * such as converting <code>"true"</code> for example, into
	 * <code>Boolean.TRUE</code>.
	 * </p>
	 * <p>
	 * If the component passed to this method is an instance of
	 * <code>EditableValueHolder</code> and the object's class is
	 * <code>String</code>, this method returns the component's submitted
	 * value, without converting it to a string. The <code>component</code>
	 * parameter can be <code>null</code>.
	 * </p>
	 * 
	 * @param obj
	 *            The object to convert
	 * @param cl
	 *            The type of object to convert to
	 */
	private static Object convert(Object obj, Class<?> cl) {

		if (cl.isInstance(obj)) {
			return obj;
		}
		if (cl == String.class) {
			return "" + obj;
		}
		if (obj instanceof String) {
			String str = (String) obj;
			if (cl == boolean.class) {
				return Boolean.valueOf(str);
			}
			if (cl == byte.class) {
				return new Byte(str);
			}
			if (cl == char.class) {
				return new Character(str.charAt(0));
			}
			if (cl == double.class) {
				return new Double(str);
			}
			if (cl == float.class) {
				return new Float(str);
			}
			if (cl == int.class) {
				return new Integer(str);
			}
			if (cl == long.class) {
				return new Long(str);
			}
			if (cl == short.class) {
				return new Short(str);
			}
		} else if (obj instanceof Number) {
			Number num = (Number) obj;
			if (cl == byte.class) {
				return new Byte(num.byteValue());
			}
			if (cl == double.class) {
				return new Double(num.doubleValue());
			}
			if (cl == float.class) {
				return new Float(num.floatValue());
			}
			if (cl == int.class) {
				return new Integer(num.intValue());
			}
			if (cl == long.class) {
				return new Long(num.longValue());
			}
			if (cl == short.class) {
				return new Short(num.shortValue());
			}
		}
		return obj;
	}

	private static boolean isInStrings(String[] strings, String string) {
		for (String s : strings) {
			if (s.equals(string)) {
				return true;
			}
		}
		return false;
	}
}
