package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.constants.Constants;
import com.abc.example.common.utils.ObjListUtil;
import com.abc.example.dao.DrFieldDao;
import com.abc.example.dao.UserCustomDrDao;
import com.abc.example.dao.UserDrDao;
import com.abc.example.entity.DrField;
import com.abc.example.entity.UserCustomDr;
import com.abc.example.entity.UserDr;
import com.abc.example.enumeration.EDataRightsType;
import com.abc.example.enumeration.EUserType;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.AccountCacheService;
import com.abc.example.service.DataRightsService;

/**
 * @className		: DataRightsServiceImpl
 * @description	: DataRightsService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/03/11	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class DataRightsServiceImpl implements DataRightsService {
	// 数据权限相关字段对象访问类
	@Autowired
	private DrFieldDao drFieldDao;	
	
	// 用户数据权限数据访问类
	@Autowired
	private UserDrDao userDrDao;	
	
	// 自定义数据权限数据访问类
	@Autowired
	private UserCustomDrDao userCustomDrDao;		
	
	// 账户缓存服务
	@Autowired
	private AccountCacheService accountCacheService;	
	
	// 所有数据权限相关字段,key为fieldId,value为DrField
	private Map<Integer,DrField> drFieldIdMap = new HashMap<Integer,DrField>();
	private Map<String,DrField> drFieldnameMap = new HashMap<String,DrField>();
	
	// 默认的用户数据权限
	private List<UserDr> defaultUserDrList = new ArrayList<UserDr>();
	
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据库中数据 
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/10	1.0.0		sheng.zheng		初版
	 *
	 */		
	@Override
	public boolean loadData() {
		// 加载所有数据权限相关字段列表
		List<DrField> itemList = drFieldDao.selectAllItems();
		UserDr userDr = null;
		
		synchronized(this) {
			// 先清空
			drFieldIdMap.clear();
			drFieldnameMap.clear();
			defaultUserDrList.clear();
			
			// 再加载
			for (DrField item : itemList) {
				drFieldIdMap.put(item.getFieldId(), item);
				drFieldnameMap.put(item.getPropName(), item);
				
				// 设置默认数据权限
				userDr = new UserDr();
				userDr.setFieldId(item.getFieldId());
				userDr.setDrType(Byte.valueOf((byte)EDataRightsType.drtDefaultE.getCode()));
				defaultUserDrList.add(userDr);
			}			
		}
			
		return true;
	}	
	
	/**
	 * 
	 * @methodName		: checkDataRights
	 * @description	: 检查当前用户是否对给定对象有数据权限
	 * @param request	: request对象，可从中获取当前用户的缓存信息
	 * @param params	: 权限字段与值的字典
	 * @return			: 允许返回true，否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/13	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkUserDr(HttpServletRequest request,Map<String,Object> params){
		boolean bRights = false;
		
		// 获取账号缓存信息
		String accountId = accountCacheService.getId(request);
		// 获取用户类型
		Integer userType = (Integer)accountCacheService.getAttribute(accountId,Constants.USER_TYPE);
		// 获取数据权限缓存信息
		Map<String,UserDr> fieldDrMap = null;
		fieldDrMap = (Map<String,UserDr>)accountCacheService.getAttribute(accountId, Constants.DR_MAP);
		if (userType != null || fieldDrMap == null) {
			if (userType == EUserType.utSystemAdminE.getCode()) {
				// 如果为系统管理员
				bRights = true;
				return;
			}
		}else {
			// 如果属性不存在
			throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);				
		}
				
		// 获取数据权限
		Object oVal = null;
		DrField drField = null;
		UserDr userDr = null;
		bRights = true;
		List<UserCustomDr> userCustomDrList = null;
		for (String propName : params.keySet()) {
			// 获取fieldId
			if (!drFieldnameMap.containsKey(propName)) {
				// 非数据权限属性字段，skip
				continue;
			}
			drField = drFieldnameMap.get(propName);
			
			// 获取被验证对象的属性值
			oVal = params.get(propName);
			
			// 获取用户对此fieldId的权限
			userDr = fieldDrMap.get(propName);
			if (userDr.getDrType() == EDataRightsType.drtAllE.getCode()) {
				// 如果为全部，有权限
				continue;
			}
			if (userDr.getDrType() == EDataRightsType.drtDefaultE.getCode()) {
				// 如果为默认权限，进一步检查字段是否为有下级对象
				if (drField.getHasSub().intValue() == 1) {
					// 如果有下级对象，获取包含自身和下级的对象值列表
					List<Integer> drList = getDefaultDrList(propName);
					boolean bFound = drList.contains(oVal);
					if (!bFound) {
						bRights = false;
					}
				}else {
					// 无下级对象
					if(userDr.getFieldValue().equals(oVal) == false) {
						// 属性值与缓存值不同，无权限
						bRights = false;
					}
				}
			}else if (userDr.getDrType() == EDataRightsType.drtCustomE.getCode()){
				// 如果为自定义数据权限
				List<Integer> drIdList = null;
				if (userCustomDrList == null) {
					// 如果自定义列表为空，则获取
					Long userId = (Long)accountCacheService.getAttribute(accountId,Constants.USER_ID);
					userCustomDrList = getUserCustomDrList(userId,propName);
					drIdList = getUserCustomFieldList(userCustomDrList,propName);
					if (drIdList != null) {
						boolean bFound = drIdList.contains(oVal);
						if (!bFound) {
							bRights = false;
						}					
					}					
				}
			}			
		}
		if (bRights == false) {
			throw new BaseException(ExceptionCodes.ACCESS_FORBIDDEN);
		}
	}	
		
	/**
	 * 
	 * @methodName		: getQueryDrList
	 * @description	: 获取查询的drId列表，如果为全部，则返回空列表
	 * @param request	: request对象
	 * @param propName	: 数据权限属性字段名
	 * @return			: 属性值列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getQueryDrList(HttpServletRequest request,String propName){
		List<Integer> drIdList = new ArrayList<Integer>();
				
		// 获取用户类型
		Integer userType = 0;
		Map<String,UserDr> fieldDrMap = null;
		UserDr userDr = null;

		// 获取accountId
		String accountId = accountCacheService.getId(request);		

		userType = (Integer)accountCacheService.getAttribute(accountId, Constants.USER_TYPE); 
		fieldDrMap = (Map<String,UserDr>)accountCacheService.getAttribute(accountId, Constants.DR_MAP);				
		if (userType == null || fieldDrMap == null) {
			// 如果属性不存在
			throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);	
		}
		
		// 先检查是否为SA
		if (userType == EUserType.utSystemAdminE.getCode()) {
			// 如果为SA，拥有全部权限
			return drIdList;
		}
		
		if (fieldDrMap.containsKey(propName)) {
			userDr = fieldDrMap.get(propName);			
		}else {
			// 未被管理的数据权限字段，这种情况不会发生
			return drIdList;
		}
		if (userDr.getDrType() == EDataRightsType.drtAllE.getCode()) {
			// 全部权限
			return drIdList;
		}else if (userDr.getDrType() == EDataRightsType.drtDefaultE.getCode()) {
			// 默认权限
			drIdList = getDefaultDrList(propName);
		}else if (userDr.getDrType() == EDataRightsType.drtCustomE.getCode()) {
			// 自定义权限
			List<UserCustomDr> userCustomDrList = null;
			Long userId = (Long)accountCacheService.getAttribute(accountId,Constants.USER_ID);
			userCustomDrList = getUserCustomDrList(userId,propName);
			drIdList = getUserCustomFieldList(userCustomDrList,propName);
		}
			
		return drIdList;
	}
	
	/**
	 * 
	 * @methodName		: getDefaultDrList
	 * @description	: 获取默认规则权限的指定dr字段名和值时，包含自身和下级的dr字段值列表
	 * @param propName	: 属性字段名
	 * @return			: dr字段值列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	private List<Integer> getDefaultDrList(String propName){
		List<Integer> drList = null;
		// 定制化代码，查询缓存对象或数据库获取
		if (propName.equals("orgId")) {
			// 如果为orgId，从缓存中获取数据权限
		}
		return drList;
	}
	
	/**
	 * 
	 * @methodName		: getUserDrList
	 * @description	: 根据用户ID，获取用户数据权限对象列表
	 * @param userId	: 用户ID
	 * @return			: drId列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<UserDr> getUserDrList(Long userId){
		List<UserDr> userDrList = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		userDrList = userDrDao.selectItems(params);
		return userDrList;
	}
	
	/**
	 * 
	 * @methodName		: getUserDr
	 * @description	: 根据用户ID和权限字段名，获取用户数据权限对象
	 * @param userId	: 用户ID
	 * @param drPropName	: 权限字段名
	 * @return			: UserDr对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public UserDr getUserDr(Long userId,String drPropName) {
		UserDr userDr = null;
		if (drFieldnameMap.containsKey(drPropName)) {
			DrField drField = drFieldnameMap.get(drPropName);
			Integer fieldId = drField.getFieldId();
			userDr = userDrDao.selectItemByKey(userId, fieldId);
		}
		return userDr;
	}

	/**
	 * 
	 * @methodName		: getUserDr
	 * @description	: 根据用户ID和权限字段名，获取用户数据权限对象
	 * @param request	: request对象
	 * @param drPropName	: 权限字段名
	 * @return			: UserDr对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserDr getUserDr(HttpServletRequest request,String drPropName) {
		UserDr item = null;
		// 获取accountId
		String accountId = accountCacheService.getId(request);	
		Map<String,UserDr> fieldDrMap = null;
		fieldDrMap = (Map<String,UserDr>)accountCacheService.getAttribute(accountId, Constants.DR_MAP);
		if (fieldDrMap != null) {
			if (fieldDrMap.containsKey(drPropName)) {
				item = fieldDrMap.get(drPropName);
			}
		}
		return item;
	}
	
	/**
	 * 
	 * @methodName		: setUserDrs
	 * @description	: 设置用户数据权限到账户缓存中
	 * @param request	: request对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setUserDrs(HttpServletRequest request) {
		// 获取accountId
		String accountId = accountCacheService.getId(request);	
		Long userId = 0L;
		userId = (Long)accountCacheService.getAttribute(accountId, Constants.USER_ID); 
		
		// 获取用户的数据权限对象列表
		List<UserDr> userDrList = getUserDrList(userId);
		// 调用setUserDrList方法设置信息到缓存中
		setUserDrList(request,userDrList);
		
	}
	
	/**
	 * 
	 * @methodName		: setUserDrList
	 * @description	: 设置用户数据权限到账户缓存中
	 * @param request	: request对象
	 * @param userId	: 用户ID
	 * @param userDrList: 用户数据权限对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setUserDrList(HttpServletRequest request,List<UserDr> userDrList) {
		Map<String,UserDr> fieldDrMap = new HashMap<String,UserDr>();
		UserDr userDr = null;
		DrField drField = null;
		Integer fieldId = 0;
		String propName = "";
		
		// 获取accountId
		String accountId = accountCacheService.getId(request);	

		for (UserDr item : userDrList) {
			userDr = new UserDr();
			fieldId = item.getFieldId();
			drField = drFieldIdMap.get(fieldId);
			propName = drField.getPropName();
			fieldDrMap.put(propName, userDr);
			// 设置fieldId和drType
			userDr.setFieldId(fieldId);
			userDr.setDrType(item.getDrType());
			if (item.getDrType().intValue() == EDataRightsType.drtAllE.getCode()) {
				// 全部，pass
			}else if(item.getDrType().intValue() == EDataRightsType.drtDefaultE.getCode()) {
				// 默认
				// 获取用户属性值
				Object oVal = accountCacheService.getAttribute(accountId, propName); 
				if (oVal == null) {
					// 如果未设置，数据权限设置异常，要求引用的属性必须先行设置
					throw new BaseException(ExceptionCodes.DRTYPE_IS_UNSUPPORT);	
				}
				userDr.setFieldValue(oVal);				 
			}else if(item.getDrType().intValue() == EDataRightsType.drtCustomE.getCode()) {
				// 自定义，pass
			}
		}
		
		// 设置信息到账号缓存中
		accountCacheService.setAttribute(accountId, Constants.DR_MAP, fieldDrMap);
	}		
	
	/**
	 * 
	 * @methodName		: getDefaultUserDrList
	 * @description	: 获取默认的用户权限列表
	 * @return			: 默认的用户权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<UserDr> getDefaultUserDrList(){
		return defaultUserDrList;
	}	
	
	/**
	 * 
	 * @methodName		: setDefaultUserDrList
	 * @description	: 设置more用户数据权限列表
	 * @param userId	: 用户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setDefaultUserDrList(Long userId) {
		UserDr newItem = null;
				
		for (UserDr item : defaultUserDrList) {
			newItem = new UserDr();
			newItem.setFieldId(item.getFieldId());
			newItem.setUserId(userId);
			newItem.setDrType(item.getDrType());
			// 新增用户数据权限记录
			userDrDao.insertItem(newItem);
		}
	}	
	
	/**
	 * 
	 * @methodName		: getUserCustomDrList
	 * @description	: 获取用户的自定义数据权限列表
	 * @param userId	: 用户ID
	 * @param propName	: 属性字段名
	 * @return			: UserCustomDr列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<UserCustomDr> getUserCustomDrList(Long userId,String propName){
		List<UserCustomDr> drList = null;
		DrField drField = getDrField(propName);
		if (drField == null) {
			return drList;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("fieldId", drField.getFieldId());
		drList = userCustomDrDao.selectItems(params);
		return drList;
	}
	
	/**
	 * 
	 * @methodName		: getUserCustomFieldList
	 * @description	: 获取自定义数据权限某属性字段值的列表
	 * @param drList	: UserCustomDr对象列表
	 * @param propName	: 数据权限属性字段名
	 * @return			: 如果为空，则成员为一个无效值；如果非空，则为此属性的值的列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/18	1.0.0		sheng.zheng		初版
	 *
	 */
	private List<Integer> getUserCustomFieldList(List<UserCustomDr> drList,String propName){
		List<Integer> drIdList = null;
		DrField drField = null;
		if (drFieldnameMap.containsKey(propName)) {
			drField = drFieldnameMap.get(propName);			
		}else {
			return drIdList;
		}
		if (drList.size() == 0) {
			// 如果没有设置记录
			drIdList = new ArrayList<Integer>();
			// 添加一个无效值的项，表示无权限，目前只有int和bigint类型
			String invalidValue = drField.getInvalidValue();
			Integer iInvalidVal = Integer.valueOf(invalidValue);
			drIdList.add(iInvalidVal);
		}else {
			// 如果有设置记录
			drIdList = ObjListUtil.getSubFieldList(drList, "fieldValue");
		}	
		return drIdList;
	}
	
	/**
	 * 
	 * @methodName		: getDrField
	 * @description	: 根据字段ID，获取数据权限字段对象
	 * @param fieldId	: 字段ID
	 * @return			: DrField对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public DrField getDrField(Integer fieldId) {
		DrField drField = null;
		if (drFieldIdMap.containsKey(fieldId)) {
			drField = drFieldIdMap.get(fieldId);
		}
		return drField;
	}
	
	/**
	 * 
	 * @methodName		: getDrField
	 * @description	: 根据字段ID，获取数据权限字段对象
	 * @param propName	: 属性名
	 * @return			: DrField对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/03/18	1.0.0		sheng.zheng		初版
	 *
	 */
	public DrField getDrField(String propName){
		DrField drField = null;
		if (drFieldnameMap.containsKey(propName)) {
			drField = drFieldnameMap.get(propName);
		}
		return drField;		
	}
}
