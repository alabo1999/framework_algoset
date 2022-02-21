package com.abc.example.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className		: AccountCache
 * @description	: 账户缓存对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class AccountCache {
	// 构造方法
	public AccountCache() {
		createTime = System.currentTimeMillis();
	}
	
	// 创建时间，为系统时间戳，即相对于1/1/1970 GMT的毫秒数，下同
	// 只读属性
	private long createTime;	
	
	// 到期时间，0为永久有效；允许在未到期前续期。
	private long expiredTime = 0;

	// 账户ID
	private String id = "";
	
	// 属性字典
	private Map<String,Object> attributeMap = new HashMap<String,Object>();	
	
	// 属性createTime的getter方法
	public long getCreateTime() {
		return createTime;
	}
		
	// 属性expiredTime的getter/setter方法
	public long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}
	// 设置存续期限，单位秒
	public void setDuration(int duration) {
		this.expiredTime = createTime + duration*1000;
	}
	
	// 属性id的getter/setter方法
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	//////// 操作属性字段 //////////////////////////
	// 获取指定属性名的属性值
	public Object getAttribute(String name) {
		Object retObj = null;
		if (attributeMap.containsKey(name)) {
			retObj = attributeMap.get(name);
		}
		return retObj;
	}
	
	// 设置指定属性名的属性值，如值为null，则删除此属性
	public void setAttribute(String name,Object value) {
		if (attributeMap.containsKey(name)) {
			// 如果属性名已存在，则修改
			if (value != null) {
				// 值非空，修改
				attributeMap.put(name,value);				
			}else {
				// 值为空，则移除
				attributeMap.remove(name);
			}
		}else {
			// 如果属性名不存在，则新增属性
			if (value != null) {
				// 值非空，修改
				attributeMap.put(name,value);				
			}			
		}
	}
	
	// 移除指定属性名的属性项
	public void removeAttribute(String name) {
		if (attributeMap.containsKey(name)) {
			attributeMap.remove(name);
		}
	}
	
	// 获取属性字典的属性名列表
	public List<String> getAttributeNames(){
		List<String> attrNames = new ArrayList<String>(attributeMap.keySet());
		return attrNames;
	}	
	
	//////// 是否过期 //////////////////////////
	public boolean isExpired() {
		boolean bRet = false;
		if (expiredTime != 0) {
			// 如果不为永久有效
			long now = System.currentTimeMillis();
			bRet = now > expiredTime;
		}
		return bRet;		
	}
	
}
