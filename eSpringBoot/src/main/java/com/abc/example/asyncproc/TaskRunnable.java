package com.abc.example.asyncproc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;

/**
 * @className		: TaskRunnable
 * @description	: 可被线程执行的任务对象
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/08/17	1.0.0		sheng.zheng		初版
 *
 */
public class TaskRunnable implements Runnable {
	// 任务信息
	private TaskInfo taskInfo;
	
	public TaskRunnable(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	// 获取任务ID
	public Integer getTaskId() {
		if (taskInfo != null) {
			return taskInfo.getTaskId();
		}
		return 0;
	}
		
	@Override
	public void run() {
		Object procObject = taskInfo.getProcObject();
		Method method = taskInfo.getMethod();
				
		try {
			// 使用反射方法，调用方法来处理任务
			method.invoke(procObject, taskInfo);
		}catch(BaseException e) {
			// 优先处理业务处理异常
			taskInfo.error(e.getCode(),e.getMessage());
			LogUtil.error(e);
		}catch(InvocationTargetException e) {
			taskInfo.error(ExceptionCodes.ERROR.getCode(),e.getMessage());
			LogUtil.error(e);
		}catch(IllegalAccessException e) {
			taskInfo.error(ExceptionCodes.ERROR.getCode(),e.getMessage());
			LogUtil.error(e);			
		}catch(IllegalArgumentException e) {
			taskInfo.error(ExceptionCodes.ERROR.getCode(),e.getMessage());
			LogUtil.error(e);			
		}catch(Exception e) {
			// 最后处理未知异常
			taskInfo.error(ExceptionCodes.ERROR.getCode(),e.getMessage());
			LogUtil.error(e);			
		}
	}
}
