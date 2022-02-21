package com.abc.example.common.impexp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import com.abc.example.common.utils.ObjListUtil;
import com.abc.example.common.utils.Utility;


/**
 * @className		: BaseImportObj
 * @description	: 导入对象基类
 * @summary		: 
 * 	1、将List<String[]>对象转换为List<T>对象。
 * 	2、标题行：支持无标题行、单标题行、多标题行数据导入。
 * 	3、有标题行情况，支持按标题名导入列，标题所在列序号任意。
 * 	4、支持一次性导入，包括标题行和数据行。
 * 	5、支持分批数据导入。
 * 	6、允许设置日期时间格式，以统一处理日期时间数据导入。
 * 	7、异常发生时的处理策略，支持继续处理和立即中止处理，默认为继续处理。
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class BaseImportObj<T>{
	
	// 异常信息列表，执行导入后，可以通过get方法获取
	protected List<String> errorLogList = new ArrayList<String>();
	
    // 数据列下标与字段名的映射表，数据列下标从1开始
    // 对于一次导入的行数据，columnIdxMap不变化，不必每个对象都创建，可以共享使用
    protected Map<Integer,String> columnIdxMap = new HashMap<Integer,String>();
    
    // 导入字段定义对象列表
	protected List<ImpExpFieldDef> fieldList = new ArrayList<ImpExpFieldDef>();	
	
	// 对象到行号的字典
	Map<T,String> obj2RowNoMap = new HashMap<T,String>();	
	
    // 数据列的开始列号,0-based
    protected Integer firstColumnIdx = 0;  
    
    // 标题行行数，子类可重新设置，默认为1
    protected Integer titleRowNums = 1;
    
	// 错误处理策略标志值:0-继续处理，1-立即中止处理
    protected Integer errorProcFlag = 0;   
    
    // 提示信息中列号表示方式，0-字母(A,B,C，...),1-数字序号(1-based)
    protected Integer columnNoStyle = 0;
    
    // 日期格式，用于日期格式转换
    protected String dateFormat = "yyyy-MM-dd hh:mm:ss";
    protected String formatDatePart = "yyyy-MM-dd";
    protected String formatTimePart = "hh:mm:ss";
    
    // 为updateItems方法调用，暂存的对象
    protected Object[] methodObjs;
    
    
    // ==============================================================================
    // ===============公共方法实现===================================================
    
    // 获取导入的异常信息列表
    public List<String> getErrorLogList(){
    	return errorLogList;
    }
    
    // 添加异常日志信息列表
    public void addErrorList(List<String> errorList) {
    	if (errorList.size() > 0) {
    		errorLogList.addAll(errorList);    		
    	}
    }
    
    // 在头部插入异常日志信息列表
    public void insertErrorList(List<String> errorList) {
    	if (errorLogList.size() > 0) {
    		for (int i = errorList.size() -1; i >= 0 ; i--) {
    			String item = errorList.get(i);
    			errorLogList.add(0, item);
    		}
    	}else {
    		addErrorList(errorList);
    	}
    }
    
    // 是否有导入异常信息
    public Boolean hasError() {
    	return (errorLogList.size() > 0);
    }
    
    // 获取标题行行数
    public Integer getTitleRowNums() {
    	return titleRowNums;
    }
    
    // 设置标题行行数
    public void setTitleRowNums(Integer titleRowNums ) {
    	this.titleRowNums = titleRowNums;
    }
    
    // dateFormat的getter方法
    public String getDateFormat() {
    	return dateFormat;
    }
    
    // dateFormat的setter方法
    public void setDateFormat(String dateFormat) {
    	this.dateFormat = dateFormat;
    	
    	// 设置日期部分和时间部分格式
    	String[] arrFormat = dateFormat.split(" ");
    	formatDatePart = arrFormat[0];
    	if (arrFormat.length > 1) {
    		formatTimePart = arrFormat[1];
    	}
    }
    
    // errorProcFlag的getter方法
    public Integer getErrorProcFlag() {
    	return errorProcFlag;
    }    

    // errorProcFlag的setter方法
    public void setErrorProcFlag(Integer errorProcFlag ) {
    	this.errorProcFlag = errorProcFlag;
    }      
    
    // columnNoStyle的getter方法
    public Integer getColumnNoStyle() {
    	return columnNoStyle;
    }    

    // columnNoStyle的setter方法
    public void setColumnNoStyle(Integer columnNoStyle ) {
    	this.columnNoStyle = columnNoStyle;
    }    
    
    // 获取导入字段名的列表
    public List<String> getFieldNameList(){    	
    	List<String> itemList = ObjListUtil.getSubFieldList(fieldList, "fieldName");
    	return itemList;
    }
    
    // 获取导入字段定义对象的列表
    public List<ImpExpFieldDef> getFieldList(){    	
    	return fieldList;
    }
    
    // 获取本次导入成功的记录的行号列表,1-based
    public Map<T,String> getObj2RowNoMap(){
    	return obj2RowNoMap;
    }
    
    /**
     * 
     * @methodName		: reset
     * @description	: 复位，便于支持导入新数据
     * @param includeTitle: 是否包含标题行信息复位
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public void reset(boolean includeTitle) {
    	errorLogList.clear();
    	obj2RowNoMap.clear();
    	
    	if (includeTitle == true) {
    		// 如果复位标题行信息
        	columnIdxMap.clear();
    	}
    }
        
    /**
     * 
     * @methodName		: importAllDatas
     * @description	: 导入所有数据，包括标题行和数据行
     * @param dataRowList: List<String[]>类型，行数据列表
     * @return			: List<T>类型数据
     * @throws Exception
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public List<T> importAllDatas(List<String[]> dataRowList) throws Exception {
    	List<T> dataList = null;
    	
    	// 处理标题行
    	importTitles(dataRowList);
    	
    	// 处理数据行    	
    	dataList = processImportDatas(dataRowList,true);
    	return dataList;
    	
    }
    
    /**
     * 
     * @methodName		: importDatas
     * @description	: 导入数据，不包含标题行数据
     * @param dataRowList: List<String[]>类型
     * @return			: List<T>类型数据
     * @throws Exception
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public List<T> importDatas(List<String[]> dataRowList) throws Exception{
    	List<T> dataList = null; 
    	// 处理数据行    	
    	dataList = processImportDatas(dataRowList,false);
    	return dataList;    	
    }    
    
    /**
     * 
     * @methodName		: setFieldList
     * @description	: 设置导入字段定义列表
     * @param titleList	: 导入导出字段定义对象的列表
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public void setFieldList(List<ImpExpFieldDef> titleList) {
    	// 先清空标题列表和字典
    	fieldList.clear();

    	// 加入fieldList
    	fieldList.addAll(titleList);
    }           
	
	/**
	 * 
	 * @methodName		: importTitles
	 * @description	: 导入标题行数据
	 * @param titleList	: 标题行列表，列表后面允许包含其它数据，
	 * 对于标题行数据，第一行的首个成员为开始列索引号	
	 * 后续行的首个成员为行索引号，1-based
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    public void importTitles(List<String[]> titleList) throws Exception{   

    	// 复位相关参数
    	reset(true);
    	
    	if (titleList.size() == 0) {
    		// 如果无标题行数据
    		logAndThrowError("无标题行数据");
    	}
    	
    	String[] rowData = null;

    	// 获取起始列索引号
    	rowData = titleList.get(0);
    	firstColumnIdx = Integer.valueOf(rowData[0]);
    	    	
    	if (this.titleRowNums > 0) {
    		// 如果标题行数大于0
    		if (titleList.size() < this.titleRowNums) {
        		logAndThrowError("标题行行数与设置不符");
    		}
    		
    		// 处理每一行标题数据
        	for (int i = 0; i < titleList.size() && i < this.titleRowNums; i++) {
        		rowData = titleList.get(i);
        		// 遍历，rowData[0]除外
        		for(int j = 1; j < rowData.length; j++) {
        			String title = rowData[j].trim();
            		// 在fieldList中查询
            		for (ImpExpFieldDef item : fieldList) {
            			if (title.equals(item.getTitleName())) {
            				// 如果标题名找到
            				columnIdxMap.put((Integer)j, item.getFieldName());
            				break;
            			}
            		}
            		
        		}        		
        	} 
        	
        	// 检查必需字段是否确认
        	String errInfo = checkTiles();    		        	
        	if (!errInfo.isEmpty()) {
        		logAndThrowError(errInfo);
        	}
    	}else {
    		// 如果标题行数等于0
    		rowData = titleList.get(0);
    		
        	// 检查列数
    		if (rowData.length != (fieldList.size() + 1)) {
    			logAndThrowError("数据列数与导入字段列设置不符");
    		}
    		// 如果标题行行数=0，按照fieldList次序加入标题
    		for(int j = 0; j < fieldList.size(); j++) {
    			ImpExpFieldDef item = fieldList.get(j);
    			// 留出rowData[0]，index + 1
    			columnIdxMap.put((Integer)(j+1), item.getFieldName());
    		}    		
    	}    	
    }
    
    /**
     * 
     * @methodName		: processImportDatas
     * @description	: 处理导入数据
     * @param dataRows	: 数据行列表
     * @param titleIncluded: 输入列表是否包含标题行
     * @return			: List<T>对象
     * @throws Exception
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    protected List<T> processImportDatas(List<String[]> dataRows,boolean titleIncluded)
    		throws Exception{
    	List<T> dataList = new ArrayList<T>();
    	String errInfo = "";
    	int stRowIdx = (titleRowNums == 0 ? 1 : titleRowNums);
    	
    	if (titleIncluded == false) {
    		// 如果不包含标题
    		stRowIdx = 0;
    	}
    	try {
        	// 遍历data    	
        	for (int i = stRowIdx; i < dataRows.size(); i++) {
        		try {
            		// 取得数据行
            		String[] dataRow = dataRows.get(i);
                	// 创建一个对象
        			T rowData = newObj();
                	// 导入数据
                	errInfo = importRowData(dataRow,rowData);
                	if (errInfo.isEmpty()) {
                		// 加入列表中
                		dataList.add(rowData); 
                		// 加入行号列表中
                		obj2RowNoMap.put(rowData, dataRow[0]);
                	}else {
                		// 如果有异常信息
                		throw new Exception(errInfo);
                	}    		        			
        		}catch(Exception e) {        			
            		procError(e);            		
            	}
        	}  
        	
        	return dataList;
    	}catch(Exception e) {
    		errorLogList.add(e.getMessage());
			throw e;
		}     	
    }
    
    /**
     * 
     * @methodName		: logAndThrowError
     * @description	: 记录日志并抛出异常
     * @param errInfo	: 异常信息
     * @throws Exception
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    protected void logAndThrowError(String errInfo) throws Exception{
    	errorLogList.add(errInfo);
    	throw new Exception(errInfo);
    }
	
	/**
	 * 
	 * @methodName		: checkTiles
	 * @description	: 检查标题行
	 * @return			: 正常返回空串，否则返回提示信息
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    protected String checkTiles() {
    	// 检查必需字段是否都存在
    	// 存放缺失的必需字段
    	String missingTitles = "";
    	for (int i = 0; i < fieldList.size(); i++) {
    		ImpExpFieldDef item = fieldList.get(i);
    		
    		if (item.getMandatory() == 0) {
    			// 可选字段，跳过
    			continue;
    		}
    		boolean bFound = false;
    		for(String subItem : columnIdxMap.values()) {
    			if(subItem.equals(item.getFieldName())) {
    				// 找到该字段
    				bFound = true;
    			}
    		}
    		if (!bFound) {
    			// 如果必需字段缺失，加入缺失字段中
    			if(missingTitles.isEmpty()) {
    				// 标题名
    				missingTitles = "数据缺失必需标题列名 : " + item.getTitleName();
    			}else {
    				missingTitles += "," + item.getTitleName();
    			}
    		}
    	}
    	    	
    	return missingTitles;		
	}
    
	/**
	 * 
	 * @methodName		: importRowData
	 * @description	: 导入行数据
	 * @param rowData	: 行数据,第一个成员为行号，1-based
	 * @return			: 异常信息，空串表示无异常
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected String importRowData(String[] rowData,T rowObj){
    	String errInfo = "";
    	
    	for(Map.Entry<Integer,String> item : columnIdxMap.entrySet()) {
    		Integer colIdx = item.getKey();
    		String fieldName = item.getValue();
    		
    		// 处理各个字段，可能数据会有问题
    		try {
    			// 调用基类方法或子类重载方法，载入数据到对象中
    			fillData(fieldName,rowData[colIdx],rowObj);
    		}catch(Exception e) {
    			// 异常单元格提示信息
    	    	// 对于数据行，arrTitle的第一个成员为行号
    			Integer currentColIdx = colIdx + firstColumnIdx;
    			// 用A-Z表示。注：CSV格式也可以用Excel打开
    			String column = "";
    			if (this.columnNoStyle == 0) {
    				// 如果用字母表示
        			column = getColumnNo(currentColIdx);        				
    			}else {
    				// 如果用索引号表示,1-based
    				column = String.valueOf(colIdx);
    			}
    			if (errInfo.isEmpty()) {
    				//由于rowData[0]为行号，因此colIdx为列号
    				errInfo = "数据行错误, row : " + rowData[0] 
    						+ ", column : [" + column;
    			}else {
    				errInfo += ";" + column; 
    			}
    		}
    	}
    	
    	if (!errInfo.isEmpty()) {
    		errInfo += "]";
    	}
    	return errInfo;  
    }
    
	/**
	 * 
	 * @methodName		: getColumnNo
	 * @description	: 根据列序号（1-based），计算字符串表示的列号 
	 * @param currentColIdx: 列序号
	 * @return			: 列号
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected String getColumnNo(int currentColIdx) {
		// excel列号命名规则：A-Z;AA-AZ;BA-BZ,...,ZA-ZZ,AAA-AAZ,...,
		// 相当于26进制
		// A的ASCII值=65
		String column = "";
		
		if (currentColIdx == 0) {
			// 如果为0，则返回空串
			return column;
		}
		
		// 计算商和余数
		int quotient = currentColIdx / 26;
		int remainder = currentColIdx % 26;
		
		if (quotient > 0) {
			// 递归获取一个字符
			column += getColumnNo(quotient);
		}
		if (remainder == 0) {
			remainder = 26;
		}
		column += String.valueOf((char)(65 + remainder -1));
		return column;
	}    
    
    /**
     * 
     * @methodName		: newObj
     * @description	: 产生一个对象
     * @param <T>		: 泛型T
     * @return			: T类型的对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @SuppressWarnings("unchecked")
    protected T newObj() throws Exception {
    	T item = null;
    	
		// 通过反射获取泛型T的真实类型    	
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[0];
		
		// 通过反射创建泛型T的实例
		item = clazz.newInstance();
		
    	return item;
    }
    
    /**
     * 
     * @methodName		: fillData
     * @description	: 将数据设置到属性字段中，如为自定义类型或特殊类型，
     * 					  或有数据变换，子类可重载此方法
     * @param fieldName	: 字段名称
     * @param cellData	: 对应单元格数据
     * @param rowObj	: 泛型T类型对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @SuppressWarnings("unchecked")
    protected void fillData(String fieldName, String cellData,T rowObj) throws Exception {

    	if (cellData.isEmpty()) {
    		// 单元格数据为空串，表示无数据，使用对象的默认值
    		return;
    	}
    	
		Class<T> newClazz = (Class<T>) rowObj.getClass();        		
		Field field = newClazz.getDeclaredField(fieldName);
		// 将私有属性改为可访问
		field.setAccessible(true);
		
		// 根据常规的数据类型，进行数据类型转换
		String type = field.getType().getTypeName();
		Object oVal = null;
		switch(type) {
		// 基本类型
		case "byte":
			oVal = Byte.valueOf(cellData).byteValue();
			break;
		case "int":
			oVal = Integer.valueOf(cellData).intValue();
			break;
		case "short":
			oVal = Short.valueOf(cellData).shortValue();
			break;
		case "long":
			oVal = Long.valueOf(cellData).longValue();
			break;
		case "float":
			oVal = Float.valueOf(cellData).floatValue();
			break;
		case "double":
			oVal = Double.valueOf(cellData).doubleValue();
			break;
		case "char":
			// 取第一个字符
			oVal = cellData.charAt(0);
			break;
		case "boolean":
			// 值为true时，字符串必须为true，大小写不敏感，其它值都为false
			oVal = Boolean.parseBoolean(cellData);
			break;
		
		// java.lang类型
		case "java.lang.Byte":
			oVal = Byte.valueOf(cellData);
			break;
		case "java.lang.Integer":
			try {
				oVal = Integer.valueOf(cellData);
			}catch(Exception e) {
				// 公式处理
				oVal = Integer.valueOf(Double.valueOf(cellData).intValue());
			}
			break;
		case "java.lang.Short":
			oVal = Short.valueOf(cellData);
			break;
		case "java.lang.Long":
			oVal = Long.valueOf(cellData);
			break;
		case "java.lang.Float":
			oVal = Float.valueOf(cellData);
			break;
		case "java.lang.Double":
			oVal = Double.valueOf(cellData).doubleValue();
			break;
		case "java.lang.Character":
			oVal = Character.valueOf(cellData.charAt(0));
			break;
		case "java.lang.String":
			oVal = cellData;
			break;
		case "java.lang.Boolean":
			// 值为true时，字符串必须为true，大小写不敏感，其它值都为false
			oVal = (Boolean)Boolean.parseBoolean(cellData);
			break; 
			
		// java.util包
		case "java.util.Date":
		{
			DateFormat df = new SimpleDateFormat(dateFormat);
			oVal = df.parse(cellData);
		}
			break;
			
		// java.time包
		case "java.time.LocalDate":
		{
			DateTimeFormatter df = DateTimeFormatter.ofPattern(formatDatePart);
			oVal = LocalDate.parse(cellData,df);
		}
			break;
		case "java.time.LocalTime":
		{
			DateTimeFormatter df = DateTimeFormatter.ofPattern(formatDatePart);
			oVal = LocalTime.parse(cellData,df);
		}
			break;
		case "java.time.LocalDateTime":
		{
			DateTimeFormatter df = DateTimeFormatter.ofPattern(dateFormat);
			oVal = LocalDateTime.parse(cellData,df);
		}
			break;			
		default:
			break;
		} 
		
	    // 给对象属性设置值
	    if(oVal != null) {
	    	field.set(rowObj,oVal);
	    }		
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
    protected void procError(Exception e) throws Exception{
		// 如果异常发生时，立即中止
		if (this.errorProcFlag == ImpExpConstants.STOP_ON_ERROR) {
			// 此处不必增加异常日志，由外层负责
			throw e;
		}else {
			// 增加异常日志
			errorLogList.add(e.getMessage());
		}            		    	
    }

	/**
	 * 
	 * @methodName		: datasIntoDBMethod1
	 * @description	: 数据入库（方法1：可变批量记录入库法）
	 * @param object	: 提供方法（insertItems和updateItem）的外部类
	 * @param dataRowList	: 对象列表
	 * @param addList	: 新增的记录列表
	 * @param insertItems	: 批量插入记录的方法
	 * @param updateItem	: 修改记录的方法
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void datasIntoDBMethod1(
			Object object,
			Object request,
			List<T> dataRowList,
			List<T> addList,
			List<T> updateList,			
			Method insertItems,
			Method updateItem) throws Exception{		
		// 返回的异常信息列表
		List<String> errorList = new ArrayList<String>();	
		
		methodObjs = new Object[] {object,updateItem};
    	
        // 根据特征字段修改对象的方法
        Method updateItems = Utility.getMethodByName(this,"updateItems");	
        // 调用可变步长的批量处理算法
		errorList = BatchProcess.varStepBatchProcess(object,this,request,dataRowList, 
				addList, updateList, insertItems, updateItems,errorProcFlag,0x00);
        
        // 汇总错误信息
        addErrorList(errorList);		
	}  
	
	public String updateItems(Object request,Exception e,String item) {
		// ,Method updateItem
		String errInfo = "";
		if (e instanceof DuplicateKeyException) {
			// 如果对象key已存在，则需要update操作
			try {
				Object object = methodObjs[0];
				Method updateItem = (Method)methodObjs[1];				
				updateItem.invoke(object, request,item, getFieldNameList());
			} catch(Exception ex) {
				// 发生异常
				errInfo = "异常数据行，row : " + obj2RowNoMap.get(item) + "；message：" + ex.getMessage();
			}
		}else {
			// 如果为其它异常
			errInfo = "异常数据行，row : " + obj2RowNoMap.get(item);
		}
		return errInfo;
		
	}	
	
	/**
	 * 
	 * @methodName		: checkAndRemoveDuplicate
	 * @description	: 检查并去重，将并后面的重复对象从输入对象列表中移除
	 * @param dataRowList: T类型对象列表
	 * @param fieldMap	: 字段字典，字段名到键值标志值的映射表
	 * @return			: dataRowList存在键值重复的对象列表
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<T> checkAndRemoveDuplicate(List<T> dataRowList,Map<String,Integer> fieldMap)
			 throws Exception{
    	// 特征重复的对象列表
    	List<T> dupList = new ArrayList<T>();
    	// 特征去重，重复的对象在dupList中，dataRowList为去重后的对象列表
    	ObjListUtil.removeDuplicate(fieldMap, dataRowList, dupList);
    	
    	// 重复对象处理
    	if (dupList.size() > 0) {
    		// 如果有重复对象
    		if (errorProcFlag == ImpExpConstants.CONTINUE_ON_ERROR) {
    			// 如果异常处理策略为继续处理
    	    	for (T item : dupList) {    		
    	    		errorLogList.add("重复数据行，row : " + obj2RowNoMap.get(item));
    	    	}    			
    		}else {
    			// 抛出第一条重复数据，作为异常信息
    			throw new Exception("重复数据行，row : " + obj2RowNoMap.get(dupList.get(0)));
    		}
    	}
    	return dupList;
	}
}
