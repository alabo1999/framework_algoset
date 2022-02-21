package com.abc.example.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.abc.example.service.VerifyCodeService;

/**
 * @className		: VerifyCodeServiceImpl
 * @description	: VerifyCodeService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService{
    private int weight = 100;           // 验证码图片的长和宽
    private int height = 40;
    private String text;                // 用来保存验证码的文本内容
    private Random r = new Random();    // 获取随机数对象
    // 字体数组
    private String[] fontNames = {"Georgia","Times New Roman","Arial"};
    // 验证码数组，避免使用肉眼不容易识别的字符，如数字1和字母l，数字0和字母O
    private String codes = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	
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
	@Override
	public BufferedImage getImage() {
        BufferedImage image = createImage();
        // 获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics(); 
        StringBuilder sb = new StringBuilder();
        // 画四个字符
        for (int i = 0; i < 4; i++)             
        {
        	// 生成随机字符
            String s = getRandomChar() + "";   
            // 添加到StringBuilder里面
            sb.append(s);                      
            // 定义字符的x坐标
            float x = i * 1.0F * weight / 4;  
            // 设置随机字体
            g.setFont(getRandomFont());           
            // 设置随机颜色
            g.setColor(getRandomColor());         
            g.drawString(s, x, height - 5);
        }
        this.text = sb.toString();
        
        // 绘制干扰线
        drawRandomLine(image);
        
        return image;		
	}
	
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
	@Override
	public String getVerifyCode() {
		return text;
	}
	
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
	@Override
	public void output(BufferedImage image, OutputStream out) throws IOException {
		ImageIO.write(image, "JPEG", out);
	}
	
	/**
	 * 
	 * @methodName		: getRandomColor
	 * @description	: 获取随机的颜色
	 * @return			: 颜色对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    private Color getRandomColor() {
    	// 构造随机颜色，颜色稍微深一点
    	int r = this.r.nextInt(224);
    	int g = this.r.nextInt(192);
    	int b = this.r.nextInt(168);
    	
        // 返回一个随机颜色
        return new Color(r, g, b);            
    }
    
    /**
     * 
     * @methodName		: getRandomFont
     * @description	: 获取随机字体
     * @return			: 字体对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private Font getRandomFont() {
    	// 获取随机的字体
        int index = r.nextInt(fontNames.length);  
        String fontName = fontNames[index];
        // 随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int style = r.nextInt(4); 
        // 随机获取字体的大小
        int size = r.nextInt(10) + 24;   
        // 返回一个随机的字体
        return new Font(fontName, style, size);   
    }    
    
    /**
     * 
     * @methodName		: getRandomChar
     * @description	: 获取随机字符
     * @return			: 一个字符
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private char getRandomChar() {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }   
    
    /**
     * 
     * @methodName		: drawRandomLine
     * @description	: 绘制随机干扰线 
     * @param image		: 图片对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private void drawRandomLine(BufferedImage image) {
    	// 定义干扰线的数量
        int num = r.nextInt(10); 
        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(weight);
            int y1 = r.nextInt(height);
            int x2 = r.nextInt(weight);
            int y2 = r.nextInt(height);
            g.setColor(getRandomColor());
            g.drawLine(x1, y1, x2, y2);
        }
    }  
    
    /**
     * 
     * @methodName		: createImage
     * @description	: 创建图片对象
     * @return			: 图片对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private BufferedImage createImage() {
        // 创建图片缓冲区
        BufferedImage image = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        // 获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        // 设置背景色随机
        g.setColor(new Color(255, 255, r.nextInt(245) + 10));
        g.fillRect(0, 0, weight, height);
        // 返回一个图片
        return image;
    }    
}
