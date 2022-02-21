package com.abc.example.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @className		: Md5Util
 * @description	: MD5工具类
 * @summary		: 提供MD5相关工具函数
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class Md5Util {
	/**
	 * 
	 * @methodName		: md5Encypt
	 * @description	: 32位MD5加密
	 * @param sac		: 待加密的字符串
	 * @return			: 32位MD5加密后的大写字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    public static String md5Encypt(String sac) {
        // commons-lang3  version:3.6
        String md5 = DigestUtils.md5Hex(sac).toUpperCase();
        return md5;
    }

    /**
     * 
     * @methodName		: md516Encypt
     * @description	: 16位MD5加密
     * @param sac		: 待加密的字符串
     * @return			: 16位MD5加密后的大写字符串
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static String md516Encypt(String sac) {
        // commons-lang3  version:3.6
    	String md5 = DigestUtils.md5Hex(sac).toUpperCase();
    	String md516 = md5.substring(8, 24);
        return md516;
    }
    
    /**
     * 
     * @methodName		: apiPasswdToDBPasswd
     * @description	: 针对前端传入的密码，加盐MD5二次加密
     * @param apiPasswd: API接口传入的密码，经过一次MD5加密
     * @param salt		: 盐
     * @param key		: 密钥
     * @return			: 加盐MD5二次加密的大写字符串
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static String apiPasswdToDBPasswd(String apiPasswd,String salt,String key) {
        String str = apiPasswd.toUpperCase() + key + salt;
        return md5Encypt(str);
    }

    /**
     * 
     * @methodName		: plaintPasswdToDbPasswd
     * @description	: 根据密码明文计算加盐MD5字符串
     * @param passwd	: 密码明文
     * @param salt		: 盐
     * @param key		: 密钥
     * @return			: 加盐MD5字符串
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static String plaintPasswdToDbPasswd(String passwd,String salt,String key) {
        // 第一次加密,md5
        String onePasswd = md5Encypt(passwd);
        // 第二次加密，加盐md5
        return apiPasswdToDBPasswd(onePasswd, salt, key);
    }
}
