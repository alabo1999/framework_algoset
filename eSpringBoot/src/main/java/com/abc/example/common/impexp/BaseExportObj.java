package com.abc.example.common.impexp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.abc.example.common.utils.LogUtil;

/**
 * @className		: BaseExportObj
 * @description	: 数据导出基类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class BaseExportObj {
	// 导出字段定义列表
	protected List<ImpExpFieldDef> titleList = new ArrayList<ImpExpFieldDef>();
	
	// 获取导出字段定义列表
	public List<ImpExpFieldDef> getTitleList(){
		return this.titleList;
	}
    
	/**
	 * 
	 * @methodName		: exportTitleList
	 * @description	: 输出标题行列表
	 * @return			: 标题行字符串数组
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    public String[] exportTitleList() {
    	String[] arrTitle = new String[titleList.size()];
    	for (int i = 0; i < titleList.size(); i++) {
    		ImpExpFieldDef item = titleList.get(i);
    		arrTitle[i] = item.getTitleName();
    	}
    	return arrTitle;
    }
    
    /**
     * 
     * @methodName		: exportDataList
     * @description	: 输出导出字段的数据行列表
     * @param objList	: 泛型T类型的对象列表
     * @return			: 数据行（字符串数组）的列表
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public <T> List<String[]> exportDataList(List<T> objList) {
    	List<String[]> itemList = new ArrayList<String[]>();
    	// 输出列数
    	int colsCount = titleList.size();
    	
    	// 遍历对象数据列表
    	for(int i = 0; i < objList.size(); i++) {
    		T item = objList.get(i);
    		// 每个对象输出到一个字符串数组中
        	String[] arrRowData = new String[colsCount];
        	
        	for (int j = 0; j < titleList.size(); j++) {
        		ImpExpFieldDef fieldItem = titleList.get(j);
        		// 使用反射方法，通过字段名获取属性值
        		Object val = getFieldValue(item, fieldItem.getFieldName());
        		if (val != null) {
            		arrRowData[j] = val.toString();        			
        		}else {
        			arrRowData[j] = "";
        		}
        	}
        	
        	// 数据行加入列表中
        	itemList.add(arrRowData);
    	}
    	    	
    	return itemList;
    }  
    
    /**
     * 
     * @methodName		: setTitles
     * @description	: 设置Excel数据的标题信息
     * @param titleList	: 标题名与导出字段定义对象的列表
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public void setTitles(List<ImpExpFieldDef> titleList) {
    	// 先清空标题列表，便于重复使用
    	this.titleList.clear();
    	
    	// 加入列表
    	this.titleList.addAll(titleList);
    } 
    
    /**
     * 
     * @methodName		: getFieldValue
     * @description	: 取得指定字段名称的字段值，子类可重载此方法
     * @param <T>		: 泛型T
     * @param item		: 输入结构体对象
     * @param fieldName	: 字段名称
     * @return			: 字段值
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @SuppressWarnings("unchecked")
	public <T> Object getFieldValue(T item,String fieldName) {
		// 取得新对象的当前字段值
    	Object retValue = null;
    	try {
    		Class<T> newClazz = (Class<T>) item.getClass();        		
    		Field newField = newClazz.getDeclaredField(fieldName);
    		newField.setAccessible(true);
    		retValue = newField.get(item);     		
    	}catch (NoSuchFieldException e) {
    		LogUtil.error(e);
    	}catch (IllegalAccessException e) {
    		LogUtil.error(e);
    	}  
		return retValue;
    	
    }    
}
