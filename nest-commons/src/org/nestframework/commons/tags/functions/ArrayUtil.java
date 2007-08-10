package org.nestframework.commons.tags.functions;


public class ArrayUtil {
	public static boolean constains(Object[] array, Object item) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null && array[i].equals(item)) {
				return true;
			}
		}
		return false;
	}
	
	private ArrayUtil() {
		super();
	}
}
