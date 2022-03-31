package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: DrField
 * @description	: 数据权限相关字段对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class DrField {
	// 字段ID
	@Column(name = "field_id")
	private Integer fieldId = 0;
	
	// 字段名称
	@Column(name = "field_name")
	private String fieldName = "";

	// 字段描述
	@Column(name = "field_desc")
	private String fieldDesc = "";
	
	// 属性名称
	@Column(name = "prop_name")
	private String propName = "";	
	
	// 无效值，用于ID字段无权限时的查询条件
	@Column(name = "invalid_value")
	private String invalidValue = "";
	
	// 是否有下级对象，0-无，1-有
	@Column(name = "has_sub")
	private Byte hasSub = 0;
	
	// 是否为用户属性字段，0-否，1-是
	@Column(name = "is_user_prop")
	private Byte isUserProp = 0;
	
	// 是否为ID字段，0-否，1-是
	@Column(name = "is_id")
	private Byte isId = 0;
	
	// 备注
	@Column(name = "remark")
	private String remark = "";
	
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
