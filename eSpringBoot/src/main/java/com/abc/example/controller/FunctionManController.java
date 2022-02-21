package com.abc.example.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.Function;
import com.abc.example.service.FunctionManService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className	: FunctionManController
 * @description	: 功能对象管理控制器类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/function")
@RestController
public class FunctionManController extends BaseController{
	// 功能对象服务类对象
	@Autowired
	private FunctionManService functionManService;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个功能对象
	 * @param request	: request对象
	 * @param item		: 功能对象
	 * @return			: BaseResponse对象，data为字典，形式如下：
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/add")
	public BaseResponse<Map<String,Object>> addItem(HttpServletRequest request,
			 @RequestBody Function item) {
		Map<String,Object> map = functionManService.addItem(request, item);
		return successResponse(map);
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批功能对象
	 * @param request	: request对象
	 * @param itemList	: 功能对象列表
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
			 @RequestBody List<Function> itemList) {
		functionManService.addItems(request, itemList);
		return successResponse();
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个功能对象
	 * @param request	: request对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
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
		functionManService.editItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个功能对象
	 * @param request	: request对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
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
		functionManService.deleteItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个功能对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"parentId"	: 0,	// 父功能ID，可选
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
		functionManService.deleteItems(request, params);
		return successResponse();
	}
	
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
	 * @return			: BaseResponse对象，data为分页功能对象列表，有page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/queryItems")
	public BaseResponse<List<Function>> queryItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		PageInfo<Function> pageInfo = functionManService.queryItems(request, params);
		return successResponse(pageInfo);
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个功能对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @return			: BaseResponse对象，data为功能对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItem")
	public BaseResponse<Function> getItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		Function item = functionManService.getItem(request, params);
		return successResponse(item);
	}
	
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
	 * @return			: BaseResponse对象，data为功能对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItems")
	public BaseResponse<List<Function>> getItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<Function> itemList = functionManService.getItems(request, params);
		return successResponse(itemList);
	}
}
