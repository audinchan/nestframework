package org.nestframework.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Properties;

import org.nestframework.action.ActionException;
import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.config.ConfigurationHelper;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.IInitable;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * Load properties from 'propertiesFile' for later using within ActionBean. The
 * default file character encoding is 'GBK', you can change it by setting 'propertiesEncoding'.
 * You can load multiple files seperated by ','.
 * <p>
 * To use, define the 'Properties' field of ActionBean with the annotation 'LoadProperties'. 
 * </p>
 * 
 * @author audin
 *
 */
@Intercept( { Stage.INITIAL_ACTIONBEAN })
public class PropertiesLoadActionHandler implements IActionHandler, IInitable {
	public static final String PROPERTY_KEY = "propertiesFile";
	public static final String PROPERTY_ENCODING_KEY = "propertiesEncoding";
	protected String defaultEncoding = "GBK";
	private static Properties properties = null;

	public boolean process(ExecuteContext context) throws Exception {
		Collection<Field> fields = NestUtil.getFields(context.getActionClass());
		for (Field f : fields) {
			LoadProperties prop = f.getAnnotation(LoadProperties.class);
			if (prop != null) {
				if (!Modifier.isPublic(f.getModifiers())) {
					try {
						f.setAccessible(true);
					} catch (SecurityException e) {
						throw new ActionException("Failed to set value for " + f.getName());
					}
				}
				f.set(context.getActionBean(), properties.clone());
			}
		}
		return false;
	}

	public void init(IConfiguration config) {
		properties = new Properties();
		String encoding = config.getProperties().get(PROPERTY_ENCODING_KEY);
		if (NestUtil.isEmpty(encoding)) {
			encoding = defaultEncoding;
		}
		ConfigurationHelper ch = new ConfigurationHelper(config.getProperties());
		for (String resourceLocation: ch.toList(PROPERTY_KEY)) {
			InputStream is = config.getExternalContext().getResourceAsStream(resourceLocation);
			try {
				NestUtil.load(properties, new InputStreamReader(is, encoding));
			} catch (Exception e) {
				throw new ActionException("Failed to load properties from:" + resourceLocation);
			}
			
		}
	}
}
