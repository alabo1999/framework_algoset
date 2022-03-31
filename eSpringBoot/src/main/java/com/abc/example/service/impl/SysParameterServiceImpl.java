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
import com.abc.example.service.BaseCommonService;
import com.abc.example.service.SysParameterService;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Service
public class SysParameterServiceImpl extends BaseCommonService implements SysParameterService {
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
	 * @methodName		: getItemsByClass
	 * @description	: 获取指定classKey的参数类别的子项列表
	 * @param classKey	: 参数类别key
	 * @param refresh	: true表示查询数据库强制刷新,false为从内存中取值
	 * @return			: 指定classKey的参数类别的子项列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<SysParameter> getItemsByClass(String classKey,boolean refresh){
		List<SysParameter> itemList = new ArrayList<SysParameter>();
		if (refresh == true) {
			// 如果需要刷新
			loadGroupItems(classKey);
		}
		//获取classKey对应的子map，将所有子项加入列表中
		getClassList(itemList,classKey);
		if (itemList.size() == 0 && refresh == false) {
			// 如果指定classKey不存在
			if(procNoClassData(classKey)) {
				getClassList(itemList,classKey);
			}				
		}
		
		return itemList;
	}
	
	/**
	 * 
	 * @methodName		: getItemByKey
	 * @description	: 根据classKey和itemKey获取参数子项
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @param refresh	: true表示查询数据库强制刷新,false为从内存中取值
	 * @return			: SysParameter对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public SysParameter getItemByKey(String classKey,String itemKey,boolean refresh) {
		if (classKey == null || itemKey == null) {
			return null;
		}
		
		SysParameter item = null;		
		if (refresh == true) {
			procNoItemData(itemKey,classKey);
		}		
		item = getItem(itemKey,classKey);
		return item;
	}
	
	/**
	 * 
	 * @methodName		: getItemByValue
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
	public SysParameter getItemByValue(String classKey,String itemValue) {
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
	 * @methodName		: removeItemByKey
	 * @description	: 根据classKey和itemKey移除参数子项
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/17	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void removeItemByKey(String classKey,String itemKey) {
		Map<String,SysParameter> subClassMap = null;
		synchronized(this) {
			if (sysParameterMap.containsKey(classKey)) {
				subClassMap = sysParameterMap.get(classKey);
				if (subClassMap.containsKey(itemKey)) {
					subClassMap.remove(itemKey);
				}
			}			
		}
	}
	
	/**
	 * 
	 * @methodName		: removeItemsByClass
	 * @description	: 根据classKey移除参数类别
	 * @param classKey	: 参数类别key
	 * @param itemKey	: 子项key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/17	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void removeItemsByClass(String classKey) {
		synchronized(this) {
			if (sysParameterMap.containsKey(classKey)) {
				sysParameterMap.remove(classKey);
			}			
		}
	}
	
	/**
	 * 
	 * @methodName		: hasGroupItems
	 * @description	: 是否存在指定key的对象组，子类需重载
	 * @param classKey	: 对象组key
	 * @return			: 存在返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected boolean hasGroupItems(Object classKey) {
		return sysParameterMap.containsKey((String)classKey);
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
	@SuppressWarnings("unchecked")
	@Override
	protected SysParameter getItem(Object itemKey,Object classKey) {
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
	 * @methodName		: fetchItem
	 * @description	: 从数据库中查询一个对象
	 * @param <T>		: 泛型方法
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @return			: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected SysParameter fetchItem(Object itemKey,Object classKey) {
		return sysParameterDao.selectItemByKey2((String)classKey, (String)itemKey);
	}	
	
	/**
	 * 
	 * @methodName		: fetchItems
	 * @description	: 从数据库中查询对象列表
	 * @param <T>		: 泛型方法
	 * @param classKey	: 对象组key
	 * @return			: 查询到的对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<SysParameter> fetchItems(Object classKey) {
		List<SysParameter> itemList = sysParameterDao.selectItemsByClassKey((String)classKey);		
		return itemList;

	}	
	
	/**
	 * 
	 * @methodName		: setItem
	 * @description	: 将一个对象加入管理集合中
	 * @param <T>		: 泛型方法
	 * @param itemKey	: 对象key
	 * @param classKey	: 对象组key
	 * @param item		: 泛型T类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected <T> void setItem(Object itemKey,Object classKey,T item) {
		SysParameter newItem = (SysParameter)item;
		if (newItem.getDeleteFlag() != 0) {
			return;
		}
		
		synchronized(this) {
			// 更新分类数据字典		
			Map<String,SysParameter> subClassMap = null;
			if (sysParameterMap.containsKey((String)classKey)) {
				subClassMap = sysParameterMap.get((String)classKey);
			}else {
				subClassMap = new HashMap<String,SysParameter>();
				sysParameterMap.put((String)classKey, subClassMap);
			}
			
			subClassMap.put(newItem.getItemKey(),newItem);
		}		
	}	
	
	/**
	 * 
	 * @methodName		: setItems
	 * @description	: 将对象列表加入管理集合中
	 * @param <T>		: 泛型方法
	 * @param classKey	: 对象组key
	 * @param items		: 对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected <T> void setItems(Object classKey,List<T> items) {
		// 更新分类数据字典
		Map<String,SysParameter> subClassMap = null;
		if (sysParameterMap.containsKey((String)classKey)) {
			// 如果该类别的key存在
			subClassMap = sysParameterMap.get((String)classKey);
		}else {
			// 如果该类别的key不存在
			subClassMap = new HashMap<String,SysParameter>();
			//加入map中
			sysParameterMap.put((String)classKey, subClassMap);			
		}
		synchronized(sysParameterMap) {
			// 清空分类字典
			subClassMap.clear();
			
			// 重新设置
			for (T item : items) {	
				SysParameter aItem = (SysParameter)item;
				if (aItem.getDeleteFlag() == 0) {
					String itemKey = aItem.getItemKey();
					subClassMap.put(itemKey,aItem);					
				}
			}				
		}		
	}	
	
	/**
	 * 
	 * @methodName		: logInfo
	 * @description	: 写日志
	 * @param methodName: 方法名
	 * @param param		: 参数值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	protected void logInfo(String methodName,String param) {
		switch(methodName) {
		case "loadItem":
			log.info("Failed to query item data with itemKey = " + param);
			break;
		case "loadGroupItems":
			log.info("Failed to query class data with classKey = " + param);
			break;
		default:
			break;
		}
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
