package com.abc.example.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.Function;

/**
 * @className	: FunctionManService
 * @description	: 功能对象管理服务接口类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
public interface FunctionManService {
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个功能对象
	 * @param request	: request对象
	 * @param item		: 功能对象
	 * @return			: 新增的功能对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public Map<String,Object> addItem(HttpServletRequest request, Function item);
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批功能对象
	 * @param request	: request对象
	 * @param itemList	: 功能对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addItems(HttpServletRequest request, List<Function> itemList);
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个功能对象
	 * @param request	: request对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
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
	 * @description		: 根据key禁用/启用一个功能对象
	 * @param request	: request对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
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
	 * @description		: 根据条件删除多个功能对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"parentId"	: 0,	// 父功能ID，可选
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
	 * @description		: 根据条件分页查询功能对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 		"funcName"		: "",	// 功能名称，like，可选
	 * 		"url"			: "",	// 访问接口url，like，可选
	 * 		"domKey"		: "",	// dom对象的ID，like，可选
	 * 		"parentId"		: 0,	// 父功能ID，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 功能对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public PageInfo<Function> queryItems(HttpServletRequest request,
			 Map<String, Object> params);
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个功能对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @return			: 功能对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public Function getItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询功能对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: ,		// limit记录偏移量，可选
	 * 		"rows"			: ,		// limit最大记录条数，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 功能对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Function> getItems(HttpServletRequest request,
			 Map<String, Object> params);
}
