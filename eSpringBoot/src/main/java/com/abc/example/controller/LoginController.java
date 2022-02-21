package com.abc.example.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abc.example.entity.LoginInfo;
import com.abc.example.service.LoginService;
import com.abc.example.vo.common.BaseResponse;


/**
 * @className		: LoginController
 * @description	: 用户登录控制器类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@RequestMapping("/login")
@RestController
public class LoginController extends BaseController{
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * 
	 * @methodName		: login
	 * @description	: 登录
	 * @param request	: request对象
	 * @param loginInfo	: 登录信息对象
	 * @return			: BaseResponse对象，含数据data，形式如下：
	 * 		{
	 * 			"token"	: "ABCDEFG12345YUAKDLDJUYDDKFE54321",
	 * 			"rights": [],	//功能树
	 * 		}
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    @RequestMapping("/login")
    public BaseResponse<Map<String,Object>> login(@Valid HttpServletRequest request, 
    		@RequestBody LoginInfo loginInfo){    	
    	Map<String,Object> map = loginService.login(request, loginInfo);
    	
    	return successResponse(map);
    }     

    /**
     * 
     * @methodName		: getVerifyCode
     * @description	: 获取验证码图片
     * @param request	: request对象
     * @param response	: response对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @RequestMapping("/getVerifyCode")
    @ResponseBody
    public void getVerifyCode(@Valid HttpServletRequest request,HttpServletResponse response){
    	loginService.getVerifyCode(request, response);
    }	    

}
