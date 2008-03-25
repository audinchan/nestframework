/**
 * 
 */
package org.nestframework.tools;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.AbstractExporter;
import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

/**
 * @author austin
 * 
 */
public class Exporter extends AbstractExporter {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(Exporter.class);
	private boolean overwriteExistsFile = false;
	private boolean createAnotherFile = true;
	private String anotherFileExt = ".__new__";
	/**
	 * 只处理包含的Model.
	 */
	private String[] includeModels = null;
	
	/**
	 * 只处理排除以外的Model.
	 */
	private String[] excludeModels = null;

//	private File outPutDir = new File("src");

	/**
	 * @param cfg
	 * @param outputdir
	 */
	public Exporter(Configuration cfg, File outputdir) {
		super(cfg, outputdir);
		init();
	}

	/**
	 * 
	 */
	public Exporter() {
		super();
		init();
	}

	private void init() {
//		if (getProperties().contains("hss_src_dir")) {
//			outPutDir = new File((String) getProperties().getProperty(
//					"hss_src_dir"));
//		}
	}

	protected void exportComponent(Map additionalContext, POJOClass element) {
		// noop - we dont want components
	}


	// @Override
	public String getName() {
		return "hbm2hss";
	}

	// @Override
	protected void setupContext() {
		if (logger.isDebugEnabled()) {
			logger.debug("setupContext() - start");
		}

//		if (!getProperties().contains("hss_src_dir")) {
//			getProperties().put("hss_src_dir", "src");
//		}
		super.setupContext();

		if (logger.isDebugEnabled()) {
			logger.debug("setupContext() - end");
		}
	}

	public void doStart() {
		if (logger.isDebugEnabled()) {
			logger.debug("doStart() - start");
		}

//		 Set props = getProperties().entrySet();
//		 for (Object object : props) {
//		 System.out.print(((Entry)object).getKey().toString());
//		 System.out.println(": "+((Entry)object).getValue());
//		 }

		PropertyHandler p = new PropertyHandler()
				.setProperties(getProperties());
		
		this.overwriteExistsFile = p.asBoolean("hss_overwrite_exists");
		this.createAnotherFile = p.asBoolean("hss_create_another_file");
		this.anotherFileExt = p.asString("hss_another_fileext");
		String ims = p.asString("include_models");
		if (!"".equals(ims)) {
			this.includeModels = ims.split(",");
		}
		String ems = p.asString("exclude_models");
		if (!"".equals(ems)) {
			this.excludeModels = ems.split(",");
		}
			
		boolean jdk5 = p.asBoolean("hss_jdk5");
        boolean mergeDao = p.asBoolean("merge_dao");


		// dao interface

        if (!mergeDao) {
    		if (p.asBoolean("hss_export_rootdao_interface")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("root dao interface code - start");
    			}
    			configureExporter(
    					"tpl/IRootDAO.java.ftl",
    					getConfStr("hss_src_dir") + "/"
    							+ getConfPath("hss_dao_package") + "/IRootDAO.java")
    					.start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("root dao interface code - start");
    			}
    		}
    
