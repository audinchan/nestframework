package org.nestframework.core;

/**
 * Default action bean resolver.
 * 
 * @author audin
 *
 */
public class ActionResolver {
	
	/**
	 * Resolve action bean from action path.
	 * @param className Action path.
	 * @return Action bean.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Object resolveAction(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		int pos = className.lastIndexOf('.');
		Class<?> class1;
		try {
			class1 = Class.forName(className.substring(0, pos));
		} catch (ClassNotFoundException e) {
			class1 = Class.forName(className.substring(0, 1).toUpperCase() + className.substring(1, pos));
		}
		return class1.newInstance();
	}
}
