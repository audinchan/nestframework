/**
 * 
 */
package ${hss_base_package}.service.ext.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.poi2.hssf.usermodel.HSSFCell;
import org.apache.poi2.hssf.usermodel.HSSFRow;
import org.apache.poi2.hssf.usermodel.HSSFSheet;
import org.apache.poi2.hssf.usermodel.HSSFWorkbook;
import org.apache.poi2.hssf.util.Region;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ${hss_base_package}.dto.AssignedCell;
import ${hss_base_package}.dto.AssignedSheet;
import ${hss_base_package}.dto.ExportExcelParameter;
import ${hss_base_package}.exception.ManagerException;
import ${hss_base_package}.service.ext.IExportSupport;

/**
 * @author austin
 * 
 */
public class ExcelExportSupportImpl implements IExportSupport {

	/**
	 * �������ݣ�֧�ֶ��sheet�ĵ����ļ�
	 * @param para
	 * @param os
	 * @param session
	 * @param sheetMap
	 */
	public void export(ExportExcelParameter para,OutputStream os,HttpSession session){
		String templateFilePath = null;
		FileInputStream fis = null;
		try {
			// ��ģ���ļ�
			// ģ���ļ��ľ���·��
			templateFilePath = getAbsolutePath(session.getServletContext(), para.getTemplateName());
			
			fis = new FileInputStream(new File(templateFilePath));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			List<String> sheetNames=new ArrayList<String>();
			//����sheet��������ģ��ҳ�е�������
			for(AssignedSheet aSheet:para.getSheets()){
				//
				HSSFSheet sheet = wb.cloneSheet(wb.getSheetIndex(aSheet.getTemplateSheetName()));
				if(!aSheet.getTemplateSheetName().equals(aSheet.getSheetName())){
						wb.setSheetName(wb.getNumberOfSheets()-1, aSheet.getSheetName(),HSSFCell.ENCODING_UTF_16);
				}
				sheetNames.add(wb.getSheetName(wb.getNumberOfSheets()-1));
				
				//��ʼ����
				int rowNumber=aSheet.getDataRow().getRow();
				HSSFRow datarow = sheet.getRow(rowNumber);
				HSSFRow currRow = null;
				HSSFRow hlDataRow=null;
				if(aSheet.getHighLightRow()!=null)
					hlDataRow=sheet.getRow(aSheet.getHighLightRow().getRow());
				if(hlDataRow==null)	
					hlDataRow=sheet.getRow(rowNumber);
				//�������
				List<AssignedCell[]> data=aSheet.getData();
				//������������ʾ�Ĵ���
				for(AssignedCell[] rowData:data){
					//������ø����е�ģʽ�������copyRows���Ƴ���Ҫ�������У����򣬴������У�����ʼ��ÿ������
					if(aSheet.isNeedCopyTemplateRow()){
						copyRows(sheet, datarow.getRowNum(), datarow.getRowNum()+aSheet.getDataRowSpan()-1, rowNumber);
					}else{
						//�������У��������ж�������������ʹ����ʽ����
						for(int i=0;i<aSheet.getDataRowSpan();i++){
							currRow = sheet.createRow(rowNumber+i);
							//�����и�
							currRow.setHeight(datarow.getHeight());
							//�������е���
							for(int j=0;j<aSheet.getTotalCol();j++){
								HSSFCell cell=currRow.createCell((short)j);
			 					cell.setCellStyle(datarow.getCell((short)j).getCellStyle());
							}
						}
					}
					
					//��������������������
					for(int k=0;k<rowData.length;k++){
						AssignedCell acell=rowData[k];
						//�������Ժϲ���Ԫ��
						sheet.addMergedRegion(new Region(rowNumber+acell.getRow(),(short)acell.getCol(),rowNumber+acell.getRowEnd(),(short)acell.getColEnd()));
						
						HSSFCell cell=sheet.getRow(rowNumber+acell.getRow()).getCell((short)acell.getCol());
						//������������
						int cType=HSSFCell.CELL_TYPE_STRING;
						
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						Object value=acell.getValue();
						if (value == null) {
							cell.setCellValue("");
						}else{
							if(value instanceof Integer || value instanceof Double){
								cType=HSSFCell.CELL_TYPE_NUMERIC;
								try{
									//cell.setCellType(cType);
									cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
								}catch(Exception e){
									cell.setCellType(HSSFCell.CELL_TYPE_STRING);
									cell.setCellValue(value.toString());
								}
							}else{
								cType=HSSFCell.CELL_TYPE_STRING;
								try{
									cell.setCellType(cType);
								}catch(Exception e){
								}
								cell.setCellValue(value.toString());
							}
						}
						//ʹ����ʽ
						if(acell.getUseStyle()==0){
							//ʹ��Ĭ����ʽ
							cell.setCellStyle(datarow.getCell((short)acell.getCol()).getCellStyle());
						}else if(acell.getUseStyle()==1){
							//ʹ��������ʽ
							cell.setCellStyle(hlDataRow.getCell((short)aSheet.getHighLightRow().getCol()).getCellStyle());
						}else if(acell.getUseStyle()==2){
							//ʹ�ø�����ʽ
							cell.setCellStyle(hlDataRow.getCell((short)acell.getCol()).getCellStyle());
						}
						
					}
					rowNumber+=aSheet.getDataRowSpan();
				}
				//���ָ��λ�õ�ֵ
				if(aSheet.getAssignedCells()!=null && aSheet.getAssignedCells().size()>0){
					for(AssignedCell cell:aSheet.getAssignedCells()){
						if(cell.getValue()==null)
							continue;
						if(cell.getRow()>sheet.getLastRowNum()){
							sheet.createRow(cell.getRow());
							for(int j=0;j<aSheet.getTotalCol();j++){
								sheet.getRow(cell.getRow()).createCell((short)j);
							}
						}
						
						if(sheet.getRow(cell.getRow())==null){
							sheet.createRow(cell.getRow());
						}
						HSSFCell assignCell=sheet.getRow(cell.getRow()).getCell((short)cell.getCol());
						if(assignCell==null){
							assignCell=sheet.getRow(cell.getRow()).createCell((short)cell.getCol());
						}
						assignCell.setEncoding(HSSFCell.ENCODING_UTF_16);
						assignCell.setCellValue(cell.getValue().toString());
					}
				}
			}
			//ɾ���ļ��г������������ݵ�sheetҳ�������sheet������ģ��sheet�Ϳհ�sheet
			int i=0;
			while(i<wb.getNumberOfSheets()){
				String sheetName=wb.getSheetName(i);
				if(sheetNames.contains(sheetName)){
					i++;
					continue;
				}else{
					wb.removeSheetAt(i);
				}
			}
			wb.write(os);
		}catch(Exception e){
			throw new ManagerException("��������", e);
		}
	}
	
