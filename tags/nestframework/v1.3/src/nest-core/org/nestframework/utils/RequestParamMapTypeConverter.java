package org.nestframework.utils;

import java.lang.reflect.Array;
import java.util.Map;

import ognl.DefaultTypeConverter;

/**
 * Convert request parameter to specified java type.
 * 
 * @author audin
 *
 */
@SuppressWarnings("unchecked")
public class RequestParamMapTypeConverter extends DefaultTypeConverter {

	@Override
	public Object convertValue(Map context, Object value, Class toType) {

		if (value.getClass().isArray() && !toType.isArray()) {
			return convertValue(context, Array.get(value, 0), toType);
		} else if (!value.getClass().isArray() && toType.isArray()) {
			Object obj = Array.newInstance(value.getClass(), 1);
			Array.set(obj, 0, value);
			return convertValue(context, obj, toType);
		}
		
		if (toType.isEnum()) {
			return Enum.valueOf(toType, (String) value);
		} else if (toType.isArray() && toType.getComponentType().isEnum()) {
			Class componentType = toType.getComponentType();
            Object result = Array.newInstance(componentType, Array.getLength(value));
            for (int i = 0, icount = Array.getLength(value); i < icount; i++) {
                Array.set(result, i, convertValue(context, Array.get(value, i), componentType));
            }
            return result;
		}
		
		// need to fix ognl 2.6.9
		if (toType == Boolean.class || toType == Boolean.TYPE) {
			if (value.getClass() == String.class) {
				return Boolean.valueOf((String) value);
			}
		}
		
		return super.convertValue(context, value, toType);
	}

}
