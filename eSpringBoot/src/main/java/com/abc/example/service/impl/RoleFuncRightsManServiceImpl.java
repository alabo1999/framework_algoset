package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.tree.TreeNode;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.dao.RoleFuncRightsDao;
import com.abc.example.entity.Function;
import com.abc.example.entity.RoleFuncRights;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.FunctionTreeService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.RoleFuncRightsManService;
import com.abc.example.service.RoleFuncRightsService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @className	: RoleFuncRightsManServiceImpl
 * @description	: 角色和功能权限关系对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class RoleFuncRightsManServiceImpl extends BaseService implements RoleFuncRightsManService{
	// 角色和功能权限关系对象数据访问类对象
	@Autowired
	private RoleFuncRightsDao roleFuncRightsDao;
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;
	
//	// 数据权限服务类对象
//	@Autowired
//	private DataRightsService drs;
//	
	// 公共配置数据服务类对象
	@Autowired
	private GlobalConfigService gcs;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个角色和功能权限关系对象
	 * @param request	: request对象
	 * @param item		: 角色和功能权限关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItem(HttpServletRequest request, RoleFuncRights item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		item.setOperatorName(operatorName);
		
		// 插入数据
		try {
			roleFuncRightsDao.insertItem(item);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleFunctionE, null, item, EDataOperationType.dotLoadE);
		
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批角色和功能权限关系对象
	 * @param request	: request对象
	 * @param itemList	: 角色和功能权限关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItems(HttpServletRequest request, List<RoleFuncRights> itemList) {
		// 输入参数校验
		checkValidForParams(request, "addItems", itemList);
		
		if (itemList.size() == 0) {
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		for(int i = 0; i < itemList.size(); i++) {
			RoleFuncRights aObj = itemList.get(i);
			aObj.setOperatorName(operatorName);
		}
		
		// 插入数据
		try {
			roleFuncRightsDao.insertItems(itemList);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleFunctionE, null, null, EDataOperationType.dotLoadE);
		
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个角色和功能权限关系对象
	 * @param request	: request对象
	 * @param params	: 角色和功能权限关系对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void editItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "editItem", params);
		
		// 获取对象
		Integer roleId = (Integer)params.get("roleId");
		Integer funcId = (Integer)params.get("funcId");
		RoleFuncRights oldItem = roleFuncRightsDao.selectItemByKey(roleId,funcId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			roleFuncRightsDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleFunctionE, null, null, EDataOperationType.dotLoadE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个角色和功能权限关系对象
	 * @param request	: request对象
	 * @param params	: 角色和功能权限关系对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItem", params);
		
		// 获取对象
		Integer roleId = (Integer)params.get("roleId");
		Integer funcId = (Integer)params.get("funcId");
		RoleFuncRights oldItem = roleFuncRightsDao.selectItemByKey(roleId,funcId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 检查删除标记
		Integer deleteFlag = (Integer)params.get("deleteFlag");
		if (deleteFlag == null) {
			// 默认为停用
			deleteFlag = EDeleteFlag.dfDeletedE.getCode();
			params.put("deleteFlag", deleteFlag);
		}
		if (oldItem.getDeleteFlag() == deleteFlag.byteValue()) {
			// 不变
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			roleFuncRightsDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleFunctionE, null, null, EDataOperationType.dotLoadE);
		
	}
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个角色和功能权限关系对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"funcId"	: 0,	// 功能ID，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItems(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItems", params);
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			// 查询修改前的数据
			// List<RoleFuncRights> oldItemList = roleFuncRightsDao.selectItems(params);
			
			roleFuncRightsDao.deleteItems(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.DELETE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleFunctionE, null, null, EDataOperationType.dotLoadE);
		
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询角色和功能权限关系对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"funcId"	: 0,	// 功能ID，可选
	 * 		"pagenum"	: 1,	// 当前页码，可选
	 * 		"pagesize"	: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 角色和功能权限关系对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<RoleFuncRights> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		PageInfo<RoleFuncRights> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<RoleFuncRights> roleFuncRightsList = roleFuncRightsDao.selectItemsByCondition(params);
			// 分页对象
			pageInfo = new PageInfo<RoleFuncRights>(roleFuncRightsList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个角色和功能权限关系对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @return			: 角色和功能权限关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public RoleFuncRights getItem(HttpServletRequest request,
			 Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Integer roleId = (Integer)params.get("roleId");
		Integer funcId = (Integer)params.get("funcId");
		// 查询数据
		try {
			// 查询记录
			RoleFuncRights item = roleFuncRightsDao.selectItemByKey(roleId,funcId);
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询角色和功能权限关系对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"	: 0,	// limit记录偏移量，可选
	 * 		"rows"		: 20,	// limit最大记录条数，可选
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"funcId"	: 0,	// 功能ID，可选
	 * 	}
	 * @return			: 角色和功能权限关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<RoleFuncRights> getItems(HttpServletRequest request,
			 Map<String, Object> params) {
		// 查询数据
		try {
			// 查询记录
			List<RoleFuncRights> itemList = roleFuncRightsDao.selectItems(params);
			return itemList;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getTree
	 * @description		: 根据角色获取功能树
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 	}
	 * @return			: 功能树字符串，形式如下：
	 * 	{
	 * 		"funcTree"	: "",		// 功能树JSON格式字符串
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Map<String, Object> getTree(HttpServletRequest request,Map<String, Object> params){
		// 输入参数校验
		checkValidForParams(request, "getTree", params);
		
		Integer roleId = (Integer)params.get("roleId");
		RoleFuncRightsService rfrs = (RoleFuncRightsService)gcs.getDataServiceObject("RoleFuncRightsService");
		TreeNode<Function> roleFuncTree = rfrs.getRoleIdTree(roleId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("funcTree", roleFuncTree.toString());
		return map;
	}
	
	/**
	 * @methodName		: setTree
	 * @description		: 设置指定角色的功能树
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 		"funcTree"	: "",	// 功能树JSON格式字符串
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */	
	@Override
	public void setTree(HttpServletRequest request,Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "setTree", params);
		
		Integer roleId = (Integer)params.get("roleId");
		String funcTreeStr = (String)params.get("funcTree");
		JSONObject jofuncTree = JSONObject.parseObject(funcTreeStr);
		// 先获取完整的功能树
		FunctionTreeService fts = (FunctionTreeService)gcs.getDataServiceObject("FunctionTreeService");		
		TreeNode<Function> roleFuncTree = fts.getFunctionTree().clone();	
		// 将JSON树的isIncluded属性设置到功能树中
		setRoleTree(jofuncTree,roleFuncTree);
		// 获取角色功能树列表
		List<RoleFuncRights> roleFuncList = new ArrayList<RoleFuncRights>();
		getRoleFuncList(roleFuncTree,roleFuncList,roleId);
		// 获取操作人账号
		String operatorName = getUserName(request);
		for(RoleFuncRights item : roleFuncList) {
			item.setOperatorName(operatorName);
		}
		// 先删除roleId的角色功能关系记录
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		roleFuncRightsDao.deleteItems(map);
		// 再批量insert
		roleFuncRightsDao.insertItems(roleFuncList);
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleFunctionE, null, null, EDataOperationType.dotLoadE);		
	}
	
	/**
	 * 
	 * @methodName		: setRoleTree
	 * @description	: 将JSON树的isIncluded属性设置到功能树中
	 * @param jofuncTree: JSON树
	 * @param roleFuncTree: 功能树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	private void setRoleTree(JSONObject jofuncTree,TreeNode<Function> roleFuncTree) {
		Integer isIncluded = jofuncTree.getInteger("isIncluded");
		JSONObject nodeData = jofuncTree.getJSONObject("nodeData");
		JSONArray children = jofuncTree.getJSONArray("children");
		// 获取节点数据
		Integer funcId = nodeData.getInteger("funcId");
		TreeNode<Function> node = null;
		node = roleFuncTree.lookUpSubNode(funcId);
		if (node != null) {
			node.setIsIncluded(isIncluded);
		}else {
			return;
		}
		// 处理子节点
		for (int i = 0; i < children.size(); i++) {
			JSONObject subItem = children.getJSONObject(i);
			setRoleTree(subItem,node);
		}
	}
	
	/**
	 * 
	 * @methodName		: getRoleFuncList
	 * @description	: 获取角色功能关系列表
	 * @param roleFuncTree: 角色功能树
	 * @param roleFuncList: RoleFuncRights对象列表
	 * @param roleId	: 角色ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	private void getRoleFuncList(TreeNode<Function> roleFuncTree,
			List<RoleFuncRights> roleFuncList,Integer roleId){

		RoleFuncRights roleFunc = null;
		Function funcItem = null;
		Integer funcId = 0;
		boolean bAllChildren = false;
		if (roleFuncTree.getNodeData().getFuncId() != 0 && roleFuncTree.getIsIncluded() == 1) {
			// 如果当前节点不为根节点，且包含
			funcItem = roleFuncTree.getNodeData();
			funcId = funcItem.getFuncId();
			// 是否包含全部子节点
			bAllChildren = roleFuncTree.isAllChildrenIncluded();
			// 构造角色功能关系对象
			roleFunc = new RoleFuncRights();
			roleFunc.setRoleId(roleId);
			roleFunc.setFuncId(funcId);
			if (bAllChildren) {
				roleFunc.setSubFullFlag((byte)1);
			}else {
				roleFunc.setSubFullFlag((byte)0);
			}
			roleFuncList.add(roleFunc);						
		}else {
			return;
		}
		
		// 处理子节点
		for (int i = 0; i < roleFuncTree.getChildren().size(); i++) {
			TreeNode<Function> childNode = roleFuncTree.getChildren().get(i);
			getRoleFuncList(childNode,roleFuncList,roleId);
		}		
	}
	
	/**
	 * @methodName			: checkValidForParams
	 * @description			: 输入参数校验
	 * @param request		: request对象
	 * @param methodName	: 方法名称
	 * @param params		: 输入参数
	 * @history				:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// RoleFuncRights item = (RoleFuncRights)params;
			
			// 检查项: TBD
		}
		break;
		case "addItems":
		{
			// List<RoleFuncRights> itemList = (List<RoleFuncRights>)params;
			
			// 检查项: TBD
		}
		break;
		case "editItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId,funcId
			checkKeyFields(map,new String[] {"roleId", "funcId"});
		}
		break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId,funcId
			checkKeyFields(map,new String[] {"roleId", "funcId"});
		}
		break;
		case "deleteItems":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId,funcId
			checkKeyFields(map,new String[] {"roleId", "funcId"});
		}
		break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId,funcId
			checkKeyFields(map,new String[] {"roleId", "funcId"});
		}
		break;
		case "getTree":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId
			checkKeyFields(map,new String[] {"roleId"});
		}
			break;
		case "setTree":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId,funcTree
			checkKeyFields(map,new String[] {"roleId", "funcTree"});
		}
			break;
		default:
			break;
		}
	}
}
