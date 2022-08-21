package com.abc.example.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.exception.BaseException;
import com.abc.example.service.BaseService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @className		: MethodLogAspect
 * @description	: 接口出入参日志打印切面类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class MethodLogAspect {
	@Autowired
	private BaseService baseService;
	
	// 设置切点
	@Pointcut ("execution(public * com.abc.example.controller..*.*(..))" )
    public void printLog() {}
	
	/**
	 * 
	 * @methodName		: logRequestParams
	 * @description	: 前置增强，打印请求参数
	 * @param joinPoint	: JoinPoint对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
    @Before(value = "printLog()")
    public void logRequestParams(JoinPoint joinPoint) {

        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        // url
        String servletPath = request.getServletPath();
        // 日志打印
        String paramsString = "";
        for (Object object : joinPoint.getArgs()) {
            if (object instanceof MultipartFile ||
                    object instanceof HttpServletRequest ||
                    object instanceof HttpServletResponse) {
                continue;
            }
            if (object != null) {
                paramsString += object.toString();            	
            }            
        }
        log.info("Request -- path: {}; user: {}; args: {}",servletPath,baseService.getUserName(request),paramsString);                	
    }	
    
    /**
     * 
     * @methodName		: afterReturning
     * @description	: 最终增强，打印响应参数
     * @param result	: response对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @AfterReturning(value="printLog()" ,returning="result")
    public void afterReturning(Object result) {
        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        // url
        String servletPath = request.getServletPath();
        // 日志打印
        log.info("Response - path: {}; user: {}; return: {}",servletPath,baseService.getUserName(request), JSONObject.toJSONString(result));        
    }    
    
    /**
     * 
     * @methodName		: afterThrowing
     * @description	: 异常处理
     * @param joinPoint	: JoinPoint对象
     * @param e			: Exception对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
    @AfterThrowing(value="printLog()" ,throwing="e")
    public void afterThrowing(JoinPoint joinPoint,Exception e) {
        String name=joinPoint.getSignature().getName();
        String className=joinPoint.getTarget().getClass().getSimpleName();
        log.error("class {} method {}  throws exception : {}",className,name,e.getMessage());
        if(!(e instanceof BaseException)) {
            LogUtil.error(e);        	
        }
    }    
}
