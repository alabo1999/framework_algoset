package com.abc.example;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.abc.example.common.utils.LogUtil;
import com.abc.example.service.GlobalConfigService;


/**
 * @className		: ApplicationStartup
 * @description	: 应用侦听器
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
    // 全局配置管理对象，此处不能自动注入
    private GlobalConfigService globalConfigService = null;
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
    	    if(contextRefreshedEvent.getApplicationContext().getParent() == null){ 
    	    	// root application context 没有parent.
    	    	System.out.println("========一次性加载项==================");
    	    	
    	    	System.out.println("========定义全局变量==================");
    	    	// 将 ApplicationContext 转化为 WebApplicationContext
    	    	applicationContext = contextRefreshedEvent.getApplicationContext();
    	        WebApplicationContext webApplicationContext =
    	                (WebApplicationContext)applicationContext;
    	        // 从 webApplicationContext 中获取  servletContext
    	        ServletContext servletContext = webApplicationContext.getServletContext();
    	        
    	        // 加载全局配置管理对象
    	        globalConfigService = (GlobalConfigService)webApplicationContext.getBean(GlobalConfigService.class);
    	        // 加载数据
    	        boolean bRet = globalConfigService.loadData();
    	        if (false == bRet) {
    	        	System.out.println("加载全局变量失败");
    	        	return;
    	        }        
    	        //======================================================================
    	        // servletContext设置值
    	        servletContext.setAttribute("GLOBAL_CONFIG_SERVICE", globalConfigService);
    	        
    	    }
    	} catch (Exception e) {
    		LogUtil.error(e);
    	}        
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }  
}
