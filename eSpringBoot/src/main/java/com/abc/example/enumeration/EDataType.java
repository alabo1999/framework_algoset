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
	dtCharE (1),		// 字符
	dtByteE (2),		// 字节
	dtShortE (3),		// 有符号短整型
	dtUShortE (4),		// 无符号短整型
	dtIntE (5),			// 有符号整型
	dtUIntE (6),		// 无符号整型
	dtLongE (7),		// 有符号长整型
	dtULongE (8),		// 无符号长整型
	dtFloatE (9),		// 单精度浮点数
	dtDoubleE (10),		// 双精度浮点数
	dtArrayE (11),		// 数组
	dtStructE (12),		// 结构体
	dtStringE (13),		// 字符串
	dtByteArrayE (14),	// 字节数组
	dtBooleanE (15),	// Int型0/1值
	dtEnumE (16),		// 枚举类型，由系统参数定义
	dtCustomEnumE (17),// 自定义枚举类型
	dtLocalDateE (18),	// 日期类型
	dtLocalTimeE (19),	// 时间类型
	dtLocalDateTimeE (20),	// 日期时间类型
	dtDateE (21),		// 日期时间类型
	dtMapE (22),		// 字典
	dtURLE (23),		// URL类型

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
