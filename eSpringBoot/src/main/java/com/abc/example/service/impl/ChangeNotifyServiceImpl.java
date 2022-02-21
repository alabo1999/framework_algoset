package com.abc.example.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abc.example.common.constants.Constants;
import com.abc.example.service.ChangeNotifyService;

/**
 * @className		: ChangeNotifyServiceImpl
 * @description	: ChangeNotifyService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class ChangeNotifyServiceImpl implements ChangeNotifyService {
	
	// 用户ID与变更过通知信息映射表
	private Map<Long,Integer> changeNotifyMap = new HashMap<Long,Integer>();
	
	/**
	 * 
	 * @methodName		: getChangeNotifyInfo
	 * @description	: 获取指定用户ID的变更通知信息 
	 * @param userId	: 用户ID
	 * @return			: 返回0表示无变更通知信息，其它值按照bitmap编码。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Integer getChangeNotifyInfo(Long userId) {
		Integer changeNotifyInfo = Constants.INVALID_VALUE;
		// 检查该用户是否有变更通知信息
		if (changeNotifyMap.containsKey(userId)) {
			changeNotifyInfo = changeNotifyMap.get(userId);
			// 移除数据，加锁保护
			synchronized(changeNotifyMap) {
				changeNotifyMap.remove(userId);
			}
		}
		return changeNotifyInfo;
	}
	
	/**
	 * 
	 * @methodName		: setChangeNotifyInfo
	 * @description	: 设置变更通知信息，该功能一般由管理员触发调用
	 * @param userId	: 用户ID
	 * @param changeNotifyInfo	: 变更通知值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setChangeNotifyInfo(Long userId,Integer changeNotifyInfo) {
		// 检查该用户是否有变更通知信息
		if (changeNotifyMap.containsKey(userId)) {
			// 如果有，表示之前变更通知未处理
			// 获取之前的值
			Integer oldChangeNotifyInfo = changeNotifyMap.get(userId);
			// 计算新值。bitmap编码，或操作
			Integer newChangeNotifyInfo = oldChangeNotifyInfo | changeNotifyInfo;
			// 移除数据，加锁保护
			synchronized(changeNotifyMap) {
				changeNotifyMap.put(userId,newChangeNotifyInfo);
			}
		}else {
			// 如果没有，设置一条
			changeNotifyMap.put(userId,changeNotifyInfo);
		}
	}
}
