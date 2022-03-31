package com.abc.example.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.SysParameter;

/**
 * @className	: SysParameterManService
 * @description	: 系统参数对象管理服务接口类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/02/02	1.0.0		sheng.zheng		初版
 *
 */
public interface SysParameterManService {
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个系统参数对象
	 * @param request	: request对象
	 * @param item		: 系统参数对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addItem(HttpServletRequest request, SysParameter item);
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批系统参数对象
	 * @param request	: request对象
	 * @param itemList	: 系统参数对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addItems(HttpServletRequest request, List<SysParameter> itemList);
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public void editItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: updateItems
	 * @description		: 根据条件修改一个或多个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，其它字段均可选，条件字段如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public void updateItems(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public void deleteItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个系统参数对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public void deleteItems(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询系统参数对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"classId"		: 0,	// 参数类别ID，可选
	 * 		"classKey"		: "",	// 参数类别key，可选
	 * 		"className"		: "",	// 参数类别名称，like，可选
	 * 		"itemKey"		: "",	// 子项key，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 系统参数对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public PageInfo<SysParameter> queryItems(HttpServletRequest request,
			 Map<String, Object> params);
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @return			: 系统参数对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public SysParameter getItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询系统参数对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"classId"		: 0,	// 参数类别ID，可选
	 * 		"classKey"		: "",	// 参数类别key，可选
	 * 		"itemKey"		: "",	// 子项key，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 系统参数对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<SysParameter> getItems(HttpServletRequest request,
			 Map<String, Object> params);
}
