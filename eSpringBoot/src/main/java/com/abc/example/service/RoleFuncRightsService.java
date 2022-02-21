package com.abc.example.service;

import java.util.List;

import com.abc.example.entity.Function;
import com.abc.example.common.tree.TreeNode;

/**
 * @className		: RoleFuncRightsService
 * @description	: 角色和功能权限关系数据服务类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface RoleFuncRightsService {

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
	 * @methodName		: getRoleRightsTree
	 * @description	: 获取指定角色ID的权限
	 * @param roleIdList	: 角色ID列表
	 * @return			: 权限对应的功能树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<Function> getRoleRightsTree(List<Integer> roleIdList);
	
	/**
	 * 
	 * @methodName		: getRoleUrlRights
	 * @description	: 获取指定角色ID组合对给定url参数的权限
	 * @param roleIdList: 角色ID列表
	 * @param url		: 功能节点数据体的url属性
	 * @return			: 节点的权限
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Integer getRoleUrlRights(List<Integer> roleIdList,String url);
	
	/**
	 * 
	 * @methodName		: setFunctionTree
	 * @description	: 设置完整功能树对象
	 * @param functionTree: 完整功能树对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setFunctionTree(TreeNode<Function> functionTree);
		
}
