package com.abc.example.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.constants.Constants;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.common.utils.Md5Util;
import com.abc.example.common.utils.ObjListUtil;
import com.abc.example.common.utils.Utility;
import com.abc.example.dao.UserCustomDrDao;
import com.abc.example.dao.UserDao;
import com.abc.example.dao.UserDrDao;
import com.abc.example.dao.UserRoleDao;
import com.abc.example.entity.DrField;
import com.abc.example.entity.Orgnization;
import com.abc.example.entity.User;
import com.abc.example.entity.UserCustomDr;
import com.abc.example.entity.UserDr;
import com.abc.example.entity.UserRole;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDataRightsType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.DataRightsService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.OrgnizationService;
import com.abc.example.service.TableCodeConfigService;
import com.abc.example.service.UserManService;

/**
 * @className	: UserManServiceImpl
 * @description	: 用户对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/06	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class UserManServiceImpl extends BaseService implements UserManService{
	// 用户对象数据访问类对象
	@Autowired
	private UserDao userDao;
	
	// 用户和角色关系对象数据访问类对象
	@Autowired
	private UserRoleDao userRoleDao;	
	
	// 用户数据权限对象数据访问类对象
	@Autowired
	private UserDrDao userDrDao;
	
	// 用户自定义数据权限对象数据访问类对象
	@Autowired
	private UserCustomDrDao userCustomDrDao;
	
	// 数据权限服务类
	@Autowired
	private DataRightsService drs;	
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;
		
	// 公共配置数据服务类对象
	@Autowired
	private GlobalConfigService gcs;
	
	/**
	 * @methodName		: addItem
	 * @description	: 新增一个用户对象
	 * @param request	: request对象
	 * @param item		: 用户对象
	 * @return			: 新增的用户对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Map<String,Object> addItem(HttpServletRequest request, User item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);		
		
		Map<String,Object> map = new HashMap<String,Object>();
		// 检查唯一性字段
		map.put("phoneNumber", item.getPhoneNumber());
		map.put("idNo", item.getIdNo());
		checkUniqueFields(map);
		
		// 获取全局记录ID
		TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
		Long globalRecId = tccs.getTableRecId("exa_users");
		Long userId = globalRecId;
		
		// 获取操作人账号
		String operatorName = getUserName(request);		
		// 获取salt
		LocalDateTime date = LocalDateTime.now();		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String salt = date.format(dateTimeFormatter);					
		//设置password
		//获取密码明文
		String originPasswd = item.getPassword();
		//加盐md5
		String encryptPasswd = Md5Util.plaintPasswdToDbPasswd(originPasswd, salt,Constants.TOKEN_KEY);		
		// 设置信息
		item.setUserId(userId);
		item.setOperatorName(operatorName);
		item.setSalt(salt);
		item.setPassword(encryptPasswd);
		
		// 插入数据
		try {
			userDao.insertItem(item);
			
			// 数据库一致性处理			
			// 增加用户的默认数据权限
			setDefaultUserDrList(request,userId);
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 构造返回值
		map.clear();
		map.put("userId", userId);
		
		return map;
	}
	
	/**
	 * @methodName		: editItem
	 * @description	: 根据key修改一个用户对象
	 * @param request	: request对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void editItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "editItem", params);
		
		// 获取对象
		Long userId = Utility.getLongValue(params, "userId");
		User oldItem = userDao.selectItemByKey(userId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
				
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 删除可能的不必要的属性
		String[] fields = new String[] {"salt","password","deleteFlag"};
		Utility.removeKeys(params, fields);
		
		// 修改数据
		try {
			userDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 检查唯一性字段
		checkUniqueFields(params);	
		
		// 缓存一致性检查
		 User newItem = userDao.selectItemByKey(userId);
		 cdcs.cacheObjectChanged(ECacheObjectType.cotUserE, oldItem, newItem, EDataOperationType.dotUpdateE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个用户对象
	 * @param request	: request对象
	 * @param params	: 用户对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItem", params);
		
		// 获取对象
		Long userId = Utility.getLongValue(params, "userId");
		User oldItem = userDao.selectItemByKey(userId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		// 检查删除标记
		Integer deleteFlag = (Integer)params.get("deleteFlag");
		if (deleteFlag == null) {
			// 默认为停用
			deleteFlag = EDeleteFlag.dfDeletedE.getCode();
			
		}
		map.put("deleteFlag", deleteFlag);
		
		if (oldItem.getDeleteFlag() == deleteFlag.byteValue()) {
			// 不变
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		map.put("operatorName", operatorName);
		
		// 修改数据
		try {
			userDao.updateItemByKey(map);
			
			// 可能的数据库一致性处理
			// 用户禁用，不必删除用户角色表和用户数据权限及用户自定义数据权限表数据 
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 检查唯一性字段
		if (deleteFlag == EDeleteFlag.dfValidE.getCode()) {
			checkUniqueFields(params);			
		}		
		
		// 缓存一致性检查
		 if (deleteFlag == EDeleteFlag.dfDeletedE.getCode()){
		 	cdcs.cacheObjectChanged(ECacheObjectType.cotUserE, oldItem, null, EDataOperationType.dotRemoveE);
		 }		
	}
	
	/**
	 * 
	 * @methodName		: resetPassword
	 * @description	: 管理员重置密码
	 * @param request	: request对象
	 * @param params	: 设置参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"password"	: "",  	//新密码，明文
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void resetPassword(HttpServletRequest request,Map<String,Object> params) {
		// 输入参数校验
		checkValidForParams(request,"resetPassword",params);	
		
		Long userId = Utility.getLongValue(params, "userId");
		
		// 获取该用户的对象
		User oldItem = userDao.selectItemByKey(userId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 新密码
		String newPassword = (String)params.get("password");
		// 获取salt
		LocalDateTime date = LocalDateTime.now();		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String salt = date.format(dateTimeFormatter);					
		//加盐md5
		String encryptPasswd = Md5Util.plaintPasswdToDbPasswd(newPassword, salt,Constants.TOKEN_KEY);		
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 构造修改对象
		Map<String,Object> dbParams = new HashMap<String,Object>();
		dbParams.put("userId", userId);
		dbParams.put("salt", salt);
		dbParams.put("password", encryptPasswd);
		dbParams.put("operatorName", operatorName);
		
		// 修改密码
		try {
			userDao.updateItemByKey(dbParams);
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);			
		}				
	}
	
	/**
	 * 
	 * @methodName		: changePassword
	 * @description	: 用户自己修改密码
	 * @param request	: request对象
	 * @param params	: 设置参数，形式如下：
	 * 	{
	 * 		"oldPassword"	: "",  	//原密码,md5值
	 * 		"newPassword"	: "",  	//新密码,md5值
	 * 		"confirmPassword"	: "",  	//确认密码,md5值
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void changePassword(HttpServletRequest request,Map<String,Object> params) {
		// 输入参数校验
		checkValidForParams(request,"changePassword",params);	
		
		Long userId = (Long)request.getSession().getAttribute("userId");
		//获取输入参数
		String oldPasswd = (String)params.get("oldPassword");
		String newPasswd = (String)params.get("newPassword");		
		String confirmPassword = (String)params.get("confirmPassword");
		if (!newPasswd.equals(confirmPassword)) {
			throw new BaseException(ExceptionCodes.PASSWORD_IS_INCONSISTENT);
		}
		
		// 获取该用户的对象
		User oldItem = userDao.selectItemByKey(userId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}		

		// 计算密码
		String encryptOldPasswd = Md5Util.apiPasswdToDBPasswd(oldPasswd,oldItem.getSalt(),Constants.TOKEN_KEY);
		if (!encryptOldPasswd.equals(oldItem.getPassword())) {
			// 如果原密码错误
			throw new BaseException(ExceptionCodes.PASSWORD_WRONG);
		}
		
		// 设置salt
		LocalDateTime date = LocalDateTime.now();		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String salt = date.format(dateTimeFormatter);					
		// 生成密码，在密文基础上加盐
		String encryptNewPasswd = Md5Util.apiPasswdToDBPasswd(newPasswd, salt,Constants.TOKEN_KEY);		
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 构造修改对象
		Map<String,Object> dbParams = new HashMap<String,Object>();
		dbParams.put("userId", userId);
		dbParams.put("salt", salt);
		dbParams.put("password", encryptNewPasswd);
		dbParams.put("operatorName", operatorName);
				
		// 修改密码
		try {
			userDao.updateItemByKey(dbParams);
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);			
		}				
	}
	
	/**
	 * @methodName		: queryItems
	 * @description	: 根据条件分页查询用户对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userType"		: 3,	// 用户类型，1-系统管理员、2-公司内部用户、3-外部用户，可选
	 * 		"sex"			: 1,	// 性别，1-无值、2-男、3-女、4-其它，可选
	 * 		"drType"		: 0,	// 数据权限类型，1-默认规则、2-自定义、3-全部，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"userName"		: "",	// 用户名，like，可选
	 * 		"phoneNumber"	: "",	// 手机号码，like，可选
	 * 		"realName"		: "",	// 真实姓名，like，可选
	 * 		"email"			: "",	// Email，like，可选
	 * 		"birthStart"	: ,		// 生日起始值，gte，可选
	 * 		"birthEnd"		: ,		// 生日终止值，lte，可选
	 * 		"orgId"			: 1,	// 组织ID，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 用户对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<User> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		
		PageInfo<User> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<User> userList = userDao.selectItemsByCondition(params);
			fillRefValue(userList);
			// 分页对象
			pageInfo = new PageInfo<User>(userList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description	: 根据key获取一个用户对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0L,	// 用户ID，必选
	 * 	}
	 * @return			: 用户对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public User getItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Long userId = Utility.getLongValue(params, "userId");
		// 查询数据
		try {
			// 查询记录
			User item = userDao.selectItemByKey(userId);
			fillRefValue(item);			
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description	: 根据条件查询用户对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"email"			: "",	// Email，可选
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-禁用，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 		"userIdList"	: [],	// 用户ID列表，可选
	 * 	}
	 * @return			: 用户对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/08	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<User> getItems(HttpServletRequest request, Map<String, Object> params) {
		
		// 查询数据
		try {
			// 查询记录
			List<User> itemList = userDao.selectItems(params);
			return itemList;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * 
	 * @methodName		: updateFuncRights
	 * @description	: 修改功能权限，即修改用户角色
	 * @param request	: request对象
	 * @param params	: 设置参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"roleIdList"	: [],  	//角色ID列表
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void updateFuncRights(HttpServletRequest request,Map<String,Object> params) {
		// 输入参数校验
		checkValidForParams(request,"updateFuncRights",params);	
				
		// 获取对象
		Long userId = Utility.getLongValue(params, "userId");
		User oldItem = userDao.selectItemByKey(userId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		if(oldItem.getDeleteFlag() == EDeleteFlag.dfDeletedE.getCode()) {
			throw new BaseException(ExceptionCodes.OBJECT_IS_REMOVED);
		}
				
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 新角色列表
		@SuppressWarnings("unchecked")
		List<Integer> roleIdList = (List<Integer>)params.get("roleIdList");	
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		for (Integer roleId : roleIdList) {
			UserRole item = new UserRole();
			item.setRoleId(roleId);
			item.setUserId(userId);
			item.setOperatorName(operatorName);
			userRoleList.add(item);
		}	
		
		try {
			// 先删除该用户的所有记录
			userRoleDao.deleteItemsByUserId(userId);
						
			// 批量插入记录
			if (userRoleList.size() > 0) {
				userRoleDao.insertItems(userRoleList);				
			}
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);			
		}	
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserRoleE, userId, null, EDataOperationType.dotUpdateE);
	}

	/**
	 * 
	 * @methodName		: getFuncRights
	 * @description	: 获取功能权限，即角色ID列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 	}	 
	 * @return			: 角色ID列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<Integer> getFuncRights(HttpServletRequest request,Map<String,Object> params){
		// 输入参数校验
		checkValidForParams(request,"getFuncRights",params);	
		
		// 获取对象
		Long userId = Utility.getLongValue(params, "userId");				
		User oldItem = userDao.selectItemByKey(userId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}		
		if(oldItem.getDeleteFlag() == EDeleteFlag.dfDeletedE.getCode()) {
			throw new BaseException(ExceptionCodes.OBJECT_IS_REMOVED);
		}
		
		List<UserRole> userRoleList = null;
		
		try {
			// 查询记录
			userRoleList = userRoleDao.selectItemsByUserId(userId);
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);			
		}		
		
		List<Integer> roleIdList = ObjListUtil.getSubFieldList(userRoleList, "roleId");
		return roleIdList;
	}		
		
	/**
	 * 
	 * @methodName		: updateUserDrs
	 * @description	: 修改用户数据权限列表
	 * @param request	: request对象
	 * @param itemList	: 用户数据权限对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void updateUserDrs(HttpServletRequest request,List<UserDr> itemList) {
		Long userId = 0L;
		// 获取操作人账号
		String operatorName = getUserName(request);
		List<String> fieldnameList = new ArrayList<String>();
		fieldnameList.add("userId");
		fieldnameList.add("fieldId");
		fieldnameList.add("drType");
		try {
			for(UserDr item : itemList) {
				userId = item.getUserId();
				item.setOperatorName(operatorName);
				Map<String,Object> map = ObjListUtil.getValuesMap(item, fieldnameList);
				userDrDao.updateItemByKey(map);
			}			
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);			
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserDRE, userId, null, EDataOperationType.dotUpdateE);
		
	}

	/**
	 * 
	 * @methodName		: getUserDrs
	 * @description	: 获取用户数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 	}	 
	 * @return			: 用户数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<UserDr> getUserDrs(HttpServletRequest request,Map<String,Object> params){
		// 输入参数校验
		checkValidForParams(request,"getUserDrs",params);	
		
		List<UserDr> itemList = null;
		
		try {
			// 查询记录
			itemList = userDrDao.selectItems(params);
			return itemList;
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);			
		}			
	}
	
	/**
	 * 
	 * @methodName		: updateUserCustomDrs
	 * @description	: 修改用户自定义数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 		"valueList"	: [], 	//字段值列表
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void updateUserCustomDrs(HttpServletRequest request,Map<String,Object> params) {
		// 输入参数校验
		checkValidForParams(request,"updateUserCustomDrs",params);	
		
		// 获取用户ID
		Long userId = Utility.getLongValue(params, "userId");	
		Integer fieldId = (Integer)params.get("fieldId");
		DrField drField = drs.getDrField(fieldId);
		// 获取用户数据权限
		UserDr userDr = drs.getUserDr(userId, drField.getPropName());
		if (userDr == null) {
			throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR);
		}
		if (userDr.getDrType().intValue() != EDataRightsType.drtCustomE.getCode()) {
			// 如果不是自定义数据权限
			throw new BaseException(ExceptionCodes.DRTYPE_IS_UNSUPPORT);
		}
		
		// 获取字段值列表
		@SuppressWarnings("unchecked")
		List<Integer> valueList = (List<Integer>)params.get("valueList");
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		List<UserCustomDr> itemList = new ArrayList<UserCustomDr>();
		UserCustomDr item = null;
		for (int i = 0; i < valueList.size(); i++) {
			Integer value = valueList.get(i);
			item = new UserCustomDr();
			item.setUserId(userId);
			item.setFieldId(fieldId);
			item.setFieldValue(value);
			item.setOperatorName(operatorName);
			itemList.add(item);			
		}
		try {
			// 先删除记录
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userId", userId);
			map.put("fieldId", fieldId);
			userCustomDrDao.deleteItems(map);
			
			// 批量插入记录
			userCustomDrDao.insertItems(itemList);
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);			
		}		
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserDRE, userId, null, EDataOperationType.dotUpdateE);		
	}
	
	/**
	 * 
	 * @methodName		: addUserCustomDr
	 * @description	: 增加用户自定义数据权限
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 		"fieldValue": 1, 	//字段值
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/07/04	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addUserCustomDr(HttpServletRequest request,Map<String,Object> params) {
		// 获取用户ID
		Long userId = Utility.getLongValue(params, "userId");	
		Integer fieldId = (Integer)params.get("fieldId");
		Integer fieldValue = (Integer)params.get("fieldValue");
		String operatorName = getUserName(request);
		
		UserCustomDr item = new UserCustomDr();
		item.setUserId(userId);
		item.setFieldId(fieldId);
		item.setFieldValue(fieldValue);
		item.setOperatorName(operatorName);
		try {
			userCustomDrDao.insertItem(item);
		}catch(Exception e) {
			if (e instanceof DuplicateKeyException) {
				// 如果已存在key
				return;
			}else {
				LogUtil.error(e);
				throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED,"新增自定义数据权限");					
			}			
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserDRE, userId, null, EDataOperationType.dotUpdateE);				
	}
	
	/**
	 * 
	 * @methodName		: getUserCustomDrs
	 * @description	: 获取用户自定义数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"userId"	: 0, 	//用户ID
	 * 		"fieldId"	: 0, 	//数据权限字段ID
	 * 	}	 
	 * @return			: 用户数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/08/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<UserCustomDr> getUserCustomDrs(HttpServletRequest request,Map<String,Object> params){
		// 输入参数校验
		checkValidForParams(request,"getUserCustomDrs",params);	
		
		List<UserCustomDr> itemList = null;
		try {
			// 查询记录
			itemList = userCustomDrDao.selectItems(params);
			return itemList;
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);			
		}		
		
	}
	
	/**
	 * 
	 * @methodName		: getCurrentUserDr
	 * @description	: 获取当前用户的数据权限列表
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"propName"	: "orgId", 	//权限属性字段名
	 * 	}	 
	 * @return			: 用户对指定属性字段的数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/02/18	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<Integer> getCurrentUserDr(HttpServletRequest request,Map<String, Object> params){
		// 输入参数校验
		checkValidForParams(request,"getCurrentUserDr",params);	
		
		String propName = (String)params.get("propName");

		List<Integer> drIdList =  drs.getQueryDrList(request, propName);
		return drIdList;
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
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// User item = (User)params;
			
			// 检查项: userName
		}
			break;
		case "editItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId"});
		}
			break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId"});
		}
			break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId"});
		}
			break;
		case "resetPassword":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId","password"});
		}
			break;
		case "changePassword":
		{
			Map<String,Object> map = (Map<String,Object>)params;			
			// 检查项: userId
			checkKeyFields(map,new String[] {"oldPassword","newPassword","confirmPassword"});			
		}
			break;
		case "updateFuncRights":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId","roleIdList"});
		}
			break;
		case "getFuncRights":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId"});
		}
			break;			
		case "getUserDrs":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId"});
		}
			break;			
		case "updateUserCustomDrs":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId","fieldId","valueList"});
		}
			break;			
		case "getUserCustomDrs":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: userId
			checkKeyFields(map,new String[] {"userId","fieldId"});
		}
			break;	
		case "getCurrentUserDr":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			// 检查项: propName
			checkKeyFields(map,new String[] {"propName"});
		}
			break;				
		case "getItemByKeyInfo":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			checkOpKeyFields(map,new String[] {"userName","phoneNumber","idNo","openId","woaOpenid"});
		}
			break;				
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @methodName		: setDefaultUserDrList
	 * @description	: 设置默认用户数据权限列表
	 * @param request	: request对象
	 * @param userId	: 用户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/02/18	1.0.0		sheng.zheng		初版
	 *
	 */
	private void setDefaultUserDrList(HttpServletRequest request, Long userId) {
		UserDr newItem = null;
		List<UserDr> defaultUserDrList = drs.getDefaultUserDrList();
		String operatorName = getUserName(request);
		for (UserDr item : defaultUserDrList) {
			newItem = new UserDr();
			newItem.setFieldId(item.getFieldId());
			newItem.setUserId(userId);
			newItem.setDrType(item.getDrType());
			newItem.setOperatorName(operatorName);
			// 新增用户数据权限记录
			userDrDao.insertItem(newItem);
		}		
	}
	
	
	/**
	 * 
	 * @methodName		: getItemByKeyInfo
	 * @description	: 根据关键信息（身份证号和手机号）获取用户对象
	 * @param params	: 查询信息，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"idNo"			: "",	// 身份证号码，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 	}	 
	 * @return
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public User getItemByKeyInfo(Map<String, Object> params) {
		// 输入参数校验，关键信息字段不可全为空
		checkValidForParams(null,"getItemByKeyInfo",params);
		// 有效的用户记录
		params.put("deleteFlag", (byte)0);	
		
		User item = null;
		List<User> itemList = userDao.selectItemByKeyInfo(params);
		if (itemList.size() > 0) {
			item = itemList.get(0);
		}
		return item;		
	}
	
	/**
	 * 
	 * @methodName		: getItemsCountByKeyInfo
	 * @description	: 根据关键信息，查询记录数
	 * @param params	: 查询信息，形式如下：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"idNo"			: "",	// 身份证号码，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 	}	 
	 * @return			: 记录数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/11	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Integer getItemsCountByKeyInfo(Map<String, Object> params) {
		Integer iCount = 0;
		iCount = userDao.selectCount(params);
		return iCount;
	}
	
	/**
	 * 
	 * @methodName		: checkUniqueFields
	 * @description	: 检查非空的唯一性字段集，不包含已删除记录
	 * 	调用方法：在新增或修改后，检查唯一性字段，如异常，将执行callback操作
	 * @param params	: 包括下列字段：
	 * 	{
	 * 		"userName"		: "",	// 用户名，可选
	 * 		"phoneNumber"	: "",	// 手机号码，可选
	 * 		"idNo"			: "",	// 身份证号码，可选
	 * 		"openId"		: "",	// 微信小程序的openid，可选
	 * 		"woaOpenid"		: "",	// 微信公众号openid，可选
	 * 	}	 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/11	1.0.0		sheng.zheng		初版
	 * 2022/06/06	1.0.1		sheng.zheng		统一在更新数据库中检查
	 *
	 */
	private void checkUniqueFields(Map<String, Object> params) {
		Map<String,Object> map = new HashMap<String,Object>();
		String[] keyFields = new String[] {"userName","phoneNumber","idNo","openId","woaOpenid"};
		for(int i = 0; i < keyFields.length; i++) {
			// 遍历唯一字段集的所有字段名
			String key = keyFields[i];
			if (params.containsKey(key)) {
				// 如果检查参数中包括此字段名
				Object oVal = params.get(key);
				String sVal = oVal.toString();
				if (!sVal.isEmpty()) {
					map.put(key, oVal);
				}
			}
		}
		Integer iCount = getItemsCountByKeyInfo(map);
		if (iCount > 1) {
			//String prompt = "userName,phoneNumber,idNo,openId,woaOpenid";
			throw new BaseException(ExceptionCodes.KEYINFO_IS_EXIST.getCode(),
					ExceptionCodes.KEYINFO_IS_EXIST.getMessage() + ":" + map.toString());			
		}		
	}
	
	/**
	 * 
	 * @methodName		: addUserRole
	 * @description	: 添加用户角色，如果用户的指定角色ID未添加，则添加
	 * @param request	: request对象
	 * @param userId	: 用户ID
	 * @param roleId	: 角色ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/04/03	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addUserRole(HttpServletRequest request,Long userId,Integer roleId) {
		String operatorName = getUserName(request);
		
		// 设置角色，检查是否存在督导人员角色
		UserRole userRole = null;
		userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setRoleId(roleId);
		userRole.setOperatorName(operatorName);
		try {
			userRoleDao.insertItem(userRole);
		}catch(Exception e) {
			if (e instanceof DuplicateKeyException) {
				// 如果已存在该角色
				return;
			}else {
				LogUtil.error(e);
				throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED,"userId="+userId+",roleId="+roleId);									
			}
		}
		
		// 更新缓存
		cdcs.cacheObjectChanged(ECacheObjectType.cotUserRoleE, null, userId, EDataOperationType.dotAddE);
	}	
	
	/**
	 * 
	 * @methodName		: addUser
	 * @description	: 添加用户，此为线程调用的方法
	 * @param item		: 用户对象，所有参数已设置
	 * @param roleId	: 角色ID
	 * @param udList	: 用户数据权限列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void addUser(User item,Integer roleId,List<UserDr> udList) {
		// 插入数据
		try {
			userDao.insertItem(item);
			Long userId = item.getUserId();
			String operatorName = item.getOperatorName();
			
			//  新增角色
			addUserRole(userId,roleId,operatorName);
			
			// 数据库一致性处理
			setUserDrList(udList,operatorName);
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED,e.getMessage());
		}		
	}
	
	/**
	 * 
	 * @methodName		: setDefaultUserDrList
	 * @description	: 设置默认用户数据权限列表
	 * @param userId	: 用户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void setUserDrList(List<UserDr> udList,String operatorName) {
		for (UserDr item : udList) {
			item.setOperatorName(operatorName);
			// 新增用户数据权限记录
			userDrDao.insertItem(item);
		}		
	}
	
	/**
	 * 
	 * @methodName		: addUserRole
	 * @description	: 添加用户角色，如果用户的指定角色ID未添加，则添加
	 * @param userId	: 用户ID
	 * @param roleId	: 角色ID
	 * @param operatorName	: 操作者账户
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/08/16	1.0.0		sheng.zheng		初版
	 *
	 */
	private void addUserRole(Long userId,Integer roleId,String operatorName) {
		// 设置角色，检查是否存在督导人员角色
		UserRole userRole = null;
		userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setRoleId(roleId);
		userRole.setOperatorName(operatorName);
		try {
			userRoleDao.insertItem(userRole);
		}catch(Exception e) {
			if (e instanceof DuplicateKeyException) {
				// 如果已存在该角色
				return;
			}else {
				LogUtil.error(e);
				throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED,"userId="+userId+",roleId="+roleId);									
			}
		}		
	}	
	
	// 填充itemList中的参照信息
	private void fillRefValue(List<User> itemList) {
		// 填充参照信息
		for(User item : itemList) {
			fillRefValue(item);
		}
	}
	
	// 填充item中的参照信息
	private void fillRefValue(User item) {
		// 填充参照信息
		OrgnizationService os = (OrgnizationService)gcs.getDataServiceObject("OrgnizationService");
		Orgnization orgObj = os.getItemByKey(item.getOrgId(), false);
		if (orgObj != null) {
			item.setOrgName(orgObj.getOrgName());
		}
	}	
}
