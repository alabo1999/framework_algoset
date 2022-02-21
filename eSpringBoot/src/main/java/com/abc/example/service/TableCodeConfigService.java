package com.abc.example.service;

/**
 * @className		: TableCodeConfigService
 * @description	: table_code_config表数据服务
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface TableCodeConfigService{
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据，包括重新加载数据，由子类重载
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean loadData();	
		
	/**
	 * 
	 * @methodName		: getTableId
	 * @description	: 根据tableName获取tableId
	 * @param tableName	: 表名称
	 * @return			: 表id
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public int getTableId(String tableName);
	
	/**
	 * 
	 * @methodName		: getTableRecId
	 * @description	: 获取指定表名的一条记录ID
	 * @param tableName	: 表名
	 * @return			: 记录ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Long getTableRecId(String tableName);
	
	/**
	 * 
	 * @methodName		: getTableRecIds
	 * @description	: 获取指定表名的多条记录ID
	 * @param tableName	: 表名
	 * @param recCount	: 记录条数
	 * @return			: 第一条记录ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public Long getTableRecIds(String tableName,int recCount);
	
	
}
