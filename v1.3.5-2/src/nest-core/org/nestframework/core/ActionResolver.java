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
		Class<?> clazz;
		// com.company.project.webapp.action.classname
		String cn = className.substring(0, pos);
		try {
			clazz = Class.forName(cn);
		} catch (ClassNotFoundException e) {
			int p2 = cn.lastIndexOf('.');
			// classname
			String shortName = cn.substring(p2 + 1);
			// com.company.project.webapp.action + . + C + lassname
			cn = cn.substring(0, p2) + '.' + shortName.substring(0, 1).toUpperCase() + shortName.substring(1);
			clazz = Class.forName(cn);
		}
		return clazz.newInstance();
	}
}
