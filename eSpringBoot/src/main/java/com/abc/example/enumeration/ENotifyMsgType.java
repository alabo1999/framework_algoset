package com.abc.example.enumeration;

/**
 * @className		: ENotifyMsgType
 * @description	: 通知消息类型
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public enum ENotifyMsgType {
	nmtUserRolesChangeE (0x01),	//修改用户的角色组合值
	nmtRoleFuncChangeE (0x02),	//修改角色的功能项
	nmtFuncChangeE (0x04),			//功能项变更
	nmtUserDisabledE (0x08),		//用户禁用
	nmtUserDrtChangeE (0x10),		//用户数据权限类型改变
	nmtDrChangeE (0x20),			//修改权限相关对象集合导致数据权限变更
	nmtUserDrChangeE (0x40),		//用户组织ID改变
	nmtUserUtChangeE (0x80),		//用户类型改变
	
	;	//结束定义
	
	//枚举值对应的code值
	int code;

	// 构造函数
	private ENotifyMsgType(int code) {
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
	public static ENotifyMsgType getType(int code) {
		//返回值变量
		ENotifyMsgType eRet = null;
		
		for (ENotifyMsgType item : values()) {
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
