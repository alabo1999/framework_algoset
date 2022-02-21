package com.abc.example.service;

/**
 * @className		: ChangeNotifyService
 * @description	: 变更通知服务
 * @summary		: 变更通知值，使用bitmap编码，定义如下：
 * 		bit0:		: 修改用户的角色组合值，从而导致权限变更；
 * 		bit1:		: 修改角色的功能项，从而导致权限变更；
 * 		bit2:		: 功能项变更，导致功能树、权限树的变更；
 * 		bit3:		: 用户禁用，从而导致权限变更；
 * 		bit4:		: 调整用户数据权限类型，导致数据权限变更；
 * 		bit5:		: 数据权限类型为自定义，修改组织集合导致数据权限变更；
 * 		bit6:		: 修改用户组织ID，导致数据权限变更
 * 		bit7:		: 修改用户类型，导致数据权限变更
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface ChangeNotifyService {

	/**
	 * 
	 * @methodName		: getChangeNotifyInfo
	 * @description	: 获取指定用户ID的变更通知信息 
	 * @param userId	: 用户ID
	 * @return			: 返回0表示无变更通知信息，其它值按照bitmap编码。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Integer getChangeNotifyInfo(Long userId);
	
	/**
	 * 
	 * @methodName		: setChangeNotifyInfo
	 * @description	: 设置变更通知信息
	 * @param userId	: 用户ID
	 * @param changeNotifyInfo	: 变更通知值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setChangeNotifyInfo(Long userId,Integer changeNotifyInfo); 	
}
