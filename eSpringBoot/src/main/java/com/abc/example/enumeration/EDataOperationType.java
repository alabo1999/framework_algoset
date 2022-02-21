package com.abc.example.enumeration;

/**
 * @className		: EDataOperationType
 * @description	: 数据操作类型
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public enum EDataOperationType {
	dotNullE (0),		//空操作
	dotLoadE (1),		//加载操作
	dotAddE (2),		//添加操作
	dotRemoveE (3),	//移除操作
	dotUpdateE (4),	//更新操作
	
	;	//结束定义
	
	//枚举值对应的code值
	int code;

	// 构造函数
	private EDataOperationType(int code) {
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
	public static EDataOperationType getType(int code) {
		//返回值变量
		EDataOperationType eRet = null;
		
		for (EDataOperationType item : values()) {
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
