package com.abc.example.service;

import java.util.List;

import com.abc.example.common.tree.TreeNode;
import com.abc.example.entity.Orgnization;

/**
 * @className		: OrgnizationService
 * @description	: 组织信息服务类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/08/16	1.0.0		sheng.zheng		初版
 *
 */
public interface OrgnizationService{
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据，包括重新加载数据，由子类重载
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean loadData();
	
	/**
	 * 
	 * @methodName		: getOrgnizationNode
	 * @description	: 获取指定组织ID的树节点
	 * @param orgId		: 组织ID
	 * @return			: 组织树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<Orgnization> getOrgnizationNode(Integer orgId);
	
	/**
	 * 
	 * @methodName		: getOrgnizationItem
	 * @description	: 获取指定组织ID的组织对象
	 * @param orgId		: 组织ID
	 * @param refresh	: true表示查询数据库强制刷新,false为从内存中取值
	 * @return			: 组织对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public Orgnization getItemByKey(Integer orgId,boolean refresh);
	
	/**
	 * 
	 * @methodName		: getOrgnizationTree
	 * @description	: 获取组织树
	 * @return			: 组织树根节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<Orgnization> getOrgnizationTree();
	
	/**
	 * 
	 * @methodName		: getSubOrgIdList
	 * @description	: 获取指定组织ID的树节点及子节点的组织ID列表
	 * @param orgId		: 组织ID
	 * @return			: 组织ID列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Integer> getSubOrgIdList(Integer orgId);
	
	/**
	 * 
	 * @methodName		: getOrgnizationNode
	 * @description	: 获取指定组织ID列表的组织树
	 * @param orgIdList	: 组织ID列表
	 * @return			: 组织树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<Orgnization> getOrgnizationNode(List<Integer> orgIdList);
	
	/**
	 * 
	 * @methodName		: getAllOrgIdList
	 * @description	: 获取全部组织ID
	 * @return			: 组织ID列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/12/19	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Integer> getAllOrgIdList();	
	
	/**
	 * 
	 * @methodName		: removeItem
	 * @description	: 移除组织对象
	 * @param orgId		: 组织ID
	 * @return			: 被移除的组织对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/12/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public Orgnization removeItem(Integer orgId);
}
