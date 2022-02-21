package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.utils.LogUtil;
import com.abc.example.dao.SysParameterDao;
import com.abc.example.entity.SysParameter;
import com.abc.example.service.SysParameterService;


/**
 * @className		: SysParameterServiceImpl
 * @description	: SysParameterService实现类
 * @summary		: 实现对系统参数的管理
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class SysParameterServiceImpl implements SysParameterService {
	// sys_parameters表数据访问对象
	@Autowired
	private SysParameterDao sysParameterDao;
		
	// 管理全部的SysParameter表记录
	private Map<String,Map<String,SysParameter>> sysParameterMap = new HashMap<String,Map<String,SysParameter>>();
	
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载及重新加载数据库中数据 
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
		try
		{
			// 查询sys_parameters表，获取全部数据
			List<SysParameter> itemList = sysParameterDao.selectAllItems();
			
			synchronized(sysParameterMap) {
				// 先清空map，便于刷新调用
				sysParameterMap.clear();
				
				// 将查询结果放入map对象中，按每个类别组织
				for(SysParameter item : itemList) {
					String classKey = item.getClassKey();
					String itemKey = item.getItemKey();
					Map<String,SysParameter> subClassMap = null;
					if (sysParameterMap.containsKey(classKey)) {
						// 如果存在该类别，则获取对象
						subClassMap = sysParameterMap.get(classKey);
					}else {
						// 如果不存在该类别，则创建
						subClassMap = new HashMap<String,SysParameter>();
						// 加入map中
						sysParameterMap.put(classKey, subClassMap);
					}
					subClassMap.put(itemKey,item);
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
	 * @methodName		: getParameterClass
	 * @description	: 获取指定classKey的参数类别的子项列表
	 * @param classKey	: 参数类别key
	 * @return			: 指定classKey的参数类别的子项列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<SysParameter> getParameterClass(String classKey){
		List<SysParameter> itemList = new ArrayList<SysParameter>();

		if (classKey == null) {
			return itemList;
		}
						
		// 获取classKey对应的子map，将所有子项加入列表中
		getClassList(itemList,classKey);
				
		return itemList;
	}
	
	/**
	 * 
	 * @methodName		: getParameterItemByKey
	 * @description	: 根据classKey和itemKey获取参数子项
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @return			: SysParameter对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public SysParameter getParameterItemByKey(String classKey,String itemKey) {
		if (classKey == null || itemKey == null) {
			return null;
		}
		
		SysParameter sysParameter = null;		
		sysParameter = getItem(itemKey,classKey);

		return sysParameter;
	}
	
	/**
	 * 
	 * @methodName		: getParameterItemByValue
	 * @description	: 根据classKey和itemValue获取参数子项
	 * @param classKey	: 参数类别key	
	 * @param itemValue	: 子项值
	 * @return			: SysParameter对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public SysParameter getParameterItemByValue(String classKey,String itemValue) {
		if (classKey == null || itemValue == null) {
			return null;
		}
		
		SysParameter sysParameter = null;
					
		if (sysParameterMap.containsKey(classKey)) {
			// 如果classKey存在
			Map<String,SysParameter> subClassMap = sysParameterMap.get(classKey);
			// 遍历
			for (Map.Entry<String,SysParameter> item : subClassMap.entrySet()) {
				if(item.getValue().getItemValue().equals(itemValue)) {
					// 如果匹配值
					sysParameter = item.getValue();
					break;
				}
			}
		}
		
		return sysParameter;
		
	}	
	
	
	/**
	 * 
	 * @methodName		: getItem
	 * @description	: 根据对象key和组key，获取对象，子类需重载
	 * @param <T>		: T类型对象
	 * @param itemKey	: 对象key值
	 * @param classKey	: 对象组key值
	 * @return			: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */	
	private SysParameter getItem(Object itemKey,Object classKey) {
		SysParameter item = null;
		if (sysParameterMap.containsKey((String)classKey)) {
			Map<String,SysParameter> subClassMap = sysParameterMap.get((String)classKey);
			if (subClassMap.containsKey((String)itemKey)) {
				item = subClassMap.get((String)itemKey);
			}
		}
		return item;
	}	
	
	/**
	 * 
	 * @methodName		: getClassList
	 * @description	: 获取指定类别key的子项列表
	 * @param itemList	: 子项列表
	 * @param classKey	: 参数类别key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void getClassList(List<SysParameter> itemList,String classKey){
		if (sysParameterMap.containsKey(classKey)) {
			Map<String,SysParameter> subClassMap = sysParameterMap.get(classKey);
			itemList.addAll(subClassMap.values());
		}	
	}
	
}
