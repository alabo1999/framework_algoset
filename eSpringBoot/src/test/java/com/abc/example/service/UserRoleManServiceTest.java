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
import com.abc.example.dao.UserRoleDao;
import com.abc.example.entity.UserDr;
import com.abc.example.entity.UserRole;

/**
 * @className	: UserRoleManServiceTest
 * @description	: 用户和角色关系对象管理服务测试类
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
public class UserRoleManServiceTest{
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
	
	// 用户和角色关系对象数据访问类对象
	@Autowired
	private UserRoleDao userRoleDao;
	
	// 用户和角色关系对象管理服务类对象
	@Autowired
	private UserRoleManService userRoleManService;
	
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
	 * @description	: 新增一个用户和角色关系对象
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
		UserRole item = new UserRole();
		unitTestService.setItem(item);
		System.out.println(item);
		
		// 调用服务类方法
		userRoleManService.addItem(request,item);
	}
	
	/**
	 * @methodName	: addItemsTest
	 * @description	: 新增一批用户和角色关系对象
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
		List<UserRole> itemList = new ArrayList<UserRole>();
		
		UserRole item = null;
		// 构造n条数据
		int n = 5;
		for (int i = 0; i < n; i++) {
			item = new UserRole();
			unitTestService.setItem(item);
			itemList.add(item);
		}
		System.out.println(itemList);
		
		// 调用服务类方法
		userRoleManService.addItems(request,itemList);
	}
	
	/**
	 * @methodName	: deleteItemTest
	 * @description	: 根据key删除一个用户和角色关系对象
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
	public void deleteItemTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<UserRole> itemList = userRoleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userId", "roleId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		userRoleManService.deleteItem(request,map);
	}
	
	/**
	 * @methodName	: deleteItemsTest
	 * @description	: 根据条件删除多个用户和角色关系对象
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
		List<UserRole> itemList = userRoleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleId", "userId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		userRoleManService.deleteItems(request,map);
	}
	
	/**
	 * @methodName	: queryItemsTest
	 * @description	: 根据条件分页查询用户和角色关系对象列表
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
	public void queryItemsTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<UserRole> itemList = userRoleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleId", "userId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("pagenum",1);
		map.put("pagesize",10);
		
		System.out.println(map);
		
		// 调用服务类方法
		PageInfo<UserRole> pageInfo = null;
		pageInfo = userRoleManService.queryItems(request,map);
		System.out.println(pageInfo.getList());
	}
	
	/**
	 * @methodName	: getItemTest
	 * @description	: 根据key获取一个用户和角色关系对象
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
		List<UserRole> itemList = userRoleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"userId", "roleId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		UserRole item = userRoleManService.getItem(request,map);
		System.out.println(item);
	}
	
	/**
	 * @methodName	: getItemsTest
	 * @description	: 根据条件查询用户和角色关系对象列表
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
	public void getItemsTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<UserRole> itemList = userRoleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleId", "userId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("offset",0);
		map.put("rows",20);
		
		System.out.println(map);
		
		// 调用服务类方法
		List<UserRole> itemList2 = null;
		itemList2 = userRoleManService.getItems(request,map);
		System.out.println(itemList2);
	}
}
