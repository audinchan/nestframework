package org.nestframework.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ognl.DefaultMemberAccess;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NestUtil {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(NestUtil.class);

	/**
	 * 将方法名转换为BeanName。
	 * 
	 * @param m
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

	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
	
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
					log.warn("getPropertiesMap(Object)", e);
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("getPropertiesMap(Object) - end");
		}
		return map;
	}
	
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
