package com.abc.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.constants.Constants;
import com.abc.example.dao.FunctionDao;
import com.abc.example.entity.Function;
import com.abc.example.entity.UserDr;

/**
 * @className	: FunctionManServiceTest
 * @description	: 功能对象管理服务测试类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FunctionManServiceTest{
	@Autowired
	private MockHttpServletRequest request;
	
//	@Autowired
//	private MockHttpServletResponse response;
//	
//	@Autowired
//	private MockHttpSession session;
	
	// 账户缓存服务类对象
	@Autowired
	private AccountCacheService accountCacheService;
	
	// 单元测试服务类对象
	@Autowired
	private UnitTestService unitTestService;
	
	// 功能对象数据访问类对象
	@Autowired
	private FunctionDao functionDao;
	
	// 功能对象管理服务类对象
	@Autowired
	private FunctionManService functionManService;
	
	/**
	 * @methodName	: init
	 * @description	: 账号缓存信息初始化
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Before
	public void init() {
		// 获取账号ID
		String accountId = accountCacheService.getId(request);
		accountCacheService.setAttribute(accountId,Constants.USER_NAME,"test");
		accountCacheService.setAttribute(accountId,Constants.USER_TYPE,2);
		Map<String,UserDr> drMap = new HashMap<String,UserDr>();
		UserDr userDr = new UserDr();
		userDr.setFieldId(1);
		userDr.setDrType((byte)3);
		drMap.put("orgId", userDr);
		accountCacheService.setAttribute(accountId,Constants.DR_MAP,drMap);			
	}
	
	/**
	 * @methodName	: addItemTest
	 * @description	: 新增一个功能对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void addItemTest() {
		// 构造数据
		Function item = new Function();
		unitTestService.setItem(item);
		System.out.println(item);
		
		// 调用服务类方法
		functionManService.addItem(request,item);
	}
	
	/**
	 * @methodName	: addItemsTest
	 * @description	: 新增一批功能对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void addItemsTest() {
		// 构造数据
		List<Function> itemList = new ArrayList<Function>();
		
		Function item = null;
		// 构造n条数据
		int n = 5;
		for (int i = 0; i < n; i++) {
			item = new Function();
			unitTestService.setItem(item);
			itemList.add(item);
		}
		System.out.println(itemList);
		
		// 调用服务类方法
		functionManService.addItems(request,itemList);
	}
	
	/**
	 * @methodName	: editItemTest
	 * @description	: 根据key修改一个功能对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void editItemTest() {
		// 构造数据
		Function item = new Function();
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Function> itemList = functionDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"funcId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getItemMapByFields(item,itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		functionManService.editItem(request,map);
	}
	
	/**
	 * @methodName	: deleteItemTest
	 * @description	: 根据key禁用/启用一个功能对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void deleteItemTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Function> itemList = functionDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"funcId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		functionManService.deleteItem(request,map);
	}
	
	/**
	 * @methodName	: deleteItemsTest
	 * @description	: 根据条件删除多个功能对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void deleteItemsTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Function> itemList = functionDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"parentId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		functionManService.deleteItems(request,map);
	}
	
	/**
	 * @methodName	: queryItemsTest
	 * @description	: 根据条件分页查询功能对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void queryItemsTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Function> itemList = functionDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"deleteFlag", "funcName", "url", "domKey", "parentId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("pagenum",1);
		map.put("pagesize",1);
		
		System.out.println(map);
		
		// 调用服务类方法
		PageInfo<Function> pageInfo = null;
		pageInfo = functionManService.queryItems(request,map);
		System.out.println(pageInfo.getList());
	}
	
	/**
	 * @methodName	: getItemTest
	 * @description	: 根据key获取一个功能对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void getItemTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Function> itemList = functionDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"funcId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		Function item = functionManService.getItem(request,map);
		System.out.println(item);
	}
	
	/**
	 * @methodName	: getItemsTest
	 * @description	: 根据条件查询功能对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void getItemsTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Function> itemList = functionDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"deleteFlag"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("offset",1);
		map.put("rows",1);
		
		System.out.println(map);
		
		// 调用服务类方法
		List<Function> itemList2 = null;
		itemList2 = functionManService.getItems(request,map);
		System.out.println(itemList2);
	}
}
