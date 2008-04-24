package org.nestframework.commons.tags.functions;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class CollectionUtil {
	public static boolean contains(Collection coll, Object item) {
		return coll.contains(item);
	}

	private CollectionUtil() {
		super();
	}
}
