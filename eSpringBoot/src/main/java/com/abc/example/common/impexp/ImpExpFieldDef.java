package com.abc.example.common.impexp;

import lombok.Data;

/**
 * @className		: ImpExpFieldDef
 * @description	: 导入导出字段定义
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class ImpExpFieldDef {
	// 标题名
	private String titleName;

	// 字段名
	private String fieldName;
	
	// 字段是否必需,1表示必需，该字段在导出时忽略
	private Integer mandatory;
	
	// 构造函数
	public ImpExpFieldDef(String fieldName,String titleName,Integer mandatory) {
		this.fieldName = fieldName;
		this.titleName = titleName;
		this.mandatory = mandatory;
	}
}
