package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: SysParameter
 * @description	: 系统参数对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/02/02	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class SysParameter {
	// 参数类别ID
	@Column(name = "class_id")
	private Integer classId = 0;
	
	// 参数类别key
	@Column(name = "class_key")
	private String classKey = "";
	
	// 参数类别名称
	@Column(name = "class_name")
	private String className = "";
	
	// 参数类别下子项ID
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
	
	// 操作人账号
	@Column(name = "operator_name")
	private String operatorName = "";
	
	// 记录删除标记，0-正常、1-已删除
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
