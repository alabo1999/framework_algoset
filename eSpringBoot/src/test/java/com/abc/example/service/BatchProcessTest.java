package com.abc.example.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.abc.example.common.impexp.BatchProcess;

/**
 * @className		: BatchProcessTest
 * @description	: 批量处理测试类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/01/20	1.0.0		sheng.zheng		初版
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BatchProcessTest {
	@Autowired
	MockHttpServletRequest request;
	
	@Test
	// 可变步长的批量处理算法测试
	public void varStepBatchProcessTest() {
		
		// 构造待处理的数据，数据类型为String
		List<String> dataRowList = new ArrayList<String>();
		int idx = 0;
		for (int i = 0; i < 1000; i++) {
			String str = "";
			if (i % 129 == 0) {
				str = i + ".1";
				idx ++;
				if (idx % 2 == 0) {
					str += "abc";
				}
			}else {
				str = i + "";
			}
			dataRowList.add(str);
		}
		
		System.out.println(dataRowList);
		
		// 调用算法
		Method method1 = getMethodByName(this,"batchProcMethod");
		Method method2 = getMethodByName(this,"singleProcMethod");
		// 用于存放正常批量处理的数据
		List<String> normalList = new ArrayList<String>();
		// 用于存放修正处理的数据
		List<String> correctList = new ArrayList<String>();
		// 调用算法
		try {
			List<String> errorList = BatchProcess.varStepBatchProcess(this, this,request,
					dataRowList,normalList, correctList, method1, method2,0,0x05);
			// 打印errorList
			System.out.println("errorList: " + errorList.toString());
			// 打印correctList
			System.out.println("correctList: " + correctList.toString());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 构造批量处理的方法
	// 将列表中字符串，批量转为整型，被反射调用，必须是public的
	public void batchProcMethod(HttpServletRequest request,List<String> subDataList) {
		for (String item : subDataList) {
			Integer.valueOf(item);
		}
	}
	
	// 构造单记录处理的方法，被反射调用，必须是public的
	public String singleProcMethod(HttpServletRequest request,Exception e,String item) {
		String errInfo = "";
		try {
			Double.valueOf(item).intValue();
		}catch(Exception ex) {
			errInfo = ex.getMessage();
		}
		
		return errInfo;
	}
	
	// 根据方法名称获取方法对象
	private Method getMethodByName(Object object,String methodName) {
		Class<?> class1 = object.getClass();
		Method retItem = null;
		Method[] methods = class1.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method item = methods[i];
			// System.out.println(item.getName());
			if (item.getName() == methodName) {
				retItem = item;
				break;
			}
		}
		return retItem;
	}	

}
