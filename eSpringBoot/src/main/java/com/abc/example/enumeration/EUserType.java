package com.abc.example.enumeration;

/**
 * @className		: EUserType
 * @description	: 用户类型枚举值
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public enum EUserType {
	utSystemAdminE (1),	//系统管理员
	utInternalPersonE (2),	//内部人员
	utExternalPersonE (3),	//外部人员
	;	//结束定义
	
	//枚举值对应的code值
	int code;

	// 构造函数
	private EUserType(int code) {
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
	public static EUserType getType(int code) {
		//返回值变量
		EUserType eRet = null;
		
		for (EUserType item : values()) {
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
