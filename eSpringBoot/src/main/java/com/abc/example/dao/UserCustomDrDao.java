package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.UserCustomDr;

/**
 * @className	: UserCustomDrDao
 * @description	: 用户数据权限关系对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface UserCustomDrDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个用户数据权限关系对象
	 * @param item	: 用户数据权限关系对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(UserCustomDr item);
	
	/**
	 * @methodName		: insertItems
	 * @description		: 新增一批用户数据权限关系对象
	 * @param itemList	: 用户数据权限关系对象列表
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItems(List<UserCustomDr> itemList);
	
	/**
	 * @methodName	: deleteItemByKey
	 * @description	: 根据key删除一个用户数据权限关系对象
	 * @param userId	: 用户ID
	 * @param fieldId	: 字段ID
	 * @param fieldValue	: 字段值
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemByKey(@Param("userId") Long userId,@Param("fieldId") Integer fieldId,
			@Param("fieldValue") Integer fieldValue);
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除相关用户数据权限关系对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 		"fieldId"	: 0,	// 字段ID，可选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItems(Map<String, Object> params);
	
	/**
	 * @methodName	: selectItemByKey
	 * @description	: 根据key查询一个用户数据权限关系对象
	 * @param userId	: 用户ID
	 * @param fieldId	: 字段ID
	 * @param fieldValue	: 字段值
	 * @return		: 用户数据权限关系对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/25	1.0.0		sheng.zheng		初版
	 *
	 */
	public UserCustomDr selectItemByKey(@Param("userId") Long userId,@Param("fieldId") Integer fieldId,
			@Param("fieldValue") Integer fieldValue);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询用户数据权限关系对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"	: 0,	// limit记录偏移量，可选
	 * 		"rows"		: 20,	// limit最大记录条数，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 		"fieldId"	: 0,	// 字段ID，可选
	 * 	}
	 * @return			: 用户数据权限关系对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/25	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserCustomDr> selectItems(Map<String, Object> params);
}
