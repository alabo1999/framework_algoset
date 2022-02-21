package com.abc.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.abc.example.entity.TableCodeConfig;

/**
 * @className		: TableCodeConfigDao
 * @description	: exa_table_code_config表数据访问类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Mapper
public interface TableCodeConfigDao {
	//查询全部记录
	@Select("SELECT table_id,table_name,field_name,prefix,prefix_len,seqno_len"
			+ " FROM exa_table_code_config")
	List<TableCodeConfig> selectAllItems();
	
	//查询指定tableName的记录
	@Select("SELECT table_id,table_name,field_name,prefix,prefix_len,seqno_len"
			+ " FROM exa_table_code_config WHERE table_name = #{tableName}")
	TableCodeConfig selectItem(@Param("tableName") String tableName);
	
}
