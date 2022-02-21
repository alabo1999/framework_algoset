package com.abc.example.vo.common;

import lombok.Data;

/**
 * @className		: Additional
 * @description	: 附加信息
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class Additional {
    // 通知码，附加信息
    private int notifycode;

    // 通知码对应的消息
    private String notification;
    
    // 更新的token
    private String token;
    
    // 更新的功能权限树
    private String rights;

}