    		if (p.asBoolean("hss_export_basedao_interface")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("base dao interface code - start");
    			}
    			configureExporter(
    					"tpl/IBaseObjectDAO.java.ftl",
    					getConfStr("hss_src_dir") + "/"
    							+ getConfPath("hss_dao_package")
    							+ (jdk5 ? "/IBaseDAO.java" : "/IBase{class-name}DAO.java")).start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("base dao interface code - start");
    			}
    		}
    
    		if (p.asBoolean("hss_export_dao_interface")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("dao interface code - start");
    			}
    			configureExporter(
    					"tpl/IObjectDAO.java.ftl",
    					getConfStr("hss_src_dir") + "/"
    							+ getConfPath("hss_dao_package")
    							+ "/I{class-name}DAO.java").start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("dao interface code - start");
    			}
    		}
    
    		// dao implementation
    
    		if (p.asBoolean("hss_export_rootdao")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("root dao code - start");
    			}
    			configureExporter(
    					"tpl/RootDAOHibernate.java.ftl",
    					getConfStr("hss_src_dir") + "/"
    							+ getConfPath("hss_dao_package")
    							+ "/hibernate/RootDAOHibernate.java").start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("root dao code - start");
    			}
    		}
    		if (p.asBoolean("hss_export_basedao")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("base dao code - start");
    			}
    			configureExporter(
    					"tpl/BaseObjectDAOHibernate.java.ftl",
    					getConfStr("hss_src_dir") + "/"
    							+ getConfPath("hss_dao_package")
    							+ (jdk5 ? "/hibernate/BaseDAOHibernate.java" : "/hibernate/Base{class-name}DAOHibernate.java") )
    					.start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("base dao code - start");
    			}
    		}
    		if (p.asBoolean("hss_export_dao")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("dao code - start");
    			}
    			configureExporter(
    					"tpl/ObjectDAOHibernate.java.ftl",
    					getConfStr("hss_src_dir") + "/"
    							+ getConfPath("hss_dao_package")
    							+ "/hibernate/{class-name}DAOHibernate.java")
    					.start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("dao code - start");
    			}
    		}
        }

		// service interface

		if (p.asBoolean("hss_export_rootservice_interface")) {
			if (logger.isDebugEnabled()) {
				logger.debug("root service interface code - start");
			}
			configureExporter(
					"tpl/IRootManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ "/IRootManager.java").start();
			if (logger.isDebugEnabled()) {
				logger.debug("root service interface code - start");
			}
		}

		if (p.asBoolean("hss_export_baseservice_interface")) {
			if (logger.isDebugEnabled()) {
				logger.debug("base service interface code - start");
			}
			configureExporter(
					"tpl/IBaseObjectManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ (jdk5 ? "/IBaseManager.java" : "/IBase{class-name}Manager.java") ).start();
			if (logger.isDebugEnabled()) {
				logger.debug("base service interface code - start");
			}
		}

		if (p.asBoolean("hss_export_service_interface")) {
			if (logger.isDebugEnabled()) {
				logger.debug("service interface code - start");
			}
			configureExporter(
					"tpl/IObjectManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ "/I{class-name}Manager.java").start();
			if (logger.isDebugEnabled()) {
				logger.debug("service interface code - start");
			}
		}
		// service implementation

		if (p.asBoolean("hss_export_rootservice")) {
			if (logger.isDebugEnabled()) {
				logger.debug("root service code - start");
			}
			configureExporter(
					"tpl/RootManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ "/impl/RootManager.java").start();
			if (logger.isDebugEnabled()) {
				logger.debug("root service code - start");
			}
		}

		if (p.asBoolean("hss_export_baseservice")) {
			if (logger.isDebugEnabled()) {
				logger.debug("base service code - start");
			}
			configureExporter(
					"tpl/BaseObjectManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ (jdk5 ? "/impl/BaseManager.java" : "/impl/Base{class-name}Manager.java") ).start();
			if (logger.isDebugEnabled()) {
				logger.debug("base service code - start");
			}
		}

		if (p.asBoolean("hss_export_service")) {
			if (logger.isDebugEnabled()) {
				logger.debug("service code - start");
			}
			configureExporter(
					"tpl/ObjectManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ "/impl/{class-name}Manager.java").start();
			if (logger.isDebugEnabled()) {
				logger.debug("service code - start");
			}
		}

		// config

		if (!mergeDao) {
    		if (p.asBoolean("hss_export_dao_context")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("dao context - start");
    			}
    			configureExporter(
    					"tpl/applicationContext-hibernate.xml.ftl",
    					getConfStr("hss_web_dir")
    							+ "/WEB-INF/applicationContext-hibernate.xml")
    					.start();
    			if (logger.isDebugEnabled()) {
    				logger.debug("dao context - start");
    			}
    		}
        }

		if (p.asBoolean("hss_export_service_context")) {
			if (logger.isDebugEnabled()) {
				logger.debug("service context - start");
			}
			configureExporter(
					"tpl/applicationContext-service.xml.ftl",
					getConfStr("hss_web_dir")
							+ "/WEB-INF/applicationContext-service.xml")
					.start();
			if (logger.isDebugEnabled()) {
				logger.debug("service context - start");
			}
		}

		if (p.asBoolean("hss_export_action_context")) {
			if (logger.isDebugEnabled()) {
				logger.debug("action context - start");
			}
			configureExporter(
					"tpl/servlet-context.xml.ftl",
					getConfStr("hss_web_dir")
							+ "/WEB-INF/servlet-context.xml").start();
			if (logger.isDebugEnabled()) {
				logger.debug("action context - start");
			}
		}
		
		if (p.asBoolean("hss_export_struts_config")) {
			if (logger.isDebugEnabled()) {
				logger.debug("struts config - start");
			}
			configureExporter(
					"tpl/struts-config.xml.ftl",
					getConfStr("hss_web_dir")
							+ "/WEB-INF/struts-config.xml").start();
			if (logger.isDebugEnabled()) {
				logger.debug("struts config - start");
			}
		}

		// demo manager and action

		if (p.asBoolean("hss_export_demo")) {
			if (logger.isDebugEnabled()) {
				logger.debug("generate demo code - start");
			}
			configureExporter(
					"tpl/IDemoManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ "/IDemoManager.java").start();

			configureExporter(
					"tpl/DemoManager.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_service_package")
							+ "/impl/DemoManager.java").start();

			configureExporter(
					"tpl/DemoAction.java.ftl",
					getConfStr("hss_src_dir") + "/"
							+ getConfPath("hss_base_package")
							+ "/webapp/action/demo/DemoAction.java").start();

//			configureExporter(
//					"tpl/DemoForm.java.ftl",
//					getConfStr("hss_src_dir") + "/"
//							+ getConfPath("hss_base_package")
//							+ "/webapp/form/DemoForm.java").start();
			
			configureExporter(
					"tpl/demo.jsp.ftl",
					getConfStr("hss_web_dir") + "/demo/demo.jsp").start();
			
			if (logger.isDebugEnabled()) {
				logger.debug("generate demo code - end");
			}
		}
        
		if (p.asBoolean("hss_export_base_testcase")) {
			if (logger.isDebugEnabled()) {
				logger.debug("generate BaseTestCase - start");
			}
	        // BaseTestCase
	        configureExporter(
	            "tpl/BaseTestCase.java.ftl",
	            getConfStr("hss_test_dir") + "/"
	                    + getConfPath("hss_base_package")
	                    + "/BaseTestCase.java").start();

	        // BaseActionTestCase
	        configureExporter(
	            "tpl/BaseActionTestCase.java.ftl",
	            getConfStr("hss_test_dir") + "/"
	                    + getConfPath("hss_base_package")
	                    + "/BaseActionTestCase.java").start();
	        if (logger.isDebugEnabled()) {
				logger.debug("generate BaseTestCase - end");
			}
		}
		
		String extraTemplate = p.asString("hss_extra_template");
		if (!"".equals(extraTemplate)) {
			String[] tpls = extraTemplate.split(",");
			for (int i = 0; i < tpls.length; i++) {
				if (logger.isDebugEnabled()) {
					logger.debug("produce extra template - start: " + tpls[i]);
				}
				String[] tpl = tpls[i].split(":");
				configureExporter(tpl[0], tpl[1]).start();
				if (logger.isDebugEnabled()) {
					logger.debug("produce extra template - end");
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("doStart() - end");
		}
        
        
	}

	private GenericExporter configureExporter(String template, String pattern) {
		if (logger.isDebugEnabled()) {
			logger.debug("configureExporter(String, String) - start");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("template: " + template + "\npattern: " + pattern + "\ntemplate paths: " + getTemplatePaths()
					+ "\noutput directory: " + getOutputDirectory());
		}

		MyExporter exporter = new MyExporter(getConfiguration(),
				getOutputDirectory());
		exporter.setProperties((Properties) getProperties().clone());
		exporter.setTemplatePath(getTemplatePaths());
		exporter.setTemplateName(template);
		exporter.setFilePattern(pattern);
		exporter.setArtifactCollector(getArtifactCollector());
		
		exporter.setOverwriteExistsFile(overwriteExistsFile);
		exporter.setCreateAnotherFile(createAnotherFile);
		exporter.setAnotherFileExt(anotherFileExt);
		exporter.setIncludeModels(includeModels);
		exporter.setExcludeModels(excludeModels);

		if (logger.isDebugEnabled()) {
			logger.debug("configureExporter(String, String) - end");
		}
		return exporter;
	}

	private String getConfStr(String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("getConfStr(String) - start");
		}

		String s = getProperties().getProperty(key);
		String returnString = (null == s) ? "" : s;
		if (logger.isDebugEnabled()) {
			logger.debug("getConfStr(String) - end");
		}
		return returnString;
	}

	private String getConfPath(String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("getConfPath(String) - start");
		}

		String returnString = getConfStr(key).replaceAll("\\.", "/");
		if (logger.isDebugEnabled()) {
			logger.debug("getConfPath(String) - end");
		}
		return returnString;
	}

}
