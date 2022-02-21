package com.abc.example.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.abc.example.entity.Role;
import com.abc.example.entity.UserRole;

/**
 * @className		: RoleService
 * @description	: 角色服务类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface RoleService {

	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据，包括重新加载数据，由子类重载
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean loadData();
	
	/**
	 * 
	 * @methodName		: getRoleItem
	 * @description	: 获取角色对象
	 * @param roleId	: 角色ID
	 * @return			: 角色对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Role getRoleItem(Integer roleId);
	
	/**
	 * 
	 * @methodName		: getRoleItems
	 * @description	: 获取全部角色列表
	 * @return			: 角色列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Role> getRoleItems();
	
	/**
	 * 
	 * @methodName		: getUserRolesByUserId
	 * @description	: 获取指定用户的角色列表
	 * @param userId	: 用户ID
	 * @return			: UserRole列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserRole> getUserRolesByUserId(Long userId);
	
	/**
	 * 
	 * @methodName		: getRoleIdsByUserId
	 * @description	: 获取指定用户ID的角色列表
	 * @param userId	: 用户ID
	 * @return			: roleId列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Integer> getRoleIdsByUserId(Long userId);
	
	/**
	 * 
	 * @methodName		: isUserContainsRoleId
	 * @description	: 判断一个用户是否有某种角色的权限
	 * @param userId	: 用户ID
	 * @param roleId	: 角色ID
	 * @return			: true表示包含，false表示不包含
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean isUserContainsRoleId(Long userId,Integer roleId);
	
	/**
	 * 
	 * @methodName		: getUserIdsByRoleId
	 * @description	: 根据角色ID获取用户ID列表
	 * @param roleId	: 角色ID
	 * @return			: 用户ID列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Long> getUserIdsByRoleId(Integer roleId);
	
	/**
	 * 
	 * @methodName		: getFuncRights
	 * @description	: 设置操作权限
	 * @param request	: request对象
	 * @param userId	: 用户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setFuncRights(HttpServletRequest request,Long userId);
	
}
