package com.abc.example.common.tree;

/**
 * @className		: ITreeNodeData
 * @description	: 树节点数据接口类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface ITreeNodeData extends Cloneable{

	//=============节点基本属性访问接口==============================
	// 获取节点ID
	int getNodeId();

	// 获取节点名称
	String getNodeName();

	// 获取父节点ID
	int getParentId();
	
	//=============Cloneable类接口===================================
	// 克隆
	public Object clone();
}
