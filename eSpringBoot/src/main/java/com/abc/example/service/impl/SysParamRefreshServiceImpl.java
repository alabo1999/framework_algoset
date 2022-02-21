package com.abc.example.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.FunctionTreeService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.RoleFuncRightsService;
import com.abc.example.service.RoleService;
import com.abc.example.service.SysParamRefreshService;
import com.abc.example.service.SysParameterService;
import com.abc.example.service.TableCodeConfigService;

/**
 * @className		: SysParamRefreshServiceImpl
 * @description	: SysParamRefreshService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class SysParamRefreshServiceImpl implements SysParamRefreshService{
	// 公共配置数据服务
	@Autowired
	private GlobalConfigService gcs;
	
	// 缓存一致性服务对象
	@Autowired
	private CacheDataConsistencyService cdcs;		
	
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
	@Override
	public void refreshSysParam(HttpServletRequest request,Map<String,Object> params) {
		if (!params.containsKey("type")) {
			throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);
		}
		String type = (String)params.get("type");
		switch(type) {
		case "SysParameterService":
		{
			SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
			sps.loadData();
		}
			break;
		case "TableCodeConfigService":
		{
			TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
			tccs.loadData();
		}
			break;
		case "RoleService":
		{
			RoleService rs = (RoleService)gcs.getDataServiceObject("RoleService");
			rs.loadData();
		}
			break;
		case "FunctionTreeService":
		{
			FunctionTreeService fts = (FunctionTreeService)gcs.getDataServiceObject("FunctionTreeService");
			fts.loadData();	
			
			// 缓存一致性检查
			cdcs.updateFuncTree();			
		}
			break;
		case "RoleFuncRightsService":
		{
			RoleFuncRightsService rfrs = (RoleFuncRightsService)gcs.getDataServiceObject("RoleFuncRightsService");
			rfrs.loadData();			

			// 缓存一致性检查
			cdcs.updateRoleFuncRights();						
		}
			break;
		default:
			break;
		}				
	}
	
}
