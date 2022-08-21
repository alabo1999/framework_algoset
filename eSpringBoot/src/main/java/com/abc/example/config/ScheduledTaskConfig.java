package com.abc.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @className		: ScheduledTaskConfig
 * @description	: 定时任务配置类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Configuration
public class ScheduledTaskConfig implements SchedulingConfigurer{
	/**
	 * 
	 * @methodName		: configureTasks
	 * @description	: 配置定时任务
	 * @param taskRegistrar	: ScheduledTaskRegistrar对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(8);
		taskScheduler.initialize();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}
}
