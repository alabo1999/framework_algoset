package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: UserDr
 * @description	: 数据权限规则对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class UserDr {
	// 用户ID
	@Column(name = "user_id")
	private Long userId = 0L;
	
	// 字段ID
	@Column(name = "field_id")
	private Integer fieldId = 0;
	
	// 字段名
	@Column(name = "field_name")
	private String fieldName = "";
	
	// 数据权限类型，1-默认规则、2-自定义、3-全部
	@Column(name = "dr_type")
	private Byte drType = 1;
	
	// 默认规则类型时，对象值
	private Object fieldValue;
	
	// 表达式
	@Column(name = "expr")
	private String expr = "";
	
	// 操作人账号
	@Column(name = "operator_name")
	private String operatorName = "";
	
	// 记录标记，保留
	@Column(name = "delete_flag")
	private Byte deleteFlag = 0;
	
	// 创建时间
	@Column(name = "create_time")
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	
	// 更新时间
	@Column(name = "update_time")
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;
	
}
