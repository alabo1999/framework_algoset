package com.abc.example.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @className		: ObjListUtil
 * @description	: 对象列表工具类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class ObjListUtil {
    /**
     * 
     * @methodName		: compareTwoList
     * @description	: 比较两个对象列表，新列表与旧列表对比，根据比较的属性字段字典，比较结果
     * 					  1、新的对象；2、键值相同，属性不同，需要修改的对象；3、属性完全相同
     * 					  这样有新增对象列表，修改对象列表，相同对象列表，剩余对象列表
     * @param <T>		: 泛型类型T
     * @param fieldMap	: 比较字段字典，key为字段名称，value为是否键值，1表示键值字段，0为普通字段。
     * 					: 如为null,则表示T为基本数据类型。
     * @param newList	: 新列表，要求键值无重复
     * @param oldList	: 旧列表，要求键值无重复
     * @param addList	: 新增对象列表
     * @param sameList	: 相同对象列表
     * @param updateList: 修改对象列表
     * @param remainderList: 剩余对象列表
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    public static <T> void compareTwoList(Map<String,Integer> fieldMap,
    		List<T> newList,List<T> oldList,
    		List<T> addList, List<T> sameList, 
    		List<T> updateList, List<T> remainderList) {
    	
    	// 复制一份copy，避免输入数据被修改
    	List<T> aOldList = new ArrayList<T>(oldList);
    	
    	// 遍历新列表
    	for(int i = 0; i < newList.size(); i++) {
    		T newItem = newList.get(i);
    		// 标记对象是否匹配
    		boolean found = false;
        	// 遍历旧列表，倒序
    		for (int j = aOldList.size() - 1; j >= 0; j--) {
    			T oldItem = aOldList.get(j);
    			// 比较两个对象
    			int compare = compareTwoItem(fieldMap,newItem,oldItem);
    			if (compare == 1) {
    				// 如果两个对象相同，加入相同列表中
    				sameList.add(newItem);
    				// 从列表中移除
    				aOldList.remove(j);
    				found = true;
    				// 结束本轮遍历
    				break;
    			}else if(compare == 2) {
    				// pass
    			}else if(compare == 3) {
    				// 匹配对象，但属性不同，加入修改表中
    				updateList.add(newItem);
    				// 从列表中移除
    				oldList.remove(j);
    				found = true;
    				// 结束本轮遍历
    				break;
    			}else {
    				// 发生异常
    				return;
    			}
    		}
    		if (found == false) {
    			// 如果本轮遍历，未找到匹配项，加入新增列表中
    			addList.add(newItem);
    		}
    	}
    	
    	// oldList中剩余的项，加入剩余列表中
    	for(int i = 0; i < oldList.size(); i++) {
    		T oldItem = oldList.get(i);
    		remainderList.add(oldItem);
    	}
    	
    }
    
    /**
     * 
     * @methodName		: compareTwoItem
     * @description	: 比较两个相同类型的对象
     * @param <T>		: 泛型类型T
     * @param fieldMap	: 比较字段字典，key为字段名称，value为是否键值，1表示键值字段。
     * @param newItem	: 新的对象
     * @param oldItem	: 旧的对象
     * @return			: 返回值定义如下：
     * 		0	:	数据处理异常
     *  	1	:	对象完全相同（比较字段的字段值都相同）
     *  	2	:	对象不同（键值字段的字段值存在不相同）
     *  	3	:	对象相同，属性不同（键值字段的字段值相同，但属性值不同）
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @SuppressWarnings("unchecked")
	public static <T> int compareTwoItem(Map<String,Integer> fieldMap,T newItem, T oldItem) {
    	int retCode = 1;
    	try {
    		if(fieldMap != null) {
    			// 如果fieldMap非空，则表示T为结构体
            	for (Map.Entry<String,Integer> entry : fieldMap.entrySet()) {
            		// 取得字段名称
            		String fieldName = entry.getKey();
            		Integer keyFlag = entry.getValue();
            		
            		// 取得新对象的当前字段值
        			Class<T> newClazz = (Class<T>) newItem.getClass();        		
            		Field newField = newClazz.getDeclaredField(fieldName);
            		newField.setAccessible(true);
            		Object newValue = newField.get(newItem);
            		
            		// 取得旧对象的当前字段值
        			Class<T> oldClazz = (Class<T>) oldItem.getClass();        		
            		Field oldField = oldClazz.getDeclaredField(fieldName);
            		oldField.setAccessible(true);
            		Object oldValue = oldField.get(oldItem);
            		
            		// 比较两个属性字段的值
            		if (!newValue.equals(oldValue)) {
            			// 如果字段值不相等
            			if (keyFlag == 1) {
            				// 如果为键值字段，表示两个对象
            				return 2;
            			}else {
            				// 非键值字段，表示有属性发生改变
            				retCode = 3;
            			}
            		}        		
            	}    			
    		}else {
    			// 如果fieldMap为空，表示T为基本数据类型，如Integer,String,
    			if(!newItem.equals(oldItem)) {
    				// 如果字段值不相等
    				retCode = 2;
    			}
    		}
    	}catch (NoSuchFieldException e) {
    		LogUtil.error(e);
    		return 0;
    	}catch (IllegalAccessException e) {
    		LogUtil.error(e);
    		return 0;
    	}
    	return retCode;
    }
    
    /**
     * 
     * @methodName		: removeDuplicate
     * @description	: 对象列表去重，同特征对象只保留最后一个
     * @param <T>		: 泛型类型T
     * @param fieldMap	: 比较字段字典，key为字段名称，value为是否键值，1表示键值字段。
     * @param inputList	: 对象列表，将被去重
     * @param dupList	: 重复的多余对象列表，将被去重
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     * 2021/08/18	1.0.1		sheng.zheng		使用Map来缓存，以提供算法效率
     *
     */
    public static <T> void removeDuplicate(Map<String,Integer> fieldMap,List<T> inputList,
    		List<T> dupList){
    	Map<String,T> keyMap = new HashMap<String,T>();
    	T item = null;
    	String key = "";
    	// 倒序遍历inputList，并加入字典
    	for (int i = inputList.size() - 1; i >= 0; i--) {
    		item = inputList.get(i);
    		key = getKey(fieldMap,item);
    		if (keyMap.containsKey(key)) {
    			// 如果已存在key值对象，表示重复
    			// 加入重复列表
    			dupList.add(item);
    			// 移除重复对象
    			inputList.remove(i);
    		}else {
    			// 如果为新key
    			keyMap.put(key, item);
    		}
    	}
    	
    	keyMap.clear();
    }
    
    /**
     * 
     * @methodName		: getKey
     * @description	: 获取对象的关键字段值组成的key
     * @param <T>		: 泛型类型
     * @param fieldMap	: 比较字段字典，key为字段名称
     * @param item		: T类型对象
     * @return			: key
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2022/08/18	1.0.0		sheng.zheng		初版
     *
     */
    @SuppressWarnings("unchecked")
	public static <T> String getKey(Map<String,Integer> fieldMap,T item) {
    	String key = "";
    	try {
    		if(fieldMap != null) {
    			// 如果fieldMap非空，则表示T为结构体
            	for (Map.Entry<String,Integer> entry : fieldMap.entrySet()) {
            		// 取得字段名称
            		String fieldName = entry.getKey();
            		Integer keyFlag = entry.getValue();
            		if (keyFlag == 0) {
            			// 如果非key字段，则跳过
            			continue;
            		}
            		// 取得新对象的当前字段值
        			Class<T> clazz = (Class<T>) item.getClass();        		
            		Field field = clazz.getDeclaredField(fieldName);
            		field.setAccessible(true);
            		Object oValue = field.get(item);
            		if (key.isEmpty()) {
            			key = oValue.toString();
            		}else {
            			key += "-" + oValue.toString();
            		}            		
            	}    			
    		}else {
    			// 如果fieldMap为空，表示T为基本数据类型，如Integer,String,
    			key = item.toString();
    		}
    		return key;
    	}catch (NoSuchFieldException e) {
    		LogUtil.error(e);
    		return key;
    	}catch (IllegalAccessException e) {
    		LogUtil.error(e);
    		return key;
    	}    	
    }
    
    /**
     * 
     * @methodName		: isTwoListTheSame
     * @description	: 比较两个列表，是否相同
     * @param <T>		: 元素的数据类型
     * @param fieldMap	: 比较字段字典，key为字段名称，value为是否键值，1表示键值字段，0为普通字段。
     * 					: 如为null,则表示T为基本数据类型。
     * @param newList	: 新列表，要求键值无重复
     * @param oldList	: 旧列表，要求键值无重复
     * @return			: 列表元素相同返回true，否则为false
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static <T> boolean isTwoListSame(Map<String,Integer> fieldMap,
    		List<T> newList,List<T> oldList) {
    	// 比较长度
    	if (newList.size() != oldList.size()) {
    		return false;
    	}
    	
    	// 遍历新列表
    	for(int i = 0; i < newList.size(); i++) {
    		T newItem = newList.get(i);
    		// 标记对象是否匹配
    		boolean found = false;
        	// 遍历旧列表，倒序
    		for (int j = oldList.size() - 1; j >= 0; j--) {
    			T oldItem = oldList.get(j);
    			// 比较两个对象
    			int compare = compareTwoItem(fieldMap,newItem,oldItem);
    			if (compare == 1) {
    				// 如果两个对象相同
    				found = true;
    				// 结束本轮遍历
    				break;
    			}else if(compare == 2) {
    				// 不相同，检查下一个元素
    			}else if(compare == 3) {
    				// 匹配对象，但属性不同
    				return false;
    			}else {
    				// 发生异常
    				return false;
    			}
    		}
    		if (found == false) {
    			// 如果本轮遍历，未找到匹配项，加入新增列表中
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * 
     * @methodName		: getSubFieldList
     * @description	: 抽取T类型的列表中指定字段的列表
     * @param <T>		: 输入列表的类型
     * @param <E>		: 指定字段的类型
     * @param inputList	: 输入列表
     * @param fieldName	: 字段名称
     * @return			: 指定字段的列表
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @SuppressWarnings("unchecked")
	public static <T,E> List<E> getSubFieldList(List<T> inputList,String fieldName){
    	List<E> retList = new ArrayList<E>();
    	// 遍历新列表
    	for(int i = 0; i < inputList.size(); i++) {
    		T item = inputList.get(i);
    		try {
        		// 取得新对象的当前字段值
    			Class<T> newClazz = (Class<T>) item.getClass();        		
        		Field newField = newClazz.getDeclaredField(fieldName);
        		newField.setAccessible(true);
        		Object newValue = newField.get(item); 
        		// 加入列表中
        		retList.add((E)newValue);
        		
    		}catch (NoSuchFieldException e) {
        		LogUtil.error(e);
        	}catch (IllegalAccessException e) {
        		LogUtil.error(e);
        	}    		
    	}
    	return retList;
    }
    
    // 计算两个列表的交集
    public static <T> List<T> getIntersection(List<T> list1,List<T> list2){
    	List<T> retList = new ArrayList<T>();
    	for (T item : list1) {
    		if (list2.contains(item)) {
    			retList.add(item);
    		}
    	}
    	return retList;
    }
    
    /**
     * 
     * @methodName		: getFieldValue
     * @description	: 取得指定字段名称的字段值
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
	public static <T> Object getFieldValue(T item,String fieldName) {
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
    
    /**
     * 
     * @methodName		: getValuesMap
     * @description	: 根据给定的字段名列表，从对象中获取对应字段的值的字典
     * @param <T>		: 泛型类型
     * @param item		: 泛型对象
     * @param fieldnameList: 字段名列表
     * @return			: Map<String,Object>类型，key为字段名，value为字段对应的值
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
	public static <T> Map<String,Object> getValuesMap(T item,List<String> fieldnameList){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
    		@SuppressWarnings("unchecked")
			Class<T> newClazz = (Class<T>) item.getClass();
    		Object oValue = null;
    		for (String fieldname : fieldnameList) {
        		Field field = newClazz.getDeclaredField(fieldname);
        		field.setAccessible(true);
        		oValue = field.get(item);     		
        		map.put(fieldname, oValue);
    		}			
		}catch (NoSuchFieldException e) {
    		LogUtil.error(e);
    	}catch (IllegalAccessException e) {
    		LogUtil.error(e);
    	}  
		return map;
	}

	/**
	 * 
	 * @methodName		: isSamePropValue
	 * @description	: 检查列表中指定属性名的值是否都相同
	 * @param <T>		: 泛型类型
	 * @param list		: T类型的列表
	 * @param propName	: 属性名
	 * @return			: 如果都相同，返回true；否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/08/12	1.0.0		sheng.zheng		初版
	 *
	 */
	public static <T> boolean isSamePropValue(List<T> list,String propName) {
		if (list.size() == 0) {
			return false;
		}
		// 获取第一个对象
		T item = list.get(0);
		Object oValue = getFieldValue(item,propName);
		for (int i = 1; i < list.size(); i++) {
			item = list.get(i);
			Object oVal = getFieldValue(item,propName);
			if (!oValue.equals(oVal)) {
				// 如果不相对
				return false;
			}
		}
		return true;
	}

}
