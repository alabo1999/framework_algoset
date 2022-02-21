package com.abc.example.service;

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
import com.abc.example.dao.UserDao;
import com.abc.example.entity.User;
import com.abc.example.entity.UserDr;

/**
 * @className	: UserManServiceTest
 * @description	: 用户对象管理服务测试类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/06	1.0.0		sheng.zheng		初版
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserManServiceTest{
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
	
	// 全局配置服务类对象
	@Autowired
	private GlobalConfigService gcs;
	
	// 单元测试服务类对象
	private UnitTestService unitTestService;
	
	// 用户对象数据访问类对象
	@Autowired
	private UserDao userDao;
	
	// 用户对象管理服务类对象
	@Autowired
	private UserManService userManService;
	
	/**
	 * @methodName	: init
	 * @description	: 账号缓存信息初始化
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@Before
	public void init() {
		// 获取账号ID
		String accountId = accountCacheService.getId(request);
		accountCacheService.setAttribute(accountId,"username","test");
		accountCacheService.setAttribute(accountId,"userType",1);
		Map<String,UserDr> drMap = new HashMap<String,UserDr>();
		UserDr userDr = new UserDr();
		userDr.setFieldId(1);
		userDr.setDrType((byte)3);
		userDr.setFieldName("org_id");
		drMap.put("orgId", userDr);
		accountCacheService.setAttribute(accountId,"drMap",drMap);	
		
		// 设置单元测试服务类对象
		unitTestService = (UnitTestService)gcs.getDataServiceObject("UnitTestService");
	}
	
	/**
	 * @methodName	: addItemTest
	 * @description	: 新增一个用户对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void addItemTest() {
		// 构造数据
		User item = new User();
		unitTestService.setItem(item);
		System.out.println(item);
		
		// 调用服务类方法
		userManService.addItem(request,item);
	}
	
	/**
	 * @methodName	: editItemTest
	 * @description	: 根据key修改一个用户对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void editItemTest() {
		// 构造数据
		User item = new User();
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<User> itemList = userDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getItemMapByFields(item,itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		userManService.editItem(request,map);
	}
	
	/**
	 * @methodName	: deleteItemTest
	 * @description	: 根据key禁用/启用一个用户对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/07	1.0.0		sheng.zheng		初版
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
		List<User> itemList = userDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		userManService.deleteItem(request,map);
	}
	
	/**
	 * @methodName	: queryItemsTest
	 * @description	: 根据条件分页查询用户对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
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
		List<User> itemList = userDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userType", "sex", "drType", "deleteFlag", "userName", "phoneNumber", "realName", "email"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("birthStart","2020-01-01 00:00:00");
		map.put("birthEnd","2020-01-01 00:00:00");
		map.put("pagenum",1);
		map.put("pagesize",1);
		
		System.out.println(map);
		
		// 调用服务类方法
		PageInfo<User> pageInfo = null;
		pageInfo = userManService.queryItems(request,map);
		System.out.println(pageInfo.getList());
	}
	
	/**
	 * @methodName	: getItemTest
	 * @description	: 根据key获取一个用户对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/08	1.0.0		sheng.zheng		初版
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
		List<User> itemList = userDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		User item = userManService.getItem(request,map);
		System.out.println(item);
	}
	
	/**
	 * @methodName	: getItemsTest
	 * @description	: 根据条件查询用户对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/06	1.0.0		sheng.zheng		初版
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
		List<User> itemList = userDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userName", "phoneNumber", "email", "drId", "deleteFlag", "openId", "woaOpenid"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("offset",1);
		map.put("rows",1);
		map.put("userIdList",null);
		
		System.out.println(map);
		
		// 调用服务类方法
		List<User> itemList2 = null;
		itemList2 = userManService.getItems(request,map);
		System.out.println(itemList2);
	}
}
