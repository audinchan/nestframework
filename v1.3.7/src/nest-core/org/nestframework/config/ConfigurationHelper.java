/**
 * 
 */
package org.nestframework.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nestframework.utils.NestUtil;

/**
 * @author audin
 *
 */
public class ConfigurationHelper {
	private Map<String, String> properties;

	public ConfigurationHelper(Map<String, String> props) {
		this.properties = props;
	}
	
	public List<String> toList(String configName) {
		return toList(configName, ",");
	}
	
	public List<String> toList(String configName, String seperator) {
		List<String> list = new ArrayList<String>();
		String value = properties.get(configName);
		if (NestUtil.isNotEmpty(value)) {
			String[] lines = NestUtil.trimAll(value)
					.split(seperator);
			for (String line : lines) {
				list.add(line.trim());
			}
		}
		
		return list;
	}
	
	public Map<String, String> toMap(String configName) {
		return toMap(configName, ",", "=");
	}
	
	public Map<String, String> toMap(String configName, String lineSeperator, String fieldSeperator) {
		Map<String, String> map = new HashMap<String, String>();
		for (String line: toList(configName, lineSeperator)) {
			String[] parts = line.split(fieldSeperator);
			map.put(parts[0].trim(), parts[1].trim());
		}
		return map;
	}

	public ConfigurationHelper handleClassInstance(String configName,
			InstanceHandler handler) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (String line: toList(configName, ",")) {
			Object instance = Class.forName(line).newInstance();
			handler.handleInstance(instance);
		}
		return this;
	}
	
	public interface InstanceHandler {
		public void handleInstance(Object instance);
	}
}
