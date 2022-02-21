package com.abc.example.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.User;
import com.abc.example.entity.UserCustomDr;
import com.abc.example.entity.UserDr;
import com.abc.example.service.UserManService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className	: UserManController
 * @description	: 用户对象管理控制器类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/06	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/user")
@RestController
public class UserManController extends BaseController{
	// 用户对象服务类对象
	@Autowired
	private UserManService userManService;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个用户对象
	 * @param request	: request对象
	 * @param item		: 用户对象
	 * @return			: BaseResponse对象，data为字典，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/add")
	public BaseResponse<Map<String,Object>> addItem(HttpServletRequest request,
			 @RequestBody User item) {
		Map<String,Object> map = userManService.addItem(request, item);
		return successResponse(map);
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个用户对象
	 * @param request	: request对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/edit")
	public BaseResponse<Object> editItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		userManService.editItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个用户对象
	 * @param request	: request对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/delete")
	public BaseResponse<Object> deleteItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		userManService.deleteItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询用户对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userType"		: 3,	// 用户类型，1-系统管理员、2-公司内部用户、3-外部用户，可选
	 * 		"sex"			: 1,	// 性别，1-无值、2-男、3-女、4-其它，可选
	 * 		"drType"		: 0,	// 数据权限类型，1-默认规则、2-自定义、3-全部，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"userName"		: "",	// 用户名，like，可选
	 * 		"phoneNumber"	: "",	// 手机号码，like，可选
	 * 		"realName"		: "",	// 真实姓名，like，可选
	 * 		"email"			: "",	// Email，like，可选
	 * 		"birthStart"	: ,		// 生日起始值，gte，可选
	 * 		"birthEnd"		: ,		// 生日终止值，lte，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: BaseResponse对象，data为分页用户对象列表，有page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/queryItems")
	public BaseResponse<List<User>> queryItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		PageInfo<User> pageInfo = userManService.queryItems(request, params);
		return successResponse(pageInfo);
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个用户对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @return			: BaseResponse对象，data为用户对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItem")
	public BaseResponse<User> getItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		User item = userManService.getItem(request, params);
		return successResponse(item);
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询用户对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"email"			: "",	// Email，可选
	 * 		"drId"			: 0,	// 权限相关对象ID，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 		"userIdList"	: [],	// 用户ID列表，可选
	 * 	}
	 * @return			: BaseResponse对象，data为用户对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItems")
	public BaseResponse<List<User>> getItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<User> itemList = userManService.getItems(request, params);
		return successResponse(itemList);
	}
	
	/**
	 * @methodName		: updateFuncRights
	 * @description	: 修改功能权限，即修改用户角色
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"roleIdList"	: [],  	//角色ID列表
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/updateFuncRights")
	public BaseResponse<Object> updateFuncRights(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		userManService.updateFuncRights(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: getFuncRights
	 * @description	: 获取功能权限，即角色ID列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 	}
	 * @return			: BaseResponse对象，data为角色ID列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getFuncRights")
	public BaseResponse<List<Integer>> getFuncRights(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<Integer> itemList = userManService.getFuncRights(request, params);
		return successResponse(itemList);
	}	
	
	/**
	 * @methodName		: updateUserDrs
	 * @description	: 修改用户数据权限列表
	 * @param request	: request对象
	 * @param itemList	: 用户数据权限对象列表
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/updateUserDrs")
	public BaseResponse<Object> updateUserDrs(HttpServletRequest request,
			 @RequestBody List<UserDr> itemList) {
		userManService.updateUserDrs(request, itemList);
		return successResponse();
	}
	
	/**
	 * @methodName		: getUserDrs
	 * @description	: 获取用户数据权限列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 	}
	 * @return			: BaseResponse对象，data为UserDr对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getUserDrs")
	public BaseResponse<List<UserDr>> getUserDrs(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<UserDr> itemList = userManService.getUserDrs(request, params);
		return successResponse(itemList);
	}	
	
	/**
	 * @methodName		: updateUserCustomDrs
	 * @description	: 修改用户自定义数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 		"valueList"	: [], 	//字段值列表
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/updateUserCustomDrs")
	public BaseResponse<Object> updateUserCustomDrs(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		userManService.updateUserCustomDrs(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: getUserCustomDrs
	 * @description	: 获取用户数据权限列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 	}
	 * @return			: BaseResponse对象，data为UserCustomDr对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getUserCustomDrs")
	public BaseResponse<List<UserCustomDr>> getUserCustomDrs(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<UserCustomDr> itemList = userManService.getUserCustomDrs(request, params);
		return successResponse(itemList);
	}		
	
	/**
	 * @methodName		: getCurrentUserDr
	 * @description	: 获取当前用户的数据权限列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"propName"	: "", 	//权限属性字段名
	 * 	}
	 * @return			: BaseResponse对象，data为UserCustomDr对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/04/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getCurrentUserDr")
	public BaseResponse<List<Integer>> getCurrentUserDr(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<Integer> itemList = userManService.getCurrentUserDr(request, params);
		return successResponse(itemList);
	}			
}
