package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: UserCustomDr
 * @description	: 用户数据权限关系对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class UserCustomDr {
	// 用户ID
	@Column(name = "user_id")
	private Long userId = 0L;
	
	// 字段ID
	@Column(name = "field_id")
	private Integer fieldId = 0;	
	
	// 字段值
	@Column(name = "field_value")
	private Integer fieldValue = 0;
	
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
