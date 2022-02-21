package com.abc.example.entity;

import javax.persistence.Column;

import lombok.Data;

/**
 * @className		: SysParameter
 * @description	: 系统参数信息对象类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class SysParameter {
	// 参数类别id
	@Column(name = "class_id")
	private Integer classId = 0;
	
	// 参数类别key
	@Column(name = "class_key")
	private String classKey = "";

	// 参数类别名称
	@Column(name = "class_name")
	private String className = "";
	
	// 子项id
	@Column(name = "item_id")
	private Integer itemId = 0;	
		
	// 子项key
	@Column(name = "item_key")
	private String itemKey = "";	
	
	// 子项值
	@Column(name = "item_value")
	private String itemValue = "";	

	// 子项描述
	@Column(name = "item_desc")
	private String itemDesc = "";	
}
