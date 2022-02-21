package com.abc.example.common.constants;

/**
 * @className		: Constants
 * @description	: 常量类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class Constants {
	
    // 用户token过期时间：3天，单位秒
    public static final int TOKEN_EXPIRE_TIME = 3*24*3600;

    // Session无效场景下，使用的自定义HTTP头部属性名
    public static final String X_SESSIONID = "x-SessionId";
    
    // 用户token的生成密钥
    public static final String TOKEN_KEY = "-example-";
        
    // 无效的记录ID
    public static final int INVALID_ID = -1; 

    // 无效的列索引
    public static final int INVALID_COL_INDEX = -1;         
      
    // 无效值
    public static final Integer INVALID_VALUE = 0;

    // 有效值
    public static final Integer VALID_VALUE = 1;
    
    // 全部值
    public static final Integer ALL_VALUE = 0;
    
    // =================================================================
    // 账号缓存的属性key值
    
    // 用户ID
    public static final String USER_ID = "userId";
    // 用户名
    public static final String USER_NAME = "userName";
    // 用户类型
    public static final String USER_TYPE = "userType";
    // 组织ID
    public static final String ORG_ID = "orgId";
    // 角色ID列表
    public static final String ROLE_ID_LIST = "roleIdList";
    // 数据权限字典
    public static final String DR_MAP = "drMap";
    // 验证码
    public static final String VERIFY_CODE = "verifyCode";
    // token
    public static final String TOKEN = "token";
    
    
}
