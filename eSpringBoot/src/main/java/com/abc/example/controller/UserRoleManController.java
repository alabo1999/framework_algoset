package com.abc.example.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.UserRole;
import com.abc.example.service.UserRoleManService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className	: UserRoleManController
 * @description	: 用户和角色关系对象管理控制器类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/userRole")
@RestController
public class UserRoleManController extends BaseController{
	// 用户和角色关系对象服务类对象
	@Autowired
	private UserRoleManService userRoleManService;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个用户和角色关系对象
	 * @param request	: request对象
	 * @param item		: 用户和角色关系对象
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
			 @RequestBody UserRole item) {
		userRoleManService.addItem(request, item);
		return successResponse();
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批用户和角色关系对象
	 * @param request	: request对象
	 * @param itemList	: 用户和角色关系对象列表
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
			 @RequestBody List<UserRole> itemList) {
		userRoleManService.addItems(request, itemList);
		return successResponse();
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
		userRoleManService.deleteItem(request, params);
		return successResponse();
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
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/deleteItems")
	public BaseResponse<Object> deleteItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		userRoleManService.deleteItems(request, params);
		return successResponse();
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
	 * @return			: BaseResponse对象，data为分页用户和角色关系对象列表，有page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/queryItems")
	public BaseResponse<List<UserRole>> queryItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		PageInfo<UserRole> pageInfo = userRoleManService.queryItems(request, params);
		return successResponse(pageInfo);
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
	 * @return			: BaseResponse对象，data为用户和角色关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItem")
	public BaseResponse<UserRole> getItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		UserRole item = userRoleManService.getItem(request, params);
		return successResponse(item);
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
	 * @return			: BaseResponse对象，data为用户和角色关系对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItems")
	public BaseResponse<List<UserRole>> getItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<UserRole> itemList = userRoleManService.getItems(request, params);
		return successResponse(itemList);
	}
}
