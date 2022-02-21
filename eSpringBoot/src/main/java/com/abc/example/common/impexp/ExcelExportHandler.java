package com.abc.example.common.impexp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * @className		: ExcelExportHandler
 * @description	: Excel导出处理类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class ExcelExportHandler {
	
	/**
	 * 
	 * @methodName		: exportExcelFile
	 * @description	: 导出Excel文件供下载
	 * @param titleList	: 导出的标题数组
	 * @param rowDataList	: 导出的字符串数组的列表
	 * @param excelFilePath	: Excel文件临时文件路径
	 * @param sheetName: sheet页名称
	 * @throws Exception	: 发生异常时，抛出
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void exportExcelFile(String[] titleList,List<String[]> rowDataList,
			String excelFilePath,String sheetName) 
			throws Exception {
		if (rowDataList.size() == 0) {
			// 无数据
			throw new Exception("无导出数据.");
		}
		
		XSSFWorkbook wb = null;
		wb = new XSSFWorkbook();

		// 创建sheet页
		XSSFSheet sheet = wb.createSheet(sheetName);
		
		// 创建标题行
		createTitle(wb,sheet,titleList);
		
		// 添加数据
		addSheetContent(sheet,wb,rowDataList);
		
		// 输出文件数据供下载
		exportExcelFile(excelFilePath,wb);
		
	}	
	
	/**
	 * 
	 * @methodName		: createTitle
	 * @description	: 设置标题行
	 * @param workbook	: workbook对象
	 * @param sheet		: sheet对象
	 * @throws Exception	: 发生异常时，抛出
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void createTitle(XSSFWorkbook workbook, XSSFSheet sheet,String[] arrTitles) throws Exception{
	    XSSFRow row = sheet.createRow(0);

	    // 设置列宽
	    for (int i = 0; i < arrTitles.length; i++){
	    	// 设置固定宽度，setColumnWidth的第二个参数的单位是1/256个字节宽度
	        sheet.setColumnWidth(i,arrTitles[i].getBytes("UTF-8").length * 256);
	    	
	    	// 设置自适应宽度，性能不高，故不用了
	    	//sheet.autoSizeColumn(i);
		}
	    
	    // 设置为居中加粗
	    XSSFCellStyle style = workbook.createCellStyle();
	    XSSFFont font = workbook.createFont();
	    font.setBold(true);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(font);

	    XSSFCell cell;
		for (int i = 0; i < arrTitles.length; i++){
			cell = row.createCell(i);
			cell.setCellValue(arrTitles[i]);
			cell.setCellStyle(style);
		}
				
	}
	
	/**
	 * 
	 * @methodName		: addSheetContent
	 * @description	: 为sheet对象添加数据行内容
	 * @param sheet		: sheet对象
	 * @param workbook	: workbook对象
	 * @param rowDataList	: 数据行列表
	 * @throws Exception	: 发生异常时，抛出
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void addSheetContent(XSSFSheet sheet, XSSFWorkbook workbook, List<String[]> rowDataList)
			 throws Exception
	{
		// 单元格居中
	    XSSFCellStyle style = workbook.createCellStyle();
	    style.setAlignment(HorizontalAlignment.CENTER);
	    
	    // 数据行下标从1开始
	    int rowNum=1;
	    // 遍历导出数据行
	    for(int i=0;i<rowDataList.size();i++){
	        XSSFRow row = sheet.createRow(rowNum);
	        XSSFCell cell;
			String[] arrRow = rowDataList.get(i);
			for (int j = 0; j < arrRow.length; j++) {
				cell = row.createCell(j);
				cell.setCellValue(arrRow[j]);
				cell.setCellStyle(style);
			}
	        rowNum++;
	    }
	}	
	
	/**
	 * 
	 * @methodName		: exportExcelFile
	 * @description	: 输出Excel文件
	 * @param filePath	: 输出的文件路径
	 * @param workbook	: workbook对象
	 * @throws Exception	: 发生异常时，抛出
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void exportExcelFile(String filePath,XSSFWorkbook workbook) throws Exception{
	    OutputStream outputStream = new FileOutputStream(new File(filePath));
	    workbook.write(outputStream);
	    outputStream.flush();
	    outputStream.close();
	}
}
