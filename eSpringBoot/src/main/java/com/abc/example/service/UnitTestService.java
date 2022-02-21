package com.abc.example.service;

import java.util.List;
import java.util.Map;

import com.abc.example.entity.SysParameter;
import com.abc.example.entity.ValueRange;

/**
 * @className		: UnitTestService
 * @description	: 单元测试服务类
 * @summary		: 提供单元的数据构造方法，下列为对应dao方法以及内部规则：
 * 	1、insertItem，对象的各个属性字段的随机值赋值，对于枚举类型，取自系统参数表的配置，下同。
 * 	2、insertItems，同insertItem，由外部循环调用。
 * 	3、updateItemByKey，where部分的key值取自查询数据集的随机key，set字段值构造同insertItem。
 * 	4、updateItems，where部分字段集由外部指定，值通过查询数据集随机设置，set字段值同insertItem。
 * 	5、deleteItemByKey，where部分的key值取自查询数据集的随机key。
 * 	6、deleteItems，where部分字段集由外部指定，值通过查询数据集随机设置。
 * 	7、selectItemsByCondition，where部分与对象有关的值通过查询数据集随机设置，其它字段集由外部指定。
 * 	8、selectItemByKey，where部分的key值取自查询数据集的随机key。
 * 	9、selectItems，where部分与对象有关的值通过查询数据集随机设置，其它字段集由外部指定。
 *  10、针对各种数据类型，构造随机数，允许设置随机数的范围，其中字符串只能设置长度范围。
 *  11、T类型对象的属性字段，允许对象嵌套，只支持基本类型的字段值构造，不支持复杂类型如数组、列表和字典类型。
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2022/01/22	1.0.0		sheng.zheng		初版
 *
 */
public interface UnitTestService {
	// ===========================================================
	// 属性的getter方法
	
	// typeMap的getter方法，typeMap为各种数据类型的默认值域范围字典
	public Map<String,ValueRange> getTypeMap();
	
	// enumMap的getter方法，enumMap为枚举值范围字典
	public Map<String,List<String>> getEnumMap();

	
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
	public void init();
	
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
	public void setEnums(List<SysParameter> sysParamList);	
	
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
	public void setDefaultRange(String type,ValueRange vr);
	
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
	public <T> void setItem(T item);
	
	/**
	 * 
	 * @methodName		: setItemUseConstraint
	 * @description	: 在默认规则基础上，施加约束条件，随机设置对象各属性字段的值
	 * 					  约束条件不会修改默认设置值，适用insertItem方法
	 * @param <T>		: 泛型类型
	 * @param item		: T类型泛型对象，由外部创建对象实例
	 * @param params	: 约束条件，规定如下：
	 * 	1、key为数据类型（短类型名），则value为ValueRange类型。
	 *  2、key为T中的字段名，则value可为ValueRange类型、数组[]类型、List类型，或限定值。
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	public <T> void setItemUseConstraint(T item,Map<String,Object> params);
	
	/**
	 * 
	 * @methodName		: getItemMapByFields
	 * @description	: 根据指定的对象条件字段名数组，按照默认规则获取字段名到值的字典
	 * 					  Fields为条件字段名数组，适用updateItemByKey和updateItems方法
	 * @param <T>		: 泛型类型
	 * @param item		: T类型泛型对象，由外部创建对象实例
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
	public <T> Map<String,Object> getItemMapByFields(T item,List<T> itemList,String[] Fields);
	
	/**
	 * 
	 * @methodName		: getItemMapByFieldsUseConstraint
	 * @description	: 在默认规则基础上，施加约束条件，随机设置对象各属性字段的值
	 * 					  Fields为条件字段名数组，适用updateItemByKey和updateItems方法
	 * @param <T>		: 泛型类型
	 * @param item		: T类型泛型对象，由外部创建对象实例
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
	public <T> Map<String,Object> getItemMapByFieldsUseConstraint(T item,List<T> itemList,
			String[] Fields,Map<String,Object> params);
	
	/**
	 * 
	 * @methodName		: getFieldMapByFields
	 * @description	: 按照默认规则获取指定范围的字段名到值的字典，适用deleteItemByKey，
	 * 					  deleteItems、selectItemsByCondition、selectItemByKey和selectItems方法
	 * @param <T>		: 泛型类型
	 * @param item		: T类型泛型对象，由外部创建对象实例
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
	public <T> Map<String,Object> getFieldMapByFields(List<T> itemList,String[] Fields);
	
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
	public Object getRandomValueByType(String type);
	
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
	public Object getRandomValueByType(String type,ValueRange vr);	
	
}
