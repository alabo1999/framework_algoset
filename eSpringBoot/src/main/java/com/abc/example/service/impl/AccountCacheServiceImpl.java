package com.abc.example.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.abc.example.common.constants.Constants;
import com.abc.example.entity.AccountCache;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.AccountCacheService;

/**
 * @className		: AccountCacheServiceImpl
 * @description	: 账户缓存服务实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class AccountCacheServiceImpl implements AccountCacheService{

	// 账户过期时间，对所有账户有效，初始值： 3*24*3600,3天
	private int maxInactiveInterval = Constants.TOKEN_EXPIRE_TIME;
	
	// 账户缓存对象字典，key为账户ID
	private Map<String,AccountCache> accountCacheMap = new HashMap<String,AccountCache>();
	
	/**
	 * 
	 * @methodName		: getMaxInactiveInterval
	 * @description	: 获取账户缓存的存续时长
	 * @return			: 缓存的存续时长，单位秒
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
    public int getMaxInactiveInterval() {
    	return maxInactiveInterval;
    }
    
	/**
	 * 
	 * @methodName		: setMaxInactiveInterval
	 * @description	: 设置缓存的存续时长
	 * @param interval	: 缓存的存续时长，单位秒
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	// ===============================================================
	// 账户对象的访问
	/**
	 * 
	 * @methodName		: getCreationTime
	 * @description	: 获取账户缓存的创建时间
	 * @param accountId	: 账户ID
	 * @return			: 创建时间，为系统时间戳，即相对于1/1/1970 GMT的毫秒数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public long getCreationTime(String accountId) {
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			if (item.isExpired()) {
				// 过期
				accountCacheMap.remove(accountId);
				throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);
			}
			return item.getCreateTime();
		}else {
			throw new BaseException(ExceptionCodes.SESSION_DATA_WRONG);	
		}
	}
	
		
	/**
	 * 
	 * @methodName		: invalidate
	 * @description	: 使得账户缓存失效，即删除该账户
	 * @param accountId	: 账户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void invalidate(String accountId) {
		if (accountCacheMap.containsKey(accountId)) {
			accountCacheMap.remove(accountId);
		}
	}
	
	/**
	 * 
	 * @methodName		: getAttributeNames
	 * @description	: 获取账户的属性名列表
	 * @param accountId	: 账户ID
	 * @return			: 账户的属性名列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String> getAttributeNames(String accountId){
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			if (item.isExpired()) {
				// 过期
				accountCacheMap.remove(accountId);
				throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);
			}
			return item.getAttributeNames();
		}else {
			throw new BaseException(ExceptionCodes.SESSION_DATA_WRONG);	
		}
	}
		
	/**
	 * 
	 * @methodName		: getAttribute
	 * @description	: 获取属性信息
	 * @param accountId	: 账户ID
	 * @param name		: 属性名
	 * @return			: 属性值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Object getAttribute(String accountId,String name) {
		Object oValue = null;
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			if (item.isExpired()) {
				// 过期
				accountCacheMap.remove(accountId);
				throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);
			}
			oValue = item.getAttribute(name);
		}
		return oValue;
	}
	
	/**
	 * 
	 * @methodName		: setAttribute
	 * @description	: 设置属性值
	 * @param accountId	: 账户ID
	 * @param name		: 属性名
	 * @param value		: 属性值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setAttribute(String accountId,String name,Object value) {
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			if (item.isExpired()) {
				// 过期
				accountCacheMap.remove(accountId);
				throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);
			}
			item.setAttribute(name,value);
		}else {
			// 新的账户，创建
			AccountCache item = new AccountCache();
			// 设置信息
			item.setId(accountId);
			// 设置存续时长
			item.setDuration(this.maxInactiveInterval);
			item.setAttribute(name,value);
			
			// 加入账户管理中
			accountCacheMap.put(accountId, item);
		}		
	}
	
	/**
	 * 
	 * @methodName		: removeAttribute
	 * @description	: 移除属性项
	 * @param accountId	: 账户ID
	 * @param name		: 属性名
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void removeAttribute(String accountId,String name) {
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			if (item.isExpired()) {
				// 过期
				accountCacheMap.remove(accountId);
				throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);
			}
			item.removeAttribute(name);
		}				
	}
	
	/**
	 * 
	 * @methodName		: getId
	 * @description	: 获取账户ID
	 * @param request	: request对象
	 * @return			: 账户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public String getId(HttpServletRequest request) {
		String sessionId = request.getHeader(Constants.X_SESSIONID);
		if (sessionId == null) {
			// 如果header中无指定属性
			// 表示可以使用Session中的SessionId
			sessionId = request.getSession().getId();
		}
		return sessionId;
	}
	
	/**
	 * 
	 * @methodName		: getExpiredTime
	 * @description	: 获取账户的过期时间
	 * @param accountId	: 账户ID
	 * @return			: 到期时间，为系统时间，1970.01.01 UTC，0表示永久有效
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public long getExpiredTime(String accountId) {
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			if (item.isExpired()) {
				// 过期
				accountCacheMap.remove(accountId);
				throw new BaseException(ExceptionCodes.TOKEN_EXPIRED);
			}
			return item.getExpiredTime();
		}else {
			throw new BaseException(ExceptionCodes.SESSION_DATA_WRONG);	
		}		
	}
	
	/**
	 * 
	 * @methodName		: setExpiredTime
	 * @description	: 设置账户的过期时间，用于个性化设置
	 * @param accountId	: 账户ID
	 * @param expireTime: 到期时间，为系统时间，1970.01.01 UTC，0表示永久有效
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setExpiredTime(String accountId,long expireTime) {
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			item.setExpiredTime(expireTime);
		}else {
			throw new BaseException(ExceptionCodes.SESSION_DATA_WRONG);	
		}			
	}
	
	/**
	 * 
	 * @methodName		: refreshExpiredTime
	 * @description	: 刷新账户的到期时间，即以当前时间续期一个周期
	 * @param accountId	: 账户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void refreshExpiredTime(String accountId) {
		if (accountCacheMap.containsKey(accountId)) {
			AccountCache item = accountCacheMap.get(accountId);
			item.setExpiredTime(System.currentTimeMillis() + maxInactiveInterval * 1000);
		}else {
			throw new BaseException(ExceptionCodes.SESSION_DATA_WRONG);	
		}			
	}
}
