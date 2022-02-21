package com.abc.example.common.impexp;

import java.io.InputStream;
import java.util.List;

/**
 * @className		: ImportHandler
 * @description	: 文件导入处理接口类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface ImportHandler {
	
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
	public List<String[]> importFile(InputStream in,String fileName,Integer titleRowNums,
			Object...params) throws Exception;	

	// ===============================================================
	// 分批读取数据，提供：打开文件、读取标题、读取数据、关闭文件的接口
	// 优点：适用大数据量场景，如果关键数据列缺失，可以中止读取数据
	// 缺点：调用复杂
	// 调用方式：先openFile，然后readTitles，然后循环调用readDatas，最后closeFile
	
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
	public boolean openFile(InputStream in,String fileName) throws Exception;
	
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
	public List<String[]> readTitles(Integer titleRowNums,Object...params) throws Exception;
	
	/**
	 * 
	 * @methodName		: readDatas
	 * @description	: 读取数据部分（非标题行）的数据，必须在调用readTitles之后调用
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
	public List<String[]> readDatas(Integer startRow,Integer recCount,
			Object...params) throws Exception;
	
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
	public List<String[]> readNextDatas(Integer recCount,Object...params) throws Exception;
	
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
	public void closeFile() throws Exception;
	
	// ===============================================================
	// 读取文件错误日志相关接口
	
	/**
	 * 
	 * @methodName		: getErrorLog
	 * @description	: 获取错误信息列表
	 * @return			: 错误信息列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<String> getErrorLog();	
	
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
	public Integer getErrorProcFalg();
	
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
	public void setErrorProcFalg(Integer errorFlag);
	
}
