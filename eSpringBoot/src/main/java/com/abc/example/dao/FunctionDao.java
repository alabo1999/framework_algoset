package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.Function;

/**
 * @className	: FunctionDao
 * @description	: 功能对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/15	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface FunctionDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个功能对象
	 * @param item	: 功能对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(Function item);
	
	/**
	 * @methodName		: insertItems
	 * @description		: 新增一批功能对象
	 * @param itemList	: 功能对象列表
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItems(List<Function> itemList);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个功能对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItemByKey
	 * @description		: 根据key删除一个功能对象
	 * @param funcId	
	 * @param funcId	: 功能ID: 功能ID
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemByKey(@Param("funcId") Integer funcId);
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除相关功能对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"parentId"	: 0,	// 父功能ID，可选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/17	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItems(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemsByCondition
	 * @description		: 根据条件查询功能对象列表，用于前端分页查询
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 		"funcName"		: "",	// 功能名称，like，可选
	 * 		"url"			: "",	// 访问接口url，like，可选
	 * 		"domKey"		: "",	// dom对象的ID，like，可选
	 * 		"parentId"		: 0,	// 父功能ID，可选
	 * 	}
	 * @return			: 功能对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/17	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Function> selectItemsByCondition(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个功能对象
	 * @param funcId	: 功能ID
	 * @return			: 功能对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public Function selectItemByKey(@Param("funcId") Integer funcId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询功能对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: ,		// limit记录偏移量，可选
	 * 		"rows"			: ,		// limit最大记录条数，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 功能对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Function> selectItems(Map<String, Object> params);

	/**
	 * 
	 * @methodName		: selectAllItems
	 * @description	: 查询全部功能
	 * @return			: 功能对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Function> selectAllItems();
}
