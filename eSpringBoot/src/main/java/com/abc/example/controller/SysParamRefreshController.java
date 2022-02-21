package com.abc.example.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.example.service.SysParamRefreshService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className		: SysParamRefreshController
 * @description	: 系统参数刷新控制器类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/refresh")
@RestController
public class SysParamRefreshController  extends BaseController{
	@Autowired
	private SysParamRefreshService sprService;
	
	/**
	 * 
	 * @methodName		: refreshSysParam
	 * @description	: 刷新系统参数
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
	@RequestMapping("/sysParam")
	public BaseResponse<Object> refreshSysParam(HttpServletRequest request,
			@RequestBody Map<String,Object> params){
		sprService.refreshSysParam(request, params);
		return successResponse();
	}	
}
