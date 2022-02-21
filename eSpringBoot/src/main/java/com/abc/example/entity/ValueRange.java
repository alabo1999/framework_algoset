package com.abc.example.entity;

import lombok.Data;

/**
 * @className		: ValueRange
 * @description	: 值域范围对象类
 * @summary		: 为了兼容多种数据类型，使用字符串类型保存上限和下限，且为左闭右开区间：[a,b)
 * 					  字符串类型，值域范围为字符串的长度，如果为字符串列表中的下标，设置标志值
 * 					  日期时间格式约定为：yyyy-MM-dd hh:mm:ss，日期或时间格式相应对应。
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/01/22	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class ValueRange {
	
	// 构造函数
	public ValueRange(String lowerLimit,String upperLimit) {
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;		
	}
	
	// 值域下限
	private String lowerLimit = "";
	
	// 值域上限
	private String upperLimit = "";		
}
