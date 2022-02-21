package com.abc.example.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @className		: VerifyCodeService
 * @description	: 验证码服务类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public interface VerifyCodeService {
	
	/**
	 * 
	 * @methodName		: getImage
	 * @description	: 获取验证码图片
	 * @return			: 图片对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public BufferedImage getImage();
	
	/**
	 * 
	 * @methodName		: getVerifyCode
	 * @description	: 获取验证码文本内容
	 * @return			: 验证码字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public String getVerifyCode();
	
	/**
	 * 
	 * @methodName		: output
	 * @description	: 输出图片到输出流中
	 * @param image		: 图片对象
	 * @param out		: 输出流对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void output(BufferedImage image, OutputStream out) throws IOException;

}
