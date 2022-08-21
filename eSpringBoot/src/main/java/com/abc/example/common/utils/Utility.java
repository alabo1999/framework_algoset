package com.abc.example.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import com.alibaba.fastjson.JSONObject;

/**
 * @className		: Utility
 * @description	: 工具类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class Utility {
	private static final String charset = "utf-8";
	
	//========================= base64编解码 ======================================
	
	/**
	 * 
	 * @methodName		: base64Decode
	 * @description	: base64解码
	 * @param data		: 待解码的字符串
	 * @return			: 解码后的字符串
	 * @throws Exception : 解码失败，抛出异常
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    public static String base64Decode(String data) throws Exception{
		if (null == data || data.equals("")) {
		    return "";
		}
		
		return new String(Base64.decodeBase64(data.getBytes(charset)), charset);
    }
    
    /**
     * 
     * @methodName		: base64Encode
     * @description	: base64编码
     * @param data		: 待编码的字符串
     * @return			: 编码后的字符串
     * @throws Exception : 编码失败，抛出异常
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static String base64Encode(String data) throws Exception{
    	if (null == data || data.equals("")) {
	        return "";
	    }
	    return new String(Base64.encodeBase64(data.getBytes(charset)), charset);
    }
    
    /**
     * 
     * @methodName		: bytes2Base64
     * @description	: byte[]转base64
     * @param b			: 待编码的byte数组
     * @return			: 编码后的字符串
     * @throws Exception : 编码失败，抛出异常
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static String bytes2Base64(byte[] b) throws Exception{
        return Base64.encodeBase64String(b);
    }
    
    /**
     * 
     * @methodName		: base642Bytes
     * @description	: base64转byte[]
     * @param base64Str: 待解码的字符串
     * @return			: 解码后的byte数组
     * @throws Exception : 解码失败，抛出异常
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static byte[] base642Bytes(String base64Str) throws Exception{
        return Base64.decodeBase64(base64Str);
    } 
    
	//========================= 文件操作 ===========================================    
    /**
     * 
     * @methodName		: is2ByeteArray
     * @description	: 从inputStream读取数据到byte[]
     * @param is		: 输入的inputStream对象
     * @return			: byte数组
     * @throws IOException: 读取失败，抛出异常
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
	public static byte[] is2ByeteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int rc = 0;
		while((rc=is.read(buff, 0, 1024))>0) {
			// 每次读取1K字节
			baos.write(buff, 0, rc);
		}
		baos.close();
		
		return baos.toByteArray();
	}  
	
	/**
	 * 
	 * @methodName		: readTxt
	 * @description	: 读取文本文件到字符串
	 * @param file		: 文件文件
	 * @return			: 文本文件的内容
	 * @throws IOException: 读取失败，抛出异常
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String readTxt(File file) throws IOException {
	    String s = "";
	    InputStreamReader in = new InputStreamReader(new FileInputStream(file),charset);
	    BufferedReader br = new BufferedReader(in);
	    StringBuffer content = new StringBuffer();
	    while ((s=br.readLine())!=null){
	      content = content.append(s);
	    }
	    br.close();
	    
	    return content.toString();
	}
	
	/**
	 * 
	 * @methodName		: copyFile
	 * @description	: 复制文件
	 * @param src		: 源文件路径
	 * @param dest		: 目标文件路径
	 * @return			: 复制成功返回true，否则为false。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static boolean copyFile(String src, String dest) {
		String inputname = src;
		String outputname = dest;
		
		try{
			FileInputStream input  = new FileInputStream(inputname);
			FileOutputStream output = new FileOutputStream(outputname);
		
			int in  = input.read();
		
			while(in!=-1){
				output.write(in);
				in = input.read();
			}
			input.close();
			output.close();
		}catch(IOException e){
			LogUtil.error(e);
			return false;
		}
		
		return true;		
	}	
	
	/**
	 * 
	 * @methodName		: moveFile
	 * @description	: 移动文件
	 * @param src		: 源文件路径
	 * @param dest		: 目标文件路径
	 * @return			: 成功返回true，否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/27	1.0.0		sheng.zheng		初版
	 *
	 */
	public static boolean moveFile(String src, String dest) {
		// 确保源文件存在
		if (checkFileExist(src) == false) {
			return false;
		}
		// 复制文件
		if (copyFile(src,dest) == false) {
			return false;
		}
		// 删除源文件
		deleteFile(src);
		
		return true;
	}
	
	/**
	 * 
	 * @methodName		: renameFile
	 * @description	: 重命名文件
	 * @param src		: 源文件路径
	 * @param newName	: 新的文件名
	 * @param bForce	: 如果新的文件名的文件已存在，是否删除,true表示删除
	 * @return			: 成功返回true，否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/28	1.0.0		sheng.zheng		初版
	 *
	 */
	public static boolean renameFile(String src,String newName,boolean bForce) {
		boolean bRet = false;
		File file = new File(src);
		if (!file.exists()) {
			// 文件不存在
			return bRet;
		}
		// 获取原文件名
		int dirPos = src.lastIndexOf("/");
		if (dirPos == -1) {
			// 不包含目录路径
			return bRet;
		}
		String dir = src.substring(0,dirPos+1);
		String newPath = dir + newName;
		File destFile = new File(newPath);
		if (destFile.exists() && bForce) {
			// 如果目标文件存在，且强制删除
			destFile.delete();
		}
		
		// 重命名
		bRet = file.renameTo(destFile);
		
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: getFilenameWithoutSuffix
	 * @description	: 取得不含后缀的文件名
	 * @param filePath	: 文件路径
	 * @return			: 不含后缀的文件名
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String getFilenameWithoutSuffix(String filePath) {
		// 去掉目录
		String fullFilename = filePath.substring(filePath.lastIndexOf("/")+1);
		// 获取文件名
		String filename = fullFilename.substring(0,fullFilename.lastIndexOf("."));	
		return filename;
	}
	
	/**
	 * 
	 * @methodName		: checkFileExist
	 * @description	: 检查文件是否存在
	 * @param filePath	: 文件路径
	 * @return			: 文件存在返回true，否则为false。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static boolean checkFileExist(String filePath) {
		boolean bRet = false;
		File file = new File(filePath);
		if (file.exists()) {
			// 文件存在
			bRet = true;
		}	
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: deleteFile
	 * @description	: 删除文件
	 * @param filePath	: 文件路径
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			// 文件存在
			file.delete();
		}			
	}	
	
	//========================= String操作 ======================================    
	/**
	 * 
	 * @methodName		: trimLeftAndRight
	 * @description	: 去掉给定字符串的首尾指定字符 
	 * @param src		: 输入字符串
	 * @param trimStr	: 需要去掉的指定字符
	 * @return			: 去掉首尾指定字符后的字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static  String trimLeftAndRight(String src,String trimStr) {
		String regex = "^" + trimStr + "*|" + trimStr + "*$";
		return src.replaceAll(regex, "");
	}	
	
	/**
	 * 
	 * @methodName		: getURLEncoderStr
	 * @description	: 获取URLEncoder字符串
	 * @param str		: 输入字符串
	 * @return			: URLEncoder后的字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String getURLEncoderStr(String str) {
		String sRet = "";
		try {
			sRet = URLEncoder.encode(str, charset);
		}catch(Exception e) {
			LogUtil.error(e);
		}
		
		return sRet;
	}
	
	public static String getUrlDecodeStr(String str) {
		String sRet = "";
		try {
			sRet = URLDecoder.decode(str, charset);
		}catch(Exception e) {
			LogUtil.error(e);
		}
		
		return sRet;		
	}	
	
	/**
	 * 
	 * @methodName		: parseThirdResponse
	 * @description	: 解析第三方响应字符串
	 * @param content	: 响应字符串
	 * @return			: JSON对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static JSONObject parseThirdResponse(String content) {
		JSONObject joResult = JSONObject.parseObject(content);
		return joResult;
	}
	
	/**
	 * 
	 * @methodName		: lineToCamel
	 * @description	: 下划线转驼峰
	 * @param strInput	: 输入字符串
	 * @return			: 驼峰表示
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String lineToCamel(String strInput) {
		String camel = "";
		String str = strInput.toLowerCase();
		String[] splits = str.split("\\_");
		for (int i = 0; i <splits.length; i++) {
			String item = splits[i];
			if (i > 0) {
				item = item.substring(0,1).toUpperCase() + item.substring(1,item.length());
			}
			camel += item;
		}
		return camel;
	}

	//========================= 权限组合值解析 ======================================    	
    /**
     * 
     * @methodName		: parseRoles
     * @description	: 解析角色组合值
     * @param roles		: 按位设置的角色组合值
     * @return			: 角色ID列表
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static List<Integer> parseRoles(int roles){
    	List<Integer> roleList = new ArrayList<Integer>();

    	int newRoles = roles;
    	int bit0 = 0;
    	int roleId = 0;
    	for (int i = 0; i < 32; i++) {
    		// 如果组合值的余位都为0，则跳出
    		if (newRoles == 0) {
    			break;
    		}
    		
    		// 取得最后一位
    		bit0 = newRoles & 0x01;
    		if (bit0 == 1) {
    			// 如果该位为1，左移i位
    			roleId = 1 << i;
    			roleList.add(roleId);
    		}
    		
    		// 右移一位
    		newRoles = newRoles >> 1;
    	}
    	return roleList;
    }
    
    /**
     * 
     * @methodName		: getLongValue
     * @description	: 获取JSON数据中Long类型值
     * @param params	: Map类型
     * @param fieldName	: 字段名
     * @return			: Long类型值
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static Long getLongValue(Map<String,Object> params,String fieldName) {
		Object objId = params.get(fieldName);
		if (objId == null) {
			return 0L;
		}
		Long longId = 0L;
		if (objId instanceof Integer) {
			Integer iId = (Integer)objId;
			longId = iId.longValue();
		}else {
			longId = (Long)objId;
		}	
		return longId;
	}  
    
    /**
     * 
     * @methodName		: calcIOU
     * @description	: 计算两个矩形的IOU值（交集面积/并集面积）
     * @param r1x1		: 矩形1的左上x轴坐标值
     * @param r1y1		: 矩形1的左上y轴坐标值
     * @param r1x2		: 矩形1的右下x轴坐标值
     * @param r1y2		: 矩形1的右下y轴坐标值
     * @param r2x1		: 矩形2的左上x轴坐标值
     * @param r2y1		: 矩形2的左上y轴坐标值
     * @param r2x2		: 矩形2的右下x轴坐标值
     * @param r2y2		: 矩形2的右下y轴坐标值
     * @return			: IOU值
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    public static double calcIOU(int r1x1,int r1y1,int r1x2,int r1y2,
    		int r2x1,int r2y1,int r2x2,int r2y2) {
    	double retRet = 0;

    	// 先计算交集矩形
    	// 无交集情况
    	// x-轴无交集：矩形2在矩形1右边或左边
    	if (r2x1 >= r1x2 || r2x2 <= r1x1) {
    		return retRet;
    	}
    	// y-轴无交集：矩形2在矩形1上边或下边
    	if (r2y1 >= r1y2 || r2y2 <= r1y1) {
    		return retRet;
    	}
    	// 交集矩形
    	int r3x1 = 0;
    	int r3y1 = 0;
    	int r3x2 = 0;
    	int r3y2 = 0;
    	int r3Square = 0;
    	
    	r3x1 = Math.max(r1x1, r2x1);
    	r3y1 = Math.max(r1y1, r2y1);
    	r3x2 = Math.min(r1x2, r2x2);
    	r3y2 = Math.min(r1y2, r2y2);
    	if (r3x2 > r3x1 && r3y2 > r3y1) {
    		r3Square = calcSquare(r3x1,r3y1,r3x2,r3y2);
    	}else {
    		return retRet;
    	}
    	
    	// 计算两个矩形面积
    	int r1Square = calcSquare(r1x1,r1y1,r1x2,r1y2);
    	int r2Square = calcSquare(r2x1,r2y1,r2x2,r2y2);
    	// 并集面积
    	int unionSquare = r1Square + r2Square - r3Square;
    	
    	// IOU
    	retRet = r3Square * 1.0 / unionSquare;
    	return retRet;
    }
    
    /**
     * 
     * @methodName		: calcSquare
     * @description	: 计算面积
     * @param topleftx	: 矩形的左上x轴坐标值
     * @param toplefty	: 矩形的左上y轴坐标值
     * @param bottomrightx	: 矩形的右下x轴坐标值
     * @param bottomrighty	: 矩形的右下y轴坐标值
     * @return			: 矩形面积
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    private static int calcSquare(int topleftx,int toplefty,int bottomrightx,int bottomrighty) {
    	int retValue = 0;
    	int width = bottomrightx - topleftx; 
    	int height = bottomrighty - toplefty;
    	retValue = width * height;
    	return retValue;
    }
    
	/**
	 * 
	 * @methodName		: closeStream
	 * @description	: 关闭文件流
	 * @param in		: 可关闭的数据流对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void closeStream(Closeable in) {
		try {
			if(in != null) {
				in.close();
			}
		}catch(IOException e) {
			LogUtil.error(e);
		}
	} 
	
	/**
	 * 
	 * @methodName		: getMethodByName
	 * @description	: 根据方法名称获取方法对象
	 * @param object	: 方法所在的类对象
	 * @param methodName: 方法名
	 * @return			: Method类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static Method getMethodByName(Object object,String methodName) {
		Class<?> class1 = object.getClass();
		Method retItem = null;
		Method[] methods = class1.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method item = methods[i];
			// System.out.println(item.getName());
			if (item.getName().equals(methodName)) {
				retItem = item;
				break;
			}
		}
		return retItem;
	}	
	
	/**
	 * 
	 * @methodName		: removeKeys
	 * @description	: 根据指定key数组，移除字典对应key的项
	 * @param params	: Map<String,Object>类型的字典
	 * @param keys		: 需要移除的key数组
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void removeKeys(Map<String,Object> params,String[] keys) {
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			if (params.containsKey(key)) {
				params.remove(key);
			}
		}
	}	
}
