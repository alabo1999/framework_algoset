package com.abc.example.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * @className		: RoleImpExpSerive
 * @description	: 角色数据导入导出服务接口类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface RoleImpExpSerive {

	/**
	 * 
	 * @methodName		: importExcelFile
	 * @description	: 导入Excel文件
	 * @param request	: request对象
	 * @param upfile	: 上传文件对象
	 * @return			: 错误提示字符串列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<String> importExcelFile(HttpServletRequest request,MultipartFile upfile);
	
	/**
	 * 
	 * @methodName		: batchImportExcelData
	 * @description	: 分批导入Excel数据
	 * @param request	: request对象
	 * @param upfile	: 上传文件对象
	 * @return			: 错误提示字符串列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<String> batchImportExcelFile(HttpServletRequest request,MultipartFile upfile);	
	
	/**
	 * 
	 * @methodName		: exportExcelFile
	 * @description	: 导出Excel文件
	 * @param request	: request对象
	 * @param response	: response对象
	 * @param params	: 请求参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void exportExcelFile(HttpServletRequest request,HttpServletResponse response,
			Map<String,Object> params);
	
	/**
	 * 
	 * @methodName		: exportCsvFile
	 * @description	: 导出Csv文件
	 * @param request	: request对象
	 * @param response	: response对象
	 * @param params	: 请求参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void exportCsvFile(HttpServletRequest request,HttpServletResponse response,
			Map<String,Object> params);	
}
