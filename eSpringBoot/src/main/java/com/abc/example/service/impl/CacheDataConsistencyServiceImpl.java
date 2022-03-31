package com.abc.example.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.utils.ObjListUtil;
import com.abc.example.dao.UserDao;
import com.abc.example.entity.Function;
import com.abc.example.entity.Orgnization;
import com.abc.example.entity.SysParameter;
import com.abc.example.entity.User;
import com.abc.example.entity.UserRole;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.enumeration.ENotifyMsgType;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.ChangeNotifyService;
import com.abc.example.service.FunctionTreeService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.OrgnizationService;
import com.abc.example.service.RoleFuncRightsService;
import com.abc.example.service.SysParameterService;

/**
 * @className		: CacheDataConsistencyServiceImpl
 * @description	: CacheDataConsistencyService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/04/16	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class CacheDataConsistencyServiceImpl implements CacheDataConsistencyService {
	// 公共配置数据服务
	@Autowired
	private GlobalConfigService gcs;	
	
	@Autowired
	private UserDao userDao;
	

	/**
	 * 
	 * @methodName		: cacheObjectChanged
	 * @description	: 普通缓存对象改变
	 * @param eCacheObjectType	: 缓存对象类型
	 * @param oldObj	: 修改之前的对象
	 * @param newObj	: 修改之后的对象
	 * @param eDataOperationType:操作类型
	 * @summary		: 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void cacheObjectChanged(ECacheObjectType eCacheObjectType,Object oldObj,Object newObj,
			EDataOperationType eDataOperationType) {
		switch(eCacheObjectType) {
		case cotUserE:
		{
			switch(eDataOperationType) {
			case dotAddE:
				break;
			case dotUpdateE:
				updateUser((User)oldObj,(User)newObj);
				break;
			case dotRemoveE:
				disableUser((User)oldObj);
				break;
			default:
				break;
			}
		}
			break;
		case cotFunctionE:
		{
			switch(eDataOperationType) {
			case dotAddE:
				refreshFunction((Function)newObj);
				break;
			case dotAddItemsE:
				refreshFunctions();
				break;
			case dotUpdateE:
				refreshFunction((Function)newObj);
				break;
			case dotRemoveE:
				removeFunction((Function)oldObj);
				break;
			case dotRemoveItemsE:
				refreshFunctions();
				break;
			default:
				break;			
			}
		}
			break;
		case cotRoleFunctionE:
		{
			switch(eDataOperationType) {
			case dotLoadE:
				updateFuncTree();
				break;
			default:
				break;
			}
		}
			break;
		case cotUserRoleE:
		{
			switch(eDataOperationType) {
			case dotAddE:
				updateUserRole((UserRole)newObj);
				break;
			case dotUpdateE:
				updateUserRole((UserRole)oldObj);
				break;
			case dotRemoveE:
				updateUserRole((UserRole)oldObj);
				break;
			default:
				break;
			}			
		}
			break;
		case cotUserDRE:
		{
			switch(eDataOperationType) {
			case dotUpdateE:
				updateUserDr((Long)oldObj);
				break;
			default:
				break;
			}			
		}
			break;		
		case cotSysParameterE:
		{
			switch(eDataOperationType) {
			case dotAddE:
				refreshSysParameter((SysParameter)newObj);
				break;
			case dotAddItemsE:
				refreshSysParameters((List<SysParameter>)newObj);
				break;
			case dotUpdateE:
				refreshSysParameter((SysParameter)oldObj);
				break;
			case dotUpdateItemsE:
				refreshSysParameters((List<SysParameter>)oldObj);
				break;
			case dotRemoveE:
				removeSysParameter((SysParameter)oldObj);
				break;
			case dotRemoveItemsE:
				removeSysParameters((List<SysParameter>)oldObj);
				break;
			default:
				break;
			}			
		}
			break;	
		case cotOrgnizationE:
		{
			switch(eDataOperationType) {
			case dotAddE:
				updateOrgnization((Orgnization)newObj);
				break;
			case dotUpdateE:
				updateOrgnization((Orgnization)oldObj);
				break;
			case dotRemoveE:
				removeOrgnization((Orgnization)oldObj);
				break;
			default:
				break;
			}				
		}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @methodName		: updateUser
	 * @description	: 修改用户信息
	 * @param oldItem	: 用户对象原值
	 * @param newItem	: 用户对象新值
	 * @summary		: 修改用户，可能的影响如下：
	 * 	1、用户的组织ID改变，导致账号缓存的数据权限变化
	 *  2、用户的用户类型改变，导致账号缓存的值变化，影响操作权限
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void updateUser(User oldItem,User newItem) {
		// 获取用户ID，新旧对象都不会变
		Long userId = oldItem.getUserId();
		ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");
		if (oldItem.getOrgId() != newItem.getOrgId()) {
			// 调整用户的组织ID，导致账号缓存和数据权限变更
			cns.setChangeNotifyInfo(userId, ENotifyMsgType.nmtUserDrChangeE.getCode());										
		}
		if (oldItem.getUserType() != newItem.getUserType()) {
			// 调整用户的用户类型，导致账号缓存和可能的数据权限变更
			cns.setChangeNotifyInfo(userId, ENotifyMsgType.nmtUserUtChangeE.getCode());	
		}
	}
	
	/**
	 * 
	 * @methodName		: disableUser
	 * @description	: 禁用用户
	 * @param oldItem	: 用户对象
	 * @summary		: 禁用用户，影响如下：
	 * 	1、已登录的用户，由于账号缓存被清空，不能访问
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void disableUser(User oldItem) {
		Long userId = oldItem.getUserId();
		
		ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");
		// 禁用用户
		cns.setChangeNotifyInfo(userId, ENotifyMsgType.nmtUserDisabledE.getCode());	
				
	}	
	
	/**
	 * 
	 * @methodName		: refreshFunction
	 * @description	: 刷新功能项
	 * @param item		: Function对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	private void refreshFunction(Function item) {
		FunctionTreeService fts = (FunctionTreeService)gcs.getDataServiceObject("FunctionTreeService");
		fts.getItemByKey(item.getFuncId(), true);
		
		updateFuncTree();
	}
	
	/**
	 * 
	 * @methodName		: refreshFunctions
	 * @description	: 刷新功能项列表
	 * @param item		: Function对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	private void refreshFunctions() {
		FunctionTreeService fts = (FunctionTreeService)gcs.getDataServiceObject("FunctionTreeService");
		fts.loadData();
		
		updateFuncTree();
	}	
	
	/**
	 * 
	 * @methodName		: removeFunction
	 * @description	: 移除功能项
	 * @param item		: Function对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/18	1.0.0		sheng.zheng		初版
	 *
	 */
	private void removeFunction(Function item) {
		FunctionTreeService fts = (FunctionTreeService)gcs.getDataServiceObject("FunctionTreeService");
		fts.removeItemByKey(item.getFuncId());
		
		updateFuncTree();
	}	
	
	
	/**
	 * 
	 * @methodName		: updateUserRole
	 * @description	: 修改操作权限
	 * @param item		: UserRole对象
	 * @summary		: 修改操作权限，影响如下：
	 * 	1、已登录的用户，账号缓存需要更新
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void updateUserRole(UserRole item) {		
		Long userId = item.getUserId();
		// 发出变更通知
		ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");
		// 修改用户的角色组合值：bit0
		cns.setChangeNotifyInfo(userId, ENotifyMsgType.nmtRoleFuncChangeE.getCode());			
	}
	
	/**
	 * 
	 * @methodName		: updateUserDr
	 * @description	: 修改数据权限
	 * @param userId	: 用户ID
	 * @summary		: 修改数据权限，影响如下：
	 * 	1、已登录的用户，账号缓存需要更新
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void updateUserDr(Long userId) {
		// 发出变更通知
		ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");
		// 修改用户数据权限类型
		cns.setChangeNotifyInfo(userId, ENotifyMsgType.nmtUserDrtChangeE.getCode());
		
	}
	
	/**
	 * 
	 * @methodName		: updateFuncTree
	 * @description	: 刷新功能树
	 * @summary		: 修改功能树，影响如下：
	 * 	1、功能树改变，导致角色功能树也变化，需要重新加载
	 * 	2、所有已登录的用户，需要更新权限树，否则前端缓存的是原权限树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override	
	public void updateFuncTree() {
		// 重新加载角色功能树
		RoleFuncRightsService rfrs = (RoleFuncRightsService)gcs.getDataServiceObject("RoleFuncRightsService");
		rfrs.loadData();
		
		// 所有用户，需要更新权限树
		// 功能项变更，导致功能树、权限树的变更：bit2
		refreshAllUser(ENotifyMsgType.nmtFuncChangeE.getCode());
	}
	
	/**
	 * 
	 * @methodName		: updateRoleFuncRights
	 * @description	: 刷新角色功能关系
	 * @summary		: 修改角色功能关系，影响如下：
	 * 	1、所有已登录的用户，需要更新权限树，否则前端缓存的是原权限树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void updateRoleFuncRights() {
		// 所有用户，需要更新权限树
		// 修改角色的功能项，从而导致权限变更：bit1
		refreshAllUser(ENotifyMsgType.nmtRoleFuncChangeE.getCode());
	}
	
	/**
	 * 
	 * @methodName		: refreshAllUser
	 * @description	: 刷新所有用户的账号缓存
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void refreshAllUser(Integer changeNotifyInfo) {
		// 所有用户，需要更新权限树
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deleteFlag", EDeleteFlag.dfValidE.getCode());
		List<User> userList = userDao.selectItemsByCondition(params);
		List<Long> userIdList = ObjListUtil.getSubFieldList(userList, "userId");
		// 发出变更通知
		ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");		
		for(Long userId : userIdList) {			
			cns.setChangeNotifyInfo(userId, changeNotifyInfo);				
		}				
	}
	
	/**
	 * 
	 * @methodName		: refreshSysParameter
	 * @description	: 刷新系统参数
	 * @param item		: SysParameter对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void refreshSysParameter(SysParameter item) {
		String classKey = item.getClassKey();
		String itemKey = item.getItemKey();
		SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		sps.getItemByKey(classKey, itemKey, true);
	}
	
	/**
	 * 
	 * @methodName		: refreshSysParameters
	 * @description	: 刷新系统参数列表
	 * @param itemList	: SysParameter对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void refreshSysParameters(List<SysParameter> itemList) {
		SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		Map<String,Integer> classKeyMap = new HashMap<String,Integer>();
		for (SysParameter item : itemList) {
			String classKey = item.getClassKey();
			if (classKeyMap.containsKey(classKey)) {
				continue;
			}
			// 添加classKey
			classKeyMap.put(classKey, 1);
			// 刷新classKey
			sps.getItemsByClass(classKey, true);
		}
	}
	
	/**
	 * 
	 * @methodName		: removeSysParameter
	 * @description	: 移除系统参数
	 * @param item		: SysParameter对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void removeSysParameter(SysParameter item) {
		String classKey = item.getClassKey();
		String itemKey = item.getItemKey();
		SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		sps.removeItemByKey(classKey, itemKey);
	}	
	
	/**
	 * 
	 * @methodName		: removeSysParameters
	 * @description	: 移除系统参数类别
	 * @param itemList	: SysParameter对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void removeSysParameters(List<SysParameter> itemList) {
		if(itemList.size() == 0) {
			return;
		}
		// 获取第一个对象
		SysParameter item = itemList.get(0);
		String classKey = item.getClassKey();
		SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		sps.removeItemsByClass(classKey);
	}	
	
	/**
	 * 
	 * @methodName		: updateOrgnization
	 * @description	: 刷新组织
	 * @param item		: Orgnization对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void updateOrgnization(Orgnization item) {
		OrgnizationService os = (OrgnizationService)gcs.getDataServiceObject("OrgnizationService");
		Integer orgId = item.getOrgId();
		os.getItemByKey(orgId, true);
	}
	
	/**
	 * 
	 * @methodName		: updateOrgnization
	 * @description	: 移除组织
	 * @param item		: Orgnization对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/04/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void removeOrgnization(Orgnization item) {
		OrgnizationService os = (OrgnizationService)gcs.getDataServiceObject("OrgnizationService");
		Integer orgId = item.getOrgId();
		os.removeItem(orgId);
	}	
	
}
