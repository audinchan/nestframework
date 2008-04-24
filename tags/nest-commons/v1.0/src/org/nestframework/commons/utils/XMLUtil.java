package org.nestframework.commons.utils;

public class XMLUtil {
	
	public static String escape(String s) {
		if (s == null) {
			return s;
		}
		return s.replaceAll("&", "&amp;")
			.replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;")
			.replaceAll("'", "&apos;")
			.replaceAll("\"", "&quot;");
	}
	
	public static String unEscape(String s) {
		if (s == null) {
			return s;
		}
		return s.replaceAll("&amp;", "&")
			.replaceAll("&lt;", "<")
			.replaceAll("&gt;", ">")
			.replaceAll("&apos;", "'")
			.replaceAll("&quot;", "\"");
	}

}
