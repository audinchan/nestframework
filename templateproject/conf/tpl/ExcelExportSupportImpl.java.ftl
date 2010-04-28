/**
 * 
 */
package ${hss_base_package}.service.ext.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.nestframework.commons.utils.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ${hss_base_package}.dto.AssignedCell;
import ${hss_base_package}.dto.AssignedSheet;
import ${hss_base_package}.dto.ExportExcelParameter;
import ${hss_base_package}.exception.ManagerException;
import ${hss_base_package}.service.ext.IExportSupport;

/**
 * @author wanghai
 * 
 */
public class ExcelExportSupportImpl implements IExportSupport {

	/**
	 * 导出数据，支持多个sheet的导出文件
	 * @param para
	 * @param os
	 * @param session
	 * @param sheetMap
	 */
	public void export(ExportExcelParameter para,OutputStream os,HttpSession session){
		String templateFilePath = null;
		FileInputStream fis = null;
		try {
			// 读模版文件
			// 模版文件的绝对路径
			templateFilePath = getAbsolutePath(session.getServletContext(), para.getTemplateName());
			
			fis = new FileInputStream(new File(templateFilePath));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			List<String> sheetNames=new ArrayList<String>();
			//创建sheet，并复制模板页中的所有行
			for(AssignedSheet aSheet:para.getSheets()){
				//
				HSSFSheet sheet = wb.cloneSheet(wb.getSheetIndex(aSheet.getTemplateSheetName()));
				if(!aSheet.getTemplateSheetName().equals(aSheet.getSheetName())){
						wb.setSheetName(wb.getNumberOfSheets()-1, aSheet.getSheetName());//,HSSFCell.ENCODING_UTF_16
				}
				sheetNames.add(wb.getSheetName(wb.getNumberOfSheets()-1));
				//开始处理
				int rowNumber=aSheet.getDataRow().getRow();
				HSSFRow datarow = sheet.getRow(rowNumber);
				HSSFRow hlDataRow=null;
				int hldatacol = 0;
				if(aSheet.getHighLightRow()!=null)
					hlDataRow=sheet.getRow(aSheet.getHighLightRow().getRow());
					hldatacol = aSheet.getHighLightRow().getCol();
				if(hlDataRow==null)	
					hlDataRow=sheet.getRow(rowNumber);
				//输出数据
				List<AssignedCell[]> data=aSheet.getData();
				
				//输出数据
				outputData(wb, sheet, datarow, hlDataRow, data, 
						aSheet.getAssignedCells(), aSheet.isNeedCopyTemplateRow(), aSheet.getDataRowSpan(), aSheet.getTotalCol(), hldatacol);
				
			}
			//删除文件中除了有所需数据的sheet页外的所有sheet，包括模板sheet和空白sheet
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
			throw new ManagerException("导出出错。", e);
		}
	}
	
