package com.abc.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.dao.FunctionDao;
import com.abc.example.entity.Function;
import com.abc.example.service.BaseCommonService;
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
public class FunctionTreeServiceImpl extends BaseCommonService implements FunctionTreeService {
	
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
	@Override
	public Function getItemByKey(Integer funcId,boolean refresh) {
		Function item = null;
		if (refresh == true) {
			procNoItemData(funcId,null);
		}
		item = getItem(funcId,null);
		return item;
	}
	
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
	@Override
	public void removeItemByKey(Integer funcId) {
		TreeNode<Function> node = null;
		node = functionTree.lookUpSubNode(funcId);
		if (node != null) {
			TreeNode<Function> parentNode = null;
			parentNode = node.getParent();
			if (parentNode != null) {
				parentNode.removeChildNode(node);
			}
		}
	}
	
	/**
	 * 
	 * @methodName		: getItem
	 * @description	: 根据对象key和组key，获取对象，子类需重载
	 * @param <T>		: T类型对象
	 * @param itemKey	: 对象key值
	 * @param classKey	: 对象组key值
	 * @return			: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */	
	@SuppressWarnings("unchecked")
	@Override
	protected Function getItem(Object itemKey,Object classKey) {
		Function item = null;
		TreeNode<Function> node = null;
		Integer funcId = (Integer)itemKey;
		node = functionTree.lookUpSubNode(funcId);
		if (node != null) {
			item = node.getNodeData();
		}
		return item;
	}
	
	/**
	 * 
	 * @methodName		: fetchItem
	 * @description	: 从数据库中查询一个对象
	 * @param <T>		: 泛型方法
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @return			: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Function fetchItem(Object itemKey,Object classKey) {
		return functionDao.selectItemByKey((Integer)itemKey);
	}	
	
	/**
	 * 
	 * @methodName		: setItem
	 * @description	: 将一个对象加入管理集合中
	 * @param <T>		: 泛型方法
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @param item		: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected <T> void setItem(Object itemKey,Object classKey,T item) {
		Function newItem = (Function)item;
		if (newItem.getDeleteFlag() != 0) {
			return;
		}
		
		synchronized(this) {
			// 更新功能树		
			int funcId = newItem.getFuncId();
			int parentId = newItem.getParentId();
			TreeNode<Function> node = null;
			TreeNode<Function> parentNode = null;
			// 获取当前节点
			node = functionTree.lookUpSubNode(funcId);
			if (node != null) {
				// 如果当前节点存在，表示修改
				node.setNodeData(newItem);
				
				parentNode = node.getParent();
				if (parentNode != null) {
					if (parentNode.getNodeData().getFuncId() != parentId) {
						// 如果父节点ID有变化
						parentNode.removeChildNode(node);
						// 获取父节点
						parentNode = functionTree.lookUpSubNode(parentId);
						if (parentNode != null) {
							parentNode.addChildNode(node);
						}
					}
				}
			}else {
				// 如果当前节点不存在，表示新增
				// 获取父节点
				parentNode = functionTree.lookUpSubNode(parentId);
				if (parentNode != null) {
					// 如果父节点存在
					node = new TreeNode<Function>();
					node.setNodeData(newItem);
					parentNode.addChildNode(node);
				}				
			}
		}		
	}
	
	/**
	 * 
	 * @methodName		: logInfo
	 * @description	: 写日志
	 * @param methodName: 方法名
	 * @param param		: 参数值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected void logInfo(String methodName,String param) {
		switch(methodName) {
		case "loadItem":
			log.info("Failed to query item data with funcId = " + param);
			break;
		default:
			break;
		}
	}		
}
