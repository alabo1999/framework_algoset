package com.abc.example.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abc.example.common.utils.Md5Util;
import com.abc.example.entity.LoginInfo;

/**
 * @className		: LoginService
 * @description	: 登录服务类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface LoginService {

	/**
	 * 
	 * @methodName		: login
	 * @description	: 用户登录
	 * @param request	: request对象
	 * @param loginInfo	: 登录信息对象
	 * @return			: Map对象，形式如下：
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
	public Map<String,Object> login(HttpServletRequest request,LoginInfo loginInfo);
	
	
	/**
	 * 
	 * @methodName		: getVerifyCode
	 * @description	: 获取验证码
	 * @param request	: request对象
	 * @param response	: response对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void getVerifyCode(HttpServletRequest request,HttpServletResponse response);	
	
	/**
	 * 
	 * @methodName		: generateToken
	 * @description	: 生成token
	 * @param loginName: 登录名
	 * @return			: token字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String generateToken(String loginName) {
		
		// 获取当前时间字符串
		Date date = new Date();		
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String sTimeStr = dateFormat.format(date);
		
		// 产生md5串，作为该用户的token
		String sStr = loginName + "-" + sTimeStr;
		
		String sToken = Md5Util.md5Encypt(sStr);
		
		return sToken;
	}	
}