	/**
	 * ��sheetҳ�ĵ�������
	 */
	public void export(ExportExcelParameter para,OutputStream os,HttpSession session, List<AssignedCell[]> data){
		String templateFilePath = null;
		FileInputStream fis = null;
		try {
			// ��ģ���ļ�
			// ģ���ļ��ľ���·��
			templateFilePath = getAbsolutePath(session.getServletContext(), para.getTemplateName());
			fis = new FileInputStream(new File(templateFilePath));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet sheet = wb.getSheetAt(0);	
			//��ʼ����
			int rowNumber=para.getDataRow().getRow();
			HSSFRow datarow = sheet.getRow(rowNumber);
			
			HSSFRow hldatarow = null;
			if(para.getHighLightRow()!=null){
				sheet.getRow(para.getHighLightRow().getRow());
			}else{
				hldatarow=datarow;
			}
				
			HSSFRow currRow = null;
			
			//�������
			for(AssignedCell[] rowData:data){
				//�������У��������ж�������������ʹ����ʽ����
				for(int i=0;i<para.getDataRowSpan();i++){
					currRow = sheet.createRow(rowNumber+i);
					//�����и�
					currRow.setHeight(datarow.getHeight());
					//�������е���
					for(int j=0;j<para.getTotalCol();j++){
						HSSFCell cell=currRow.createCell((short)j);
	 					cell.setCellStyle(datarow.getCell((short)j).getCellStyle());
					}
				}
				//��������������������
				for(int k=0;k<rowData.length;k++){
					AssignedCell acell=rowData[k];
					//�������Ժϲ���Ԫ��
					sheet.addMergedRegion(new Region(rowNumber+acell.getRow(),(short)acell.getCol(),rowNumber+acell.getRowEnd(),(short)acell.getColEnd()));
					
					HSSFCell cell=sheet.getRow(rowNumber+acell.getRow()).getCell((short)acell.getCol());
					//������������
					int cType=HSSFCell.CELL_TYPE_STRING;
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					Object value=acell.getValue();
					if (value == null) {
						cell.setCellValue("");
					}else{
						if(value instanceof Integer || value instanceof Double){
							cType=HSSFCell.CELL_TYPE_NUMERIC;
							try{
								//cell.setCellType(cType);
								cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
							}catch(Exception e){
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(value.toString());
							}
						}else{
							cType=HSSFCell.CELL_TYPE_STRING;
							cell.setCellType(cType);
							cell.setCellValue(value.toString());
						}
					}
					//ʹ����ʽ
					if(acell.getUseStyle()==0){
						//ʹ��Ĭ����ʽ
						cell.setCellStyle(datarow.getCell((short)acell.getCol()).getCellStyle());
					}else if(acell.getUseStyle()==1){
						//ʹ��������ʽ
						cell.setCellStyle(hldatarow.getCell((short)para.getHighLightRow().getCol()).getCellStyle());
					}else if(acell.getUseStyle()==2){
						//ʹ�ø�����ʽ
						cell.setCellStyle(hldatarow.getCell((short)acell.getCol()).getCellStyle());
					}
				}
				rowNumber+=para.getDataRowSpan();
			}
			//���ָ��λ�õ�ֵ
			if(para.getAssignedCells()!=null && para.getAssignedCells().size()>0){
				for(AssignedCell cell:para.getAssignedCells()){
					if(cell.getValue()==null)
						continue;
					if(cell.getRow()>sheet.getLastRowNum()){
						sheet.createRow(cell.getRow());
						for(int j=0;j<para.getTotalCol();j++){
							currRow.createCell((short)j);
						}
					}
					HSSFCell assignCell=sheet.getRow(cell.getRow()).getCell((short)cell.getCol());
					assignCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					assignCell.setCellValue(cell.getValue().toString());
				}
			}
			wb.write(os);
		}catch(Exception e){
			throw new ManagerException("��������", e);
		}
	}
	
