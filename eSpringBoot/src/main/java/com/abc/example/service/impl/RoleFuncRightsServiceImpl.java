package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.dao.RoleFuncRightsDao;
import com.abc.example.entity.Function;
import com.abc.example.entity.RoleFuncRights;
import com.abc.example.service.RoleFuncRightsService;
import com.abc.example.common.tree.TreeNode;
import com.abc.example.common.utils.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @className		: RoleFuncRightsServiceImpl
 * @description	: RoleFuncRightsService实现类
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
public class RoleFuncRightsServiceImpl implements RoleFuncRightsService{

	// role_func_rights表数据访问对象
	@Autowired	
	private RoleFuncRightsDao roleFuncRightsDao;
	
	// 原子角色集ID的权限树map,key为roleId
	private Map<Integer,TreeNode<Function>> roleIdRightsMap = 
			new HashMap<Integer,TreeNode<Function>>();

	// 角色集合={roleId1,..,roleIdn}
	// 各角色集合的权限树map,key为roleId正序列表转字符串
	private Map<String,TreeNode<Function>> roleIdSetRightsMap = 
			new HashMap<String,TreeNode<Function>>();
	
	// 各角色集合的url对应的功能节点的权限值，只使用url非空串的值
	private Map<String,Map<String,Integer>> roleIdUrlMap = 
			new HashMap<String,Map<String,Integer>>();
	
	// 完整的功能树，由父对象设置
	private TreeNode<Function> functionTree;
	
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
			// 查询role_func_rights表，获取全部数据
			List<RoleFuncRights> roleFuncRightsList = roleFuncRightsDao.selectAllItems();
			
