package com.abc.example.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.abc.example.entity.DrField;
import com.abc.example.entity.UserCustomDr;
import com.abc.example.entity.UserDr;

/**
 * @className		: DataRightsService
 * @description	: 数据权限服务类
 * @summary		: 数据权限包括DrField(dr字段)、UserDr、UserCustomDr三种对象。
 * 	1、与数据权限管理相关的字段，需要在exa_dr_fields表中配置，包括下列属性：
 *     字段ID、字段名称、字段数据类型、是否为用户属性字段、是否为ID字段、是否有下级对象、无效值
 * 	1.1、如果为ID字段，后续包含2个属性：是否有下级对象、无效值。
 * 	   如果包含下级对象，如组织，则数据集将扩展到该对象及下级对象。如果不包含下级对象，则不扩展。
 * 	   无效值，在数据权限为空集时，作为查询条件，使用无效值，可以使得查询结果为空集。
 *  ----------------------------------------------------------------------------
 * 	2、用户数据权限，需要在exa_user_drs表中配置，包括下列属性：
 * 	2.1、每个用户创建时，同时创建相关默认记录，每个数据权限字段一条记录，不允许删除，只允许修改。
 * 	2.2、如果为用户属性字段，可以使用默认规则权限类型，否则不能使用。
 * 	2.3、用户属性字段，默认值为默认规则，否则默认值为自定义。
 * 	2.4、自定义权限，如果无记录，表示无权限。其它，根据记录确定数据权限。
 *  ----------------------------------------------------------------------------
 *  3、用户的数据权限使用：
 *  3.1、用户登录成功后，用户的数据权限保存到账户缓存中，避免频繁查询。
 *  3.2、业务需要检查数据权限时，分操作的数据权限和查询的数据权限。操作时，如果无权限，抛出异常；
 *       查询时，做过滤，仅允许获取有数据权限的数据集。
 *  -----------------------------------------------------------------------------
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/03/10	1.0.0		sheng.zheng		初版
 *
 */
public interface DataRightsService {
	
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据库中数据 
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */		
	public boolean loadData();	
	
	/**
	 * 
	 * @methodName		: checkUserDr
	 * @description	: 检查当前用户是否对给定对象有数据权限
	 * @param request	: request对象，可从中获取当前用户的缓存信息
	 * @param params	: 权限字段与值的字典
	 * @return			: 允许返回true，否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public void checkUserDr(HttpServletRequest request,Map<String,Object> params);
	
		
	/**
	 * 
	 * @methodName		: getQueryDrList
	 * @description	: 获取查询的dr字段值列表
	 * 	如果为全部，则返回空列表；如果无权限，则返回包含一个无效值的列表
	 * 	如果为其它情况，则返回一个dr字段值列表
	 * @param request	: request对象
	 * @param propName	: 属性字段名
	 * @return			: 属性值列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Integer> getQueryDrList(HttpServletRequest request,String propName);	
	
	/**
	 * 
	 * @methodName		: getUserDrList
	 * @description	: 根据用户ID，获取用户数据权限对象列表
	 * @param userId	: 用户ID
	 * @return			: appId列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserDr> getUserDrList(Long userId);	
	
	/**
	 * 
	 * @methodName		: getUserDr
	 * @description	: 根据用户ID和权限字段名，获取用户数据权限对象
	 * @param userId	: 用户ID
	 * @param drPropName	: 权限字段名
	 * @return			: UserDr对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public UserDr getUserDr(Long userId,String drPropName);	
	
	/**
	 * 
	 * @methodName		: getUserDr
	 * @description	: 根据用户ID和权限字段名，获取用户数据权限对象
	 * @param request	: request对象
	 * @param drPropName	: 权限字段名
	 * @return			: UserDr对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public UserDr getUserDr(HttpServletRequest request,String drPropName);		
	
	/**
	 * 
	 * @methodName		: setUserDrs
	 * @description	: 设置用户数据权限到账户缓存中
	 * @param request	: request对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setUserDrs(HttpServletRequest request);
	
	/**
	 * 
	 * @methodName		: setUserDrList
	 * @description	: 设置用户数据权限到账户缓存中
	 * @param request	: request对象
	 * @param userId	: 用户ID
	 * @param userDrList: 用户数据权限对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setUserDrList(HttpServletRequest request,List<UserDr> userDrList);
	
	/**
	 * 
	 * @methodName		: getDefaultUserDrList
	 * @description	: 获取默认的用户权限列表
	 * @return			: 默认的用户权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserDr> getDefaultUserDrList();
	
	/**
	 * 
	 * @methodName		: setDefaultUserDrList
	 * @description	: 设置more用户数据权限列表
	 * @param userId	: 用户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setDefaultUserDrList(Long userId);
	
	/**
	 * 
	 * @methodName		: getUserCustomDrList
	 * @description	: 获取用户的自定义数据权限列表
	 * @param userId	: 用户ID
	 * @param propName	: 属性字段名
	 * @return			: UserCustomDr列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserCustomDr> getUserCustomDrList(Long userId,String propName);
	
	/**
	 * 
	 * @methodName		: getDrField
	 * @description	: 根据字段ID，获取数据权限字段对象
	 * @param fieldId	: 字段ID
	 * @return			: DrField对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public DrField getDrField(Integer fieldId);
	
	/**
	 * 
	 * @methodName		: getDrField
	 * @description	: 根据字段ID，获取数据权限字段对象
	 * @param propName	: 属性名
	 * @return			: DrField对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public DrField getDrField(String propName);	
	
}
