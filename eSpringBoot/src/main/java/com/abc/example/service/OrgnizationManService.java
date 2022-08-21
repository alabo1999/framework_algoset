package com.abc.example.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.Orgnization;

/**
 * @className	: OrgnizationManService
 * @description	: 组织机构对象管理服务接口类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/02/14	1.0.0		sheng.zheng		初版
 *
 */
public interface OrgnizationManService {
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个组织机构对象
	 * @param request	: request对象
	 * @param item		: 组织机构对象
	 * @return			: 新增的组织机构对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	public Map<String,Object> addItem(HttpServletRequest request, Orgnization item);
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	public void editItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public void deleteItem(HttpServletRequest request, Map<String, Object> params);
	
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
	 * @return			: 组织机构对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public PageInfo<Orgnization> queryItems(HttpServletRequest request,
			 Map<String, Object> params);
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @return			: 组织机构对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public Orgnization getItem(HttpServletRequest request, Map<String, Object> params);
	
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
	 * @return			: 组织机构对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Orgnization> getItems(HttpServletRequest request,
			 Map<String, Object> params);
	
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
	public void exportExcelFile(HttpServletRequest request,
			 HttpServletResponse response,Map<String, Object> params);		
}
