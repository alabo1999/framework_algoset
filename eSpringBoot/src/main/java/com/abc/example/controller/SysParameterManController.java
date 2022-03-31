package com.abc.example.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.SysParameter;
import com.abc.example.service.SysParameterManService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className	: SysParameterManController
 * @description	: 系统参数对象管理控制器类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/02/02	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/sysParameter")
@RestController
public class SysParameterManController extends BaseController{
	// 系统参数对象服务类对象
	@Autowired
	private SysParameterManService sysParameterManService;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个系统参数对象
	 * @param request	: request对象
	 * @param item		: 系统参数对象
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/add")
	public BaseResponse<Object> addItem(HttpServletRequest request,
			 @RequestBody SysParameter item) {
		sysParameterManService.addItem(request, item);
		return successResponse();
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批系统参数对象
	 * @param request	: request对象
	 * @param itemList	: 系统参数对象列表
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/addItems")
	public BaseResponse<Object> addItems(HttpServletRequest request,
			 @RequestBody List<SysParameter> itemList) {
		sysParameterManService.addItems(request, itemList);
		return successResponse();
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/edit")
	public BaseResponse<Object> editItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		sysParameterManService.editItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: updateItems
	 * @description		: 根据条件修改一个或多个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，其它字段均可选，条件字段如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/updateItems")
	public BaseResponse<Object> updateItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		sysParameterManService.updateItems(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/delete")
	public BaseResponse<Object> deleteItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		sysParameterManService.deleteItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个系统参数对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/deleteItems")
	public BaseResponse<Object> deleteItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		sysParameterManService.deleteItems(request, params);
		return successResponse();
	}
	
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
	 * @return			: BaseResponse对象，data为分页系统参数对象列表，有page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/queryItems")
	public BaseResponse<List<SysParameter>> queryItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		PageInfo<SysParameter> pageInfo = sysParameterManService.queryItems(request, params);
		return successResponse(pageInfo);
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @return			: BaseResponse对象，data为系统参数对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItem")
	public BaseResponse<SysParameter> getItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		SysParameter item = sysParameterManService.getItem(request, params);
		return successResponse(item);
	}
	
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
	 * @return			: BaseResponse对象，data为系统参数对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItems")
	public BaseResponse<List<SysParameter>> getItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<SysParameter> itemList = sysParameterManService.getItems(request, params);
		return successResponse(itemList);
	}
}