	protected void copyRows(HSSFSheet sheet, int pStartRow, int pEndRow,
			int pPosition) {
		HSSFRow sourceRow = null;
		HSSFRow targetRow = null;
		HSSFCell sourceCell = null;
		HSSFCell targetCell = null;
		Region region = null;

		if ((pStartRow == -1) || (pEndRow == -1)) {
			return;
		}
		if(pStartRow==pPosition)
			return;
		// �����ϲ��ĵ�Ԫ��
		int numregions= sheet.getNumMergedRegions();
		for (int i = 0; i < numregions; i++) {
			region = sheet.getMergedRegionAt(i);
			if ((region.getRowFrom() >= pStartRow)
					&& (region.getRowTo() <= pEndRow)) {
				int targetRowFrom = region.getRowFrom() - pStartRow + pPosition;
				int targetRowTo = region.getRowTo() - pStartRow + pPosition;
				region.setRowFrom(targetRowFrom);
				region.setRowTo(targetRowTo);
				sheet.addMergedRegion(region);
			}
		}
		// �����в��������
		for (int i = pStartRow; i <= pEndRow; i++) {
			sourceRow = sheet.getRow(i);
			if (sourceRow == null) {
				continue;
			}
			targetRow = sheet.createRow(i - pStartRow + pPosition);
			targetRow.setHeight(sourceRow.getHeight());
			for (short j = sourceRow.getFirstCellNum(); j < sourceRow
					.getPhysicalNumberOfCells(); j++) {
				sourceCell = sourceRow.getCell(j);
				if (sourceCell == null) {
					continue;
				}
				targetCell = targetRow.createCell(j);
				targetCell.setEncoding(sourceCell.getEncoding());
				targetCell.setCellStyle(sourceCell.getCellStyle());
				int cType = sourceCell.getCellType();
//				targetCell.setCellType(cType);
				switch (cType) {
				case HSSFCell.CELL_TYPE_BOOLEAN:
					targetCell.setCellValue(sourceCell.getBooleanCellValue());
					// System.out.println("--------TYPE_BOOLEAN:" +
					// targetCell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					targetCell
							.setCellErrorValue(sourceCell.getErrorCellValue());
					// System.out.println("--------TYPE_ERROR:" +
					// targetCell.getErrorCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					targetCell.setCellFormula(sourceCell.getCellFormula());
					// System.out.println("--------TYPE_FORMULA:" +
					// targetCell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					targetCell.setCellValue(sourceCell.getNumericCellValue());
					// System.out.println("--------TYPE_NUMERIC:" +
					// targetCell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					targetCell.setCellValue(sourceCell.getStringCellValue());
					// System.out.println("--------TYPE_STRING:" +
					// targetCell.getStringCellValue());
					break;
				}
			}
		}
	}

	/**
	 * ���·��ת����·��
	 * @param sc   ServletContext.         
	 * @param fileRelativePath  �����webӦ�õĸ�Ŀ¼���ļ�·��(���ļ�ȫ��)
	 * @return  String.
	 * @throws IOException
	 */
	private String getAbsolutePath(
			ServletContext sc, String fileRelativePath) throws IOException
	{
		String ret = "";
		ApplicationContext ctx = WebApplicationContextUtils.
			getWebApplicationContext(sc);
		Resource resource = ctx.getResource(fileRelativePath);
		try
		{
			ret = resource.getFile().getAbsolutePath();
		}
		catch (IOException ioe)
		{
			throw ioe;
		}
		return ret;
	}
}