			// 加锁，进行保护，防止脏读
			synchronized(this) {
				// 先清空roleIdRightsMap，便于刷新调用
				roleIdRightsMap.clear();
				roleIdSetRightsMap.clear();
				roleIdUrlMap.clear();
				
				// 将查询结果放入roleIdRightsMap对象中，按每个角色ID组织
				TreeNode<Function> roleFunctionTree = null;
				for(RoleFuncRights item : roleFuncRightsList) {
					Integer roleId = item.getRoleId();
					
					// 取得或创建roleId对应的功能树
					if (roleIdRightsMap.containsKey(roleId)) {
						// 如果存在roleId的key，则获取value
						roleFunctionTree = roleIdRightsMap.get(roleId);
					}else {
						// 如果不存在roleId的key，则复制完整功能树
						roleFunctionTree = functionTree.clone();
						// 加入roleIdRightsMap中
						roleIdRightsMap.put(roleId, roleFunctionTree);
					}
					
					// 处理当前功能节点
					int funcId = item.getFuncId();
					// 在角色功能树中，查找节点
					TreeNode<Function> treeNode = roleFunctionTree.lookUpSubNode(funcId);
					if (treeNode == null) {
						// 如果funcId在完整树中未找到
						log.info("Something is wrong in table role_func_rights with func_id = " + funcId);
						// 跳过
						continue;
					}
					
					// 处理subFullFlag字段
					if (item.getSubFullFlag() == 1) {
						// 如果包含全部子节点，设置子节点权限
						treeNode.setChildrenIsIncluded(1);
					}
					
					// 当前节点权限
					treeNode.setIsIncluded(1);			
				}
				
				
				// 建立角色的url与权限映射关系
				for (Integer roleId : roleIdRightsMap.keySet()) {
					// 为原子角色ID创建剪枝的功能树
					roleFunctionTree = roleIdRightsMap.get(roleId);
					List<Integer> roleIdList = new ArrayList<Integer>();
					roleIdList.add(roleId);
					String roleIdStr = roleIdList.toString();
					// 为权限访问，再复制一棵树
					TreeNode<Function> roleIdFunctionTree = roleFunctionTree.clone();
					// 创建该角色对url的权限，必须用剪枝之前的完整树
					createRoleIdUrlMapItem(roleIdStr,roleFunctionTree);
					// 剪枝处理
					roleIdFunctionTree.arrange();
					// 加入roleIdSetRightsMap中
					roleIdSetRightsMap.put(roleIdStr, roleIdFunctionTree);
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
	 * @methodName		: getRoleRightsTree
	 * @description	: 获取指定角色ID的权限
	 * @param roleIds	: 角色ID组合，可以原子的角色ID，也可以是组合的角色ID
	 * @return			: 权限对应的功能树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public TreeNode<Function> getRoleRightsTree(List<Integer> roleIdList){
		// 组合角色的功能树
		TreeNode<Function> rolesFunctionTree = null;
		// 正序排序
		roleIdList.sort(Comparator.comparingInt(Integer::intValue));
		// 角色ID列表输出字符串，作为key
		String roleIdsStr = roleIdList.toString();
		
		// 先检查roleIdSetRightsMap中是否存在key为roleIdsStr的数据
		if (roleIdSetRightsMap.containsKey(roleIdsStr)) {
			// 如果存在该roleIdsStr对应的数据
			rolesFunctionTree = roleIdSetRightsMap.get(roleIdsStr);
			return rolesFunctionTree;
		}
		
		// key为roleIdsStr的数据不存在，表示为新的角色ID组合
		// 取得原子角色ID的功能树，进行合并
		for (int i = 0; i < roleIdList.size(); i++) {
			// 构造原子角色ID的key
			Integer roleId = roleIdList.get(i);
			if (roleIdRightsMap.containsKey(roleId)) {
				// 如果包含此角色ID
				// 取得该角色ID对应的功能树
				TreeNode<Function> roleFunctionTree = roleIdRightsMap.get(roleId);
				if (rolesFunctionTree == null) {
					// 如果组合功能树为null，则复制角色功能树
					rolesFunctionTree = roleFunctionTree.clone();
				}else {
					// 如果组合功能树不为null，将当前角色功能树并入组合功能树
					// 由于树结构是一致的，区别在于节点的isIncluded属性
					rolesFunctionTree.combineTreeNode(roleFunctionTree);
				}
			}
		}
		
		if (rolesFunctionTree != null) {
			// 建立角色的url与权限映射关系，必须用剪枝之前的完整树
			createRoleIdUrlMapItem(roleIdsStr,rolesFunctionTree);			

			// 剪枝处理
			rolesFunctionTree.arrange();
			// 然后加入roleIdSetRightsMap中，便于下次调用时获取
			// 多线程调用，可能存在访问冲突，加锁保护
			synchronized(roleIdSetRightsMap) {
				if (!roleIdSetRightsMap.containsKey(roleIdsStr)) {
					// 如果key不存在，则加入
					roleIdSetRightsMap.put(roleIdsStr, rolesFunctionTree);
				}
			}
		}
		
		return rolesFunctionTree;
	}
	
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
	@Override
	public Integer getRoleUrlRights(List<Integer> roleIdList,String url) {
		
		if (url.equals("")) {
			// 如果为空串，表示该url未纳入权限管理
			return 1;
		}
		
		// 如果url未找到，表示未纳入权限管理，默认为有权限
		Integer rights = 1;
		
		// 正序排序
		roleIdList.sort(Comparator.comparingInt(Integer::intValue));
		// 角色ID列表输出字符串，作为key
		String roleIdsStr = roleIdList.toString();
		if (roleIdUrlMap.containsKey(roleIdsStr)) {
			Map<String, Integer> urlMap = roleIdUrlMap.get(roleIdsStr);
			if (urlMap.containsKey(url)) {
				rights = urlMap.get(url);
			}
		}
		return rights;
	}
	
	/**
	 * 
	 * @methodName		: setFunctionTree
	 * @description	: 设置完整功能树对象
	 * @param functionTree：完整功能树对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setFunctionTree(TreeNode<Function> functionTree) {
		this.functionTree = functionTree;
	}	
	
	/**
	 * 
	 * @methodName		: createRoleIdUrlMapItem
	 * @description	: 根据角色功能树，创建该角色url对应权限映射项
	 * @param roleIdsStr	: 角色ID的正序列表字符串输出值
	 * @param roleFunctionTree: 角色功能树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void createRoleIdUrlMapItem(String roleIdsStr,TreeNode<Function> roleFunctionTree) {
		if (roleIdUrlMap.containsKey(roleIdsStr)) {
			// 如果已包含该key，则返回
			return;
		}
		
		Map<String,Integer> urlRightsMap = new HashMap<String,Integer>();
		// 获取树节点列表
		List<TreeNode<Function>> nodeList = roleFunctionTree.getNodeList(null);
		// 遍历角色树节点列表
		for (int j = 0; j < nodeList.size(); j++) {
			TreeNode<Function> subItem = nodeList.get(j);
			String url = subItem.getNodeData().getUrl();
			if (url != null && !url.equals("")) {
				// 如果url非空，加入urlRightsMap中
				urlRightsMap.put(url, subItem.getIsIncluded());
			}
		}
				
		// 将urlRightsMap加入roleIdUrlMap中，考虑可能的访问冲突，加锁保护
		synchronized(roleIdUrlMap) {
			if (!roleIdUrlMap.containsKey(roleIdsStr)) {
				// 如果key不存在，则加入
				roleIdUrlMap.put(roleIdsStr, urlRightsMap);
			}
		}				
	}	
	
	/**
	 * 
	 * @methodName		: getRoleIdTree
	 * @description	: 获取指定角色ID的权限
	 * @param roleIdList	: 角色ID
	 * @return			: 角色ID对应的功能树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public TreeNode<Function> getRoleIdTree(Integer roleId){
		// 组合ID的功能树
		TreeNode<Function> roleIdTree = null;
		if (roleIdRightsMap.containsKey(roleId)) {
			roleIdTree = roleIdRightsMap.get(roleId); 
		}
		return roleIdTree;
	}
}
