package com.abc.example.vo.common;

import lombok.Data;

/**
 * @className		: Page
 * @description	: 分页信息对象
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class Page {
	// 当前页码
	private int pageNum;
	
	// 每页记录数
	private int pageSize;
	
	// 总页数
	private int pages;
	
	// 总记录数
	private long total;
}
