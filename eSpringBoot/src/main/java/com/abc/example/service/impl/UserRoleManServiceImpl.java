package com.abc.example.service.impl;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.common.utils.Utility;
import com.abc.example.dao.UserRoleDao;
import com.abc.example.entity.UserRole;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.UserRoleManService;

/**
 * @className	: UserRoleManServiceImpl
 * @description	: 用户和角色关系对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class UserRoleManServiceImpl extends BaseService implements UserRoleManService{
	// 用户和角色关系对象数据访问类对象
	@Autowired
	private UserRoleDao userRoleDao;
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;	
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个用户和角色关系对象
	 * @param request	: request对象
	 * @param item		: 用户和角色关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItem(HttpServletRequest request, UserRole item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		item.setOperatorName(operatorName);
		
		// 插入数据
		try {
			userRoleDao.insertItem(item);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserRoleE, null, item, EDataOperationType.dotAddE);
		
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批用户和角色关系对象
	 * @param request	: request对象
	 * @param itemList	: 用户和角色关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItems(HttpServletRequest request, List<UserRole> itemList) {
		// 输入参数校验
		checkValidForParams(request, "addItems", itemList);
		
		if (itemList.size() == 0) {
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		for(int i = 0; i < itemList.size(); i++) {
			UserRole aObj = itemList.get(i);
			aObj.setOperatorName(operatorName);
		}
		
		// 插入数据
		try {
			userRoleDao.insertItems(itemList);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查		
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserRoleE, null, itemList.get(0), EDataOperationType.dotAddE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key删除一个用户和角色关系对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID
	 * 		"roleId"	: 0,	// 角色ID
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
		Long userId = Utility.getLongValue(params, "userId");
		Integer roleId = (Integer)params.get("roleId");
		UserRole oldItem = userRoleDao.selectItemByKey(userId,roleId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
				
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 删除数据
		try {
			userRoleDao.deleteItemByKey(userId,roleId);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.DELETE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserRoleE, oldItem, null, EDataOperationType.dotRemoveE);
		
	}
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个用户和角色关系对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItems(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItems", params);
		
		// 查询修改前的数据
		List<UserRole> oldItemList = userRoleDao.selectItems(params);
		if (oldItemList.size() == 0) {
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据		
		try {			
			userRoleDao.deleteItems(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.DELETE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserRoleE, oldItemList.get(0), null, EDataOperationType.dotRemoveE);
		
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询用户和角色关系对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 		"pagenum"	: 1,	// 当前页码，可选
	 * 		"pagesize"	: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 用户和角色关系对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<UserRole> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		PageInfo<UserRole> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<UserRole> userRoleList = userRoleDao.selectItemsByCondition(params);
			// 分页对象
			pageInfo = new PageInfo<UserRole>(userRoleList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个用户和角色关系对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 	}
	 * @return			: 用户和角色关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public UserRole getItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Long userId = Utility.getLongValue(params, "userId");
		Integer roleId = (Integer)params.get("roleId");
		
		// 查询数据
		try {
			// 查询记录
			UserRole item = userRoleDao.selectItemByKey(userId,roleId);
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询用户和角色关系对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"	: 0,	// limit记录偏移量，可选
	 * 		"rows"		: 20,	// limit最大记录条数，可选
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 	}
	 * @return			: 用户和角色关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<UserRole> getItems(HttpServletRequest request,
			 Map<String, Object> params) {
		// 查询数据
		try {
			// 查询记录
			List<UserRole> itemList = userRoleDao.selectItems(params);
			return itemList;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
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
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// UserRole item = (UserRole)params;
			
			// 检查项: TBD
		}
		break;
		case "addItems":
		{
			// List<UserRole> itemList = (List<UserRole>)params;
			
			// 检查项: TBD
		}
		break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId,roleId
			checkKeyFields(map,new String[] {"userId", "roleId"});
		}
		break;
		case "deleteItems":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId,roleId
			checkKeyFields(map,new String[] {"userId", "roleId"});
		}
		break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId,roleId
			checkKeyFields(map,new String[] {"userId", "roleId"});
		}
		break;
		default:
			break;
		}
	}
}
