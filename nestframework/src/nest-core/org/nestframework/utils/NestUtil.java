package org.nestframework.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ognl.DefaultMemberAccess;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nestframework.core.BeanContext;
import org.nestframework.core.ExecuteContext;
import org.nestframework.localization.ActionMessages;

/**
 * Nest utility class.
 * 
 * @author audin
 *
 */
public class NestUtil {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(NestUtil.class);
	
	/**
	 * Execute a method beyond context.
	 * @param m Method to be executed.
	 * @param obj The object own this method.
	 * @param ctx Execute context.
	 * @return Method result.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public static Object execMethod(Method m, Object obj, ExecuteContext ctx)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IOException {
		Object rt;
		List<Object> paras = new ArrayList<Object>();
		Class<?>[] paraTypes = m.getParameterTypes();
		// auto parameter type match
		for (Class<?> clazz : paraTypes) {
			if (clazz.equals(HttpServletRequest.class)) {
				paras.add(ctx.getRequest());
			} else if (clazz.equals(HttpServletResponse.class)) {
				paras.add(ctx.getResponse());
			} else if (clazz.equals(HttpSession.class)) {
				paras.add(ctx.getRequest().getSession(true));
			} else if (clazz.equals(BeanContext.class)) {
				paras.add(ctx.getBeanContext());
			} else if (clazz.equals(ActionMessages.class)) {
				paras.add(ctx.getActionErrors());
			} else if (clazz.equals(PrintWriter.class)) {
				paras.add(ctx.getResponse().getWriter());
			} else if (clazz.equals(ctx)) {
				paras.add(ctx);
			}
		}
		rt = m.invoke(obj, (Object[]) paras.toArray(new Object[] {}));
		return rt;
	}

	/**
	 * Convert mathod to property name.
	 * 
	 * @param m Method.
	 * @return
	 */
	public static String methodToPropertyName(Method m) {
		if (log.isDebugEnabled()) {
			log.debug("methodToPropertyName(Method) - start");
		}

		String name = m.getName();
		if ((name.startsWith("set") || name.startsWith("get"))
				&& name.length() > 3) {
			String ret = name.substring(3, 4).toLowerCase();
			if (name.length() > 4)
				ret += name.substring(4);

			if (log.isDebugEnabled()) {
				log.debug("methodToPropertyName(Method) - end");
			}
			return ret;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("methodToPropertyName(Method) - end");
			}
			return name;
		}
	}

	/**
	 * Get all methods of a class.
	 * 
	 * @param clazz The class.
	 * @return All methods of a class.
	 */
	public static Collection<Method> getMethods(Class<?> clazz) {
		if (log.isDebugEnabled()) {
			log.debug("getMethods(Class<?>) - start");
		}

		Collection<Method> found = new ArrayList<Method>();
		while (clazz != null) {
			for (Method m1 : clazz.getDeclaredMethods()) {
				boolean overridden = false;

				for (Method m2 : found) {
					if (m2.getName().equals(m1.getName())
							&& Arrays.deepEquals(m1.getParameterTypes(), m2
									.getParameterTypes())) {
						overridden = true;
						break;
					}
				}

				if (!overridden)
					found.add(m1);
			}

			clazz = clazz.getSuperclass();
		}

		if (log.isDebugEnabled()) {
			log.debug("getMethods(Class<?>) - end");
		}
		return found;
	}

	/**
	 * Get all fields of a class.
	 * 
	 * @param clazz The class.
	 * @return All fields of a class.
	 */
	public static Collection<Field> getFields(Class<?> clazz) {
		if (log.isDebugEnabled()) {
			log.debug("getFields(Class<?>) - start");
		}

		Map<String, Field> fields = new HashMap<String, Field>();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (!fields.containsKey(field.getName())) {
					fields.put(field.getName(), field);
				}
			}

			clazz = clazz.getSuperclass();
		}

		Collection<Field> returnCollection = fields.values();
		if (log.isDebugEnabled()) {
			log.debug("getFields(Class<?>) - end");
		}
		return returnCollection;
	}

	/**
	 * Check whether is the string is empty.
	 * 
	 * @param value String to be checked.
	 * @return <code>true</code> if the value is null or it's length is 0 or is contains only space characters, else is <code>false</code>.
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	/**
	 * Check whether is the string is not empty.
	 * @param value String to be checked.
	 * @return
	 * @see isEmpty.
	 */
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
	
	/**
	 * Replace all \r,\n and trim space at both side.
	 * 
	 * @param value
	 * @return
	 */
	public static String trimAll(String value) {
		if (value == null) {
			return value;
		}
		return value.replaceAll("\\r\\n", ",").replaceAll("[\\r|\\n]", ",").trim();
	}
	
	/**
	 * Implode strings with seperator.
	 * 
	 * @param strings
	 * @param seperator
	 * @return
	 */
	public static String implode(String[] strings, String seperator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			if (i > 0) {
				sb.append(seperator);
			}
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	/**
	 * Get all properties and values of an object.
	 * 
	 * @param bean
	 * @param clazz
	 * @return
	 */
	public static Map<String, Object> getPropertiesMap(Object bean, Class<?> clazz) {
		if (log.isDebugEnabled()) {
			log.debug("getPropertiesMap(Object) - start");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Map<?, ?> ognlContext = Ognl.createDefaultContext(bean);
		DefaultMemberAccess memberAccess = new DefaultMemberAccess(true);
		Ognl.setMemberAccess(ognlContext, memberAccess);
		// get all properties with accessors.
		BeanInfo info = null;
		try {
			info = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			log.warn("getPropertiesMap(Object)", e);
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (PropertyDescriptor d : props) {
			try {
				map.put(d.getName(), Ognl.getValue(d.getName(), ognlContext,
						bean));
			} catch (OgnlException e) {
				log.warn("getPropertiesMap(Object)", e);
			}
		}

		// get all public properties.
		Collection<Field> fields = NestUtil.getFields(clazz);
		for (Field f : fields) {
			if (Modifier.isPublic(f.getModifiers())) {
				try {
					map.put(f.getName(), Ognl.getValue(f.getName(),
							ognlContext, bean));
				} catch (OgnlException e) {
					if (e.getReason() instanceof NoSuchFieldException) {
						// maybe is static final field. no need to handle it.
					} else {
						log.warn("getPropertiesMap(Object)", e);
					}
				} catch (RuntimeException e) {
					log.warn("getPropertiesMap(Object)", e);
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("getPropertiesMap(Object) - end");
		}
		return map;
	}
	
	/**
	 * Get specified properties and values of an object.
	 * 
	 * @param bean
	 * @param propertyNames Which properties to be feched.
	 * @return
	 */
	public static Map<String, Object> getPropertiesMap(Object bean, String... propertyNames) {
		if (log.isDebugEnabled()) {
			log.debug("getPropertiesMap(Object, String) - start");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Map<?, ?> ognlContext = Ognl.createDefaultContext(bean);
		DefaultMemberAccess memberAccess = new DefaultMemberAccess(true);
		Ognl.setMemberAccess(ognlContext, memberAccess);
		for (String propName: propertyNames) {
			try {
				map.put(propName, Ognl.getValue(propName, ognlContext, bean));
			} catch (OgnlException e) {
				log.warn("getPropertiesMap(Object, String)", e);
			}
		}
		

		if (log.isDebugEnabled()) {
			log.debug("getPropertiesMap(Object, String) - end");
		}
		return map;
	}
}
