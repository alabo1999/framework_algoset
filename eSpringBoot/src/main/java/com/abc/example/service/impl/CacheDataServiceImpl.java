package com.abc.example.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.abc.example.dao.OrgnizationDao;
import com.abc.example.entity.Orgnization;
import com.abc.example.service.CacheDataService;
import com.abc.example.vo.CacheObj;

/**
 * @className		: CacheDataServiceImpl
 * @description	: 缓存数据服务实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/06/04	1.0.0		sheng.zheng		初版
 *
 */
@EnableScheduling  //开启定时任务
@EnableAsync
@Service
public class CacheDataServiceImpl implements CacheDataService {
	// 查询失败后，再次查询的时间间隔（毫秒）
	private final long queryInterval = 60*1000;

	// 访问到期的时间间隔（毫秒）,7天
	private final long accInterval = 7*24*3600*1000;
	
	@Autowired 
	private OrgnizationDao orgnizationDao; 		
		
	// 企业单位缓存对象，key为orgId
	Map<Integer,CacheObj<Orgnization>> orgMap = new HashMap<Integer,CacheObj<Orgnization>>();
	
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
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getItem(String clazz,Object itemId) {
		T item = null;
		
		switch(clazz) {
		case "Orgnization":
			item = (T)getObjItem(clazz,orgMap,(Integer)itemId,accInterval,queryInterval,true);
			break;				
		default:
			break;
		}
		return item;
	}
	
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
	@SuppressWarnings("unchecked")
	@Override
	public <T> CacheObj<T> getCacheObj(String clazz,Object itemId){
		CacheObj<T> item = null;
		switch(clazz) {
		case "Orgnization":
			item = (CacheObj<T>) getCacheObjItem(clazz,orgMap,(Integer)itemId,accInterval,queryInterval,true);				
			break;				
		default:
			break;
		}
		return item;
	}

	/**
	 * 
	 * @methodName		: setItem
	 * @description	: 设置指定ID的对象值
	 * @summary			: 如果外部新增对象，可直接设置缓存，避免再次查询数据库
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
	@Override
	public <T> void setItem(String clazz,Object itemId,T item) {
		switch(clazz) {
		case "Orgnization":
			setObjItem(clazz,orgMap,(Integer)itemId,(Orgnization)item,
					accInterval,queryInterval,true);			
			break;	
		default:
			break;
		}		
	}
	
	/**
	 * 
	 * @methodName		: getObjItem
	 * @description	: 根据对象ID获取对象
	 * @param <T>		: 缓存对象类型
	 * @param <E>		: 缓存对象字典的key的数据类型
	 * @param clazz		: 缓存对象类名
	 * @param cacheMap	: 缓存对象字典
	 * @param itemId	: 对象ID
	 * @param accInterval	: 到期时间间隔
	 * @param queryInterval	: 对象为null时，可查询的时间间隔
	 * @param refresh	: 访问是否更新到期时间
	 * @return			: 对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/09	1.0.0		sheng.zheng		初版
	 *
	 */
	private <T,E> T getObjItem(String clazz,Map<E,CacheObj<T>> cacheMap,E itemId,
			long accInterval,long queryInterval,boolean refresh) {
		
		CacheObj<T> cacheObj = null;
		T item = null;
		// 获取缓存对象
		cacheObj = getCacheObjItem(clazz,cacheMap,itemId,accInterval,queryInterval,refresh);
		item = cacheObj.getItemObj();
		return item;
	}	
	
