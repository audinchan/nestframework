package ${hss_base_package}.dto;

/**
 * ָ��λ��
 * @author wanghai
 */
public class AssignedCell{
	private int row;
	private int col;
	private int rowEnd;
	private int colEnd;
	private Object value;
	/**
	 * ʹ����ʽ��Ĭ��Ϊ0��ʹ����ͨ���ݵ���ʽ
	 * 1.ʹ��ģ������Ӧλ�õ���ʽ
	 * 2.ʹ��ģ����������ʾ����ʽ
	 */
	private int useStyle = 0; 
	
	public AssignedCell(int row,int col,Object value){
		this.row=row;
		this.col=col;
		this.rowEnd=row;
		this.colEnd=col;
		this.value=value;
		this.useStyle=0;
	}
	public AssignedCell(int row,int col,int rowEnd,int colEnd,Object value){
		this.row=row;
		this.col=col;
		this.rowEnd=rowEnd;
		this.colEnd=colEnd;
		this.value=value;
		this.useStyle=0;
	}
	public AssignedCell(int row,int col,Object value, int useStyle){
		this.row=row;
		this.col=col;
		this.rowEnd=row;
		this.colEnd=col;
		this.value=value;
		this.useStyle=useStyle;
	}
	
	public AssignedCell(int row,int col,int rowEnd,int colEnd,Object value, int useStyle){
		this.row=row;
		this.col=col;
		this.rowEnd=rowEnd;
		this.colEnd=colEnd;
		this.value=value;
		this.useStyle=useStyle;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getRowEnd() {
		return rowEnd;
	}
	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}
	public int getColEnd() {
		return colEnd;
	}
	public void setColEnd(int colEnd) {
		this.colEnd = colEnd;
	}
	public int getUseStyle() {
		return useStyle;
	}
	public void setUseStyle(int useStyle) {
		this.useStyle = useStyle;
	}
	
}
