package com.abc.example.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.Orgnization;
import com.abc.example.service.OrgnizationManService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className	: OrgnizationManController
 * @description	: 组织机构对象管理控制器类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/02/14	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/orgnization")
@RestController
public class OrgnizationManController extends BaseController{
	// 组织机构对象服务类对象
	@Autowired
	private OrgnizationManService orgnizationManService;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个组织机构对象
	 * @param request	: request对象
	 * @param item		: 组织机构对象
	 * @return			: BaseResponse对象，data为字典，形式如下：
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/add")
	public BaseResponse<Map<String,Object>> addItem(HttpServletRequest request,
			 @RequestBody Orgnization item) {
		Map<String,Object> map = orgnizationManService.addItem(request, item);
		return successResponse(map);
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/edit")
	public BaseResponse<Object> editItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		orgnizationManService.editItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @return			: BaseResponse对象，无data
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/delete")
	public BaseResponse<Object> deleteItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		orgnizationManService.deleteItem(request, params);
		return successResponse();
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询组织机构对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"parentId"		: 0,	// 父组织ID，可选
	 * 		"orgType"		: 3,	// 机构类型，1-本公司，2-政府管理部门，3-学院，可选
	 * 		"orgCode"		: "",	// 组织机构编号，like，可选
	 * 		"orgName"		: "",	// 组织机构名称，like，可选
	 * 		"orgFullname"	: "",	// 组织机构全称，like，可选
	 * 		"leader"		: "",	// 负责人名称，like，可选
	 * 		"address"		: "",	// 地址，like，可选
	 * 		"district"		: "",	// 行政区省、市、区县名称，like，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，1-已删除，可选
	 * 		"orgIdList"		: [],	// 组织ID列表，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: BaseResponse对象，data为分页组织机构对象列表，有page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/queryItems")
	public BaseResponse<List<Orgnization>> queryItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		PageInfo<Orgnization> pageInfo = orgnizationManService.queryItems(request, params);
		return successResponse(pageInfo);
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @return			: BaseResponse对象，data为组织机构对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItem")
	public BaseResponse<Orgnization> getItem(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		Orgnization item = orgnizationManService.getItem(request, params);
		return successResponse(item);
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询组织机构对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"parentId"		: 0,	// 父组织ID，可选
	 * 		"orgType"		: 3,	// 机构类型，1-本公司，2-政府管理部门，3-学院，可选
	 * 		"orgCode"		: "",	// 组织机构编号，可选
	 * 		"orgName"		: "",	// 组织机构名称，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，1-已删除，可选
	 * 		"orgIdList"		: [],	// 组织ID列表，可选
	 * 	}
	 * @return			: BaseResponse对象，data为组织机构对象列表，无page
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItems")
	public BaseResponse<List<Orgnization>> getItems(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		List<Orgnization> itemList = orgnizationManService.getItems(request, params);
		return successResponse(itemList);
	}
	
	/**
	 * @methodName		: exportExcelFile
	 * @description		: 导出组织机构对象Excel数据文件
	 * @param request	: request对象
	 * @param response	: response对象
	 * @param params	: 查询参数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/export")
	public void exportExcelFile(HttpServletRequest request,
			 HttpServletResponse response,@RequestBody Map<String, Object> params) {
		orgnizationManService.exportExcelFile(request, response,params);
	}
}
