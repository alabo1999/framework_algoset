package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.UserRole;

/**
 * @className	: UserRoleDao
 * @description	: 用户和角色关系对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface UserRoleDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个用户和角色关系对象
	 * @param item	: 用户和角色关系对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(UserRole item);
	
	/**
	 * @methodName		: insertItems
	 * @description		: 新增一批用户和角色关系对象
	 * @param itemList	: 用户和角色关系对象列表
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItems(List<UserRole> itemList);
	
	/**
	 * @methodName		: deleteItemByKey
	 * @description		: 根据key删除一个用户和角色关系对象
	 * @param userId	: 用户ID
	 * @param roleId	: 角色ID
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemByKey(@Param("userId") Long userId,
			 @Param("roleId") Integer roleId);
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除相关用户和角色关系对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItems(Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: deleteItemsByUserId
	 * @description	: 删除指定用户ID的所有对象
	 * @param userId	: 用户ID
	 * @return			: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemsByUserId(@Param("userId") Long userId);
	
	/**
	 * 
	 * @methodName		: deleteItemsByRoleId
	 * @description	: 删除指定角色ID的所有对象
	 * @param roleId	: 角色ID
	 * @return			: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemsByRoleId(@Param("roleId") Integer roleId);
	
	/**
	 * @methodName		: selectItemsByCondition
	 * @description		: 根据条件查询用户和角色关系对象列表，用于前端分页查询
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 	}
	 * @return			: 用户和角色关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserRole> selectItemsByCondition(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个用户和角色关系对象
	 * @param userId	: 用户ID
	 * @param roleId	: 角色ID
	 * @return			: 用户和角色关系对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public UserRole selectItemByKey(@Param("userId") Long userId,
			 @Param("roleId") Integer roleId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询用户和角色关系对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"	: 0,	// limit记录偏移量，可选
	 * 		"rows"		: 20,	// limit最大记录条数，可选
	 * 		"roleId"	: 0,	// 角色ID，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 	}
	 * @return			: 用户和角色关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserRole> selectItems(Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: selectItemsByUserId
	 * @description	: 查询指定用户ID的所有对象
	 * @param userId	: 用户ID
	 * @return			: 用户和角色关系对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserRole> selectItemsByUserId(@Param("userId") Long userId);
	
	/**
	 * 
	 * @methodName		: selectItemsByRoleId
	 * @description	: 查询指定角色ID的所有对象
	 * @param roleId	: 角色ID
	 * @return			: 用户和角色关系对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserRole> selectItemsByRoleId(@Param("roleId") Integer roleId);
}
