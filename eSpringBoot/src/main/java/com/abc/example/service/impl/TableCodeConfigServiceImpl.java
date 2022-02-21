package com.abc.example.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.constants.Constants;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.dao.GetGlobalIdDao;
import com.abc.example.dao.TableCodeConfigDao;
import com.abc.example.entity.TableCodeConfig;
import com.abc.example.service.TableCodeConfigService;



/**
 * @className		: TableCodeConfigServiceImpl
 * @description	: TableCodeConfigService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class TableCodeConfigServiceImpl implements TableCodeConfigService{

	// table_code_config表数据访问对象
	@Autowired
	private TableCodeConfigDao tableCodeConfigDao;
	
	// 获取全局ID的数据访问类对象
	@Autowired
	private GetGlobalIdDao getGlobalIdDao;		
	
	// 管理全部的table_code_config表记录
	private Map<String,TableCodeConfig> tableCodeConfigMap = new HashMap<String,TableCodeConfig>(); 

	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据库中数据 
	 * @return			: 成功返回true，否则返回false。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public boolean loadData() {
		try {
			// 查询table_code_config表，获取全部数据
			List<TableCodeConfig> tableCodeConfigList = tableCodeConfigDao.selectAllItems();
			
			// 先清空map，便于刷新调用
			// 加锁保护
			synchronized(tableCodeConfigMap) {
				tableCodeConfigMap.clear();
				// 将查询结果放入map对象中			
				for(TableCodeConfig item : tableCodeConfigList){
					tableCodeConfigMap.put(item.getTableName(), item);
				}							
			}
		}catch(Exception e) {
			LogUtil.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @methodName		: getTableId
	 * @description	: 根据tableName获取tableId
	 * @param tableName	: 表名称
	 * @return			: 表id,如果表不存在，则返回-1
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public int getTableId(String tableName) {
		int retId = Constants.INVALID_ID;
		
		// 如果tableName存在，则获取对应的ID
		if (tableCodeConfigMap.containsKey(tableName)) {
			retId = tableCodeConfigMap.get(tableName).getTableId();
		}
		
		return retId;
	}	
	
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
	@Override
	public Long getTableRecId(String tableName) {
		int tableId = getTableId(tableName);
		Long recId = getGlobalIdDao.getTableRecId(tableId);
		return recId;
	}
	
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
	@Override
	public Long getTableRecIds(String tableName,int recCount) {
		int tableId = getTableId(tableName);
		Long recId = getGlobalIdDao.getTableRecIds(tableId,recCount);
		return recId;		
	}
}
