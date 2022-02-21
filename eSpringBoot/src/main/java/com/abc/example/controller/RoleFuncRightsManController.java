package com.abc.example.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.RoleFuncRights;
import com.abc.example.service.RoleFuncRightsManService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className	: RoleFuncRightsManController
 * @description	: 角色和功能权限关系对象管理控制器类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/roleFuncRights")
@RestController
public class RoleFuncRightsManController extends BaseController{
	// 角色和功能权限关系对象服务类对象
	@Autowired
	private RoleFuncRightsManService roleFuncRightsManService;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个角色和功能权限关系对象
	 * @param request	: request对象
	 * @param item		: 角色和功能权限关系对象
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/add")
	public BaseResponse<Object> addItem(HttpServletRequest request,
			 @RequestBody RoleFuncRights item) {
		roleFuncRightsManService.addItem(request, item);
		return successResponse();
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批角色和功能权限关系对象
	 * @param request	: request对象
	 * @param itemList	: 角色和功能权限关系对象列表
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/addItems")
	public BaseResponse<Object> addItems(HttpServletRequest request,
			 @RequestBody List<RoleFuncRights> itemList) {
		roleFuncRightsManService.addItems(request, itemList);
		return successResponse();
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
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/edit")
	public BaseResponse<Object> editItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		roleFuncRightsManService.editItem(request, params);
		return successResponse();
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
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/delete")
	public BaseResponse<Object> deleteItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		roleFuncRightsManService.deleteItem(request, params);
		return successResponse();
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
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/deleteItems")
	public BaseResponse<Object> deleteItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		roleFuncRightsManService.deleteItems(request, params);
		return successResponse();
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
	 * @return			: BaseResponse对象，data为分页角色和功能权限关系对象列表，有page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/queryItems")
	public BaseResponse<List<RoleFuncRights>> queryItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		PageInfo<RoleFuncRights> pageInfo = roleFuncRightsManService.queryItems(request, params);
		return successResponse(pageInfo);
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
	 * @return			: BaseResponse对象，data为角色和功能权限关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItem")
	public BaseResponse<RoleFuncRights> getItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		RoleFuncRights item = roleFuncRightsManService.getItem(request, params);
		return successResponse(item);
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
	 * @return			: BaseResponse对象，data为角色和功能权限关系对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItems")
	public BaseResponse<List<RoleFuncRights>> getItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<RoleFuncRights> itemList = roleFuncRightsManService.getItems(request, params);
		return successResponse(itemList);
	}
}