	/**
	 * 单sheet页的导出处理
	 */
	public void export(ExportExcelParameter para,OutputStream os,HttpSession session, List<AssignedCell[]> data){
		String templateFilePath = null;
		FileInputStream fis = null;
		try {
			// 读模版文件
			// 模版文件的绝对路径
			templateFilePath = getAbsolutePath(session.getServletContext(), para.getTemplateName());
			fis = new FileInputStream(new File(templateFilePath));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet sheet = wb.getSheetAt(0);	
			//开始处理
			HSSFRow datarow = sheet.getRow(para.getDataRow().getRow());
			
			HSSFRow hldatarow = null;
			int hldatacol = 0;
			if(para.getHighLightRow()!=null){
				hldatarow=sheet.getRow(para.getHighLightRow().getRow());
				hldatacol = para.getHighLightRow().getCol();
			}else{
				hldatarow=datarow;
			}

			//输出数据
			outputData(wb, sheet, datarow, hldatarow, data, 
					para.getAssignedCells(), para.isNeedCopyTemplateRow(), para.getDataRowSpan(), para.getTotalCol(), hldatacol);
			
			wb.write(os);
		}catch(Exception e){
			throw new ManagerException("导出出错。", e);
		}
	}
	
	
	private void outputData(HSSFWorkbook wb,HSSFSheet sheet,HSSFRow datarow,HSSFRow hldatarow,
			List<AssignedCell[]> data,List<AssignedCell> assignedCells,
			boolean isNeedCopyTemplateRow,int dataRowSpan,int totalCol,int hldatacol)
	{
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		int rowNumber=datarow.getRowNum();
		
		HSSFRow currRow = null;
		
		//输出数据
		for(AssignedCell[] rowData:data){
			//如果是用复制行的模式，则调用copyRows复制出需要的内容行，否则，创建新行，并初始化每列数据
			if(isNeedCopyTemplateRow){
				copyRows(sheet, datarow.getRowNum(), datarow.getRowNum()+dataRowSpan-1, rowNumber, totalCol);
			}else{
				//创建多行，把所有列都创建出来，并使用样式处理
				for(int i=0;i<dataRowSpan;i++){
					currRow = sheet.createRow(rowNumber+i);
					//设置行高
					currRow.setHeight(datarow.getHeight());
					//创建所有的列
					for(int j=0;j<totalCol;j++){
						HSSFCell cell=currRow.createCell(j);
	 					cell.setCellStyle(datarow.getCell(j).getCellStyle());
					}
				}
			}
			//根据列总数处理所有列
			for(int k=0;k<rowData.length;k++){
				AssignedCell acell=rowData[k];
				if(acell==null)
					continue;
				//对特殊样式的处理
				if(acell.getUseStyle()==9){
					//写照片
					//处理照片
					HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short)acell.getCol() ,
							rowNumber+acell.getRow() ,
							(short)(acell.getColEnd()+1) , 
							rowNumber+acell.getRowEnd()+1);
				    anchor.setAnchorType( 2 );
				   //2008-09-19 修改批量报名的可以没有照片
				    if(StringUtil.isNotEmpty((String)acell.getValue())){
				    	patriarch.createPicture(
					    		anchor, loadPicture( (String)acell.getValue(), wb ));
				    }
					continue;
				}
				
				//根据属性合并单元格
				if(acell.getRow()!=acell.getRowEnd() || acell.getCol()!=acell.getColEnd())
					sheet.addMergedRegion(new CellRangeAddress(rowNumber+acell.getRow(),rowNumber+acell.getRowEnd(),acell.getCol(),acell.getColEnd()));
				
				HSSFCell cell=sheet.getRow(rowNumber+acell.getRow()).getCell(acell.getCol());
				//根据类型设置
				int cType=HSSFCell.CELL_TYPE_STRING;
				
				Object value=acell.getValue();
				if (value == null) {
					cell.setCellValue("");
				}else{
					if(value instanceof Integer || value instanceof Double){
						cType=HSSFCell.CELL_TYPE_NUMERIC;
						try{
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
				//使用样式
				if(acell.getUseStyle()==0){
					//使用默认样式
					cell.setCellStyle(datarow.getCell(acell.getCol()).getCellStyle());
				}else if(acell.getUseStyle()==1){
					//使用数据样式
					cell.setCellStyle(hldatarow.getCell(hldatacol).getCellStyle());
				}else if(acell.getUseStyle()==2){
					//使用高亮样式
					cell.setCellStyle(hldatarow.getCell(acell.getCol()).getCellStyle());
				}
			}
			rowNumber+=dataRowSpan;
		}
		//输出指定位置的值
		if(assignedCells!=null && assignedCells.size()>0){
			for(AssignedCell cell:assignedCells){
				if(cell.getValue()==null)
					continue;
				if(cell.getRow()>sheet.getLastRowNum()){
					currRow=sheet.createRow(cell.getRow());
					for(int j=0;j<totalCol;j++){
						currRow.createCell(j);
					}
				}
				HSSFCell assignCell=sheet.getRow(cell.getRow()).getCell(cell.getCol());
				assignCell.setCellValue(cell.getValue().toString());
			}
		}
	}
	private static int loadPicture(URL filePath, HSSFWorkbook wb) {   
        int result = 0;   
        try {   
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();   
            java.awt.image.BufferedImage bufferImg =  ImageIO.read(filePath);
            ImageIO.write(bufferImg,"jpg",byteArrayOut);   
            result = wb.addPicture(byteArrayOut.toByteArray(),org.apache.poi.hssf.usermodel.HSSFWorkbook.PICTURE_TYPE_JPEG);   
        }   
        catch (Exception e) {   
            e.printStackTrace();   
        }   
        return result;   
    }    
	
	private static int loadPicture(String filePath, HSSFWorkbook wb) {   
        int result = 0;   
        try {   
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();   
            File url = new File(filePath);
            java.awt.image.BufferedImage bufferImg =  ImageIO.read(url);
            ImageIO.write(bufferImg,"jpg",byteArrayOut);   
            result = wb.addPicture(byteArrayOut.toByteArray(),org.apache.poi.hssf.usermodel.HSSFWorkbook.PICTURE_TYPE_JPEG);   
        }   
        catch (Exception e) {   
            e.printStackTrace();   
        }   
        return result;   
    }    
	
	/**
	 * 复制sheet中的行数据
	 * @param sheet
	 * @param pStartRow
	 * @param pEndRow
	 * @param pPosition
	 */
	protected void copyRows(HSSFSheet sheet, int pStartRow, int pEndRow,
			int pPosition,int colTotal) {
		HSSFRow sourceRow = null;
		HSSFRow targetRow = null;
		HSSFCell sourceCell = null;
		HSSFCell targetCell = null;
		CellRangeAddress region = null;

		if ((pStartRow == -1) || (pEndRow == -1)) {
			return;
		}
		if(pStartRow==pPosition)
			return;
		// 拷贝合并的单元格
		int numregions= sheet.getNumMergedRegions();
		for (int i = 0; i < numregions; i++) {
			region = sheet.getMergedRegion(i);
			CellRangeAddress region2 = null;
			if ((region.getFirstRow() >= pStartRow)
					&& (region.getLastRow() <= pEndRow)) {
				int targetRowFrom = region.getFirstRow() - pStartRow + pPosition;
				int targetRowTo = region.getLastRow() - pStartRow + pPosition;
				region2 = new CellRangeAddress(targetRowFrom,targetRowTo,region.getFirstColumn(),region.getLastColumn());
				sheet.addMergedRegion(region2);
			}
		}
		// 拷贝行并填充数据
		for (int i = pStartRow; i <= pEndRow; i++) {
			sourceRow = sheet.getRow(i);
			if (sourceRow == null) {
				continue;
			}
			targetRow = sheet.createRow(i - pStartRow + pPosition);
			targetRow.setHeight(sourceRow.getHeight());
			for (int j = sourceRow.getFirstCellNum(); j < colTotal; j++) {
				sourceCell = sourceRow.getCell(j);
				if (sourceCell == null) {
					continue; 
				}
				targetCell = targetRow.createCell(j);
				targetCell.setCellStyle(sourceCell.getCellStyle());
				int cType = sourceCell.getCellType();
				switch (cType) {
				case HSSFCell.CELL_TYPE_BOOLEAN:
					targetCell.setCellValue(sourceCell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					targetCell
							.setCellErrorValue(sourceCell.getErrorCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					targetCell.setCellFormula(sourceCell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					targetCell.setCellValue(sourceCell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					targetCell.setCellValue(sourceCell.getStringCellValue());
					break;
				}
			}
		}
	}

	/**
	 * 相对路径转绝对路径
	 * @param sc   ServletContext.         
	 * @param fileRelativePath  相对于web应用的根目录的文件路径(含文件全名)
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