	/**
	 * 
	 * @methodName		: getCacheObjItem
	 * @description	: 获取指定类名的指定ID的缓存对象
	 * @param <T>		: 缓存对象类型
	 * @param <E>		: 缓存对象字典的key的数据类型
	 * @param clazz		: 缓存对象类名
	 * @param cacheMap	: 缓存对象字典
	 * @param itemId	: 对象ID
	 * @param accInterval	: 到期时间间隔
	 * @param queryInterval	: 对象为null时，可查询的时间间隔
	 * @param refresh	: 访问是否更新到期时间
	 * @return
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	private <T,E> CacheObj<T> getCacheObjItem(String clazz,
			Map<E,CacheObj<T>> cacheMap,E itemId,
			long accInterval,long queryInterval,boolean refresh){
		// 获取当前时间戳
		long current = System.currentTimeMillis();
		CacheObj<T> cacheObj = null;
		T item = null;
		if (cacheMap.containsKey(itemId)) {
			cacheObj = cacheMap.get(itemId);
			if(!cacheObj.isExpired(current)) {
				// 如果未过期，直接获取对象
				item = cacheObj.getItemObj();				
			}else {
				// 如果已过期，则需要查询
				item = (T)fetchItem(clazz,itemId);
				// 更新最近查询时间
				cacheObj.setLastQueryTime(current);
				if (item != null) {
					cacheObj.setItemObj(item);
				}
				// 更新过期时间
				cacheObj.resetLastAccTime(current, accInterval);
			}			
		}else {
			// 如果itemId不在字典中，创建一个对象并加入字典中
			cacheObj = new CacheObj<T>();
			cacheMap.put(itemId, cacheObj);
			// 更新过期时间
			cacheObj.resetLastAccTime(current, accInterval);			
		}
		if (item == null) {
			if(cacheObj.canQuery(current, queryInterval)) {
				// 如果可以查询，则查询数据库
				item = (T)fetchItem(clazz,itemId);
				// 更新最近查询时间
				cacheObj.setLastQueryTime(current);
				if (item != null) {
					cacheObj.setItemObj(item);
				}
			}
		}
		// 更新最后访问时间
		if(refresh) {
			cacheObj.resetLastAccTime(current, accInterval);			
		}else {
			cacheObj.setLastAccTime(current);
		}
		
		return cacheObj;		
	}		
	
	/**
	 * 
	 * @methodName		: fetchItem
	 * @description	: 查询数据库，获取对象
	 * @param clazz		: 类名称
	 * @param itemId	: 对象ID
	 * @return			: 对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/09	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object fetchItem(String clazz,Object itemId) {
		Object item = null;
		switch(clazz) {
		case "Orgnization":
			item = orgnizationDao.selectItemByKey((Integer)itemId);
			break;
		default:
			break;
		}
		return item;
	}
	
	/**
	 * 
	 * @methodName		: setObjItem
	 * @description	: 设置缓存对象
	 * @param <T>		: 缓存对象类型
	 * @param <E>		: 缓存对象字典的key的数据类型
	 * @param clazz		: 缓存对象类名
	 * @param cacheMap	: 缓存对象字典
	 * @param itemId	: 对象ID
	 * @param itemObj	: 对象值
	 * @param accInterval	: 到期时间间隔
	 * @param queryInterval	: 对象为null时，可查询的时间间隔
	 * @param refresh	: 访问是否更新到期时间
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/17	1.0.0		sheng.zheng		初版
	 *
	 */
	private <T,E> void setObjItem(String clazz,
			Map<E,CacheObj<T>> cacheMap,E itemId,T itemObj,
			long accInterval,long queryInterval,boolean refresh) {
		// 获取当前时间戳
		long current = System.currentTimeMillis();
		CacheObj<T> cacheObj = null;
		// 确保存在此key的缓存对象
		if (cacheMap.containsKey(itemId)) {
			cacheObj = cacheMap.get(itemId);
		}else {
			cacheObj = new CacheObj<T>();
			cacheMap.put(itemId, cacheObj);			
		}
		// 设置对象值
		cacheObj.setItemObj(itemObj);
		cacheObj.setLastQueryTime(current);
		// 更新最后访问时间
		if(refresh) {
			cacheObj.resetLastAccTime(current, accInterval);			
		}else {
			cacheObj.setLastAccTime(current);
		}		
	}
	
	/**
	 * 
	 * @methodName		: removeItem
	 * @description	: 移除指定类名的指定ID的对象,
	 * @summary			: 如果数据发生改变或删除，则调用此方法
	 * @param clazz		: 类名，短名称
	 * @param itemId	: 对象ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/04	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void removeItem(String clazz,Object itemId) {
		switch(clazz) {
		case "Orgnization":
			removeItem(orgMap,(Integer)itemId);
			break;
		default:
			break;
		}	
	}
	
	/**
	 * 
	 * @methodName		: removeItem
	 * @description	: 根据key移除对象
	 * @param <T>		: 对象类型
	 * @param <E>		: key数据类型
	 * @param cacheMap	: 缓存字典
	 * @param itemId	: 对象key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/09	1.0.0		sheng.zheng		初版
	 *
	 */
	private <T,E> void removeItem(Map<E,CacheObj<T>> cacheMap,E itemId) {
		if (cacheMap.containsKey(itemId)) {
			cacheMap.remove(itemId);
		}		
	}
	
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
	@Scheduled(cron = "0 0 1 * * ?")
	@Async
	@Override
	public void removeExpireObj() {
		

		// 使用迭代器来遍历同时移除对象，下同
		
		// 处理企业单位对象
		synchronized(orgMap) {
			checkAndremove(orgMap);
		}		
	}	
	
	/**
	 * 
	 * @methodName		: checkAndremove
	 * @description	: 检查并移除过期对象
	 * @param <T>		: 对象类型
	 * @param <E>		: key数据类型
	 * @param cacheMap	: 缓存字典
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/09	1.0.0		sheng.zheng		初版
	 *
	 */
	private <T,E> void checkAndremove(Map<E,CacheObj<T>> cacheMap) {
		Iterator<Map.Entry<E,CacheObj<T>>> iter = cacheMap.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<E,CacheObj<T>> entry = iter.next();
			CacheObj<T> cacheObj = entry.getValue();
			if (cacheObj.isExpired()) {
				// 如果过期，移除
				iter.remove();
			}
		}		
	}
}
