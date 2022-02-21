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
	 * @methodName		: getParameterClass
	 * @description	: 获取指定classKey的参数类别的子项列表
	 * @param classKey	: 参数类别key
	 * @return			: 指定classKey的参数类别的子项列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<SysParameter> getParameterClass(String classKey);
	
	/**
	 * 
	 * @methodName		: getParameterItemByKey
	 * @description	: 根据classKey和itemKey获取参数子项
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @return			: SysParameter对象
	 * @history		: 
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public SysParameter getParameterItemByKey(String classKey,String itemKey);
	
	/**
	 * 
	 * @methodName		: getParameterItemByValue
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
	public SysParameter getParameterItemByValue(String classKey,String itemValue);
}
