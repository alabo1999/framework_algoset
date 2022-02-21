package com.abc.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.dao.FunctionDao;
import com.abc.example.entity.Function;
import com.abc.example.service.FunctionTreeService;
import com.abc.example.common.tree.TreeNode;
import com.abc.example.common.utils.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @className		: FunctionTreeServiceImpl
 * @description	: FunctionTreeService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Slf4j
@Service
public class FunctionTreeServiceImpl implements FunctionTreeService {
	
	// functions表数据访问对象
	@Autowired
	private FunctionDao functionDao;
	
	// 功能树对象
	private TreeNode<Function> functionTree = new TreeNode<Function>();
			
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
	@Override
	public boolean loadData() {
		
		try {
			// 查询function_tree表，获取全部数据
			List<Function> functionList = functionDao.selectAllItems();
			
			// 加锁保护，防止脏读
			synchronized(this) {
				functionTree.clear();
								
				// 设置根节点
				setRootNode(functionTree);	
				// 将功能信息对象列表加载到功能树中
				List<Function> errorList = functionTree.loadData(functionList);
				// 检查是否有错误信息
				if (errorList.size() > 0) {					
					// 写日志
					for(Function item : errorList) {
						log.error("FunctionTree error with item : " + item.toString());
					}
					// 此时，functionTree是剔除了异常数据的功能树
					// 返回true或false，视业务需求而定
					return true;
				}
			}							
		}catch(Exception e) {
			LogUtil.error(e);
			return false;
		}
		
		return true;
	 }
		
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
	@Override
	public TreeNode<Function> getFunctionTree(){
		return functionTree;
	}	

	
	/**
	 * 
	 * @methodName		: setRootNode
	 * @description	: 设置根节点
	 * @param node		: 输入的功能树根节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void setRootNode(TreeNode<Function> node) {
		node.setParent(null);
		// 创建空节点数据
		node.setNodeData(new Function());
		// 约定根节点的节点ID为0
		node.getNodeData().setFuncId(0);
		node.getNodeData().setFuncName("root");
		node.getNodeData().setParentId(-1);
		node.getNodeData().setLevel((byte)0);
		node.getNodeData().setOrderNo(0);
		node.getNodeData().setDomKey("");
		node.getNodeData().setUrl("");
		node.getNodeData().setImgTag("");
		// 根节点总是包含的
		node.setIsIncluded(1);		
	}	
}
