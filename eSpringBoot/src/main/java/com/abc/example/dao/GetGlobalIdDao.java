package com.abc.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @className		: GetGlobalIdDao
 * @description	: 获取全局ID Dao类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface GetGlobalIdDao {

	/**
	 * 
	 * @methodName		: getTableRecId
	 * @description	: 获取表ID的一个记录ID
	 * @param tableId	: 表ID
	 * @return			: 记录ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Select("SELECT exa_get_global_id(#{tableId}, 1)")
	Long getTableRecId(@Param("tableId") Integer tableId);
	
	/**
	 * 
	 * @methodName		: getTableRecIds
	 * @description	: 获取表ID的多个记录ID
	 * @param tableId	: 表ID
	 * @param count		: ID个数
	 * @return			: 开始的记录ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Select("SELECT exa_get_global_id(#{tableId}, #{count})")
	Long getTableRecIds(@Param("tableId") Integer tableId, @Param("count") Integer count);
}
