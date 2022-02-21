package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: Role
 * @description	: 角色对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class Role {
	// 角色ID
	@Column(name = "role_id")
	private Integer roleId = 0;
	
	// 角色名称
	@Column(name = "role_name")
	private String roleName = "";
	
	// 角色类型，参见系统参数表
	@Column(name = "role_type")
	private Byte roleType = 0;
	
	// 描述
	@Column(name = "remark")
	private String remark = "";
	
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
