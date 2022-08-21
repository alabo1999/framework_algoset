package com.abc.example.service;

import com.abc.example.vo.CacheObj;

/**
 * @className		: CacheDataService
 * @description	: 缓存数据服务接口类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/06/04	1.0.0		sheng.zheng		初版
 *
 */
public interface CacheDataService {
	
	/**
	 * 
	 * @methodName		: getItem
	 * @description	: 获取指定类名的指定ID的对象
	 * @summary			: 如果指定ID对象不在内存中，则查询数据库
	 * @param <T>		: 对象类型
	 * @param clazz		: 类名，短名称
	 * @param itemId	: 对象ID
	 * @return			: 对象，调用处进行强制类型转换
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public <T> T getItem(String clazz,Object itemId);
	
	/**
	 * 
	 * @methodName		: getCacheObj
	 * @description	: 获取指定类名的指定ID的缓存对象
	 * @param <T>		: 对象类型
	 * @param clazz		: 类名，短名称
	 * @param itemId	: 对象ID
	 * @return
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public <T> CacheObj<T> getCacheObj(String clazz,Object itemId);
	
	/**
	 * 
	 * @methodName		: setItem
	 * @description	: 设置指定ID的对象值
	 * @param <T>		: 对象类型
	 * @param clazz		: 类名，短名称
	 * @param itemId	: 对象ID
	 * @param item		: 对象值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/17	1.0.0		sheng.zheng		初版
	 *
	 */
	public <T> void setItem(String clazz,Object itemId,T item);
	
	/**
	 * 
	 * @methodName		: removeItem
	 * @description	: 移除指定类名的指定ID的对象
	 * @param clazz		: 类名，短名称
	 * @param itemId	: 对象ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeItem(String clazz,Object itemId);
	
	/**
	 * 
	 * @methodName		: removeExpireObj
	 * @description	: 移除过期的对象，在定时器中执行
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeExpireObj();
	
}
