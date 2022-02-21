package com.abc.example.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.abc.example.common.constants.Constants;
import com.abc.example.common.tree.TreeNode;
import com.abc.example.dao.UserDao;
import com.abc.example.entity.Function;
import com.abc.example.entity.User;
import com.abc.example.enumeration.ENotifyMsgType;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.AccountCacheService;
import com.abc.example.service.ChangeNotifyService;
import com.abc.example.service.DataRightsService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.LoginService;
import com.abc.example.service.RoleFuncRightsService;
import com.abc.example.service.RoleService;
import com.abc.example.vo.common.Additional;
import com.abc.example.vo.common.BaseResponse;


/**
 * @className		: AuthorizationAspect
 * @description	: 接口访问鉴权切面类
 * @summary		: 使用AOP，进行token认证以及用户对接口的访问权限鉴权
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Aspect
@Component
@Order(2)
public class AuthorizationAspect {
	
	@Autowired
    private UserDao userDao;
	
	@Autowired
	private DataRightsService dataRightsService;
	
	// 公共配置服务类	
	@Autowired
	private GlobalConfigService gcs;
	
	// 账户缓存服务
	@Autowired
	protected AccountCacheService accountCacheService;
	
	
	// 设置切点
    @Pointcut("execution(public * com.abc.example.controller..*.*(..))" +
    "&& !execution(public * com.abc.example.controller.LoginController.*(..))") 
    public void verify(){}
    
    /**
     * 
     * @methodName		: doVerify
     * @description	: 前置增强，鉴权
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @Before("verify()") 
    public void doVerify(){
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		HttpServletRequest request=attributes.getRequest(); 
		
		// ================================================================================
		// token认证
		
		// 从header中获取token值
		String token = request.getHeader("Authorization");
		if (null == token || token.equals("")){ 
			//return;
			throw new BaseException(ExceptionCodes.TOKEN_IS_NULL); 
		} 
		
		String savedToken = "";
		// 获取缓存账号ID
		String accountId = accountCacheService.getId(request);
		// 获取缓存的token值
		savedToken = (String)accountCacheService.getAttribute(accountId, Constants.TOKEN);
		
    			
		// 判断缓存的token值是否有信息，可能是非登录用户
		if (null == savedToken) {				
			throw new BaseException(ExceptionCodes.SESSION_DATA_WRONG);
		}
    	
		// 比较token
		if(!token.equalsIgnoreCase(savedToken)) {
			throw new BaseException(ExceptionCodes.TOKEN_WRONG);			
		}
		
		// token未过期，刷新过期时间
		accountCacheService.refreshExpiredTime(accountId);
		
		// ================================================================================
		// 接口调用权限
		// 获取用户ID
		Long userId = (Long)accountCacheService.getAttribute(accountId, Constants.USER_ID); 
		
		//===================变更通知处理开始==============================================
		// 检查有无变更通知信息
		ChangeNotifyService cns = (ChangeNotifyService)gcs.getDataServiceObject("ChangeNotifyService");
		Integer changeNotifyInfo = cns.getChangeNotifyInfo(userId);
		// 设置成员属性为false
		boolean rightsChangedFlag = false;		
		if (changeNotifyInfo != Constants.INVALID_VALUE) {
			// 有通知信息
			if((changeNotifyInfo & ENotifyMsgType.nmtUserDisabledE.getCode()) > 0) {
				// 用户禁用，从而导致权限变更
				// 设置无效token,可阻止该用户访问系统
				accountCacheService.setAttribute(accountId, Constants.TOKEN, "");
				// 直接抛出异常，由前端显示：Forbidden页面
				throw new BaseException(ExceptionCodes.ACCESS_FORBIDDEN);
			}
			// 与session有关的变更
			if (changeNotifyInfo > 0) {
				// 调整用户数据权限类型，导致数据权限变更
				User userInfo = userDao.selectItemByKey(userId);
				// 更新Session
				accountCacheService.setAttribute(accountId, Constants.USER_TYPE, Integer.valueOf(userInfo.getUserType()));
				accountCacheService.setAttribute(accountId, Constants.ORG_ID, userInfo.getOrgId());
		    	// roleIdList
		    	RoleService rs = (RoleService)gcs.getDataServiceObject("RoleService");
		    	rs.setFuncRights(request, userId);
		    	
		    	// drMap
		    	dataRightsService.setUserDrs(request);
		    					
	    		// 权限变更标志置位
	    		rightsChangedFlag = true;
			}
			
			if (rightsChangedFlag == true) {
				// 写Session，用于将信息传递到afterReturning方法中
				accountCacheService.setAttribute(accountId, "rightsChanged", Constants.VALID_VALUE);
			}
		}
		//===================变更通知处理结束==============================================
		// 获取角色ID列表
		@SuppressWarnings("unchecked")
		List<Integer> roleIdList = (List<Integer>)accountCacheService.getAttribute(accountId,Constants.ROLE_ID_LIST);
		// 获取当前接口url值
		String servletPath = request.getServletPath();
				
		// 获取该角色对url的访问权限
		RoleFuncRightsService rfrs = (RoleFuncRightsService)gcs.getDataServiceObject("RoleFuncRightsService");
		Integer rights = rfrs.getRoleUrlRights(roleIdList, servletPath);
		if (rights == 0) {
			// 如果无权限访问此接口，抛出异常，由前端显示：Forbidden页面
			System.out.println(servletPath);
			throw new BaseException(ExceptionCodes.ACCESS_FORBIDDEN);
		}		
    }    
    
    /**
     * 
     * @methodName		: afterReturning
     * @description	: 后置增强
     * @param <T>		: 泛型T
     * @param result	: response的返回对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @AfterReturning(value="verify()" ,returning="result")
    public <T> void afterReturning(BaseResponse<T> result) {
    	// 限制必须是BaseResponse类型，其它类型的返回值忽略
    	// 获取Session
        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
		// 获取缓存账号ID
		String accountId = accountCacheService.getId(request);
    	Integer rightsChanged = (Integer)accountCacheService.getAttribute(accountId,"rightsChanged");
    	if (rightsChanged != null && rightsChanged == Constants.VALID_VALUE) {
    		// 如果有用户权限变更，通知前端来刷新该用户的功能权限树
    		// 构造附加信息
    		Additional additional = new Additional();
    		additional.setNotifycode(ExceptionCodes.USER_RIGHTS_CHANGED.getCode());
    		additional.setNotification(ExceptionCodes.USER_RIGHTS_CHANGED.getMessage());
    		
    		// 更新token
    		String loginName = (String)accountCacheService.getAttribute(accountId,Constants.USER_NAME);
    		String token = LoginService.generateToken(loginName);
    		additional.setToken(token);
    		// 更新token，要求下次url访问使用新的token
    		accountCacheService.setAttribute(accountId,Constants.TOKEN, token);
    		
    		// 获取用户的功能权限树
    		@SuppressWarnings("unchecked")
    		List<Integer> roleIdList = (List<Integer>)accountCacheService.getAttribute(accountId,Constants.ROLE_ID_LIST);
    		RoleFuncRightsService rfrs = (RoleFuncRightsService)gcs.getDataServiceObject("RoleFuncRightsService");
        	// 获取用户权限的角色功能树
        	TreeNode<Function> rolesFunctionTree = rfrs.getRoleRightsTree(roleIdList);
        	additional.setRights(rolesFunctionTree.toString());
    		// 修改response信息
        	result.setAdditional(additional);
        	
    		// 移除Session的rightsChanged项
        	accountCacheService.removeAttribute(accountId,"rightsChanged");
    	}
    }         		
}
