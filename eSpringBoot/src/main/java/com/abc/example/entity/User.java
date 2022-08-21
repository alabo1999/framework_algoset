package com.abc.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @className	: User
 * @description	: 用户对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/02/06	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class User {
	// 用户ID
	@Column(name = "user_id")
	private Long userId = 0L;
	
	// 用户名
	@Column(name = "user_name")
	private String userName = "";
	
	// 用户密码
	@Column(name = "password")
	private String password = "";
	
	// 加盐md5算法中的盐
	@Column(name = "salt")
	private String salt = "";
	
	// 用户类型，1-系统管理员、2-公司内部用户、3-外部用户
	@Column(name = "user_type")
	private Byte userType = 3;
	
	// 组织机构ID
	@Column(name = "org_id")
	private Integer orgId = 0;
	
	// 组织名称
	private String orgName = "";	
	
	// 真实姓名
	@Column(name = "real_name")
	private String realName = "";
	
	// Email
	@Column(name = "email")
	private String email = "";
	
	// 手机号码
	@Column(name = "phone_number")
	private String phoneNumber = "";
	
	// 性别，1-无值、2-男、3-女、4-其它
	@Column(name = "sex")
	private Byte sex = 1;
	
	// 生日
	@Column(name = "birth")
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime birth;
	
	// 身份证号码
	@Column(name = "id_no")
	private String idNo = "";
	
	// 微信小程序的openid
	@Column(name = "open_id")
	private String openId = "";
	
	// 微信公众号openid
	@Column(name = "woa_openid")
	private String woaOpenid = "";
	
	// 备注
	@Column(name = "remark")
	private String remark = "";
	
	// 操作人账号
	@Column(name = "operator_name")
	private String operatorName = "";
	
	// 记录删除标记，0-正常、1-禁用
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
