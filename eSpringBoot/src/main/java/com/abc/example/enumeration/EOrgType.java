package com.abc.example.enumeration;

/**
 * @className		: EOrgType
 * @description	: 组织类型枚举类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/07/11	1.0.0		sheng.zheng		初版
 *
 */
public enum EOrgType {
	otOurCompanyE (1),	// 本公司

	;	//结束定义
	
	//枚举值对应的code值
	int code;

	// 构造函数
	private EOrgType(int code) {
	    this.code = code;
	}

	// 属性code的getter方法
	public int getCode() {
	    return code;
	}

	// 属性code的setter方法
	public void setCode(int code) {
	    this.code = code;
	}
	
	/**
	 * 
	 * @methodName		: getType
	 * @description	: 根据code获取枚举值
	 * @param code		: code值 
	 * @return			: code对应的枚举值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static EOrgType getType(int code) {
		//返回值变量
		EOrgType eRet = null;
		
		for (EOrgType item : values()) {
			//遍历每个枚举值
			if (code == item.getCode()) {
				//code匹配
				eRet = item;
				break;
			}
		}
		
		return eRet;
	}
}
