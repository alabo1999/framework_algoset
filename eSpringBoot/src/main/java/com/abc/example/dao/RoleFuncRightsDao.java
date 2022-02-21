package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.abc.example.entity.RoleFuncRights;

/**
 * @className	: RoleFuncRightsDao
 * @description	: 角色和功能权限关系对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface RoleFuncRightsDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个角色和功能权限关系对象
	 * @param item	: 角色和功能权限关系对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(RoleFuncRights item);
	
	/**
	 * @methodName		: insertItems
	 * @description		: 新增一批角色和功能权限关系对象
	 * @param itemList	: 角色和功能权限关系对象列表
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItems(List<RoleFuncRights> itemList);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个角色和功能权限关系对象
	 * @param params	: 角色和功能权限关系对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItemByKey
	 * @description		: 根据key删除一个角色和功能权限关系对象
	 * @param roleId	: 角色ID
	 * @param funcId	: 功能ID
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemByKey(@Param("roleId") Integer roleId,
			 @Param("funcId") Integer funcId);
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除相关角色和功能权限关系对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"funcId"	: 0,	// 功能ID，可选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItems(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemsByCondition
	 * @description		: 根据条件查询角色和功能权限关系对象列表，用于前端分页查询
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"funcId"	: 0,	// 功能ID，可选
	 * 	}
	 * @return			: 角色和功能权限关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<RoleFuncRights> selectItemsByCondition(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个角色和功能权限关系对象
	 * @param roleId	: 角色ID
	 * @param funcId	: 功能ID
	 * @return			: 角色和功能权限关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public RoleFuncRights selectItemByKey(@Param("roleId") Integer roleId,
			 @Param("funcId") Integer funcId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询角色和功能权限关系对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"	: 0,	// limit记录偏移量，可选
	 * 		"rows"		: 20,	// limit最大记录条数，可选
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"funcId"	: 0,	// 功能ID，可选
	 * 	}
	 * @return			: 角色和功能权限关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<RoleFuncRights> selectItems(Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: selectAllItems
	 * @description	: 查询全部角色和功能权限关系对象列表
	 * @return			: 角色和功能权限关系对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<RoleFuncRights> selectAllItems();	
}
