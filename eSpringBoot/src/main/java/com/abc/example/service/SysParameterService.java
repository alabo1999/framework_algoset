package com.abc.example.service;

import java.util.List;

import com.abc.example.entity.SysParameter;

/**
 * @className		: SysParameterService
 * @description	: 系统参数数据服务
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface SysParameterService{
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据，包括重新加载数据，由子类重载
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
	 * @methodName		: getItemsByClass
	 * @description	: 获取指定classKey的参数类别的子项列表
	 * @param classKey	: 参数类别key
	 * @param refresh	: true表示查询数据库强制刷新,false为从内存中取值
	 * @return			: 指定classKey的参数类别的子项列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<SysParameter> getItemsByClass(String classKey,boolean refresh);
	
	/**
	 * 
	 * @methodName		: getItemByKey
	 * @description	: 根据classKey和itemKey获取参数子项
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @param refresh	: true表示查询数据库强制刷新,false为从内存中取值
	 * @return			: SysParameter对象
	 * @history		: 
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public SysParameter getItemByKey(String classKey,String itemKey,boolean refresh);
	
	/**
	 * 
	 * @methodName		: getItemByValue
	 * @description	: 根据classKey和itemValue获取参数子项
	 * @param classKey	: 参数类别key	
	 * @param itemValue	: 子项值
	 * @return			: SysParameter对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public SysParameter getItemByValue(String classKey,String itemValue);
	
	/**
	 * 
	 * @methodName		: removeItemByKey
	 * @description	: 根据classKey和itemKey移除参数子项
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/17	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeItemByKey(String classKey,String itemKey);
	
	/**
	 * 
	 * @methodName		: removeItemsByClass
	 * @description	: 根据classKey移除参数类别
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/17	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeItemsByClass(String classKey);	
}
