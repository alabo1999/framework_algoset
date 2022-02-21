package com.abc.example.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @className		: AccountCacheService
 * @description	: 账户缓存服务接口类
 * @summary		: 用于管理访问账户的缓存，由于微信小程序访问及内部服务器跨域访问时，
 * 	SessionId经常发生改变，导致Session缓存失效，因此使用此类来管理账户的缓存信息
 *  单机版可以在内存中管理，集群部署可以在redis中管理
 *  WEB端，使用SessionId作为accountId，其它使用自定义的HTTP头x-SessionId，在存续期间使用
 *  登录时，需要生成一个随机的字符串，提交登录，如果此值已使用但缓存账户信息不一致，则无效
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface AccountCacheService {
	// ===============================================================
	// 服务对象的属性访问
	
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
    public int getMaxInactiveInterval();
    
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
	public void setMaxInactiveInterval(int interval);	

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
	public long getCreationTime(String accountId);		
			
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
	public void invalidate(String accountId);	
	
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
	public List<String> getAttributeNames(String accountId);
		
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
	public Object getAttribute(String accountId,String name);
	
	/**
	 * 
	 * @methodName		: setAttribute
	 * @description	: 设置属性值
	 * @param request	: request对象
	 * @param name		: 属性名
	 * @param value		: 属性值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setAttribute(String accountId,String name,Object value);
	
	/**
	 * 
	 * @methodName		: removeAttribute
	 * @description	: 移除属性项
	 * @param request	: request对象
	 * @param name		: 属性名
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeAttribute(String accountId,String name);	
	
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
	public String getId(HttpServletRequest request);
			
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
	public long getExpiredTime(String accountId);	
	
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
	public void setExpiredTime(String accountId,long expireTime);	
	
	/**
	 * 
	 * @methodName		: refreshExpiredTime
	 * @description	: 刷新账户的到期时间，即以当前时间续期一个周期
	 * @param accountId	: 账户ID
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/10	1.0.0		sheng.zheng		初版
	 *
	 */
	public void refreshExpiredTime(String accountId);
}
