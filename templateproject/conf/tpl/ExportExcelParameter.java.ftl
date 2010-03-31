package ${hss_base_package}.dto;

import java.util.List;

/**
 * ����Excel�ļ�ʹ�õ����ò���
 * @author wanghai
 *
 */
public class ExportExcelParameter extends AssignedSheet{

	/**
	 * ģ���ļ�����
	 */
	private String templateName;
	
	/**
	 * Sheet�б�
	 */
	private List<AssignedSheet> sheets;
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<AssignedSheet> getSheets() {
		return sheets;
	}

	public void setSheets(List<AssignedSheet> sheets) {
		this.sheets = sheets;
	}

}
