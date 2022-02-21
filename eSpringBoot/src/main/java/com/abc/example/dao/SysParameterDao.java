package com.abc.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

	//查询所有系统参数，按class_id,item_id排序
	@Select("SELECT class_id,class_key,class_name,item_id,item_key,item_value,item_desc"
			+ " FROM exa_sys_parameters WHERE delete_flag = 0" 
			+ " ORDER BY class_id,item_id")
    List<SysParameter> selectAllItems();
	
	// 查询指定类别key的参数类别下的所有子项	
	@Select("SELECT class_id,class_key,class_name,item_id,item_key,item_value,item_desc"
			+ " FROM exa_sys_parameters WHERE delete_flag = 0 and class_key = #{classKey}" 
			+ " ORDER BY item_id")
	List<SysParameter> selectItems(@Param("classKey") String classKey);
	
	// 查询指定类别key、子项key的子项
	@Select("SELECT class_id,class_key,class_name,item_id,item_key,item_value,item_desc"
			+ " FROM exa_sys_parameters WHERE delete_flag = 0 and class_key = #{classKey}" 
			+ " and item_key = #{itemKey}")
	SysParameter selectItemByKey(@Param("classKey") String classKey,@Param("itemKey") String itemKey);
	
	// 更新指定类别key、子项key的子项值
	@Update("UPDATE exa_sys_parameters set item_value = #{itemValue} "
			+ "WHERE class_key = #{classKey} "
			+ "and item_key = #{itemKey}")
	int updateItem(@Param("classKey") String classKey,@Param("itemKey") String itemKey,@Param("itemValue") String itemValue);
	
}
