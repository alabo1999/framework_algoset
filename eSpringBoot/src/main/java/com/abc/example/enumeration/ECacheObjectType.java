package com.abc.example.enumeration;

/**
 * @className		: ECacheObjectType
 * @description	: 缓存对象类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public enum ECacheObjectType {
	cotUserE (0),				// 用户
	cotUserRoleE(1),			// 用户角色关系对象
	cotUserDRE (2),			// 用户数据权限
	cotFunctionE(3),			// 功能操作对象
	cotRoleFunctionE(5),		// 角色功能关系对象
	cotRoleE(6),				// 角色对象数据
	cotSysParameterE(7),		// 系统参数对象
	cotOrgnizationE(8),		// 组织对象
	
	;	//结束定义
	
	//枚举值对应的code值
	int code;

	// 构造函数
	private ECacheObjectType(int code) {
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
	public static ECacheObjectType getType(int code) {
		//返回值变量
		ECacheObjectType eRet = null;
		
		for (ECacheObjectType item : values()) {
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
