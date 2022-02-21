package com.abc.example.service;

/**
 * @className		: GlobalConfigService
 * @description	: 全局变量管理类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface GlobalConfigService {
	
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据 
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean loadData();
	
	/**
	 * 
	 * @methodName		: getDataServiceObject
	 * @description	: 根据数据服务对象key，获取数据服务对象
	 * @param dsoKey	: 数据服务对象key
	 * @return			: 数据服务对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Object getDataServiceObject(String dsoKey);	
}
