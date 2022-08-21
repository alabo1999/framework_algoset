package com.abc.example.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.abc.example.entity.SysParameter;

/**
 * @className		: SysParameterDao
 * @description	: exa_sys_parameters表数据访问类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface SysParameterDao {

	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个系统参数对象
	 * @param item	: 系统参数对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(SysParameter item);
	
	/**
	 * @methodName		: insertItems
	 * @description		: 新增一批系统参数对象
	 * @param itemList	: 系统参数对象列表
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItems(List<SysParameter> itemList);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个系统参数对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName		: updateItems
	 * @description		: 根据条件修改一个或多个系统参数对象
	 * @param params	: 系统参数对象相关字段字典，其它字段均可选，条件字段如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 		"itemKey"	: "",	// 子项key，可选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItems(Map<String, Object> params);
	
	/**
	 * @methodName		: deleteItemByKey
	 * @description		: 根据key删除一个系统参数对象
	 * @param classId	: 参数类别ID
	 * @param itemId	: 参数类别下子项ID
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemByKey(@Param("classId") Integer classId,
			 @Param("itemId") Integer itemId);
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除相关系统参数对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItems(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemsByCondition
	 * @description		: 根据条件查询系统参数对象列表，用于前端分页查询
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"classId"		: 0,	// 参数类别ID，可选
	 * 		"classKey"		: "",	// 参数类别key，可选
	 * 		"className"		: "",	// 参数类别名称，like，可选
	 * 		"itemKey"		: "",	// 子项key，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 系统参数对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<SysParameter> selectItemsByCondition(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个系统参数对象
	 * @param classId	: 参数类别ID
	 * @param itemId	: 参数类别下子项ID
	 * @return			: 系统参数对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	public SysParameter selectItemByKey(@Param("classId") Integer classId,
			 @Param("itemId") Integer itemId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询系统参数对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"classId"		: 0,	// 参数类别ID，可选
	 * 		"classKey"		: "",	// 参数类别key，可选
	 * 		"itemKey"		: "",	// 子项key，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 系统参数对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<SysParameter> selectItems(Map<String, Object> params);
	
	//查询所有系统参数，按class_id,item_id排序
    List<SysParameter> selectAllItems();
	
	// 查询指定类别key的参数类别下的所有子项	
	List<SysParameter> selectItemsByClassKey(@Param("classKey") String classKey);
	
	// 查询指定类别key、子项key的子项
	SysParameter selectItemByKey2(@Param("classKey") String classKey,@Param("itemKey") String itemKey);	
}
