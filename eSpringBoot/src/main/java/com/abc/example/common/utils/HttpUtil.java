package com.abc.example.common.utils;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSONObject;


/**
 * @className		: HttpUtil
 * @description	: HTTP访问工具类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class HttpUtil {

	/**
	 * 
	 * @methodName		: sendHttpRequest
	 * @description	: 发送HTTP请求
	 * @param httpURL	: 请求url
	 * @param httpMethod: 请求方式，为POST或GET
	 * @param headers	: 请求头，Map类型
	 * @param body		: 请求体，请求方式为POST时需要
	 * @return			: 响应结果
	 * @throws Exception
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    public static String sendHttpRequest(String httpURL, String httpMethod,
    		Map<String,String> headers, JSONObject body) throws Exception {
    	
        // 建立URL连接对象
        URL url = new URL(httpURL);
        // 创建连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // 设置headers
        for (Map.Entry<String,String> entry : headers.entrySet()) {
        	String key = entry.getKey();
        	String value = entry.getValue();
        	conn.setRequestProperty(key, value); 
        }
        
        // 设置请求的方式
        if(httpMethod.equalsIgnoreCase("POST")) {
        	conn.setRequestMethod("POST");
        }else if(httpMethod.equalsIgnoreCase("GET")) {
        	conn.setRequestMethod("GET");
        }else {
        	throw new Exception("unsupported request method");
        }
        
        // 设置需要输出
        conn.setDoOutput(true);
        // 设置是否从httpUrlConnection读入
        conn.setDoInput(true);
        
        // 发送请求到服务器
        conn.connect();
        
        BufferedWriter writer = null;
        if(body != null) {
        	writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(body.toJSONString());
            writer.close();       
        } 
                
        // 获取远程响应的内容.
        String responseContent = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("utf-8"));
        conn.disconnect();
        return responseContent;
    }       
}
