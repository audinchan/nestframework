package org.nestframework.core;

public class ActionResolver {
	public static Object resolveAction(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		int pos = className.lastIndexOf('.');
		return Class.forName(className.substring(0, pos)).newInstance();
	}
}
