package org.nestframework.localization;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class LocalizationUtil {
	private static String resource = "resource/messages";
	private static Map<String, ResourceBundle> bundles = new ConcurrentHashMap<String, ResourceBundle>();

	public static String getResource() {
		return resource;
	}

	public static void setResource(String resource) {
		LocalizationUtil.resource = resource;
	}

	public static String getMessage(Locale locale, String key) {
		return getBundle(locale).getString(key);
	}
	
	public static String getMessage(Locale locale, String key, Object... params) {
		return MessageFormat.format(getMessage(locale, key), params);
	}
	
	public static String getMessageOldStyle(Locale locale, String key, Object[] params) {
		return MessageFormat.format(getMessage(locale, key), params);
	}
	
	protected static ResourceBundle getBundle(Locale locale) {
		String key = "";
		if (locale == null) {
			key = "";
		} else {
			key += locale.toString();
			if (locale.getVariant().length() > 0) {
				key += "___"+locale.getVariant();
			}
		}
		ResourceBundle bundle = bundles.get(key);
		if (bundle == null) {
			if (locale == null) {
				bundle = ResourceBundle.getBundle(resource);
			} else {
				bundle = ResourceBundle.getBundle(resource, locale);
			}
			bundles.put(key, bundle);
		}
		
		return bundle;
	}
	
	public static boolean isLocaleResourceExists(Locale locale) {
		StringBuffer sb= new StringBuffer(resource);
		if (locale != null) {
			sb.append("_").append(locale.toString());
			if (locale.getVariant().length() > 0) {
				sb.append("___")
				.append(locale.getVariant());
			}
		}
		sb.append(".properties");
		URL url = LocalizationUtil.class.getClassLoader().getResource(sb.toString());
		return url != null;
	}
	
	public static Locale[] detectLocale(String accept) {
		List<Locale> list = new ArrayList<Locale>();
		Locale locale = null;
		if (accept == null) {
			return list.toArray(new Locale[0]);
		}
		String[] langs = accept.split(",");
		for (String lang: langs) {
			lang = lang.trim();
			int semiPos = lang.indexOf(';');
			if (semiPos != -1) {
				lang = lang.substring(0, semiPos);
			}
			int dashPos = lang.indexOf('-');
			if (dashPos == -1) {
				locale = new Locale(lang);
			} else {
				locale = new Locale(lang.substring(0, dashPos), lang.substring(dashPos + 1));
			}
			list.add(locale);
		}
		return list.toArray(new Locale[0]);
	}
	
	public static void main(String[] args) {
		LocalizationUtil.setResource("org/demo/i18n/Resources");
		Locale[] locales = detectLocale("en, zh_CN;q=0.9, es;q=0.8, de;q=0.7, ja;q=0.3, zh-TW;q=0.1");
		for (Locale locale : locales) {
			System.out.println(locale);
			System.out.println(isLocaleResourceExists(locale));
			System.out.println(getMessage(locale, "msg.hello"));
		}
//		System.out.println(LocalizationUtil.class.getName());
	}
}
