package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.constants.Constants;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.common.utils.ObjListUtil;
import com.abc.example.dao.RoleDao;
import com.abc.example.dao.UserRoleDao;
import com.abc.example.entity.Role;
import com.abc.example.entity.UserRole;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.AccountCacheService;
import com.abc.example.service.RoleService;


/**
 * @className		: RoleServiceImpl
 * @description	: 
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class RoleServiceImpl implements RoleService{
	// 账户缓存服务
	@Autowired
	protected AccountCacheService accountCacheService;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	// 管理全部的exa_roles表记录
	private Map<Integer,Role> roleMap = new HashMap<Integer,Role>(); 
	
	
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
	@Override
	public boolean loadData() {
		try {
			// 查询exa_roles表，获取全部数据
			List<Role> roleList = roleDao.selectAllItems();
			
			// 先清空map，便于刷新调用
			// 加锁保护
			synchronized(roleMap) {
				roleMap.clear();
				// 将查询结果放入map对象中			
				for(Role item : roleList){
					roleMap.put(item.getRoleId(), item);
				}							
			}
		}catch(Exception e) {
			LogUtil.error(e);
			return false;
		}
		return true;
	}
	
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
	@Override
	public Role getRoleItem(Integer roleId) {
		Role item = null;
		if (roleMap.containsKey(roleId)) {
			item = roleMap.get(roleId);
		}
		return item;
	}
	
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
	@Override
	public List<Role> getRoleItems(){
		List<Role> roleList = new ArrayList<Role>();
		for (Role item : roleMap.values()) {
			roleList.add(item);
		}
		return roleList;
	}
	
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
	@Override
	public List<UserRole> getUserRolesByUserId(Long userId){
		// 获取用户角色列表			
		List<UserRole> userRoleList = userRoleDao.selectItemsByUserId(userId);
		return userRoleList;
	}
	
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
	@Override
	public List<Integer> getRoleIdsByUserId(Long userId){
		List<UserRole> userRoleList = getUserRolesByUserId(userId);
		List<Integer> roleIdList = ObjListUtil.getSubFieldList(userRoleList, "roleId");
    	return roleIdList;		
	}
	
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
	@Override
	public boolean isUserContainsRoleId(Long userId,Integer roleId) {
		boolean bRet = false;
		List<UserRole> userRoleList = getUserRolesByUserId(userId);
		for (UserRole userRole : userRoleList) {
			if (userRole.getRoleId() == roleId.intValue()) {
				// 如果角色ID为指定角色ID
				bRet = true;
				break;
			}
		}
		return bRet;
	}
	
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
	@Override
	public List<Long> getUserIdsByRoleId(Integer roleId){		
		List<UserRole> userRoleList = userRoleDao.selectItemsByRoleId(roleId);
		List<Long> userIdList = ObjListUtil.getSubFieldList(userRoleList, "userId");
		return userIdList;
	}
	
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
	public void setFuncRights(HttpServletRequest request,Long userId) {
		// 获取缓存账号ID
		String accountId = accountCacheService.getId(request);		
    	
    	// 操作权限  
    	List<Integer> roleIdList = getRoleIdsByUserId(userId);
    	if (roleIdList.size() == 0) {
    		// 未分配权限
    		throw new BaseException(ExceptionCodes.NO_ROLE_ASSIGNED); 
    	}
    	
		// 设置角色列表
    	accountCacheService.setAttribute(accountId,Constants.ROLE_ID_LIST,roleIdList);
		
	}
	
}
