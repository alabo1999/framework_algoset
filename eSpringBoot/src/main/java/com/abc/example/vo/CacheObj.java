package com.abc.example.vo;

import lombok.Data;

/**
 * @className		: CacheObj
 * @description	: 缓存对象类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/06/04	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class CacheObj<T> {
	// 缓存对象
	private T itemObj;
	// 最后访问时间，UTC
	private long lastAccTime = 0;
	// 到期时间，UTC
	private long expiredTime = 0;
	// 最后查询数据库时间，UTC
	private long lastQueryTime = 0;

	/**
	 * 
	 * @methodName		: resetLastAccTime
	 * @description	: 重置最后访问时间，同时更新到期时间
	 * @param accTime	: 访问时间
	 * @param interval	: 到期时间间隔
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public void resetLastAccTime(long accTime,long interval) {
		lastAccTime = accTime;
		expiredTime = accTime + interval;
	}
	
	// 是否到期
	public boolean isExpired() {
		return (lastAccTime >= expiredTime);
	}
	
	// 是否到期
	public boolean isExpired(long current) {
		return (current >= expiredTime);
	}	
	
	// 是否可以查询
	public boolean canQuery(long current,long interval) {
		return (current >= lastQueryTime + interval);
	}
}
