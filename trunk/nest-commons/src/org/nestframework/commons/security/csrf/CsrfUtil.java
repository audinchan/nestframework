package org.nestframework.commons.security.csrf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nestframework.commons.utils.StringUtil;

public class CsrfUtil {
	private static final Map<String, Boolean> rules = new ConcurrentHashMap<String, Boolean>();
	
	private static volatile boolean inited = false;
	
	public static void init(InputStream config) {
		if (!inited) {
			try {
				InputStreamReader reader = new InputStreamReader(config, "UTF-8");
				BufferedReader r = new BufferedReader(reader);
				String line = null;
				while ((line = r.readLine()) != null) {
					if (line.startsWith("#")) {
						continue;
					}
					String[] parts = line.split("=");
					if (parts.length == 2) {
						rules.put(parts[0], Boolean.valueOf(parts[1]));
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("The Charset of configuration file is not UTF-8", e);
			} catch (IOException e) {
				throw new RuntimeException("Can't read configuration file.", e);
			}
		}
	}
	
	public static String genToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String ssKey = getSessionKey(request); 
		String value = UUID.randomUUID().toString();
		session.removeAttribute(ssKey);
		session.setAttribute(ssKey, value);
		
		return value;
	}
	
	public static void validateToken(HttpServletRequest request) throws CsrfVerifyException {
		String url = request.getRequestURI();
		if (url == null) return;
		boolean needVerify = false;
		for (String rule: rules.keySet()) {
			if (url.matches(rule)) {
				needVerify = rules.get(rule).booleanValue();
			}
		}
		if (!needVerify) {
			return;
		}
		HttpSession session = request.getSession();
		String ssKey = getSessionKey(request);
		String savedValue = (String)session.getAttribute(ssKey);
		if (StringUtil.isEmpty(savedValue)) {
			throw new CsrfVerifyException("No CSRF Token found at server side.");
		}
		String submitValue = request.getParameter(CsrfTokenTag.TOKEN_FIELD_NAME);
		if (StringUtil.isEmpty(submitValue)) {
			throw new CsrfVerifyException("No CSRF Token found from client request.");
		}
		if (StringUtil.isNotSame(savedValue, submitValue)) {
			throw new CsrfVerifyException("CSRF Token verifiy failed.");
		}
	}
	
	protected static String getSessionKey(HttpServletRequest request) {
		return request.getRequestURI() + "|" + CsrfTokenTag.TOKEN_FIELD_NAME;
	}
	
}
