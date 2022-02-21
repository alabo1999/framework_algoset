package com.abc.example.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.dao.FunctionDao;
import com.abc.example.entity.Function;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
//import com.abc.example.service.DataRightsService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.TableCodeConfigService;
import com.abc.example.service.FunctionManService;

/**
 * @className	: FunctionManServiceImpl
 * @description	: 功能对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class FunctionManServiceImpl extends BaseService implements FunctionManService{
	// 功能对象数据访问类对象
	@Autowired
	private FunctionDao functionDao;
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;
	
	// 数据权限服务类对象
//	@Autowired
//	private DataRightsService drs;
	
	// 公共配置数据服务类对象
	@Autowired
	private GlobalConfigService gcs;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个功能对象
	 * @param request	: request对象
	 * @param item		: 功能对象
	 * @return			: 新增的功能对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Map<String,Object> addItem(HttpServletRequest request, Function item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);
		
		// 获取全局记录ID
		TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
		Long globalRecId = tccs.getTableRecId("exa_functions");
		Integer funcId = globalRecId.intValue();
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		item.setFuncId(funcId);
		item.setOperatorName(operatorName);
		
		// 插入数据
		try {
			functionDao.insertItem(item);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotFunctionE, null, item, EDataOperationType.dotAddE);
		
		// 构造返回值
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("funcId", funcId);
		
		return map;
	}
	
	/**
	 * @methodName		: addItems
	 * @description		: 新增一批功能对象
	 * @param request	: request对象
	 * @param itemList	: 功能对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addItems(HttpServletRequest request, List<Function> itemList) {
		// 输入参数校验
		checkValidForParams(request, "addItems", itemList);
		
		if (itemList.size() == 0) {
			return;
		}
		
		// 获取全局记录ID
		TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
		Long globalRecId = tccs.getTableRecIds("exa_functions", itemList.size());
		Integer funcId = globalRecId.intValue();
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		for(int i = 0; i < itemList.size(); i++) {
			Function aObj = itemList.get(i);
			aObj.setFuncId(funcId + i);
			aObj.setOperatorName(operatorName);
		}
		
		// 插入数据
		try {
			functionDao.insertItems(itemList);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		// cdcs.cacheObjectChanged(ECacheObjectType.cotFunctionE, null, itemList, EDataOperationType.dotAddE);
		
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个功能对象
	 * @param request	: request对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void editItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "editItem", params);
		
		// 获取对象
		Integer funcId = (Integer)params.get("funcId");
		Function oldItem = functionDao.selectItemByKey(funcId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			functionDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		// Function newItem = functionDao.selectItemByKey(funcId);
		// cdcs.cacheObjectChanged(ECacheObjectType.cotFunctionE, oldItem, newItem, EDataOperationType.dotUpdateE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个功能对象
	 * @param request	: request对象
	 * @param params	: 功能对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItem", params);
		
		// 获取对象
		Integer funcId = (Integer)params.get("funcId");
		Function oldItem = functionDao.selectItemByKey(funcId);
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
			functionDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		// if (deleteFlag == EDeleteFlag.dfDeletedE.getCode()){
		// 	cdcs.cacheObjectChanged(ECacheObjectType.cotFunctionE, oldItem, null, EDataOperationType.dotRemoveE);
		// }else{
		// 	oldItem.setDeleteFlag(deleteFlag.byteValue());
		// 	cdcs.cacheObjectChanged(ECacheObjectType.cotFunctionE, null, oldItem, EDataOperationType.dotAddE);
		// }
		
	}
	
	/**
	 * @methodName		: deleteItems
	 * @description		: 根据条件删除多个功能对象
	 * @param request	: request对象
	 * @param params	: 相关条件字段字典，形式如下：
	 * 	{
	 * 		"parentId"	: 0,	// 父功能ID，可选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
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
		
		// 修改数据
		try {
			// 查询修改前的数据
			// List<Function> oldItemList = functionDao.selectItems(params);
			
			functionDao.deleteItems(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.DELETE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		// cdcs.cacheObjectChanged(ECacheObjectType.cotFunctionE, oldItemList, null, EDataOperationType.dotRemoveE);
		
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询功能对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 		"funcName"		: "",	// 功能名称，like，可选
	 * 		"url"			: "",	// 访问接口url，like，可选
	 * 		"domKey"		: "",	// dom对象的ID，like，可选
	 * 		"parentId"		: 0,	// 父功能ID，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 功能对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<Function> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		PageInfo<Function> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<Function> functionList = functionDao.selectItemsByCondition(params);
			// 分页对象
			pageInfo = new PageInfo<Function>(functionList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个功能对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"funcId"	: 0,	// 功能ID，必选
	 * 	}
	 * @return			: 功能对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Function getItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Integer funcId = (Integer)params.get("funcId");
		// 查询数据
		try {
			// 查询记录
			Function item = functionDao.selectItemByKey(funcId);
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询功能对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: ,		// limit记录偏移量，可选
	 * 		"rows"			: ,		// limit最大记录条数，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 功能对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<Function> getItems(HttpServletRequest request,
			 Map<String, Object> params) {
		// 查询数据
		try {
			// 查询记录
			List<Function> itemList = functionDao.selectItems(params);
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
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// Function item = (Function)params;
			
			// 检查项: TBD
		}
		break;
		case "addItems":
		{
			// List<Function> itemList = (List<Function>)params;
			
			// 检查项: TBD
		}
		break;
		case "editItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: funcId
			checkKeyFields(map,new String[] {"funcId"});
		}
		break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: funcId
			checkKeyFields(map,new String[] {"funcId"});
		}
		break;
		case "deleteItems":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: funcId
			checkKeyFields(map,new String[] {"funcId"});
		}
		break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: funcId
			checkKeyFields(map,new String[] {"funcId"});
		}
		break;
		default:
			break;
		}
	}
}
