package com.abc.example.common.impexp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.abc.example.common.utils.Utility;


/**
 * @className		: ExcelImportHandler
 * @description	: Excel文件导入处理类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class ExcelImportHandler implements ImportHandler {
	
	// 开始列号，0-based
	private Integer firstColumnIdx = -1;
	
	// 结束列号+1，0-based
	private Integer lastColumnIdx = -1;
	
	// 标题行的列数
	private Integer columnCount = 0;
	
	// 文件类型，0-xls；1-xlsx
	private Integer xlsFileType = 0;
	
	// 当前sheet索引号
	private Integer sheetIdx = 0;
	
	// 错误处理策略标志值：0-继续处理，1-立即中止处理
	private Integer errorProcFlag = 0;
	
	// 标题行行数
	private Integer titleRowCount = 1;
	
	// 当前行索引号
	private Integer curRowIdx = -1;
	
	// 数据行开始索引号
	private Integer stDataRowIdx = -1;	
	
	// 异常日志列表
	private List<String> errorLogList = new ArrayList<String>();
	
	// Excel工作薄
	private Workbook workbook = null;
	
	// Excel公式计算器
	FormulaEvaluator formulaEval = null;
	
	// Excel数据格式化对象
	DataFormatter formatter = new DataFormatter();
	
	// ===============================================================
	// 内部属性值的获取
	
	// firstColumnIdx的getter方法
	public Integer getFirstColumnIdx() {
		return this.firstColumnIdx;
	}
	
	// lastColumnIdx的getter方法
	public Integer getLastColumnIdx() {
		return this.lastColumnIdx;
	}

	// columnCount的getter方法
	public Integer getColumnCount() {
		return this.columnCount;
	}
	
	// xlsFileType的getter方法
	public Integer getXlsFileType() {
		return this.xlsFileType;
	}	

	// sheetIdx的getter方法
	public Integer getSheetIdx() {
		return this.sheetIdx;
	}
	
	// titleRowCount的getter方法
	public Integer getTitleRowCount() {
		return this.titleRowCount;
	}

	// curRowIdx的getter方法
	public Integer getCurRowIdx() {
		return this.curRowIdx;
	}
	
	// stDataRowIdx的getter方法
	public Integer getStDataRowIdx() {
		return this.stDataRowIdx;
	}	
	
	// ===============================================================
	// 接口实现

	// ===============================================================
	// 一次性读取数据
	
	/**
	 * 
	 * @methodName		: importFile
	 * @description	: 导入文件，返回包含标题行的全部数据
	 * 	一次性读取全部数据，内部实现打开文件、读取数据、关闭文件的全部过程。
	 *  优点：调用简单。
	 *  缺点：针对关键数据列缺失，有无效读取的情况，不能适用大数据量场景。
	 * @param in		: 输入文件流，由外部负责close
	 * @param fileName	: 包含后缀的文件名
	 * @param titleRowNums: 标题行行数，0表示无标题行，1为常规情况，
	 * 	超过1行，为汇总分项情况,下同
	 * @param params	: 附加可变参数，个数不定，每个参数含义由实现类约定，下同
	 * @return			: String[]类型的列表
	 *  其中第一行的String[0]为列开始索引号，0-based
	 *  后面行的String[0]为行索引号，索引号1-based，String[]长度为实际数据列数+1
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String[]> importFile(InputStream in,String fileName,Integer titleRowNums,
			Object...params) throws Exception{

        // 创建Excel工作薄
    	openFile(in, fileName);

		try {
			// 返回的数据行列表
			List<String[]> dataRowList = null;			
			        	
        	// 创建公式计算器
        	this.formulaEval = this.workbook.getCreationHelper().createFormulaEvaluator();
	        
        	// 设置标题行数
        	this.titleRowCount = titleRowNums;
        	
	        // 取得参数
	        // Sheet页索引号
        	this.sheetIdx = (Integer)params[0];
	        
	        // 导入数据
	        dataRowList = loadAllData(this.sheetIdx);
	        
			return dataRowList;			
		}catch(Exception e) {
			// 增加异常日志
			addErrorLog(e.getMessage());			
			throw e;
		}finally {
			// 关闭文件流
			Utility.closeStream(this.workbook);
			this.workbook = null;
		}		
	}
	
	/**
	 * 
	 * @methodName		: openFile
	 * @description	: 打开并检查文件格式
	 * @param in		: 输入文件流，由外部负责最后关闭
	 * @param fileName	: 包含后缀的文件名
	 * @return			: 成功返回true，文件格式错误返回false
	 * @throws Exception：文件格式异常时，允许抛出异常
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public boolean openFile(InputStream in,String fileName) throws Exception{
		try {
			// Excel工作薄
			this.workbook = null;			
			// 复位
			reset();
			
	        String fileType = fileName.substring(fileName.lastIndexOf("."));
            if(".xls".equals(fileType)){
            	this.workbook = new HSSFWorkbook(in);
            	this.xlsFileType = 0;
            }else if(".xlsx".equals(fileType)){
            	this.workbook = new XSSFWorkbook(in);
            	this.xlsFileType = 1;
            }else {
            	throw new Exception("文件格式错误，文件后缀须为xls/xlsx");
            }	
            
        	// 创建公式计算器
        	this.formulaEval = this.workbook.getCreationHelper().createFormulaEvaluator();
            
		}catch(Exception e) {
			// 增加异常日志
			addErrorLog(e.getMessage());			
			throw e;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @methodName		: readTitles
	 * @description	: 读取标题行，如果标题行行数为0，也必须返回一行数据
	 * @param titleRowNums: 标题行行数
	 * @param params	: 附加可变参数，个数不定，每个参数含义由实现类约定
	 * @return			: String[]类型的列表
	 *  其中第一行的String[0]为列开始索引号，0-based
	 *  后面行的String[0]为行索引号，索引号1-based，String[]长度为实际数据列数+1
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String[]> readTitles(Integer titleRowNums,Object...params) throws Exception{		
		try {
			// 返回的数据行列表
			List<String[]> dataRowList = new ArrayList<String[]>();
	    	
			if (this.workbook == null) {
				// 工作薄未打开
				throw new Exception("workbook not opend!");
			}
			
	    	// 设置标题行数
	    	this.titleRowCount = titleRowNums;

	        // 取得参数
	        // Sheet页索引号
        	this.sheetIdx = (Integer)params[0];
	    	
            Sheet sheet = this.workbook.getSheetAt(this.sheetIdx);
            
            // 第一行数据的标记
            boolean bFirstRow = false;
            Integer titleRow = 0;
            // 读取数据块
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            	try {
                	// 取得一行数据
                    Row row = sheet.getRow(i);  
                    
                    // 获取数据
                    String[] rowData = null;
                    if (bFirstRow == true) {
                    	// 如果第一行已读取
                    	rowData = readRowData(row,false);
                    }else {
                    	// 如果第一行未读取
                    	rowData = readRowData(row,true);
                    	if (rowData != null) {
                    		// 有效标题行
                        	bFirstRow = true;
                    	}
                    }

                	// 设置当前行位置
                	this.curRowIdx = i;
                    
                    // 加入列表
                    if (rowData != null) {
                    	// 如果为有效数据行，则加入列表中
                    	dataRowList.add(rowData); 
                    	
                    	titleRow++;
                    	if (titleRow >= this.titleRowCount) {
                    		// 如果标题行已读取完毕
                    		// 设置数据行的开始行索引号
                    		if (this.titleRowCount > 0) {
                        		this.stDataRowIdx = this.curRowIdx + 1;                			
                    		}else {
                    			this.stDataRowIdx = this.curRowIdx;
                    		}
                    		break;
                    	}
                    }            		
            	}catch(Exception e) {        			
            		procError(e);            		
            	}
            }        
            return dataRowList;
		}catch(Exception e) {
			// 增加异常日志
			addErrorLog(e.getMessage());
			throw e;
		}    	
	}
	
	/**
	 * 
	 * @methodName		: readDatas
	 * @description	: 读取数据部分（非标题行）的数据
	 * @param titleRowNums	: 标题行行数
	 * @param startRow	: 开始行号，0-based
	 * @param recCount	: 读取的最大记录数，-1表示直到最后一条记录
	 * @param params	: 附加可变参数，个数不定，每个参数含义由实现类约定
	 * @return			: String[]类型的列表，String[0]为行索引号，索引号1-based
	 * 					  String[]长度为实际数据列数+1
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String[]> readDatas(Integer startRow,Integer recCount,
			Object...params) throws Exception{
		// 返回的数据行列表
		List<String[]> dataRowList = new ArrayList<String[]>();
		
		try {
			if (this.workbook == null) {
				// 工作薄未打开
				throw new Exception("workbook not opend!");
			}
			
	        // 取得参数
	        // Sheet页索引号
        	this.sheetIdx = (Integer)params[0];
	    	
            Sheet sheet = this.workbook.getSheetAt(this.sheetIdx);
            
            // 读取数据
            Integer dataRowNum = 0;
            for (int i = this.stDataRowIdx + startRow; i <= sheet.getLastRowNum(); i++) {
            	try {
                	// 取得一行数据
                    Row row = sheet.getRow(i);  
                    
                    // 获取数据
                    String[] rowData = readRowData(row,false);

                	// 设置当前行位置
                	this.curRowIdx = i;
                    
                    // 加入列表
                    if (rowData != null) {
                    	// 如果为有效数据行，则加入列表中
                    	dataRowList.add(rowData); 
                    	
                    	dataRowNum++;
                    	if (recCount != ImpExpConstants.READ_TO_TAIL && dataRowNum >= recCount) {
                    		// 如果不是需要全部读完且需要读取的最大行数已读取完毕
                    		break;
                    	}
                    }            		
            	}catch(Exception e) {        			
            		procError(e);           		
            	}
            }        
            return dataRowList;        		    	
		}catch(Exception e) {
			// 增加异常日志
			addErrorLog(e.getMessage());			
			throw e;
		} 		
	}
	
	/**
	 * 
	 * @methodName		: readNextDatas
	 * @description	: 从当前位置，读取下一部分数据
	 * @param recCount	: 读取的最大记录数，-1表示直到最后一条记录
	 * @param params	: 附加可变参数，个数不定，每个参数含义由实现类约定
	 * @return			: String[]类型的列表，String[0]为行索引号，索引号1-based
	 * 					  String[]长度为实际数据列数+1
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String[]> readNextDatas(Integer recCount,Object...params) throws Exception{
		// 返回的数据行列表
		List<String[]> dataRowList = new ArrayList<String[]>();
		
		try {
			if (this.workbook == null) {
				// 工作薄未打开
				throw new Exception("workbook not opend!");
			}
			
	        // 取得参数
	        // Sheet页索引号
        	this.sheetIdx = (Integer)params[0];
	    	
            Sheet sheet = this.workbook.getSheetAt(this.sheetIdx);
            
            // 当前开始读取的位置
            Integer newRowIdx = this.curRowIdx + 1;
            // 读取数据
            Integer dataRowNum = 0;            
            for (int i = newRowIdx; i <= sheet.getLastRowNum(); i++) {
            	try {
                	// 取得一行数据
                    Row row = sheet.getRow(i);  
                    
                    // 获取数据
                    String[] rowData = readRowData(row,false);

                	// 设置当前行位置
                	this.curRowIdx = i;
                    
                    // 加入列表
                    if (rowData != null) {
                    	// 如果为有效数据行，则加入列表中
                    	dataRowList.add(rowData); 
                    	
                    	dataRowNum++;
                    	if (recCount != ImpExpConstants.READ_TO_TAIL && dataRowNum >= recCount) {
                    		// 如果不是需要全部读完且需要读取的最大行数已读取完毕
                    		break;
                    	}
                    }            		
            	}catch(Exception e) {        			
            		procError(e);            		
            	}
            }        
            return dataRowList;        		    	
		}catch(Exception e) {
			// 增加异常日志
			addErrorLog(e.getMessage());
			throw e;
		} 				
	}
	
	/**
	 * 
	 * @methodName		: closeFile
	 * @description	: 关闭文件
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void closeFile() throws Exception{
		if (this.workbook != null) {
			Utility.closeStream(this.workbook);
			this.workbook = null;
		}
	}
	
	// ===============================================================
	// 读取文件错误日志相关接口
	
	/**
	 * 
	 * @methodName		: getErrorLog
	 * @description	: 获取错误信息列表，外部应及时获取
	 * @return			: 错误信息列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String> getErrorLog(){
		return this.errorLogList;
	}
		
	// ===============================================================
	// 异常发生时的处理策略，默认为继续处理
	// 0-继续处理，1-立即中止处理
	
	/**
	 * 
	 * @methodName		: getErrorProcFalg
	 * @description	: 获取错误处理策略标志值
	 * @return			: 0-继续处理，1-立即中止处理
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Integer getErrorProcFalg() {
		return this.errorProcFlag;
	}
	
	/**
	 * 
	 * @methodName		: setErrorProcFalg
	 * @description	: 设置错误处理策略标志值
	 * @param errorFlag	: 0-继续处理，1-立即中止处理
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setErrorProcFalg(Integer errorFlag) {
		this.errorProcFlag = errorFlag;
	}
		
	/**
	 * 
	 * @methodName		: readRowData
	 * @description	: 读取一行数据
	 * @param row		: Row类型对象
	 * @param firstRow	: 是否为第一行有效行
	 * @return			: 返回该行数据的字符串数组值，如果为空行，返回null
	 * 	字符串数组值长度为数据列数+1，第一行首个元素为起始列索引号，其它为行索引号
	 * 	如果标题行行数为0，则数组数据字段部分都为空串。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private String[] readRowData(Row row,boolean firstRow) {
		String[] rowData = null;
		if(row == null) {
			return null;
		}
		
		if (firstRow) {
			// 如果为第一行
			// 数据区域第一行，决定了后续数据列的读取范围
            // getFirstCellNum，数据区域的第一列的下标，0-based
            // getLastCellNum，数据区域的最后列的下标+1，0-based
			
			this.firstColumnIdx = Integer.valueOf(row.getFirstCellNum());
			this.lastColumnIdx = Integer.valueOf(row.getLastCellNum());
        	// 设置数据区域的列数
			this.columnCount = this.lastColumnIdx - this.firstColumnIdx;			
		}
		
		// 此时firstColumnIdx值有效
		rowData = new String[this.columnCount+1];
		
		if(firstRow && this.titleRowCount == 0) {
			// 如果为第一行，且无标题行
        	// 取得起始列索引号
        	int colIdx = row.getCell(this.firstColumnIdx).getColumnIndex();
        	// 设置起始列索引号
        	rowData[0] = String.valueOf(colIdx); 

        	// 后面均填空串
        	for(int i = 1; i < rowData.length; i++) {
        		rowData[i] = "";
        	}
        	return rowData;
		}
		
		
		// 获取单元格数据
        for (int j = this.firstColumnIdx; j < this.lastColumnIdx; j++) {
        	// 读取当前行的各个单元格的值
            Cell cell = row.getCell(j);
            if (cell == null) {
            	// 如果单元格为空，置为空串
            	rowData[j - this.firstColumnIdx + 1] = "";
            	continue;
            }
            
            // 读取单元格
            if (cell.getCellType() == CellType.FORMULA) {
            	// 如果为公式，取得计算值
            	rowData[j - this.firstColumnIdx + 1] = this.formulaEval.evaluate(cell).formatAsString();
            }else {
            	rowData[j - this.firstColumnIdx + 1] = this.formatter.formatCellValue(cell);
            }                                
        }							
        
        if (firstRow) {
        	// 如果为第一行
        	// 取得起始列索引号
        	int colIdx = row.getCell(this.firstColumnIdx).getColumnIndex();
        	// 设置起始列索引号
        	rowData[0] = String.valueOf(colIdx); 
        }else {
        	// 如果为非第一行
    		// 如果数据部分全为空，则丢弃
            if (isAllEmpty(rowData) == true) {
            	return null;            	
            }	
            
    		// 取得当前行号，1-based
    		int rowIdx = row.getRowNum() + 1;
    		// 设置行号
    		rowData[0] = String.valueOf(rowIdx);     		    		
        }
        
		return rowData;
	}
			
    /**
     * 
     * @methodName		: loadAllData
     * @description	: 读取excel的sheet页的所有数据，将数据存入字符串数组的列表中
     * @param sheetIndex	: sheet页的索引，0-based 
     * @return			: 字符串数组的列表
     * @throws Exception: 异常发生时，抛出
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private List<String[]> loadAllData(int sheetIndex) throws Exception {
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        List<String[]> data = new ArrayList<>();
        
        // 第一行数据的标记
        boolean bFirstRow = false;
        // 读取数据块
        // getFirstRowNum，数据区域第一行的下标，0-based
        // getLastRowNum，数据区域的最后一行的下标，0-based
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
        	try {
            	// 取得一行数据
                Row row = sheet.getRow(i);  
                
                // 获取数据
                String[] rowData = null;
                if (bFirstRow == false) {
                	// 如果第一行未读取
                	rowData = readRowData(row,true);
                	if (rowData != null) {
                		// 有效标题行
                    	bFirstRow = true;
                        if (this.titleRowCount == 0){
                        	// 如果无标题行
                            // 加入列表
                        	data.add(rowData);
                        	// 然后重新读取一次数据行
                        	rowData = readRowData(row,false);
                        }                	
                	}
                }else {
                	// 如果第一行已读取
                	rowData = readRowData(row,false);
                }
                
                // 加入列表
                if (rowData != null) {
                	// 如果为有效数据行，则加入列表中
                	data.add(rowData); 
                }        		
        	}catch(Exception e) {   
        		procError(e);
        	}
        }        
        return data;
    } 	
	
    /**
     * 
     * @methodName		: procError
     * @description	: 异常发生时的处理
     * @param e			: Exception类型对象
     * @throws Exception
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private void procError(Exception e) throws Exception{
		// 如果异常发生时，立即中止
		if (this.errorProcFlag == ImpExpConstants.STOP_ON_ERROR) {
			// 此处不必增加异常日志，由外层负责
			throw e;
		}else {
			// 增加异常日志
			addErrorLog(e.getMessage());        				
		}            		    	
    }
    
    /**
     * 
     * @methodName		: reset
     * @description	: 复位属性值
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private void reset() {
    	this.firstColumnIdx = -1;
    	this.lastColumnIdx = -1;
    	this.columnCount = 0; 
    	this.curRowIdx = -1;
    	this.titleRowCount = 0;
    	this.sheetIdx = 0;
    	this.stDataRowIdx = -1;
    	this.errorLogList.clear();
    }
    
    /**
     * 
     * @methodName		: isAllEmpty
     * @description	: 检查当前行的每个单元格都为空
     * @param arrRow	: 数据行内容
     * @return			: 如果都为空，返回true，否则为false
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private boolean isAllEmpty(String[] arrRow) {
    	boolean bRet = true;
    	// 第一个成员为行号或列号，不考虑
    	for (int i = 1; i < arrRow.length; i++) {
    		if (!arrRow[i].isEmpty()) {
    			bRet = false;
    			break;
    		}
    	}
    	return bRet;
    }	    
	
	/**
	 * 
	 * @methodName		: addErrorLog
	 * @description	: 向错误日志列表加入一条错误信息
	 * @param error		: 错误信息
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void addErrorLog(String error) {
		this.errorLogList.add(error);
	}
    
}
