package com.abc.example.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.abc.example.entity.Orgnization;

/**
 * @className	: OrgnizationDao
 * @description	: 组织机构对象数据访问类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/02/14	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface OrgnizationDao {
	/**
	 * @methodName	: insertItem
	 * @description	: 新增一个组织机构对象
	 * @param item	: 组织机构对象
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	public int insertItem(Orgnization item);
	
	/**
	 * @methodName		: updateItemByKey
	 * @description		: 根据key修改一个组织机构对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @return			: 受影响的记录数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	public int updateItemByKey(Map<String, Object> params);
	
	/**
	 * @methodName	: deleteItemByKey
	 * @description	: 根据key删除一个组织机构对象
	 * @param orgId	
	 * @param orgId	: 组织ID: 组织ID
	 * @return		: 受影响的记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public int deleteItemByKey(@Param("orgId") Integer orgId);
	
	/**
	 * @methodName		: selectItemsByCondition
	 * @description		: 根据条件查询组织机构对象列表，用于前端分页查询
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"parentId"		: 0,	// 父组织ID，可选
	 * 		"orgType"		: 3,	// 机构类型，1-本公司，2-政府管理部门，3-学院，可选
	 * 		"orgCode"		: "",	// 组织机构编号，like，可选
	 * 		"orgName"		: "",	// 组织机构名称，like，可选
	 * 		"orgFullname"	: "",	// 组织机构全称，like，可选
	 * 		"leader"		: "",	// 负责人名称，like，可选
	 * 		"address"		: "",	// 地址，like，可选
	 * 		"district"		: "",	// 行政区省、市、区县名称，like，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，1-已删除，可选
	 * 		"orgIdList"		: [],	// 组织ID列表，可选
	 * 	}
	 * @return			: 组织机构对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Orgnization> selectItemsByCondition(Map<String, Object> params);
	
	/**
	 * @methodName	: selectItemByKey
	 * @description	: 根据key查询一个组织机构对象
	 * @param orgId	: 组织ID
	 * @return		: 组织机构对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public Orgnization selectItemByKey(@Param("orgId") Integer orgId);
	
	/**
	 * @methodName		: selectItems
	 * @description		: 根据条件查询组织机构对象列表，用于前端和内部查询记录
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"parentId"		: 0,	// 父组织ID，可选
	 * 		"orgType"		: 3,	// 机构类型，1-本公司，2-政府管理部门，3-学院，可选
	 * 		"orgCode"		: "",	// 组织机构编号，可选
	 * 		"orgName"		: "",	// 组织机构名称，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，1-已删除，可选
	 * 		"orgIdList"		: [],	// 组织ID列表，可选
	 * 	}
	 * @return			: 组织机构对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<Orgnization> selectItems(Map<String, Object> params);
	
	/**
	 * @methodName	: selectItemByName
	 * @description	: 根据组织名称查询一个组织机构对象
	 * @param orgName	: 组织名称
	 * @return		: 组织对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	public Orgnization selectItemByName(@Param("orgName") String orgName);	
}
