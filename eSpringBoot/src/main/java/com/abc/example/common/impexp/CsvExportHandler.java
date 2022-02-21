package com.abc.example.common.impexp;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.abc.example.common.utils.LogUtil;

/**
 * @className		: CsvExportHandler
 * @description	: CSV文件导出处理类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class CsvExportHandler {

	/**
	 * 
	 * @methodName			: exportCsvFile
	 * @description		: 导出CSV文件
	 * @param titleList		: 导出的标题数组
	 * @param rowDataList	: 导出的字符串数组的列表
	 * @param csvFilePath	: 输出的CSV文件路径
	 * @throws Exception	: 异常发生时，抛出
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void exportCsvFile(String[] titleList,List<String[]> rowDataList,String csvFilePath) 
			throws Exception{
		
		if (rowDataList.size() == 0) {
			// 必须要有导出数据，否则创建标题列失败
			throw new Exception("无导出数据.");
		}	
		
		
		// 将数据写入csv格式文件
		writeToCsv(titleList,rowDataList,csvFilePath);
	}
	
	/**
	 * 
	 * @methodName		: writeToCsv
	 * @description	: 将数据写入csv格式文件
	 * @param titleList	: 导出的标题数组
	 * @param rowDataList: 导出的字符串数组的列表
	 * @param filePath	: 输出的文件路径
	 * @throws Exception: 异常发生时，抛出
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    private void writeToCsv(String[] titleList,List<String[]> rowDataList, String filePath) throws Exception {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String enter = "\r\n";
		String sLine = "";
        StringBuffer write ;
    	fos = new FileOutputStream(new File(filePath));
    	bos = new BufferedOutputStream(fos);
		// 标题行
    	sLine = csvFormatLineStr(titleList);
		write = new StringBuffer();
		write.append(sLine);
		// 加换行符
		write.append(enter);
		bos.write(write.toString().getBytes("UTF-8"));
        for (int i = 0; i < rowDataList.size(); i++) {
            write = new StringBuffer();
            String[] dataRow = rowDataList.get(i);
			// 输出CSV格式的数据行
        	sLine = csvFormatLineStr(dataRow);
			// 写数据行
			write.append(sLine);
            // 加换行符
            write.append(enter);
            bos.write(write.toString().getBytes("UTF-8"));
        }
        // 刷新数据
        bos.flush();
        
        // 关闭流
        closeStream(bos);
        closeStream(fos);
    }	
    
	/**
	 * 
	 * @methodName		: closeStream
	 * @description	: 关闭文件流
	 * @param in		: 可关闭的数据流对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    private void closeStream(Closeable in) {
		try {
			if(in != null) {
				in.close();
			}
		}catch(IOException e) {
			LogUtil.error(e);
		}
	}  
	
    /**
     * 
     * @methodName		: csvFormatLineStr
     * @description	: 将数据行转换为csv格式的字符串
     * @param dataRow	: 数据行
     * @return			: csv格式字符串
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private String csvFormatLineStr(String[] dataRow) {
    	String sLine = "";
    	for(int i = 0; i < dataRow.length; i++) {
    		if (i == 0) {
    			sLine = csvFormat(dataRow[i]);
    		}else {
    			sLine += "," + csvFormat(dataRow[i]);
    		}
    	} 
    	return sLine;
    }
    
    /**
     * 
     * @methodName		: csvFormat
     * @description	: 将输入字符串格式化成CSV格式的字符串
     * @param input		: 输入字符串
     * @return			: 符合CSV格式的字符串
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
	private String csvFormat(String input) {
		boolean bFound = false;
		// 如果值中含有逗号、换行符、制表符（Tab）、单引号，双引号，则需要用双引号括起来；
		// 如果值中包含双引号，则需要用两个双引号来替换。
		// 正则匹配：",'\"\r\n\t"
		bFound = input.matches("(.*)(,|'|\"|\r|\n|\t)(.*)");
		if (bFound) {
			// 如果存在匹配字符
			// 先将双引号替换为两个双引号
			String sTemp = input.replaceAll("\"", "\"\"");
			// 然后，两端使用"字符
			sTemp ="\"" + sTemp + "\"";
			return sTemp;
		}
		return input;
	}	
}
