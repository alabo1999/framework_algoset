package com.abc.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.example.common.utils.LogUtil;
import com.abc.example.entity.SysParameter;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.SysParameterService;
import com.abc.example.vo.common.BaseResponse;


/**
 * @className		: SysParameterController
 * @description	: 系统参数控制器类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/sysParam")
@RestController
public class SysParameterController extends BaseController {
	// 公共配置服务类
	@Autowired
	private GlobalConfigService gcs;
	
	/**
	 * 
	 * @methodName		: getParameClasses
	 * @description	: 获取多个参数类别的参数项列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"classKeyList"	: ["user_type","sex"]	//系统参数表的参数类别key列表
	 * 	}
	 * @return			: BaseResponse对象，data部分的数据为SysParameter类型列表。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getClasses")
	public BaseResponse<List<List<SysParameter>>> getParameClasses(HttpServletRequest request,
			@RequestBody Map<String,Object> params){
		if(!params.containsKey("classKeyList")) {
			throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);
		}
		
		// 获取classKeyList
		@SuppressWarnings("unchecked")
		List<String> classKeyList = (List<String>)params.get("classKeyList");

		List<List<SysParameter>> sysParamsList = new ArrayList<List<SysParameter>>();
		// 获取全部数据服务对象
		SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		if (sps == null) {
			LogUtil.info("SysParameterService sps is null");
		}
		for(String classKey : classKeyList) {
			List<SysParameter> classList = sps.getItemsByClass(classKey,false);
			if (classList != null) {
				LogUtil.info(classList.toString());
				sysParamsList.add(classList);				
			}else {
				LogUtil.info("classList is null");
			}
		}
		
		return successResponse(sysParamsList);
	}
	
	
	/**
	 * 
	 * @methodName		: getParameterClass
	 * @description	: 获取指定classKey的参数类别的子项列表
	 * @param request	: request对象
	 * @param params	: Map对象，形式如下：
	 * 	{
	 * 		"classKey"	: "user_type"	//系统参数表的参数类别key
	 * 	}
	 * @return			: BaseResponse对象，data部分的数据为SysParameter类型列表。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getClassInfo")
	public BaseResponse<List<SysParameter>> getParameterClass(HttpServletRequest request,
			@RequestBody Map<String,Object> params){
		
		String classKey = (String)params.get("classKey");
		SysParameterService sysParameterService = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		
		List<SysParameter> sysParamList = sysParameterService.getItemsByClass(classKey,false);
		if (sysParamList == null) {
			throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);
		}
		return successResponse(sysParamList);
	}
	
	/**
	 * 
	 * @methodName		: getParameterItemByKey
	 * @description	: 根据classKey和itemKey获取参数子项
	 * @param request	: request对象
	 * @param params	: Map对象，形式如下：
	 * 	{
	 * 		"classKey"	: "user_type",	//系统参数表的参数类别key
	 * 		"itemKey"	: "1"			//参数子项key
	 * 	}
	 * @return			: BaseResponse对象，data部分的数据为SysParameter类型数据。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItemByKey")
	public BaseResponse<SysParameter> getParameterItemByKey(HttpServletRequest request,
			@RequestBody Map<String,Object> params){

		String classKey = (String)params.get("classKey");
		String itemKey = (String)params.get("itemKey");
		
		SysParameterService sysParameterService = (SysParameterService)gcs.getDataServiceObject("SysParameterService");		
		SysParameter sysParameter = sysParameterService.getItemByKey(classKey,itemKey,false);
		if (sysParameter == null) {
			throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);
		}
		return successResponse(sysParameter);
	}
	
	/**
	 * 
	 * @methodName		: getItemByValue
	 * @description	: 根据classKey和itemValue获取参数子项
	 * @param request	: request对象
	 * @param params	: Map对象，形式如下：
	 * 	{
	 * 		"classKey"	: "user_type",		//系统参数表的参数类别key
	 * 		"itemValue"	: "公司内部用户"	//参数子项value
	 * 	}
	 * @return			: BaseResponse对象，data部分的数据为SysParameter类型数据。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@RequestMapping("/getItemByValue")
	public BaseResponse<SysParameter> getItemByValue(HttpServletRequest request,
			@RequestBody Map<String,Object> params){

		String classKey = (String)params.get("classKey");
		String itemValue = (String)params.get("itemValue");
		
		SysParameterService sysParameterService = (SysParameterService)gcs.getDataServiceObject("SysParameterService");		
		SysParameter sysParameter = sysParameterService.getItemByValue(classKey,itemValue);
		if (sysParameter == null) {
			throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);
		}		
		return successResponse(sysParameter);
	}			

}
