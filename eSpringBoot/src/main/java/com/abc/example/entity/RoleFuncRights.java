package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: RoleFuncRights
 * @description	: 角色和功能权限关系对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class RoleFuncRights {
	// 角色ID
	@Column(name = "role_id")
	private Integer roleId = 0;
	
	// 角色名称,from exa_roles
	@Column(name = "role_name")
	private String roleName = "";
	
	// 角色类型，参见系统参数表,from exa_roles
	@Column(name = "role_type")
	private Byte roleType = 0;
	
	// 功能ID
	@Column(name = "func_id")
	private Integer funcId = 0;
	
	// 功能名称,from exa_functions
	@Column(name = "func_name")
	private String funcName = "";
	
	// 父功能ID,from exa_functions
	@Column(name = "parent_id")
	private Integer parentId = 0;
	
	// 是否包含全部子节点权限，0-否，1-是
	@Column(name = "sub_full_flag")
	private Byte subFullFlag = 0;
	
	// 操作人账号
	@Column(name = "operator_name")
	private String operatorName = "";
	
	// 记录删除标记，保留字段
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
