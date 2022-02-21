package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.User;

/**
 * @className	: UserDao
 * @description	: 用户对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/02/06	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface UserDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个用户对象
	 * @param item	: 用户对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/06	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(User item);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个用户对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/06	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemsByCondition
	 * @description		: 根据条件查询用户对象列表，用于前端分页查询
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userType"		: 3,	// 用户类型，1-系统管理员、2-公司内部用户、3-外部用户，可选
	 * 		"sex"			: 1,	// 性别，1-无值、2-男、3-女、4-其它，可选
	 * 		""				: ,		// 
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"userName"		: "",	// 用户名，like，可选
	 * 		"phoneNumber"	: "",	// 手机号码，like，可选
	 * 		"idNo"			: "",	// 身份证号码，like，可选
	 * 		"realName"		: "",	// 真实姓名，like，可选
	 * 		"email"			: "",	// Email，like，可选
	 * 		"birthStart"	: ,		// 生日起始值，gte，可选
	 * 		"birthEnd"		: ,		// 生日终止值，lte，可选
	 * 		"orgIdList"		: [],	// 组织机构ID列表，可选
	 * 	}
	 * @return			: 用户对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/07	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<User> selectItemsByCondition(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个用户对象
	 * @param userId	: 用户ID
	 * @return			: 用户对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/07	1.0.0		sheng.zheng		初版
	 *
	 */
	public User selectItemByKey(@Param("userId") Long userId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询用户对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"idNo"			: "",	// 身份证号码，可选
	 * 		"email"			: "",	// Email，可选
	 * 		"orgId"			: 0,	// 组织机构ID，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 		"userIdList"	: [],	// 用户ID列表，可选
	 * 		"orgIdList"		: [],	// 组织机构ID列表，可选
	 * 	}
	 * @return			: 用户对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/08	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<User> selectItems(Map<String, Object> params);
}
