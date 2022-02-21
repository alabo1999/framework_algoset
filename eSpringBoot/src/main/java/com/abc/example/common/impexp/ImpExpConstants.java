package com.abc.example.common.impexp;

/**
 * @className		: Constants
 * @description	: 数据导入导出相关常量定义
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class ImpExpConstants {
	// 读取数据至尾部
	public static final Integer READ_TO_TAIL = -1;

	// 异常发生时的处理策略
	public static final Integer CONTINUE_ON_ERROR = 0;
	public static final Integer STOP_ON_ERROR = 1;
	
	// 错误数据列号显示方式
	public static final Integer COLUMN_ALPHABET = 0;
	public static final Integer COLUMN_INDEX = 1;
}
