package com.abc.example.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @className		: SysParamRefreshService
 * @description	: 
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface SysParamRefreshService {
	
	/**
	 * 
	 * @methodName		: refreshSysParam
	 * @description	: 刷新系统参数，即刷子由后台数据库配置的数据
	 * @param request	: request对象
	 * @param params	: 请求参数，形式为：
	 * 	{
	 * 		"type"		: "", 	//参数类型，
	 * 	}	
	 * 	type取值如下：
	 * 	SysParameterService		：系统参数 
	 * 	TableCodeConfigService	：ID编码配置 
	 * 	RoleService				：角色配置 
	 * 	FunctionTreeService		：功能树配置 
	 * 	RoleFuncRightsService	：角色功能树配置 
	 * @return
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void refreshSysParam(HttpServletRequest request,Map<String,Object> params);
}
