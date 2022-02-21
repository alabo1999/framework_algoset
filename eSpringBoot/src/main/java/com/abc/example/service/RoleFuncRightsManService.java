package com.abc.example.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.RoleFuncRights;

/**
 * @className	: RoleFuncRightsManService
 * @description	: 角色和功能权限关系对象管理服务接口类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
public interface RoleFuncRightsManService {
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
	public void addItem(HttpServletRequest request, RoleFuncRights item);
	
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
	public void addItems(HttpServletRequest request, List<RoleFuncRights> itemList);
	
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
	public void editItem(HttpServletRequest request, Map<String, Object> params);
	
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
	public void deleteItem(HttpServletRequest request, Map<String, Object> params);
	
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
	public void deleteItems(HttpServletRequest request, Map<String, Object> params);
	
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
	public PageInfo<RoleFuncRights> queryItems(HttpServletRequest request,
			 Map<String, Object> params);
	
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
	public RoleFuncRights getItem(HttpServletRequest request,
			 Map<String, Object> params);
	
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
	public List<RoleFuncRights> getItems(HttpServletRequest request,
			 Map<String, Object> params);
}
