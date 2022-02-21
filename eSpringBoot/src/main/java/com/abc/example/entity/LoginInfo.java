package com.abc.example.entity;

import lombok.Data;

/**
 * @className		: LoginInfo
 * @description	: 登录信息对象类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class LoginInfo {
	//登录账号用户名
	private String userName = "";

	//密码，md5 32bit
	private String password = "";
	
	//验证码
	private String verifyCode = "";
}
