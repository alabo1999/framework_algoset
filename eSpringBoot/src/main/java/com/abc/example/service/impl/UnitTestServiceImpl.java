package com.abc.example.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.abc.example.common.utils.Utility;
import com.abc.example.entity.SysParameter;
import com.abc.example.entity.ValueRange;
import com.abc.example.service.UnitTestService;

/**
 * @className		: UnitTestServiceImpl
 * @description	: UnitTestService实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/01/22	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class UnitTestServiceImpl implements UnitTestService {
	
	// ===========================================================
	// 属性
	
	// 各种数据类型的默认值域范围字典
	private Map<String,ValueRange> typeMap = new HashMap<String,ValueRange>();
	
	// 枚举值范围字典，key为字段名，由于系统参数多用途，字典可能包含无用的参数名
	// 另外，如果不同表使用同一个枚举名称，但含义不同，导致系统参数改变key值，将无法生效
	private Map<String,List<String>> enumMap = new HashMap<String,List<String>>();
	
	// ===========================================================
	// 属性的getter方法
	// typeMap的getter方法，typeMap为各种数据类型的默认值域范围字典
	@Override
	public Map<String,ValueRange> getTypeMap(){
		return typeMap;
	}
	
	// enumMap的getter方法，enumMap为枚举值范围字典
	@Override
	public Map<String,List<String>> getEnumMap(){
		return enumMap;
	}
	
	// ===========================================================
	// 初始化及只需调用一次的方法
	
	/**
	 * 
	 * @methodName		: init
	 * @description	: 初始化，实现各数据类型的默认值域范围设置
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void init() {
		// 设置各种数据类型的默认值域范围		
		//	1、基本类型：byte、short、int、long、float、double、char、boolean。
		typeMap.put("byte", new ValueRange("0","256"));
		typeMap.put("short", new ValueRange("0","1000"));
		typeMap.put("int", new ValueRange("0","1000"));
		typeMap.put("long", new ValueRange("0","1000"));
		typeMap.put("float", new ValueRange("0.0","1000.0"));
		typeMap.put("double", new ValueRange("0.0","1000.0"));
		// 字符，使用长度，限定1个长度
		typeMap.put("char", new ValueRange("1","2"));
		typeMap.put("boolean", new ValueRange("false","true"));

		//  2、java.lang类型：Byte、Short、Integer、Long、Float、Double、Character、String、Boolean。
		typeMap.put("Byte", new ValueRange("0","256"));
		typeMap.put("Short", new ValueRange("0","1000"));
		typeMap.put("Integer", new ValueRange("0","1000"));
		typeMap.put("Long", new ValueRange("0","1000"));
		typeMap.put("Float", new ValueRange("0.0","1000.0"));
		typeMap.put("Double", new ValueRange("0.0","1000.0"));
		// 字符，使用长度，限定1个长度
		typeMap.put("Character", new ValueRange("1","2"));
		// 字符串，长度
		typeMap.put("String", new ValueRange("3","10"));
		typeMap.put("Boolean", new ValueRange("FALSE","TRUE"));
		
		//  3、java.util类型：Date。
		typeMap.put("Date", new ValueRange("2020-01-01 00:00:00","2025-01-01 00:00:00"));

		//  4、java.time类型：LocalDate、LocalTime、LocalDateTime。
		typeMap.put("LocalDate", new ValueRange("2020-01-01","2025-01-01"));
		typeMap.put("LocalTime", new ValueRange("00:00:01","23:59:59"));
		typeMap.put("LocalDateTime", new ValueRange("2020-01-01 00:00:00","2025-01-01 00:00:00"));		
	}
	
	/**
	 * 
	 * @methodName		: setEnums
	 * @description	: 设置枚举值
	 * @param sysParamList: 系统参数列表，应该是全部系统参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setEnums(List<SysParameter> sysParamList) {
		// 遍历系统参数列表
		for (SysParameter item : sysParamList) {
			// 获取参数类别key和子项值，其中类别需要用字段名作为key
			String classKey = item.getClassKey();
			// 下划线转驼峰
			classKey = Utility.lineToCamel(classKey);
			String itemKey = item.getItemKey();
			List<String> valueList = null;
			
			if (enumMap.containsKey(classKey)) {
				// 如果类别key已存在
				valueList = enumMap.get(classKey);
			}else {
				// 如果为新类别key，创建
				valueList = new ArrayList<String>();
				// 加入字典
				enumMap.put(classKey, valueList);
			}
			
			// 添加新枚举值
			valueList.add(itemKey);				
		}
	}
	
	// ===========================================================
	// 测试类调用的方法
	
	/**
	 * 
	 * @methodName		: setDefaultRange
	 * @description	: 设置数据类型的默认值域
	 * @param type		: 数据类型字符串，支持下列值：
	 * 	1、基本类型：byte、short、int、long、float、double、char、boolean。
	 *  2、java.lang类型：Byte、Short、Integer、Long、Float、Double、Character、String、Boolean。
	 *  3、java.util类型：Date。
	 *  4、java.time类型：LocalDate、LocalTime、LocalDateTime。
	 * @param vr
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void setDefaultRange(String type,ValueRange vr) {
		if (typeMap.containsKey(type)) {
			// 如果是支持的数据类型
			ValueRange item = typeMap.get(type);
			// 设置值域范围
			item.setLowerLimit(vr.getLowerLimit());
			item.setUpperLimit(vr.getUpperLimit());
		}
	}
	
	/**
	 * 
	 * @methodName		: setItem
	 * @description	: 按照默认规则随机设置对象各属性字段的值，适用insertItem方法
	 * @param <T>		: 泛型类型
	 * @param item		: T类型泛型对象，由外部创建对象实例
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public <T> void setItem(T item) {
		if(item == null) {
			return;
		}
		
		// 按照默认规则随机设置对象各属性字段的值
		setItemWithOption(item,null,null);
	}
	
	/**
	 * 
	 * @methodName		: setItemUseConstraint
	 * @description	: 在默认规则基础上，施加约束条件，随机设置对象各属性字段的值
	 * 					  约束条件不会修改默认设置值，适用insertItem方法
	 * @param <T>		: 泛型类型
	 * @param item		: T类型泛型对象，由外部创建对象实例
	 * @param params	: 约束条件，规定如下：
	 * 	1、key为数据类型，则value为ValueRange类型。
	 *  2、key为T中的字段名，则value可为ValueRange类型、数组[]类型、List类型，或限定值。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public <T> void setItemUseConstraint(T item,Map<String,Object> params) {
		if(item == null) {
			return;
		}
		
		// 在默认规则基础上，施加约束条件,随机设置对象各属性字段的值
		setItemWithOption(item,null,params);
		
	}
	
	/**
	 * 
	 * @methodName		: getItemMapByFields
	 * @description	: 根据指定的对象条件字段名数组，按照默认规则获取字段名到值的字典
	 * 					  Fields为条件字段名数组，适用updateItemByKey和updateItems方法
	 * @param <T>		: 泛型类型
	 * @param itemList	: T类型泛型对象列表，由外部查询得到
	 * @param Fields	: T类型泛型对象的条件字段名数组
	 * @return			: key为字段名，value为字段值的字典
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public <T> Map<String,Object> getItemMapByFields(T item,List<T> itemList,String[] Fields){
		Map<String,Object> map = new HashMap<String,Object>();
		// 按照默认规则随机设置对象各属性字段的值
		setItemWithOption(item,map,null);
		
		// 从列表中随机获取一条记录，限定指定字段名数组的值
		setItemWithList(itemList,Fields,map);
		
		return map;
	}
	
	/**
	 * 
	 * @methodName		: getItemMapByFieldsUseConstraint
	 * @description	: 在默认规则基础上，施加约束条件，随机设置对象各属性字段的值
	 * 					  Fields为条件字段名数组，适用updateItemByKey和updateItems方法
	 * @param <T>		: 泛型类型
	 * @param itemList	: T类型泛型对象列表，由外部查询得到
	 * @param Fields	: T类型泛型对象的条件字段名数组
	 * @param params	: 约束条件，规定见setItemUseConstraint
	 * @return			: key为字段名，value为字段值的字典
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public <T> Map<String,Object> getItemMapByFieldsUseConstraint(T item,List<T> itemList,
			String[] Fields,Map<String,Object> params){
		Map<String,Object> map = new HashMap<String,Object>();
		// 按照约束条件随机设置对象各属性字段的值
		setItemWithOption(item,map,params);
		
		// 从列表中随机获取一条记录，限定指定字段名数组的值
		setItemWithList(itemList,Fields,map);
		
		return map;
		
	}
	
	/**
	 * 
	 * @methodName		: getFieldMapByFields
	 * @description	: 按照默认规则获取指定范围的字段名到值的字典，适用deleteItemByKey，
	 * 					  deleteItems、selectItemsByCondition、selectItemByKey和selectItems方法
	 * @param <T>		: 泛型类型
	 * @param itemList	: T类型泛型对象列表，由外部查询得到
	 * @param Fields	: T类型泛型对象的条件字段名数组
	 * @return			: key为字段名，value为字段值的字典
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public <T> Map<String,Object> getFieldMapByFields(List<T> itemList,String[] Fields){
		Map<String,Object> map = new HashMap<String,Object>();
		
		// 从列表中随机获取一条记录，限定指定字段名数组的值
		setItemWithList(itemList,Fields,map);
		
		return map;
		
	}
	
	
	// ===========================================================
	// 根据数据类型获取随机值的方法
	
	/**
	 * 
	 * @methodName		: getRandomValueByType
	 * @description	: 获取指定数据类型的默认值域范围的随机值
	 * @param type		: 见setDefaultRange
	 * @return			: 匹配数据类型的默认值域范围的随机值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Object getRandomValueByType(String type) {
		Object oVal = null;		
		
		// 根据类型，获取默认值域范围对象
		ValueRange vr = getVrObjByType(type);
		// 根据类型和值域范围对象，获取匹配的随机值
		oVal = getRandomValueByType(type,vr);
				
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: getRandomValueByType
	 * @description	: 获取指定数据类型的指定值域范围的随机值
	 * @param type		: 见setDefaultRange
	 * @return			: 匹配数据类型的指定值域范围的随机值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Object getRandomValueByType(String type,ValueRange vr) {
		Object oVal = null;		
		
		if (vr == null) {
			// 未定义值域范围
			return oVal;
		}		
		// 获取短类型名
		String shortType = getShortTypeName(type);
		
		switch(shortType) {
		case "byte":
		case "short":
		case "int":
		case "Byte":
		case "Short":
		case "Integer":
			oVal = getRandomInt(vr,shortType);
			break;
		case "long":
		case "Long":
			oVal = getRandomLong(vr,shortType);
			break;
		case "float":
		case "doulbe":
		case "Float":
		case "Double":
			oVal = getRandomDouble(vr,shortType);
			break;
		case "char":
			oVal = RandomStringUtils.randomAlphanumeric(1).charAt(0);			
			break;
		case "Character":
			oVal = Character.valueOf(RandomStringUtils.randomAlphanumeric(1).charAt(0));			
			break;
		case "boolean":
		case "Boolean":
			oVal = getRandomBoolean(vr,shortType);
			break;
		case "String":
			oVal = getRandomString(vr,shortType);
			break;
		case "Date":
		case "LocalDate":
		case "LocalTime":
		case "LocalDateTime":
			oVal = getRandomDateTime(vr,shortType);
			break;
		default:
			break;
		}
		
		return oVal;		
	}
	
	/**
	 * 
	 * @methodName		: getVrObjByType
	 * @description	: 根据数据类型名，获取对应的值域范围对象
	 * @param type		: 数据类型，支持形如String和java.lang.String形式
	 * @return			: 值域范围对象，如果为不支持的数据类型名，则返回null
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private ValueRange getVrObjByType(String type) {
		ValueRange vr = null;
		// 取得短类型名，以支持java.lang.String形式
		String newType = getShortTypeName(type);
		if (typeMap.containsKey(newType)) {
			// 如果类型名是key
			vr = typeMap.get(newType);
		}else {
			if (typeMap.containsKey(newType)) {
				vr = typeMap.get(newType);
			}
		}
		
		return vr;		
	}
	
	/**
	 * 
	 * @methodName		: getShortTypeName
	 * @description	: 获取短类型名
	 * @param type		: 类型名，可以为String或java.lang.String形式
	 * @return			: 短类型名
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	private String getShortTypeName(String type) {
		String shortType = "";
		// 类似java.lang.String形式
		String[] splits = type.split("\\.");
		if (splits.length > 0) {
			// 取最后一个元素
			shortType = splits[splits.length - 1];
		}
		return shortType;
	}
	
	/**
	 * 
	 * @methodName		: getRandomInt
	 * @description	: 获取整型随机数
	 * @param vr		: 值域范围对象
	 * @param type		: 数据类型名，短类型名
	 * @return			: 匹配数据类型的整型随机数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomInt(ValueRange vr,String type) {
		Object oVal = null;	
		// 随机数生成器
		Random r = new Random();
		
		int lower = Integer.valueOf(vr.getLowerLimit()).intValue();
		int upper = Integer.valueOf(vr.getUpperLimit()).intValue();
		// 范围，保护性取值
		int range = Math.max(upper - lower,1);
		// 获取随机值
		int randomVal = r.nextInt(range);
		// 增加偏移量
		randomVal += lower;
		
		// 强制类型转换
		switch(type) {
		case "byte":
			oVal = (byte)randomVal;
			break;
		case "short":
			oVal = (short)randomVal;
			break;
		case "int":
			oVal = (int)randomVal;
			break;
		case "Byte":
			oVal = Byte.valueOf((byte)randomVal);
			break;
		case "Short":
			oVal = Short.valueOf((short)randomVal);
			break;
		case "Integer":
			oVal = Integer.valueOf(randomVal);
			break;
		default:
			break;
		}
		
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: getRandomLong
	 * @description	: 获取长整型随机数
	 * @param vr		: 值域范围对象
	 * @param type		: 数据类型名，短类型名
	 * @return			: 匹配数据类型的长整型随机数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomLong(ValueRange vr,String type) {
		Object oVal = null;	
		// 随机数生成器
		Random r = new Random();
		
		long lower = Long.valueOf(vr.getLowerLimit()).longValue();
		long upper = Long.valueOf(vr.getUpperLimit()).longValue();
		// 范围，保护性取值
		long range = Math.max(upper - lower,1);
		// 获取随机值
		long randomVal = (long)(r.nextDouble() * range);
		// 增加偏移量
		randomVal += lower;
		
		// 强制类型转换
		switch(type) {
		case "long":
			oVal = randomVal;
			break;
		case "Long":
			oVal = Long.valueOf(randomVal);
			break;
		default:
			break;
		}
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: getRandomDouble
	 * @description	: 获取浮点型随机数
	 * @param vr		: 值域范围对象
	 * @param type		: 数据类型名，短类型名
	 * @return			: 匹配数据类型的浮点型随机数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomDouble(ValueRange vr,String type) {
		Object oVal = null;	
		// 随机数生成器
		Random r = new Random();
		
		double lower = Double.valueOf(vr.getLowerLimit()).doubleValue();
		double upper = Double.valueOf(vr.getUpperLimit()).doubleValue();
		// 范围，保护性取值
		double range = Math.max(upper-lower, 0.1);
		// 获取随机值
		double randomVal = r.nextDouble() * range;
		// 增加偏移量
		randomVal += lower;
		
		// 强制类型转换
		switch(type) {
		case "double":
			oVal = randomVal;
			break;
		case "Double":
			oVal = Double.valueOf(randomVal);
			break;
		case "float":
			oVal = (float)randomVal;
			break;
		case "Float":
			oVal = Float.valueOf((float)randomVal);
			break;
		default:
			break;
		}
		
		return oVal;
		
	}
	
	/**
	 * 
	 * @methodName		: getRandomBoolean
	 * @description	: 获取布尔型随机数
	 * @param vr		: 值域范围对象
	 * @param type		: 数据类型名
	 * @return			: 匹配数据类型的布尔型随机数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomBoolean(ValueRange vr,String type) {
		Object oVal = null;	
		// 随机数生成器
		Random r = new Random();
		
		boolean lower = Boolean.valueOf(vr.getLowerLimit()).booleanValue();
		boolean upper = Boolean.valueOf(vr.getUpperLimit()).booleanValue();
		// 获取随机值
		int randomVal = r.nextInt(2);
		boolean bRandom = false;
		if (randomVal == 0) {
			bRandom = lower;
		}else {
			bRandom = upper;
		}		
		
		// 强制类型转换
		switch(type) {
		case "boolean":
			oVal = bRandom;
			break;
		case "Boolean":
			oVal = Boolean.valueOf(bRandom);
			break;
		default:
			break;
		}
				
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: getgetRandomString
	 * @description	: 获取随机长度字符串
	 * @param vr		: 值域范围对象
	 * @param type		: 数据类型名，短类型名
	 * @return			: 匹配数据类型的随机字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomString(ValueRange vr,String type) {
		Object oVal = null;	
		// 随机数生成器
		Random r = new Random();
		int lower = Integer.valueOf(vr.getLowerLimit()).intValue();
		int upper = Integer.valueOf(vr.getUpperLimit()).intValue();
		// 范围，保护性取值
		int range = Math.max(upper - lower,1);
		// 获取随机值
		int randomVal = r.nextInt(range);
		// 增加偏移量
		randomVal += lower;
		String str = RandomStringUtils.randomAlphanumeric(randomVal);
		
		// 强制类型转换
		switch(type) {
		case "String":
			oVal = str;
			break;
		default:
			break;
		}
				
		return oVal;	
	}
	
	/**
	 * 
	 * @methodName		: getRandomDateTime
	 * @description	: 获取随机日期时间值
	 * @param vr		: 值域范围对象
	 * @param type		: 数据类型名，短类型名
	 * @return			: 匹配数据类型的日期时间值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomDateTime(ValueRange vr,String type) {
		Object oVal = null;
		
		String lower = vr.getLowerLimit();
		String upper = vr.getUpperLimit();
		
		// 将字符串类型日期时间转换为时间戳
		// 日期时间格式
		DateTimeFormatter df = null;
		String dateFormat = "";
		long longStart = 0;
		long longEnd = 0;
		
		switch(type) {
		case "LocalDate":
		{
			dateFormat = "yyyy-MM-dd";
			df = DateTimeFormatter.ofPattern(dateFormat);	
			// 字符串转LocalDate
			LocalDate ldStart = LocalDate.parse(lower, df);
			LocalDate ldEnd = LocalDate.parse(upper, df);
			// 相对于1970-01-01的天数
			longStart = ldStart.toEpochDay();
			longEnd = ldEnd.toEpochDay();
		}
			break;
		case "LocalTime":
		{
			dateFormat = "HH:mm:ss";
			df = DateTimeFormatter.ofPattern(dateFormat);
			// 字符串转LocalTime
			LocalTime ltStart = LocalTime.parse(lower, df);
			LocalTime ltEnd = LocalTime.parse(upper, df);
			// LocalTime转1天内的秒数			
			longStart = ltStart.toSecondOfDay();
			longEnd = ltEnd.toSecondOfDay();
			
		}
			break;	
		case "LocalDateTime":
		case "Date":
		{
			dateFormat = "yyyy-MM-dd HH:mm:ss";
			df = DateTimeFormatter.ofPattern(dateFormat);
			// 字符串转LocalDateTime
			LocalDateTime ldtStart = LocalDateTime.parse(lower, df);
			LocalDateTime ldtEnd = LocalDateTime.parse(upper, df);
			// LocalDateTime转时间戳，毫秒数
			longStart = ldtStart.toInstant(ZoneOffset.of("+8")).toEpochMilli();
			longEnd = ldtEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		}
			break;
		default:
			break;
		}		
		
		// 构造值域范围对象
		ValueRange newVr = new ValueRange(String.valueOf(longStart),String.valueOf(longEnd));
		long randomVal = (long)getRandomLong(newVr,"long");
		
		// 强制类型转换
		switch(type) {
		case "LocalDate":
			// randomVal的单位为天
			oVal = LocalDate.ofEpochDay(randomVal);
			break;
		case "LocalTime":
			// randomVal的单位为秒
			oVal = LocalTime.ofSecondOfDay(randomVal);			
			break;
		case "LocalDateTime":
			// randomVal的单位为毫秒
			oVal = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomVal), ZoneOffset.of("+8"));
			break;
		case "Date":
			// randomVal的单位为毫秒
			Date date = new Date(randomVal);
			oVal = date;			
			break;			
		default:
			break;
		}
		
		return oVal;					
	}
	
	/**
	 * 
	 * @methodName		: getRandomIndex
	 * @description	: 取得指定长度数组或列表的随机下标
	 * @param len		: 长度
	 * @return			: 随机下标值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	private int getRandomIndex(int len) {
		// 随机数生成器
		Random r = new Random();
		int randomVal = r.nextInt(len);
		return randomVal;		
	}
	
	// ===========================================================
	// 反射相关的方法
	
	/**
	 * 
	 * @methodName		: isSupportedType
	 * @description	: 判断给定类型名是否支持的数据类型名
	 * @param type		: 数据类型名
	 * @return			: 支持为true，否则为false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	private boolean isSupportedType(String type) {
		boolean bRet = false;
		ValueRange item = getVrObjByType(type);
		if (item != null) {
			bRet = true;
		}
		
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: setItemWithOption
	 * @description	: 设置对象的属性值，允许有限制或没有限制
	 * @param item		: 类对象
	 * @param fieldMap	: 字段键值字典，如果不为null，则输出
	 * @param params	: 约束条件，允许为null，如不是null，则规定如下：
	 * 	1、key为数据类型，则value为ValueRange类型。
	 *  2、key为item中的属性名，则value可为ValueRange类型、数组[]类型、List类型，或限定值。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	private void setItemWithOption(Object item,Map<String,Object> fieldMap,Map<String,Object> params) {
		
		// 获取对象item的运行时的类
		Class<?> clazz = (Class<?>) item.getClass();
		// 获取属性字段数组
		Field[] fields = clazz.getDeclaredFields();
		String type = "";
		// 遍历所有属性字段
		for (Field field : fields) {
			try {
				// 将私有属性改为可访问
				field.setAccessible(true);
				// 获取字段类型
				type = field.getType().getTypeName();
				if (field.getType().isArray() || type.equals("java.util.List") || type.equals("java.util.Map")) {
					// 如果为数组、列表、字典，忽略
					continue;
				}
				// 判断是否为支持的数据类型
				boolean bSupport = isSupportedType(type);
				if (bSupport) {
					// 如果支持的数据类型，可以设置随机值
					// 获取字段名
					String fieldName = field.getName();
					Object oVal = getRandomValueWithOption(fieldName,type,params);
				    // 给对象属性设置值
				    if(oVal != null) {
				    	try {
				    		field.set(item,oVal);
				    		// 如果fieldMap不为null，则输出
				    		if (fieldMap != null) {
				    			fieldMap.put(fieldName, oVal);
				    		}
				    	}catch(Exception e) {
				    		e.printStackTrace();
				    	}
				    }														
				}else {
					// 嵌套对象类class或不支持的类型
					// System.out.println("field.getName():" + field.getName());
					PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                    Method method = descriptor.getReadMethod();
                    Object obj = method.invoke(item);
                    // 递归
                    setItemWithOption(obj,fieldMap,params);
				}								
			}catch(Exception e) {
	    		e.printStackTrace();
	    	}
		}					
	}
	
	/**
	 * 
	 * @methodName		: getRandomValueWithOption
	 * @description	: 获取字段在可能的约束条件下的随机值
	 * @param fieldName	: 字段名
	 * @param type		: 数据类型名，允许长类型名
	 * @param params	: 约束条件，规定如下：
	 * 	1、key为数据类型（短类型名），则value为ValueRange类型。
	 *  2、key为T中的字段名，则value可为ValueRange类型、数组[]类型、List类型，或限定值。
	 *     注意：数组、List、或限定值的数据类型与字段数据类型一致
	 * @return			: 
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object getRandomValueWithOption(String fieldName,String type,Map<String,Object> params) {
		Object oVal = null;
		String shortType = getShortTypeName(type);
		
//		if (fieldName.equals("drType")) {
//			int ii = 0;
//			ii++;
//		}
		
		try {
			// 首先检查是否使用约束条件，如果有，约束条件优先
			if (params != null) {
				// 如果有约束条件
				// 首先判断是否含义当前字段名，如果有，字段约束条件有最高优先级
				if (params.containsKey(fieldName)) {
					// 如果有，则value可为ValueRange类型、数组[]类型、List类型，或限定值
					Object value = params.get(fieldName);
					// 获取字段约束条件下的随机值
					oVal = getFieldRandomValue(shortType,value);
				}else {
					// 其次判断是否含有当前数据类型
					if (params.containsKey(shortType)) {
						// 有当前数据类型的约束条件，则value必须为ValueRange类型对象
						ValueRange vr = (ValueRange)params.get(shortType);
						// 取得指定值域范围的随机值
						oVal = getRandomValueByType(shortType,vr);	
					}					
				}				
			}
			// 如果约束条件生效
			if (oVal != null) {
				return oVal;				
			}
			
			// 其次检查是否为枚举类型，如果有，使用随机枚举值
			if (enumMap.containsKey(fieldName)) {
				// 如果为枚举类型字段名
				List<String> enumValueList = enumMap.get(fieldName);
				int len = enumValueList.size();
				// 取得随机下标
				int randomIdx = getRandomIndex(len);
				// 获取枚举值
				String sVal = enumValueList.get(randomIdx);
				// 将枚举值转为类型值
				oVal = dataTypeConv(sVal,shortType);
			}else {
				// 最后，使用数据类型对应的默认值域范围
				ValueRange vr = getVrObjByType(shortType);
				oVal = getRandomValueByType(shortType,vr);	
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: getFieldRandomValue
	 * @description	: 获取字段在约束条件下的随机值
	 * @param type		: 数据类型名，短类型名
	 * @param value		: 约束条件对象，可为ValueRange类型、数组[]类型、List类型，或限定值
	 * @return
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getFieldRandomValue(String type,Object value) {
		Object oVal = null;
		try {
			if (value instanceof ValueRange) {
				// 如果为ValueRange类型
				ValueRange vr = (ValueRange)value;
				// 取得指定值域范围的随机值
				oVal = getRandomValueByType(type,vr);
			}else if(value.getClass().isArray()) {
				// 如果为数组，取得数组长度
				int len = Array.getLength(value);				
				// 取得随机下标
				int randomIdx = getRandomIndex(len);
				// 获取对应的元素
				oVal = Array.get(value, randomIdx);
			}else if (value instanceof java.util.List) {
				// 如果为列表，取得列表长度
				Class clazz = value.getClass();
				Method sizeMethod = clazz.getDeclaredMethod("size");
				if (!sizeMethod.isAccessible()) {
					sizeMethod.setAccessible(true);					
				}				
				int len = (int)sizeMethod.invoke(value);
				// 取得随机下标
				int randomIdx = getRandomIndex(len);
				// 获取对应的元素
				Method getMethod = clazz.getDeclaredMethod("get",int.class);
				if (!sizeMethod.isAccessible()) {
					getMethod.setAccessible(true);					
				}				
				oVal = getMethod.invoke(value, randomIdx);
			}else {
				// 限定值
				oVal = value;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return oVal;		
	}
	
	/**
	 * 
	 * @methodName		: dataTypeConv
	 * @description	: 将字符串形式的枚举值按照指定类型名进行数据类型转换
	 * @param sVal		: 字符串值
	 * @param type		: 数据类型名，短类型名
	 * @return			: 匹配类型的值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	private Object dataTypeConv(String sVal,String type) {
		Object oVal = null;
		// 枚举类型，字段类最多为下列类型：byte,Byte,int,Integer,String
		switch(type) {
		case "byte":
			oVal = Byte.valueOf(sVal).byteValue();
			break;
		case "Byte":
			oVal = Byte.valueOf(sVal);
			break;
		case "int":
			oVal = Integer.valueOf(sVal).intValue();
			break;
		case "Integer":
			oVal = Integer.valueOf(sVal);
			break;
		case "String":
			oVal = sVal;
			break;
		default:
			break;
		}
		
		return oVal;
	}
	
	/**
	 * 
	 * @methodName		: setItemWithList
	 * @description	: 根据指定字段名数组，获取从对象列表的随机抽取记录的指定字段集的对应值
	 * @param <T>		: 泛型类型
	 * @param itemList	: 对象列表
	 * @param Fields	: 字段名数组
	 * @param params	: 存放字段键值对的字段
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/23	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	private <T> void setItemWithList(List<T> itemList,String[] Fields,Map<String,Object> params) {
		// 获取列表长度
		int len = itemList.size();
		// 取得随机下标
		int randomIdx = getRandomIndex(len);
		// 获取抽取到的记录
		T randomItem = itemList.get(randomIdx);
		
		// 将randomItem的某些字段值，设置到item中
		Class<T> srcClazz = (Class<T>) randomItem.getClass();
		// 遍历条件字段名数组
		for (String fieldName : Fields) {
			try {
				// 从randomItem中获取字段值
        		Field srcField = srcClazz.getDeclaredField(fieldName);
        		srcField.setAccessible(true);
        		Object oVal = srcField.get(randomItem);
        		// 设置字段值
        		params.put(fieldName, oVal);
        		
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
