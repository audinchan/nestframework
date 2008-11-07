package org.nestframework.data;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.util.PropertyFilter;

public class JsonPropertyFilter implements PropertyFilter
{
    private Map<String, String> ignoreValue = new HashMap<String, String>();

    public boolean apply(Object source, String name, Object value)
    {
        String pname = source.getClass().getName() + "." + name;

        if (ignoreValue.containsKey(pname))
        {
            String[] vs = ((String) ignoreValue.get(pname)).split("_");
            for (String v : vs)
            {
                if ((value == null && v.equals("null"))
                    || (v.equals(value.toString())))
                    return true;
            }
        }
        return false;
    }

    public void setIgnoreValue(String pname, String value)
    {
        ignoreValue.put(pname, value);
    }
}
