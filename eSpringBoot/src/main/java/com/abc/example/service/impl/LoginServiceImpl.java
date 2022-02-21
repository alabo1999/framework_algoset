package com.abc.example.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.constants.Constants;
import com.abc.example.common.tree.TreeNode;
import com.abc.example.common.utils.Md5Util;
import com.abc.example.dao.UserDao;
import com.abc.example.entity.Function;
import com.abc.example.entity.LoginInfo;
import com.abc.example.entity.User;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.ChangeNotifyService;
import com.abc.example.service.DataRightsService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.LoginService;
import com.abc.example.service.RoleFuncRightsService;
import com.abc.example.service.RoleService;
import com.abc.example.service.VerifyCodeService;


/**
 * @className		: LoginServiceImpl
 * @description	: LoginService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class LoginServiceImpl extends BaseService implements LoginService {

	@Autowired
    private UserDao userDao;
	
	@Autowired
	private DataRightsService drs;
	
	@Autowired	
	private VerifyCodeService verifyCodeService;
	
	// 公共配置数据服务
	@Autowired
	private GlobalConfigService gcs;	
	
	/**
	 * 
	 * @methodName		: login
	 * @description	: 用户登录
	 * @param request	: request对象
	 * @param loginInfo	: 登录信息对象
	 * @return			: Map对象，形式如下：
	 * 		{
	 * 			"token"	: "ABCDEFG12345YUAKDLDJUYDDKFE54321",
	 * 			"rights": "{...}",		//权限树JSON串
	 * 		}
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Map<String,Object> login(HttpServletRequest request,LoginInfo loginInfo){
				
		// 输入参数校验
		checkValidForParams(request,"login",loginInfo);
		
		// 获取缓存账号ID
		String accountId = accountCacheService.getId(request);
		String sessionId = request.getHeader(Constants.X_SESSIONID);
		
    	// 验证码检查
		if (sessionId == null) {
			// 如果不是内部系统，则需要验证码
			// 读取cache中验证码
			String cacheVerifyCode = (String)accountCacheService.getAttribute(accountId,Constants.VERIFY_CODE);
			if (cacheVerifyCode == null || cacheVerifyCode.isEmpty()) {
				// cache中没有验证码			
				throw new BaseException(ExceptionCodes.VERIFYCODE_WRONG);
			}
			// 比较之前生成的验证码与输入的验证码是否一致，不区分大小写
			if (!cacheVerifyCode.equalsIgnoreCase(loginInfo.getVerifyCode())) {
				// 如果验证码不相符
				throw new BaseException(ExceptionCodes.VERIFYCODE_WRONG);
			}
		}
		
		// 获取登录名的用户信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userName", loginInfo.getUserName());
    	List<User> userList = userDao.selectItems(params);
    	if(userList.size() == 0){
    		// 如果该登录账户不存在
    		throw new BaseException(ExceptionCodes.LOGINNAME_PASSWORD_WRONG);    		
    	}
    	User userInfo = userList.get(0);
    	if(userInfo.getDeleteFlag() != EDeleteFlag.dfValidE.getCode()) {
    		// 用户禁用
    		throw new BaseException(ExceptionCodes.ACCESS_FORBIDDEN);
    	}
    	
    	// 密码检查
    	boolean passwordValidFlag = checkPassword(
    			loginInfo.getPassword(), userInfo.getPassword(), userInfo.getSalt(),Constants.TOKEN_KEY);    	
    	if (false == passwordValidFlag) {
    		//密码错误
    		throw new BaseException(ExceptionCodes.LOGINNAME_PASSWORD_WRONG);    		    		
    	}    	
    	
    	// 生成token
    	String token = LoginService.generateToken(loginInfo.getUserName());
    	    			
    	// 保存信息到cache中
    	accountCacheService.setAttribute(accountId,Constants.TOKEN, token);
    	accountCacheService.setAttribute(accountId,Constants.USER_NAME, loginInfo.getUserName()); 
    	accountCacheService.setAttribute(accountId,Constants.USER_ID, userInfo.getUserId());
    	accountCacheService.setAttribute(accountId,Constants.USER_TYPE, (int)userInfo.getUserType());
    	accountCacheService.setAttribute(accountId,Constants.ORG_ID, userInfo.getOrgId());
    	
    	// =====================================================
    	// 设置用户的权限
    	// 设置操作权限
    	TreeNode<Function> rolesFunctionTree = setFuncRights(request,userInfo);
    	if (rolesFunctionTree == null) {
    		throw new BaseException(ExceptionCodes.NO_ROLE_ASSIGNED); 
    	}
    	
    	// 设置数据权限
    	drs.setUserDrs(request);     	
    	// =====================================================
    	
    	// 复位通知消息
    	ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");		
    	cns.getChangeNotifyInfo(userInfo.getUserId());
    	
    	// 构造输出信息
    	Map<String,Object> retMap = new HashMap<String,Object>();
    	retMap.put("token",token);
    	retMap.put("rights",rolesFunctionTree.toString());    
    	
		return retMap;		    	    
	}
	
	/**
	 * 
	 * @methodName		: getVerifyCode
	 * @description	: 获取验证码
	 * @param request	: request对象
	 * @param response	: response对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void getVerifyCode(HttpServletRequest request,HttpServletResponse response) {
		//获取验证码图片
		BufferedImage image = verifyCodeService.getImage();
		// 获取缓存账号ID
		String accountId = accountCacheService.getId(request);		
		//将验证码的文本存在cache中
		accountCacheService.setAttribute(accountId,"verifyCode", verifyCodeService.getVerifyCode()); 
		
        try {
        	//将验证码图片响应给客户端
        	verifyCodeService.output(image, response.getOutputStream());
        } catch(IOException e) {
    		throw new BaseException(ExceptionCodes.VERIFYCODE_OUTPUT_FAILED);    		    		        	
        } 		
	}
	
	/**
	 * 
	 * @methodName		: checkValidForParams
	 * @description	: 输入参数校验
	 * @param request	: request对象
	 * @param methodName: 方法名称
	 * @param params	: 输入参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void checkValidForParams(HttpServletRequest request,String methodName, Object params) {
		switch(methodName) {
		case "login":
		{
			LoginInfo loginInfo = (LoginInfo)params;
			
			//检查登录名
			String userName = loginInfo.getUserName();
			if (userName == null || userName.isEmpty()) {
				//登录名为空
				throw new BaseException(ExceptionCodes.LOGINNAME_IS_EMPTY);
			}
			
			//检查密码
			String password = loginInfo.getPassword();
			if (password == null || password.isEmpty()) {
				//密码为空
				throw new BaseException(ExceptionCodes.PASSWORD_IS_EMPTY);
			}			
			
			//检查验证码
			String verifyCode = loginInfo.getVerifyCode();
			if (verifyCode == null || verifyCode.isEmpty()) {
				//密码为空
				throw new BaseException(ExceptionCodes.VERIFYCODE_IS_EMPTY);
			}					
		}
		break;
		default:
			break;
		}
	}			
	
	/**
	 * 
	 * @methodName		: checkPassword
	 * @description	: 检查密码是否正确
	 * @param passwd	: 接口中的密码参数，已经经过MD5加密
	 * @param dbPasswd	: 数据库中的密码
	 * @param salt		: 盐
	 * @param key		: 密钥
	 * @return			: 密码正确返回true，否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private boolean checkPassword(String passwd, String dbPasswd, String salt,String key) {
		String verifyPasswd = "";
		boolean bRet = false;
		
		if (dbPasswd == null)
		{
			return bRet;
		}
		
		//计算加盐后的二次签名，大写
		verifyPasswd = Md5Util.apiPasswdToDBPasswd(passwd,salt,key);
		
		//与数据库的密码比较
		bRet = verifyPasswd.equals(dbPasswd);
		
		return bRet;
	}	
	
	/**
	 * 
	 * @methodName		: setFuncRights
	 * @description	: 设置功能权限并返回权限树
	 * @param request	: request对象
	 * @param userInfo	: 用户信息
	 * @return			: 权限树
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private TreeNode<Function> setFuncRights(HttpServletRequest request,User userInfo) {
    	Long userId = userInfo.getUserId();
		// 获取缓存账号ID
		String accountId = accountCacheService.getId(request);		

    	// 操作权限  
    	RoleService rs = (RoleService)gcs.getDataServiceObject("RoleService");
    	List<Integer> roleIdList = rs.getRoleIdsByUserId(userId);
    	if (roleIdList.size() == 0) {
    		// 未分配权限
    		throw new BaseException(ExceptionCodes.NO_ROLE_ASSIGNED); 
    	}

    	// 获取全局变量
		RoleFuncRightsService rfrs = (RoleFuncRightsService)gcs.getDataServiceObject("RoleFuncRightsService");
    	// 获取用户权限的角色功能树
		TreeNode<Function> rolesFunctionTree = rfrs.getRoleRightsTree(roleIdList);
		// 设置角色列表
		accountCacheService.setAttribute(accountId,Constants.ROLE_ID_LIST,roleIdList);
		
		return rolesFunctionTree;
	}	
		
}
