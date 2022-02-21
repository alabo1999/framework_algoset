package com.abc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @className		: ExampleApplication
 * @description	: 
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@EnableScheduling
@SpringBootApplication()
public class ExampleApplication {

	/**
	 * @methodName		: main
	 * @description	: 
	 * @param args
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static void main(String[] args) {
		// 启动Spring Boot应用
		SpringApplication springApplication = new SpringApplication(ExampleApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		springApplication.run(args);
	}	

}
