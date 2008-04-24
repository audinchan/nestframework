package org.nestframework.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.ExporterException;
import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.TemplateProducer;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

public class MyExporter extends GenericExporter {
	
	public MyExporter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyExporter(Configuration cfg, File outputdir) {
		super(cfg, outputdir);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 是否覆盖存在的文件.
	 */
	private boolean overwriteExistsFile = false;

	public boolean isOverwriteExistsFile() {
		return overwriteExistsFile;
	}

	public void setOverwriteExistsFile(boolean overwriteExistsFile) {
		this.overwriteExistsFile = overwriteExistsFile;
	}
	
	/**
	 * 文件存在时是否已附加后缀创建新文件.
	 */
	private boolean createAnotherFile = true;
	
	/**
	 * 新文件的附加后缀是什么.
	 */
	private String anotherFileExt = ".__new__";

	public boolean isCreateAnotherFile() {
		return createAnotherFile;
	}

	public void setCreateAnotherFile(boolean createAnotherFile) {
		this.createAnotherFile = createAnotherFile;
	}

	public String getAnotherFileExt() {
		return anotherFileExt;
	}

	public void setAnotherFileExt(String anotherFileExt) {
		this.anotherFileExt = anotherFileExt;
	}
	
	/**
	 * 只处理包含的Model.
	 */
	private String[] includeModels = null;
	
	/**
	 * 只处理排除以外的Model.
	 */
	private String[] excludeModels = null;
	
	public String[] getIncludeModels() {
		return includeModels;
	}

	public void setIncludeModels(String[] includeModels) {
		this.includeModels = includeModels;
	}

	public String[] getExcludeModels() {
		return excludeModels;
	}

	public void setExcludeModels(String[] excludeModels) {
		this.excludeModels = excludeModels;
	}

	protected void doStart() {
		if(getFilePattern()==null) throw new ExporterException("File pattern not set on " + this.getClass());
		if(getTemplateName()==null) throw new ExporterException("Template name not set on " + this.getClass());
		
		if(getFilePattern().indexOf("{class-name}")>=0) {				
			super.doStart();
		} else {
			File file = new File(getOutputDirectory(),getFilePattern());
			if (!overwriteExistsFile && file.exists()) {
				if (createAnotherFile) {
					file = new File(getOutputDirectory(),getFilePattern() + anotherFileExt);
				} else {
					return;
				}
			}
			TemplateProducer producer = new TemplateProducer(getTemplateHelper(),getArtifactCollector());
			producer.produce(new HashMap(), getTemplateName(), file, getTemplateName());
		}
	}

	protected void exportPOJO(Map additionalContext, POJOClass element) {
		if (includeModels != null) {
			boolean handleIt = false;
			for (int i = 0; i < includeModels.length; i++) {
				if (element.getShortName().equalsIgnoreCase(includeModels[i])) {
					handleIt = true;
					break;
				}
			}
			if (!handleIt) {
				return;
			}
		} else if (excludeModels != null) {
			for (int i = 0; i < excludeModels.length; i++) {
				if (element.getShortName().equalsIgnoreCase(excludeModels[i])) {
					return;
				}
			}
		}
		TemplateProducer producer = new TemplateProducer(getTemplateHelper(),getArtifactCollector());					
		additionalContext.put("pojo", element);
		additionalContext.put("clazz", element.getDecoratedObject());
		//System.err.println(element.getShortName());
		String filename = resolveFilename( element );
		File file = new File(getOutputDirectory(),filename);
		if (!overwriteExistsFile && file.exists()) {
			if (createAnotherFile) {
				file = new File(getOutputDirectory(),filename + anotherFileExt);
			} else {
				return;
			}
		}
		if(filename.endsWith(".java") && filename.indexOf('$')>=0) {
			log.warn("Filename for " + getClassNameForFile( element ) + " contains a $. Innerclass generation is not supported.");
		}
		producer.produce(additionalContext, getTemplateName(), file, getTemplateName());
	}

}
