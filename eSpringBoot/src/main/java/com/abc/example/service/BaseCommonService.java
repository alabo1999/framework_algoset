package com.abc.example.service;

import java.util.List;

/**
 * @className		: BaseCommonService
 * @description	: 公共服务基类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class BaseCommonService {
	// 数据访问计数服务类
	protected DacService dacService = new DacService(2,0,60000);
		
	/**
	 * 
	 * @methodName		: loadItem
	 * @description	: 根据key加载对象 
	 * @param itemKey	: 对象的key
	 * @param classKey	: 对象组的key
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> boolean loadItem(Object itemKey,Object classKey) {
		boolean bRet = false;
		if (classKey != null) {
			// 如果有对象组，则先查询组key
			if (hasGroupItems(classKey) == false) {
				// 如果组对象不存在
				// 加载对象组
				bRet = loadGroupItems(classKey);
				if (bRet == true) {
					// 如果加载成功，获取子项
					T item = getItem(itemKey,classKey);
					if (item == null) {
						// 如果子项不存在
						bRet = false;
					}
				}				
			}else {
				// 如果组对象已存在，此时子项不存在
				// 查询数据库，获取子项
				T item = fetchItem(itemKey,classKey);
				if(item != null) {
					// 加载成功，加入管理集合中
					setItem(itemKey,classKey,item);
					bRet = true;
				}else {
					bRet = false;
				}
			}			
		}else {
			// 如果没有对象组
			// 查询数据库，获取子项
			T item = fetchItem(itemKey,classKey);
			if(item != null) {
				// 加载成功，加入管理集合中
				setItem(itemKey,classKey,item);
				bRet = true;
			}else {
				bRet = false;
			}			
		}
		
		// 设置访问控制
		if(bRet == false) {
			// 如果加载子项失败，开启错误计数
			String key = getCombineKey(itemKey,classKey);
			Long currentTime = System.currentTimeMillis();
			dacService.putItemkey(key,currentTime);
			// 写日志
			logInfo("loadItem",key);
		}
		
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: loadGroupItems
	 * @description	: 根据分组key加载分组对象列表 
	 * @param classKey	: 分组key
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> boolean loadGroupItems(Object classKey) {
		boolean bRet = false;
		String key = getCombineKey(null,classKey);
		// 查询数据库，获取对象组列表
		List<T> itemList = fetchItems(classKey);
		if (itemList.size() == 0) {
			// 指定key的分组数据不存在，开启错误计数
			Long currentTime = System.currentTimeMillis();
			dacService.putItemkey(key,currentTime);
			// 写日志
			logInfo("loadGroupItems",key);
		}else {
			// 加载成功，加入管理集合中
			setItems(classKey,itemList);
			bRet = true;
		}
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: hasGroupItems
	 * @description	: 是否存在指定key的对象组，子类需重载
	 * @param classKey	: 对象组key
	 * @return			: 存在返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected boolean hasGroupItems(Object classKey) {
		return false;
	}
	
	/**
	 * 
	 * @methodName		: getItem
	 * @description	: 根据对象key和组key，获取对象，子类需重载
	 * @param <T>		: T类型对象
	 * @param itemKey	: 对象key值
	 * @param classKey	: 对象组key值
	 * @return			: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> T getItem(Object itemKey,Object classKey) {
		return null;
	}
	
	/**
	 * 
	 * @methodName		: fetchItem
	 * @description	: 从数据库中查询一个对象
	 * @param <T>		: 泛型方法
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @return			: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> T fetchItem(Object itemKey,Object classKey) {
		return null;
	}

	/**
	 * 
	 * @methodName		: fetchItems
	 * @description	: 从数据库中查询对象列表
	 * @param <T>		: 泛型方法
	 * @param classKey	: 对象组key
	 * @return			: 查询到的对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> List<T> fetchItems(Object classKey) {
		return null;
	}	
	
	/**
	 * 
	 * @methodName		: setItem
	 * @description	: 将一个对象加入管理集合中
	 * @param <T>		: 泛型方法
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @param item		: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> void setItem(Object itemKey,Object classKey,T item) {
	}	
	
	/**
	 * 
	 * @methodName		: setItems
	 * @description	: 将对象列表加入管理集合中
	 * @param <T>		: 泛型方法
	 * @param classKey	: 对象组key
	 * @param items		: 对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected <T> void setItems(Object classKey,List<T> items) {
	}		
	
	/**
	 * 
	 * @methodName		: logInfo
	 * @description	: 写日志
	 * @param methodName: 方法名
	 * @param param		: 参数值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected void logInfo(String methodName,String param) {
		
	}
	
	/**
	 * 
	 * @methodName		: procNoClassData
	 * @description	: 对象组key对应的数据不存在时的处理
	 * @param classKey	: 对象组key
	 * @return			: 数据加载成功，返回true,否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected boolean procNoClassData(Object classKey) {
		// 如果指定classKey不存在，此时classKey不可能为null
		boolean bRet = false;
		String key = getCombineKey(null,classKey);
		Long currentTime = System.currentTimeMillis();
		// 判断计数器是否将满
		if (dacService.isItemKeyFull(key,currentTime)) {
			// 如果计数将满
			// 复位
			dacService.resetItemKey(key);
			// 加载
			bRet = loadGroupItems(classKey);
		}
		dacService.putItemkey(key,currentTime);
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: procNoItemData
	 * @description	: 对象key对应的数据不存在时的处理
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @return			: 数据加载成功，返回true,否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected boolean procNoItemData(Object itemKey, Object classKey) {
		// 如果itemKey不存在
		boolean bRet = false;
		String key = getCombineKey(itemKey,classKey);
		
		Long currentTime = System.currentTimeMillis();
		if (dacService.isItemKeyFull(key,currentTime)) {
			// 如果计数将满
			// 复位
			dacService.resetItemKey(key);
			// 加载
			bRet = loadItem(itemKey, classKey);
		}
		// 计数不满
		dacService.putItemkey(key,currentTime);
		return bRet;
	}

	/**
	 * 
	 * @methodName		: getCombineKey
	 * @description	: 获取组合key值
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @return			: 组合key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	protected String getCombineKey(Object itemKey, Object classKey) {
		String sItemKey = (itemKey == null ? "" : itemKey.toString());
		String sClassKey = (classKey == null ? "" : classKey.toString());
		String key = "";
		if (!sClassKey.isEmpty()) {
			key = sClassKey;
		}
		if (!sItemKey.isEmpty()) {
			if (!key.isEmpty()) {
				key += "-" + sItemKey;
			}else {
				key = sItemKey;
			}
		}
		return key;
	}

}
