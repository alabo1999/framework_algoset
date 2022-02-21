package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.DrField;

/**
 * @className	: DrFieldDao
 * @description	: 数据权限相关字段对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/23	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface DrFieldDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个数据权限相关字段对象
	 * @param item	: 数据权限相关字段对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(DrField item);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个数据权限相关字段对象
	 * @param params	: 数据权限相关字段对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"fieldId"	: 0,	// 字段ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName		: selectItemByKey
	 * @description		: 根据key查询一个数据权限相关字段对象
	 * @param fieldId	: 字段ID
	 * @return			: 数据权限相关字段对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public DrField selectItemByKey(@Param("fieldId") Integer fieldId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询数据权限相关字段对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"fieldId"		: 0,	// 字段ID，可选
	 * 		"deleteFlag"	: 0,	// 记录标记，，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 数据权限相关字段对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<DrField> selectItems(Map<String, Object> params);
	
	/**
	 * 
	 * @methodName		: selectAllItems
	 * @description	: 查询全部数据权限相关字段对象列表
	 * @return			: 数据权限相关字段对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/24	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<DrField> selectAllItems();	
}
