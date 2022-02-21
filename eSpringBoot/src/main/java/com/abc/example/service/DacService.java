package com.abc.example.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


/**
 * @className		: DacService
 * @description	: 数据访问计数服务类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class DacService {
	
	// 计数器类型：1-数量；2-时间窗口；3-时间窗口+数量
	private int counterType; 
	
	// 计数器数量门限
	private int counterThreshold = 5;
	
	// 时间窗口长度，单位毫秒
	private int windowSize = 60000;
	
	// 对象key的访问计数器
	private Map<String,Integer> itemMap;

	// 对象key的访问滑动窗口
	private Map<String,Deque<Long>> itemSlideWindowMap;
	
	/**
	 * 构造函数
	 * @param counterType		: 计数器类型，值为1，2，3之一
	 * @param counterThreshold	: 计数器数量门限，如果类型为1或3，需要此值
	 * @param windowSize		: 窗口时间长度，如果为类型为2,3，需要此值
	 */
	public DacService(int counterType, int counterThreshold, int windowSize) {
		this.counterType = counterType;
		this.counterThreshold = counterThreshold;
		this.windowSize = windowSize;
		
		if (counterType == 1) {
			// 如果与计数器有关
			itemMap = new HashMap<String,Integer>();
		}else if (counterType == 2 || counterType == 3) {
			// 如果与滑动窗口有关
			itemSlideWindowMap = new HashMap<String,Deque<Long>>();
		}
	}		
		
	/**
	 * 
	 * @methodName		: isItemKeyFull
	 * @description	: 对象key的计数是否将满
	 * @param itemKey	: 对象key
	 * @param timeMillis: 时间戳，毫秒数，如为滑窗类计数器，使用此参数值
	 * @return			: 满返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean isItemKeyFull(String itemKey,Long timeMillis) {
		boolean bRet = false;
		
		if (this.counterType == 1) {
			// 如果为计数器类型			
			if (itemMap.containsKey(itemKey)) {
				synchronized(itemMap) {
					Integer value = itemMap.get(itemKey);
					// 如果计数器将超越门限
					if (value >= this.counterThreshold - 1) {
						bRet = true;
					}					
				}
			}else {
				// 新的对象key，视业务需要，取值true或false
				bRet = true;
			}
		}else if(this.counterType == 2){
			// 如果为滑窗类型			
			if (itemSlideWindowMap.containsKey(itemKey)) {
				Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);
				synchronized(itemQueue) {
					if (itemQueue.size() > 0) {
						Long head = itemQueue.getFirst();
						if (timeMillis - head >= this.windowSize) {
							// 如果窗口将满
							bRet = true;
						}
					}									
				}
			}else {
				// 新的对象key，视业务需要，取值true或false
				bRet = true;				
			}			
		}else if(this.counterType == 3){
			// 如果为滑窗+数量类型
			if (itemSlideWindowMap.containsKey(itemKey)) {
				Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);
				synchronized(itemQueue) {
					Long head = 0L;
					// 循环处理头部数据，确保新数据帧加入后，维持窗口宽度
					while(true) {
						// 取得头部数据
						head = itemQueue.peekFirst();
						if (head == null || timeMillis - head <= this.windowSize) {
							break;
						}
						// 移除头部
						itemQueue.remove();
					}	
					if (itemQueue.size() >= this.counterThreshold -1) {
						// 如果窗口数量将满
						bRet = true;
					}													
				}
			}else {
				// 新的对象key，视业务需要，取值true或false
				bRet = true;				
			}			
		}
		
		return bRet;		
	}
		
	/**
	 * 
	 * @methodName		: resetItemKey
	 * @description	: 复位对象key的计数 
	 * @param itemKey	: 对象key
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void resetItemKey(String itemKey) {
		if (this.counterType == 1) {
			// 如果为计数器类型
			if (itemMap.containsKey(itemKey)) {
				// 更新值，加锁保护
				synchronized(itemMap) {
					itemMap.put(itemKey, 0);
				}			
			}		
		}else if(this.counterType == 2){
			// 如果为滑窗类型
			// 清空
			if (itemSlideWindowMap.containsKey(itemKey)) {
				Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);
				if (itemQueue.size() > 0) {
					// 加锁保护
					synchronized(itemQueue) {
						// 先清空
						itemQueue.clear();
					}								
				}
			}						
		}else if(this.counterType == 3){
			// 如果为滑窗+数量类型
			if (itemSlideWindowMap.containsKey(itemKey)) {
				Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);
				synchronized(itemQueue) {
					// 清空
					itemQueue.clear();
				}
			}
		}
	}
	
	/**
	 * 
	 * @methodName		: putItemkey
	 * @description	: 更新对象key的计数
	 * @param itemKey	: 对象key
	 * @param timeMillis: 时间戳，毫秒数，如为滑窗类计数器，使用此参数值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void putItemkey(String itemKey,Long timeMillis) {
		if (this.counterType == 1) {
			// 如果为计数器类型
			if (itemMap.containsKey(itemKey)) {
				// 更新值，加锁保护
				synchronized(itemMap) {
					Integer value = itemMap.get(itemKey);
					// 计数器+1
					value ++;
					itemMap.put(itemKey, value);
				}
			}else {
				// 新key值，加锁保护
				synchronized(itemMap) {
					itemMap.put(itemKey, 1);
				}			
			}
		}else if(this.counterType == 2){
			// 如果为滑窗类型	
			if (itemSlideWindowMap.containsKey(itemKey)) {
				Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);				
				// 加锁保护
				synchronized(itemQueue) {
					// 加入
					itemQueue.add(timeMillis);
				}								
			}else {
				// 新key值，加锁保护
				Deque<Long> itemQueue = new ArrayDeque<Long>();
				synchronized(itemSlideWindowMap) {
					// 加入映射表
					itemSlideWindowMap.put(itemKey, itemQueue);
					itemQueue.add(timeMillis);
				}
			}
		}else if(this.counterType == 3){
			// 如果为滑窗+数量类型
			if (itemSlideWindowMap.containsKey(itemKey)) {
				Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);				
				// 加锁保护
				synchronized(itemQueue) {
					Long head = 0L;
					// 循环处理头部数据
					while(true) {
						// 取得头部数据
						head = itemQueue.peekFirst();
						if (head == null || timeMillis - head <= this.windowSize) {
							break;
						}
						// 移除头部
						itemQueue.remove();
					}
					// 加入新数据
					itemQueue.add(timeMillis);					
				}								
			}else {
				// 新key值，加锁保护
				Deque<Long> itemQueue = new ArrayDeque<Long>();
				synchronized(itemSlideWindowMap) {
					// 加入映射表
					itemSlideWindowMap.put(itemKey, itemQueue);
					itemQueue.add(timeMillis);
				}
			}			
		}				
	}
		
	/**
	 * 
	 * @methodName		: clear
	 * @description	: 清空字典
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void clear() {
		if (this.counterType == 1) {
			// 如果为计数器类型
			synchronized(this) {
				itemMap.clear();
			}				
		}else if(this.counterType == 2){
			// 如果为滑窗类型	
			synchronized(this) {
				itemSlideWindowMap.clear();
			}				
		}else if(this.counterType == 3){
			// 如果为滑窗+数量类型
			synchronized(this) {
				itemSlideWindowMap.clear();
			}				
		}			
	}
	
	/**
	 * 
	 * @methodName		: getTimeDuration
	 * @description	: 获取开始计时的时长，适用于类型2和3
	 * @param itemKey	: key
	 * @param timeMillis: 当前时间
	 * @return			: 时长，单位毫秒
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public long getTimeDuration(String itemKey,Long timeMillis) {
		long duration = 0;
		if(this.counterType == 1) {
			return duration;
		}
		
		if (itemSlideWindowMap.containsKey(itemKey)) {
			Deque<Long> itemQueue = itemSlideWindowMap.get(itemKey);
			if (itemQueue.size() > 0) {
				Long head = itemQueue.getFirst();
				duration = timeMillis - head;
			}									
		}
		return duration;
	}
}
