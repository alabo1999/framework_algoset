package com.abc.example.service;

import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;

/**
 * @className		: CacheDataConsistencyService
 * @description	: 缓存数据一致性服务类
 * @summary		: 缓存数据一致性，包括：
 * 	1、Session数据一致性，管理员修改用户数据，与用户登录缓存到Session不一致。
 * 	2、后台配置数据一致性，管理员通过后台数据库修改系统参数相关表，导致与缓存不一致。
 * 	3、维护对象数据，导致缓存的对象字典的数据一致性，要考虑多服务器情况，session共享来解决。
 * 	4、各对象之间相关影响，导致缓存数据不一致。
 * 	5、多个缓存对象之间的数据不一致，应考虑只有一份对象，其它通过ID存储。
 *  6、多个数据库表之间的数据一致性，属于持久存储，不在此处考虑。
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/04/15	1.0.0		sheng.zheng		初版
 *
 */
public interface CacheDataConsistencyService {
	
	/**
	 * 
	 * @methodName		: updateFuncTree
	 * @description	: 刷新功能树
	 * @summary		: 修改功能树，影响如下：
	 * 	1、功能树改变，导致角色功能树也变化，需要重新加载
	 * 	2、所有已登录的用户，需要更新权限树，否则前端缓存的是原权限树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public void updateFuncTree();
	
	/**
	 * 
	 * @methodName		: updateRoleFuncRights
	 * @description	: 刷新角色功能关系
	 * @summary		: 修改角色功能关系，影响如下：
	 * 	1、所有已登录的用户，需要更新权限树，否则前端缓存的是原权限树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public void updateRoleFuncRights();
	
	/**
	 * 
	 * @methodName		: cacheObjectChanged
	 * @description	: 普通缓存对象改变
	 * @param eCacheObjectType	: 缓存对象类型
	 * @param oldObj	: 修改之前的对象
	 * @param newObj	: 修改之后的对象
	 * @param eDataOperationType:操作类型
	 * @summary		: 
	 *  如果修改组织、监控点、设备、设备关系、督导人员、督导人员监控点关系将导致看板缓存刷新
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public void cacheObjectChanged(ECacheObjectType eCacheObjectType,Object oldObj,Object newObj,
			EDataOperationType eDataOperationType);
}
