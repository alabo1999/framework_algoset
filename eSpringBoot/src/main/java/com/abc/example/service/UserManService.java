package com.abc.example.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.github.pagehelper.PageInfo;
import com.abc.example.entity.User;
import com.abc.example.entity.UserCustomDr;
import com.abc.example.entity.UserDr;

/**
 * @className	: UserManService
 * @description	: 用户对象管理服务接口类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/06	1.0.0		sheng.zheng		初版
 *
 */
public interface UserManService {
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个用户对象
	 * @param request	: request对象
	 * @param item		: 用户对象
	 * @return			: 新增的用户对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	public Map<String,Object> addItem(HttpServletRequest request, User item);
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个用户对象
	 * @param request	: request对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	public void editItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个用户对象
	 * @param request	: request对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	public void deleteItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: resetPassword
	 * @description	: 管理员重置密码
	 * @param request	: request对象
	 * @param params	: 设置参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"password"	: "",  	//新密码
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public void resetPassword(HttpServletRequest request,Map<String,Object> params);
	
	/**
	 * 
	 * @methodName		: changePassword
	 * @description	: 用户自己修改密码
	 * @param request	: request对象
	 * @param params	: 设置参数，形式如下：
	 * 	{
	 * 		"oldPassword"	: "",  	//原密码,md5值
	 * 		"newPassword"	: "",  	//新密码,md5值
	 * 		"confirmPassword"	: "",  	//确认密码,md5值
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public void changePassword(HttpServletRequest request,Map<String,Object> params);	
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询用户对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userType"		: 3,	// 用户类型，1-系统管理员、2-公司内部用户、3-外部用户，可选
	 * 		"sex"			: 1,	// 性别，1-无值、2-男、3-女、4-其它，可选
	 * 		"drType"		: 0,	// 数据权限类型，1-默认规则、2-自定义、3-全部，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"userName"		: "",	// 用户名，like，可选
	 * 		"phoneNumber"	: "",	// 手机号码，like，可选
	 * 		"realName"		: "",	// 真实姓名，like，可选
	 * 		"email"			: "",	// Email，like，可选
	 * 		"birthStart"	: ,		// 生日起始值，gte，可选
	 * 		"birthEnd"		: ,		// 生日终止值，lte，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 用户对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	public PageInfo<User> queryItems(HttpServletRequest request,
			 Map<String, Object> params);
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个用户对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @return			: 用户对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
	 *
	 */
	public User getItem(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询用户对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"email"			: "",	// Email，可选
	 * 		"drId"			: 0,	// 权限相关对象ID，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 		"userIdList"	: [],	// 用户ID列表，可选
	 * 	}
	 * @return			: 用户对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<User> getItems(HttpServletRequest request, Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: updateFuncRights
	 * @description	: 修改功能权限，即修改用户角色
	 * @param request	: request对象
	 * @param params	: 设置参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"roleIdList"	: [],  	//角色ID列表
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public void updateFuncRights(HttpServletRequest request,Map<String,Object> params);

	/**
	 * 
	 * @methodName		: getFuncRights
	 * @description	: 获取功能权限，即角色ID列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 	}	 
	 * @return			: 角色ID列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Integer> getFuncRights(HttpServletRequest request,Map<String,Object> params);	
	
	/**
	 * 
	 * @methodName		: updateUserDrs
	 * @description	: 修改用户数据权限列表
	 * @param request	: request对象
	 * @param itemList	: 用户数据权限对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public void updateUserDrs(HttpServletRequest request,List<UserDr> itemList);

	/**
	 * 
	 * @methodName		: getUserDrs
	 * @description	: 获取用户数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 	}	 
	 * @return			: 用户数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserDr> getUserDrs(HttpServletRequest request,Map<String,Object> params);	
	
	/**
	 * 
	 * @methodName		: updateUserCustomDrs
	 * @description	: 修改用户自定义数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 		"valueList"	: [], 	//字段值列表
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public void updateUserCustomDrs(HttpServletRequest request,Map<String,Object> params);

	/**
	 * 
	 * @methodName		: addUserCustomDr
	 * @description	: 增加用户自定义数据权限
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 		"fieldValue": 1, 	//字段值
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/07/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addUserCustomDr(HttpServletRequest request,Map<String,Object> params);
	
	/**
	 * 
	 * @methodName		: getUserCustomDrs
	 * @description	: 获取用户自定义数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 	}	 
	 * @return			: 用户数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserCustomDr> getUserCustomDrs(HttpServletRequest request,Map<String,Object> params);	
	
	/**
	 * 
	 * @methodName		: getCurrentUserDr
	 * @description	: 获取当前用户的数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"propName"	: "orgId", 	//权限属性字段名
	 * 	}	 
	 * @return			: 用户对指定属性字段的数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Integer> getCurrentUserDr(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: getItemByKeyInfo
	 * @description	: 根据关键信息获取用户对象
	 * @param params	: 查询信息，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"idNo"			: "",	// 身份证号码，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 	}	 
	 * @return				: User对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public User getItemByKeyInfo(Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: getItemsCountByKeyInfo
	 * @description	: 根据关键信息，查询记录数
	 * @param params	: 查询信息，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"idNo"			: "",	// 身份证号码，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 	}	 
	 * @return			: 记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/11	1.0.0		sheng.zheng		初版
	 *
	 */
	public Integer getItemsCountByKeyInfo(Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: addUserRole
	 * @description	: 添加用户角色，如果用户的指定角色ID未添加，则添加
	 * @param request	: request对象
	 * @param userId	: 用户ID
	 * @param roleId	: 角色ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addUserRole(HttpServletRequest request,Long userId,Integer roleId);
	
	/**
	 * 
	 * @methodName		: addUser
	 * @description	: 添加用户，此为线程调用的方法
	 * @param item		: 用户对象，所有参数已设置
	 * @param roleId	: 角色ID
	 * @param udList	: 用户数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addUser(User item,Integer roleId,List<UserDr> udList);	
}