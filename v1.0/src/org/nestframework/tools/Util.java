package org.nestframework.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;

public class Util {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(Util.class);

	public boolean isEntity(Object obj) {
		if (log.isDebugEnabled()) {
			log.debug("isEntity(Object) - start: " + obj);
		}

		boolean returnboolean = obj instanceof RootClass;
		if (log.isDebugEnabled()) {
			log.debug("isEntity(Object) - end");
		}
		return returnboolean;
	}

	public List getClassMappings(Configuration cfg) {
		if (log.isDebugEnabled()) {
			log.debug("getClassMappings(Configuration) - start");
		}

		List list = new ArrayList();
		Iterator classMappings = cfg.getClassMappings();
		while (classMappings.hasNext()) {
			PersistentClass element = (PersistentClass) classMappings.next();
			list.add(element);
			if (log.isDebugEnabled()) {
				log.debug("Add element: " + element.getClassName());
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("getClassMappings(Configuration) - end");
		}
		return list;
	}
	
	public String firstLowerCase(String s) {
		if (log.isDebugEnabled()) {
			log.debug("firstLowerCase(String) - start");
		}

		if (null == s) {
			if (log.isDebugEnabled()) {
				log.debug("firstLowerCase(String) - end");
			}
			return "";
		}
		String returnString = s.substring(0, 1).toLowerCase() + s.substring(1);
		if (log.isDebugEnabled()) {
			log.debug("firstLowerCase(String) - end");
		}
		return returnString;
	}
	
	public String toBeanName(PersistentClass element) {
		if (log.isDebugEnabled()) {
			log.debug("toBeanName(PersistentClass) - start");
		}

		String name = element.getClassName();
		name = name.substring(name.lastIndexOf('.') + 1);
		String returnString = name.substring(0, 1).toLowerCase()
				+ name.substring(1);
		if (log.isDebugEnabled()) {
			log.debug("toBeanName(PersistentClass) - end");
		}
		return returnString;
	}

	public String getDeclarationName(PersistentClass element) {
		if (log.isDebugEnabled()) {
			log.debug("getDeclarationName(PersistentClass) - start");
		}

		String name = element.getClassName();
		String returnString = name.substring(name.lastIndexOf('.') + 1);
		if (log.isDebugEnabled()) {
			log.debug("getDeclarationName(PersistentClass) - end");
		}
		return returnString;
	}
	
	public String getMappingFileResource(PersistentClass element) {
		if (log.isDebugEnabled()) {
			log.debug("getMappingFileResource(PersistentClass) - start");
		}

		String returnString = element.getClassName().replace('.', '/')
				+ ".hbm.xml";
		if (log.isDebugEnabled()) {
			log.debug("getMappingFileResource(PersistentClass) - end");
		}
		return returnString;
	}
	
	public String toXML(String s) {
		if (log.isDebugEnabled()) {
			log.debug("toXML(String) - start");
		}

		if (null == s) {
			if (log.isDebugEnabled()) {
				log.debug("toXML(String) - end");
			}
			return "";
		}
		String returnString = s.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(
						"\"", "&quot;");
		if (log.isDebugEnabled()) {
			log.debug("toXML(String) - end");
		}
		return returnString;
	}
	
	public String null2Str(String s) {
		if (log.isDebugEnabled()) {
			log.debug("null2Str(String) - start");
		}

		if (null == s) {
			if (log.isDebugEnabled()) {
				log.debug("null2Str(String) - end");
			}
			return "";
		}

		if (log.isDebugEnabled()) {
			log.debug("null2Str(String) - end");
		}
		return s;
	}
    
    public String test(Property p) {
		if (log.isDebugEnabled()) {
			log.debug("test(Property) - start");
		}

        System.out.println("BBBBBBBBBBBBBBBBBB:"+p.getType().getReturnedClass().getName());

		if (log.isDebugEnabled()) {
			log.debug("test(Property) - end");
		}
        return "AAAA";
    }
}
