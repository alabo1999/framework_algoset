package com.abc.example.service.impl;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.dao.SysParameterDao;
import com.abc.example.entity.SysParameter;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.SysParameterManService;

/**
 * @className	: SysParameterManServiceImpl
 * @description	: 系统参数对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/02/02	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class SysParameterManServiceImpl extends BaseService implements SysParameterManService{
	// 系统参数对象数据访问类对象
	@Autowired
	private SysParameterDao sysParameterDao;
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;	
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个系统参数对象
	 * @param request	: request对象
	 * @param item		: 系统参数对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItem(HttpServletRequest request, SysParameter item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		item.setOperatorName(operatorName);
		
		// 插入数据
		try {
			sysParameterDao.insertItem(item);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, null, item, EDataOperationType.dotAddE);
		
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批系统参数对象
	 * @param request	: request对象
	 * @param itemList	: 系统参数对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItems(HttpServletRequest request, List<SysParameter> itemList) {
		// 输入参数校验
		checkValidForParams(request, "addItems", itemList);
		
		if (itemList.size() == 0) {
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		for(int i = 0; i < itemList.size(); i++) {
			SysParameter aObj = itemList.get(i);
			aObj.setOperatorName(operatorName);
		}
		
		// 插入数据
		try {
			sysParameterDao.insertItems(itemList);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, null, itemList, EDataOperationType.dotAddItemsE);
		
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void editItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "editItem", params);
		
		// 获取对象
		Integer classId = (Integer)params.get("classId");
		Integer itemId = (Integer)params.get("itemId");
		SysParameter oldItem = sysParameterDao.selectItemByKey(classId,itemId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			sysParameterDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, oldItem, null, EDataOperationType.dotUpdateE);
		
	}
	
	/**
	 * @methodName		: updateItems
	 * @description		: 根据条件修改一个或多个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，其它字段均可选，条件字段如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void updateItems(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "updateItems", params);
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		List<SysParameter> oldItemList = null;
		try {
			// 查询修改前的数据
			oldItemList = sysParameterDao.selectItems(params);
			
			sysParameterDao.updateItems(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, oldItemList, null, EDataOperationType.dotUpdateItemsE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 系统参数对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItem", params);
		
		// 获取对象
		Integer classId = (Integer)params.get("classId");
		Integer itemId = (Integer)params.get("itemId");
		SysParameter oldItem = sysParameterDao.selectItemByKey(classId,itemId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 检查删除标记
		Integer deleteFlag = (Integer)params.get("deleteFlag");
		if (deleteFlag == null) {
			// 默认为停用
			deleteFlag = EDeleteFlag.dfDeletedE.getCode();
			params.put("deleteFlag", deleteFlag);
		}
		if (oldItem.getDeleteFlag() == deleteFlag.byteValue()) {
			// 不变
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			sysParameterDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		 if (deleteFlag == EDeleteFlag.dfDeletedE.getCode()){
		 	cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, oldItem, null, EDataOperationType.dotRemoveE);
		 }else{
		 	oldItem.setDeleteFlag(deleteFlag.byteValue());
		 	cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, null, oldItem, EDataOperationType.dotAddE);
		 }
		
	}
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个系统参数对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，可选
	 * 		"classKey"	: "",	// 参数类别key，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/04	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItems(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItems", params);
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 移除数据
		List<SysParameter> oldItemList = null;
		try {
			// 查询修改前的数据
			oldItemList = sysParameterDao.selectItems(params);
			
			sysParameterDao.deleteItems(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.DELETE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotSysParameterE, oldItemList, null, EDataOperationType.dotRemoveItemsE);
		
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询系统参数对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"classId"		: 0,	// 参数类别ID，可选
	 * 		"classKey"		: "",	// 参数类别key，可选
	 * 		"className"		: "",	// 参数类别名称，like，可选
	 * 		"itemKey"		: "",	// 子项key，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 系统参数对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<SysParameter> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		PageInfo<SysParameter> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<SysParameter> sysParameterList = sysParameterDao.selectItemsByCondition(params);
			// 分页对象
			pageInfo = new PageInfo<SysParameter>(sysParameterList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个系统参数对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"classId"	: 0,	// 参数类别ID，必选
	 * 		"itemId"	: 0,	// 参数类别下子项ID，必选
	 * 	}
	 * @return			: 系统参数对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/02	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public SysParameter getItem(HttpServletRequest request,
			 Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Integer classId = (Integer)params.get("classId");
		Integer itemId = (Integer)params.get("itemId");
		
		// 查询数据
		try {
			// 查询记录
			SysParameter item = sysParameterDao.selectItemByKey(classId,itemId);
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询系统参数对象列表
	 * @param request	: request对象
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
	@Override
	public List<SysParameter> getItems(HttpServletRequest request,
			 Map<String, Object> params) {
		// 查询数据
		try {
			// 查询记录
			List<SysParameter> itemList = sysParameterDao.selectItems(params);
			return itemList;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName			: checkValidForParams
	 * @description			: 输入参数校验
	 * @param request		: request对象
	 * @param methodName	: 方法名称
	 * @param params		: 输入参数
	 * @history				:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/02/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// SysParameter item = (SysParameter)params;
			
			// 检查项: TBD
		}
		break;
		case "addItems":
		{
			// List<SysParameter> itemList = (List<SysParameter>)params;
			
			// 检查项: TBD
		}
		break;
		case "editItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: classId,itemId
			checkKeyFields(map,new String[] {"classId", "itemId"});
		}
		break;
		case "updateItems":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: classId,classKey
			checkOpKeyFields(map,new String[] {"classId", "classKey"});
		}
		break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: classId,itemId
			checkKeyFields(map,new String[] {"classId", "itemId"});
		}
		break;
		case "deleteItems":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			if (map.size() == 0) {
				// 至少有一个参数
				throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);				
			}
			
			// 检查项: classId,classKey
			checkOpKeyFields(map,new String[] {"classId", "classKey"});
		}
		break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: classId,itemId
			checkKeyFields(map,new String[] {"classId", "itemId"});
		}
		break;
		default:
			break;
		}
	}
}
