package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.UserDr;

/**
 * @className	: UserDrDao
 * @description	: 数据权限规则对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface UserDrDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个数据权限规则对象
	 * @param item	: 数据权限规则对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(UserDr item);
	
	/**
	 * @methodName		: insertItems
	 * @description		: 新增一批数据权限规则对象
	 * @param itemList	: 数据权限规则对象列表
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItems(List<UserDr> itemList);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个数据权限规则对象
	 * @param params	: 数据权限规则对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 		"fieldId"	: 0,	// 字段ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个数据权限规则对象
	 * @param userId	: 用户ID
	 * @param fieldId	: 字段ID
	 * @return			: 数据权限规则对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public UserDr selectItemByKey(@Param("userId") Long userId,
			 @Param("fieldId") Integer fieldId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询数据权限规则对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"	: 0,	// limit记录偏移量，可选
	 * 		"rows"		: 20,	// limit最大记录条数，可选
	 * 		"userId"	: 0L,	// 用户ID，可选
	 * 		"fieldId"	: 0,	// 字段ID，可选
	 * 		"drType"	: 1,	// 数据权限类型，1-默认规则、2-自定义、3-全部，可选
	 * 	}
	 * @return			: 数据权限规则对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/25	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<UserDr> selectItems(Map<String, Object> params);
}
