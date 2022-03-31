package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.tree.TreeNode;
import com.abc.example.dao.OrgnizationDao;
import com.abc.example.entity.Orgnization;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.service.BaseCommonService;
import com.abc.example.service.OrgnizationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @className		: OrgnizationServiceImpl
 * @description	: OrgnizationService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/08/16	1.0.0		sheng.zheng		初版
 *
 */
@Slf4j
@Service
public class OrgnizationServiceImpl extends BaseCommonService implements OrgnizationService {
	
	@Autowired 
	private OrgnizationDao orgnizationDao; 	
		
	// 组织ID与组织信息对象映射表
	private Map<Integer,Orgnization> orgnizationMap = new HashMap<Integer,Orgnization>();
	
	// 组织树
	private TreeNode<Orgnization> orgnizationTree = new TreeNode<Orgnization>();
		
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
	@Override
	public boolean loadData() {
		// 查询全部组织数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleteFlag", EDeleteFlag.dfValidE.getCode());
		List<Orgnization> orgnizationList = orgnizationDao.selectItemsByCondition(params);
		
		synchronized(this) {
			//先清空map，便于刷新调用
			orgnizationMap.clear();
			orgnizationTree.clear();
			dacService.clear();
			
			//将查询结果放入map对象中，按每个类别组织
			for(Orgnization item : orgnizationList) {
				Integer itemKey = item.getOrgId();
				orgnizationMap.put(itemKey, item);				
			}
			
			//设置根节点
			setRootNode(orgnizationTree);	
			//将组织信息对象列表加载到组织树中
			List<Orgnization> errorList = orgnizationTree.loadData(orgnizationList);
			//检查是否有错误信息
			if (errorList.size() > 0) {					
				//写日志
				for(Orgnization item : errorList) {
					log.error("orgnizationTree error with item : " + item.toString());
				}
				return true;
			}			
		}

		return true;		
	}
	
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
	@Override
	public TreeNode<Orgnization> getOrgnizationNode(Integer orgId){
		TreeNode<Orgnization> node = null;
		node = orgnizationTree.lookUpSubNode(orgId);
		return node;		
	}
	
	/**
	 * 
	 * @methodName		: getItemByKey
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
	@Override
	public Orgnization getItemByKey(Integer orgId,boolean refresh) {
		Orgnization item = null;	
		Integer itemKey = orgId;
		if (refresh == true) {
			procNoItemData(itemKey,null);
		}
		item = getItem(itemKey,null);
		if (item == null && refresh == false) {
			// 如果itemKey不存在
			if (procNoItemData(itemKey,null)) {
				// 如果加载成功
				item = getItem(itemKey,null);
			}
		}
		return item;		
	}
	
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
	@Override
	public TreeNode<Orgnization> getOrgnizationTree(){		
		return orgnizationTree;
	}
	
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
	@Override
	public List<Integer> getSubOrgIdList(Integer orgId){	
		List<Integer> orgIdList = new ArrayList<Integer>();
		TreeNode<Orgnization> rootNode = orgnizationTree.lookUpSubNode(orgId);
		if (rootNode == null) {
			return orgIdList;
		}
		List<TreeNode<Orgnization>> orgList = rootNode.getNodeList(null);
		for (TreeNode<Orgnization> item : orgList) {
			orgIdList.add(item.getNodeData().getOrgId());
		}

		return orgIdList;
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
	 * 2021/08/03	1.0.0		sheng.zheng		初版
	 *
	 */	
	@SuppressWarnings("unchecked")
	@Override
	protected Orgnization getItem(Object itemKey,Object classKey) {
		Orgnization item = null;
		if (orgnizationMap.containsKey((Integer)itemKey)) {
			item = orgnizationMap.get((Integer)itemKey);
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
	 * 2021/08/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Orgnization fetchItem(Object itemKey,Object classKey) {
		return orgnizationDao.selectItemByKey((int)itemKey);
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
	 * 2021/08/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected <T> void setItem(Object itemKey,Object classKey,T item) {		
		Orgnization newItem = (Orgnization)item;
		
		if (newItem.getDeleteFlag() != EDeleteFlag.dfValidE.getCode()) {
			return;
		}
		
		// 如果非禁用
		synchronized(this) {
			Integer orgId = (Integer)itemKey;
			if(orgnizationMap.containsKey(orgId)) {
				// 如果key已存在，更新操作
				// 获取旧值
				Orgnization oldItem = orgnizationMap.get(orgId);
				// 更新值
				orgnizationMap.put(orgId, newItem);
				// 比较父节点
				if (oldItem.getParentId() != newItem.getParentId()) {
					// 如果父节点有变化
					// 刷新组织树
					refreshTree();
				}
			}else {
				// 如果key不存在，新增操作
				// 更新值
				orgnizationMap.put(orgId, newItem);
				// 刷新组织树
				refreshTree();
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
	protected void logInfo(String methodName,String param) {
		switch(methodName) {
		case "loadItem":
			log.info("Failed to query item data with orgId = " + param);
			break;
		default:
			break;
		}
	}	
	
	/**
	 * 
	 * @methodName		: setRootNode
	 * @description	: 设置根节点
	 * @param node		: 输入的组织树根节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/06/25	1.0.0		sheng.zheng		初版
	 *
	 */
	private void setRootNode(TreeNode<Orgnization> node) {
		node.setParent(null);
		//创建空节点数据
		node.setNodeData(new Orgnization());
		//约定根节点的节点ID为0
		node.getNodeData().setOrgId(0);
		node.getNodeData().setOrgName("root");
		node.getNodeData().setParentId(-1);
		//根节点总是包含的
		node.setIsIncluded(1);		
	}			
	
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
	@Override
	public TreeNode<Orgnization> getOrgnizationNode(List<Integer> orgIdList){
		// 复制组织树
		TreeNode<Orgnization> newTree = orgnizationTree.clone();
		for(Integer orgId : orgIdList) {
			TreeNode<Orgnization> node = newTree.lookUpSubNode(orgId);
			if (node != null) {
				node.setIsIncluded(1);
			}
		}
		// 剪枝处理
		newTree.arrange();
		return newTree;
	}
	
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
	@Override
	public List<Integer> getAllOrgIdList(){
		List<Integer> orgList = new ArrayList<Integer>(orgnizationMap.keySet());
		return orgList;
	}
	
	/**
	 * 
	 * @methodName		: refreshTree
	 * @description	: 刷新组织树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/12/20	1.0.0		sheng.zheng		初版
	 *
	 */
	private void refreshTree() {
		List<Orgnization> orgList = new ArrayList<Orgnization>(orgnizationMap.values());
		orgnizationTree.clear();
		orgnizationTree.loadData(orgList);
	}
	
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
	@Override
	public Orgnization removeItem(Integer orgId) {
		Orgnization item = null;

		if (orgnizationMap.containsKey(orgId)) {
			synchronized(this) {
				item = orgnizationMap.get(orgId);
				orgnizationMap.remove(orgId);
				// 刷新组织树
				refreshTree();				
			}
		}
		return item;
	}
	
}
