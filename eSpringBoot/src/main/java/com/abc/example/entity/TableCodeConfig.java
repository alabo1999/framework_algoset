package com.abc.example.entity;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

/**
 * @className		: TableCodeConfig
 * @description	: ID编码配置信息对象实体类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class TableCodeConfig {
	// 表id
	@Id
	@Column(name = "table_id")
	private Integer tableId = 0;
	
	// 表名称
	@Column(name = "table_name")
	private String tableName = "";
	
	// ID字段名称
	@Column(name = "field_name")
	private String fieldName = "";
	
	// 编码前缀字符串
	@Column(name = "prefix")
	private String prefix = "";

	// 编码前缀字符串长度
	@Column(name = "prefix_len")
	private byte prefixLen = 0;

	// 序列号长度
	@Column(name = "seqno_len")
	private byte seqnoLen = 0;	
}
