package com.abc.example.common.utils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;


/**
 * @className		: UnitTestUtil
 * @description	: 单元测试工具类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class UnitTestUtil {

	/**
	 * 
	 * @methodName		: setTestData
	 * @description	: 给对象设置随机测试数据
	 * @param <T>		: 泛型类型
	 * @param item		: 需要设置测试数据的泛型对象，由外部new
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static <T> void setTestData(T item){
		if(item == null) {
			return;
		}
		
		// 获取对象item的运行时的类
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) item.getClass();
		// 获取属性字段数组
		Field[] fields = clazz.getDeclaredFields();
		// 遍历所有属性字段
		for (Field field : fields) {
			// 将私有属性改为可访问
			field.setAccessible(true);
			// 获取字段类型
			String type = field.getType().getTypeName();
			// 根据数据类型，构造随机数据
			Object oVal = getRandomValueByType(type);
		    // 给对象属性设置值
		    if(oVal != null) {
		    	try {
		    		field.set(item,oVal);
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    	}
		    }									
		}				
	}
	
	/**
	 * 
	 * @methodName		: getTestDataMap
	 * @description	: 获取测试数据的字典对象
	 * @param <T>		: 泛型类型
	 * @param item		: 需要设置测试数据的泛型对象，由外部new
	 * @return			: 以字段名为key的测试数据字典对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static <T> Map<String,Object> getTestDataMap(T item) {
		if(item == null) {
			return null;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		// 先构造测试数据
		setTestData(item);
		
		// 然后，给map对象赋值
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) item.getClass();
		// 获取属性字段数组
		Field[] fields = clazz.getDeclaredFields();
		// 遍历所有属性字段
		for (Field field : fields) {
			// 将私有属性改为可访问
			field.setAccessible(true);
			// 获取字段名称
			String fieldName = field.getName();
			// 获取字段值
			Object oVal = null;
	    	try {
	    		oVal = field.get(item);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	map.put(fieldName, oVal);
		}				
		return map;
	}
	
	/**
	 * 
	 * @methodName		: getRandomValueByType
	 * @description	: 根据常用数据类型，获取随机值
	 * @param type		: 数据类型，为Field.getType().getTypeName()获取到的值
	 * @return			: 符合type要求的随机值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static Object getRandomValueByType(String type) {
		Object oVal = null;
		// 随机数生成器
		Random r = new Random();
		// 获取一个随机整数
		int iVal = r.nextInt(1000);
		// 随机日期范围：2020-01-01 - 2025-01-01，对应时间戳范围:1577808000000 - 1735660800000
		long startTime = 1577808000L;
		long endTime =   1735660800L;
		                 
		
		switch(type) {
		// 基本类型
		case "byte":
			iVal = r.nextInt(255);
			oVal = (byte)iVal;
			break;
		case "int":
			oVal = iVal;
			break;
		case "short":
			iVal = iVal & 0xFFFF;
			oVal = (short)iVal;
			break;
		case "long":
			oVal = (long)iVal;
			break;
		case "float":
			oVal = Double.valueOf(iVal + 0.1).floatValue();
			break;
		case "double":
			oVal = Double.valueOf(iVal + 0.1).doubleValue();
			break;
		case "char":
			// 取第一个字符
			oVal = RandomStringUtils.randomAlphanumeric(1).charAt(0);
			break;
		case "boolean":
			// 取2的模，来构造随机boolean值
			if(iVal % 2 == 0) {
				oVal = false;
			}else {
				oVal = true;
			}
			break;
		
		// java.lang类型
		case "java.lang.Byte":
			iVal = r.nextInt(255);
			oVal = Byte.valueOf((byte)iVal);
			break;
		case "java.lang.Integer":
			oVal = Integer.valueOf(iVal);
			break;
		case "java.lang.Short":
			iVal = iVal & 0xFFFF;
			oVal = Short.valueOf((short)iVal);
			break;
		case "java.lang.Long":
			oVal = Long.valueOf((long)iVal);
			break;
		case "java.lang.Float":
			oVal = Float.valueOf((float)(iVal + 0.1));
			break;
		case "java.lang.Double":
			oVal = Double.valueOf(iVal + 0.1);
			break;
		case "java.lang.Character":
			oVal = Character.valueOf(RandomStringUtils.randomAlphanumeric(1).charAt(0));
			break;
		case "java.lang.String":
			// 随机长度（1-10）的字符串
			iVal = r.nextInt(10) + 1;
			oVal = RandomStringUtils.randomAlphanumeric(iVal);
			break;
		case "java.lang.Boolean":
			if(iVal % 2 == 0) {
				oVal = false;
			}else {
				oVal = true;
			}			
			oVal = (Boolean)Boolean.valueOf((boolean)oVal);
			break; 
			
		// java.util包
		case "java.util.Date":
		{
			// 获取随机时间戳
			long times = getRandomTimeStamp(startTime,endTime);
			Date date = new Date(times);
			oVal = date;
		}
			break;
			
		// java.time包
		case "java.time.LocalDate":
		{
			// 获取随机时间戳
			long times = getRandomTimeStamp(startTime,endTime);
			LocalDate date = LocalDate.ofEpochDay(times);
			oVal = date;
		}
			break;
		case "java.time.LocalTime":
		{
			// 获取随机时间戳
			long times = getRandomTimeStamp(startTime,endTime);
			// 24小时,取模
			long dayMilliSeconds = 24*3600*1000; 
			long secondOfDay = times % dayMilliSeconds;
			oVal = LocalTime.ofSecondOfDay(secondOfDay);
		}
			break;
		case "java.time.LocalDateTime":
		{
			// 获取随机时间戳
			long times = getRandomTimeStamp(startTime,endTime);
			oVal = LocalDateTime.ofInstant(Instant.ofEpochMilli(times), ZoneOffset.of("+8"));
		}
			break;			
		default:
			break;
		} 		
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: getRandomTimeStamp
	 * @description	: 获取指定日期时间范围内的随机时间戳
	 * @param stDatetime: 开始日期字符串，格式为：yyyy-MM-dd hh:mm:ss
	 * @param endDatetime:结束日期字符串，格式为：yyyy-MM-dd hh:mm:ss
	 * @return			: 指定日期时间范围内的随机时间戳
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static long getRandomTimeStamp(String stDatetime,String endDatetime) {
		// 随机数生成器
		Random r = new Random();
		
		// 将日期字符串转为时间LocalDateTime
		String dateFormat = "yyyy-MM-dd hh:mm:ss";
		DateTimeFormatter df = DateTimeFormatter.ofPattern(dateFormat);
		LocalDateTime ldtStart = LocalDateTime.parse(stDatetime,df);
		LocalDateTime ldtEnd = LocalDateTime.parse(endDatetime,df);
		
		// 将LocalDateTime转为long型时间戳
		long timeStampStart = ldtStart.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		long timeStampEnd = ldtEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		// 保护性处理
		if (timeStampStart >= timeStampEnd) {
			timeStampEnd = timeStampStart + 1;
		}
				
		long stTimeStamp = timeStampStart;
		int bound = (int)(timeStampEnd - timeStampStart);
		int iVal = r.nextInt(bound);
		long times = stTimeStamp + iVal;
		
		return times;
	}
	
	/**
	 * 
	 * @methodName		: getRandomTimeStamp
	 * @description	: 获取指定时间戳范围内的随机时间戳
	 * @param timeStampStart: 开始日期时间的时间戳，单位秒
	 * @param timeStampEnd: 结束日期时间的时间戳，单位秒
	 * @return			: 指定日期时间时间戳范围内的随机时间戳
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static long getRandomTimeStamp(long timeStampStart,long timeStampEnd) {
		// 随机数生成器
		Random r = new Random();
		
		// 保护性处理
		if (timeStampStart >= timeStampEnd) {
			timeStampEnd = timeStampStart + 1;
		}
		
		long stTimeStamp = timeStampStart;
		int bound = (int)(timeStampEnd - timeStampStart);
		int iVal = r.nextInt(bound);
		long times = (stTimeStamp + iVal) * 1000;
		
		return times;		
	}
}
