/**
 * 
 */
package ${hss_base_package}.service.ext;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import ${hss_base_package}.dto.AssignedCell;
import ${hss_base_package}.dto.ExportExcelParameter;


/**
 * ����֧�ֽӿ�.
 * 
 * @author wanghai
 * 
 */
public interface IExportSupport {
	/**
	 * ����ģ�������ļ�
	 * @param para
	 * @param os
	 * @param session
	 * @param data
	 */
	public void export(ExportExcelParameter para,OutputStream os,
			HttpSession session, List<AssignedCell[]> data);
	
	/**
	 * �������ݣ�֧�ֶ��sheet�ĵ����ļ�
	 * @param para
	 * @param os
	 * @param session
	 * @param sheetMap
	 */
	public void export(ExportExcelParameter para,OutputStream os,HttpSession session);
}
