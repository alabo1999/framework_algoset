package com.abc.example.vo;

import com.abc.example.entity.User;

import lombok.Data;

/**
 * @className		: UserVo
 * @description	: 
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class UserVo {
	public UserVo() {
		user = new User();
	}
	// 用户信息
	private User user;
	
	private String addr = "";
	
}
