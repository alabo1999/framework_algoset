package com.abc.example.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

/**
 * @className		: UploadConfig
 * @description	: 文件上传配置
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Configuration
public class UploadConfig {

	@Value("${file.uploadFolder}")
	private String uploadFolder;
	
	
    // 配置文件上传目录
    @Bean
    public MultipartConfigElement multipartConfigElement(){
    	MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
		String location = System.getProperty("user.dir") + uploadFolder;
		File file = new File(location);
		if(!file.exists()){
			file.mkdirs();
		}		
				
		multipartConfigFactory.setLocation(location);
        // 单文件大小：10M
		multipartConfigFactory.setMaxFileSize(DataSize.ofMegabytes(10));
        // 设置总上传数据总大小：20M
		multipartConfigFactory.setMaxRequestSize(DataSize.ofMegabytes(20));
		
		return multipartConfigFactory.createMultipartConfig();
    }
    
    // 获取文件上传路径
    public String getUploadPath() {
    	String location = System.getProperty("user.dir") + uploadFolder;
    	return location;
    }
    
}
