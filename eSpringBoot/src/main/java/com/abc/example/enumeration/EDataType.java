package com.abc.example.enumeration;

/**
 * @className		: EDataType
 * @description	: 数据类型
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/02/13	1.0.0		sheng.zheng		初版
 *
 */
public enum EDataType {
	dtTinyIntE (1),	//有符号1字节整数
	dtUTinyIntE (2),	//无符号1字节整数
	dtSmallIntE (3),	//有符号2字节整数
	dtUSmallIntE (4),	//无符号2字节整数
	dtMediumIntE (5),	//有符号3字节整数
	dtUMediumIntE (6),	//无符号3字节整数
	dtIntE (7),			//有符号4字节整数
	dtUIntE (8),		//无符号4字节整数
	dtBigIntE (9),		//有符号8字节整数
	dtUBigIntE (10),	//无符号8字节整数

	;	//结束定义
	
	//枚举值对应的code值
	int code;

	// 构造函数
	private EDataType(int code) {
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
	public static EDataType getType(int code) {
		//返回值变量
		EDataType eRet = null;
		
		for (EDataType item : values()) {
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
