package com.abc.example.service;

import com.abc.example.entity.Function;
import com.abc.example.common.tree.TreeNode;

/**
 * @className		: FunctionTreeService
 * @description	: 功能树服务
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface FunctionTreeService {

	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据库中数据 
	 * @return			: 成功返回true，否则返回false。
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
	 * @methodName		: getFunctionTree
	 * @description	: 获取整个功能树
	 * @return			: 完整的功能树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<Function> getFunctionTree();
	
	/**
	 * 
	 * @methodName		: getItemByKey
	 * @description	: 根据key获取对象
	 * @param funcId	: 功能ID
	 * @param refresh	: true表示查询数据库强制刷新,false为从内存中取值
	 * @return			: Function对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Function getItemByKey(Integer funcId,boolean refresh);
	
	/**
	 * 
	 * @methodName		: removeItemByKey
	 * @description	: 移除指定功能ID的功能项
	 * @param funcId	: 功能ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeItemByKey(Integer funcId);
}
