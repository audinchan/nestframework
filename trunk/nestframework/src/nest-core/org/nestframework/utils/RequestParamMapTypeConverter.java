package org.nestframework.utils;

import java.lang.reflect.Array;
import java.util.Map;

import ognl.DefaultTypeConverter;

public class RequestParamMapTypeConverter extends DefaultTypeConverter {

	@Override
	public Object convertValue(Map context, Object value, Class toType) {
		if (value.getClass().isArray() && !toType.isArray()) {
			return super.convertValue(context, Array.get(value, 0), toType);
		} else if (!value.getClass().isArray() && toType.isArray()) {
			Object obj = Array.newInstance(value.getClass(), 1);
			Array.set(obj, 0, value);
			return super.convertValue(context, obj, toType);
		} else {
			return super.convertValue(context, value, toType);
		}
	}

}
