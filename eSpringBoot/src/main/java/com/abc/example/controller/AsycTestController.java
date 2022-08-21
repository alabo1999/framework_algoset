package com.abc.example.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.example.asyncproc.TestTaskService;
import com.abc.example.vo.common.BaseResponse;

/**
 * @className		: AsycTestController
 * @description	: 异步任务测试控制器类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/08/19	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/asycTest")
@RestController
public class AsycTestController extends BaseController{
	@Autowired	
	private TestTaskService tts;
	
	@RequestMapping("/addTask")
	public BaseResponse<Map<String,Object>> addTask(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		Map<String,Object> map = tts.addAsyncTask(request,params);
		return successResponse(map);
	}	
	
	@RequestMapping("/getTaskInfo")
	public BaseResponse<Map<String,Object>> getTaskInfo(HttpServletRequest request,
			 @RequestBody Map<String, Object> params) {
		Map<String,Object> map = tts.getTaskInfo(request, params);
		return successResponse(map);
	}	
}
