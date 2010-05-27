package ${hss_base_package}.dto;


import java.util.List;

/**
 * ָ����Sheetҳ����
 * @author wanghai
 *
 */
public class AssignedSheet {
	/**
	 * Sheet����
	 */
	private String sheetName="Sheet1";
	/**
	 * ʹ��ģ��Sheet����
	 */
	private String templateSheetName="Sheet1";
	
	/**
	 * ģ���ļ���������(ʵ������)
	 */
	private int totalCol;
	
	/**
	 * ��ָ��λ����ʾ����
	 */
	private List<AssignedCell> assignedCells;
	
	/**
	 * �����У���������ȡ������ʽ
	 */
	private AssignedCell dataRow;
	
	/**
	 * ������ʾ�У������ж�ȡ������ʽ
	 */
	private AssignedCell highLightRow;
	/**
	 * ��������ռ������Ĭ��Ϊ1
	 */
	private int dataRowSpan=1;
	
	/**
	 * �����ݰ��½���copyģ��ķ�ʽ
	 */
	private boolean needCopyTemplateRow=false;
	
	/**
	 * ׷�ӵ�Sheet
	 */
	private String AppendToSheet;
	
	/**
	 * �����Ƿ��Զ������иߣ�Ĭ��Ϊfalse��ʹ��ģ����ָ�����к�
	 */
	private boolean autoHeight = false;
	
	/**
	 * ��������
	 */
	private List<AssignedCell[]> data;
	
	public AssignedSheet(){
		
	}
	
	public AssignedSheet(String sheetName,String templateSheetName){
		this.sheetName=sheetName;
		this.templateSheetName=templateSheetName;
	}
	
	public String getSheetName() {
		return sheetName;
	}
	
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public String getTemplateSheetName() {
		return templateSheetName;
	}
	
	public void setTemplateSheetName(String templateSheetName) {
		this.templateSheetName = templateSheetName;
	}

	public int getTotalCol() {
		return totalCol;
	}

	public void setTotalCol(int totalCol) {
		this.totalCol = totalCol;
	}

	public List<AssignedCell> getAssignedCells() {
		return assignedCells;
	}

	public void setAssignedCells(List<AssignedCell> assignedCells) {
		this.assignedCells = assignedCells;
	}

	public AssignedCell getDataRow() {
		return dataRow;
	}

	public void setDataRow(AssignedCell dataRow) {
		this.dataRow = dataRow;
	}

	public AssignedCell getHighLightRow() {
		return highLightRow;
	}

	public void setHighLightRow(AssignedCell highLightRow) {
		this.highLightRow = highLightRow;
	}

	public int getDataRowSpan() {
		return dataRowSpan;
	}

	public void setDataRowSpan(int dataRowSpan) {
		this.dataRowSpan = dataRowSpan;
	}

	public List<AssignedCell[]> getData() {
		return data;
	}

	public void setData(List<AssignedCell[]> data) {
		this.data = data;
	}

	public boolean isNeedCopyTemplateRow() {
		return needCopyTemplateRow;
	}

	public void setNeedCopyTemplateRow(boolean needCopyTemplateRow) {
		this.needCopyTemplateRow = needCopyTemplateRow;
	}
	
	public boolean isAutoHeight() {
		return autoHeight;
	}

	public void setAutoHeight(boolean autoHeight) {
		this.autoHeight = autoHeight;
	}
	
	public String getAppendToSheet() {
		return AppendToSheet;
	}
	
	public void setAppendToSheet(String appendToSheet) {
		AppendToSheet = appendToSheet;
	}

}
