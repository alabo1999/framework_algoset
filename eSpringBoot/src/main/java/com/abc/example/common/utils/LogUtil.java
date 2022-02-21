package com.abc.example.common.utils;


import lombok.extern.slf4j.Slf4j;

/**
 * @className		: LogUtil
 * @description	: 日志工具类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Slf4j
public class LogUtil {
	
	/**
	 * 
	 * @methodName		: info
	 * @description	: 输出提示信息
	 * @param logInfo	: 提升信息
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void info(String logInfo) {
		System.out.println(logInfo);
		log.info(logInfo);
	}
	
	/**
	 * 
	 * @methodName		: info
	 * @description	: 输出提示信息
	 * @param logInfo	: 提升信息	
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void info(Object logInfo) {
		System.out.println(logInfo);
		log.info(logInfo.toString());
	}	
	
	/**
	 * 
	 * @methodName		: error
	 * @description	: 输出异常信息
	 * @param e			: Exception对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void error(Exception e) {
		e.printStackTrace();
		String ex = getString(e);
		log.error(ex);
	}	
	
	/**
	 * 
	 * @methodName		: error
	 * @description	: 输出异常信息
	 * @param logInfo	: 异常信息	
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void error(String logInfo) {
		System.out.println(logInfo);
		log.error(logInfo);
	}		
	
	/**
	 * 
	 * @methodName		: getString
	 * @description	: 获取Exception的getStackTrace信息
	 * @param ex		: Exception对象
	 * @return			: 错误调用栈信息
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    public static String getString(Exception ex) {

        StringBuilder stack = new StringBuilder();
        StackTraceElement[] sts = ex.getStackTrace();
        for (StackTraceElement st : sts) {
            stack.append(st.toString()).append("\r\n");
        }
        return stack.toString();
    }
    
    /**
     * 
     * @methodName		: getString
     * @description	: 将字符串数组内容组成字符串输出
     * @param arrStr	: 字符串数组
     * @return			: 拼接得到的字符串
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static String getString(String[] arrStr) {
		String strItem = "[";
		for (int j = 0; j < arrStr.length; j++) {
			if (j == 0) {
    			strItem += arrStr[j];	        					        				
			}else {
    			strItem += "," + arrStr[j];	        				
			}
		}
		strItem += "]";
		return strItem;
    }
}
