package com.abc.example.service;

//import java.util.ArrayList;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.constants.Constants;
import com.abc.example.dao.OrgnizationDao;
import com.abc.example.entity.Orgnization;
import com.abc.example.entity.UserDr;

/**
 * @className	: OrgnizationManServiceTest
 * @description	: 组织机构对象管理服务测试类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/02/14	1.0.0		sheng.zheng		初版
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrgnizationManServiceTest{
	@Autowired
	private MockHttpServletRequest request;
	
	@Autowired
	private MockHttpServletResponse response;
	
	// 账户缓存服务类对象
	@Autowired
	private AccountCacheService accountCacheService;	
	
	// 单元测试服务类对象
	@Autowired
	private UnitTestService unitTestService;
	
	// 组织机构对象数据访问类对象
	@Autowired
	private OrgnizationDao orgnizationDao;
	
	// 组织机构对象管理服务类对象
	@Autowired
	private OrgnizationManService orgnizationManService;
	
	/**
	 * @methodName	: init
	 * @description	: 账号缓存信息初始化
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
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
		accountCacheService.setAttribute(accountId,"drMap",drMap);		
	}
	
	/**
	 * @methodName	: addItemTest
	 * @description	: 新增一个组织机构对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	// @Rollback(value=false)
	public void addItemTest() {
		// 构造数据
		Orgnization item = new Orgnization();
		unitTestService.setItem(item);
		System.out.println(item);
		
		// 调用服务类方法
		orgnizationManService.addItem(request,item);
	}
	
	/**
	 * @methodName	: editItemTest
	 * @description	: 根据key修改一个组织机构对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void editItemTest() {
		// 构造数据
		Orgnization item = new Orgnization();
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Orgnization> itemList = orgnizationDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"orgId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getItemMapByFields(item,itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		orgnizationManService.editItem(request,map);
	}
	
	/**
	 * @methodName	: deleteItemTest
	 * @description	: 根据key禁用/启用一个组织机构对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
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
		List<Orgnization> itemList = orgnizationDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"orgId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		orgnizationManService.deleteItem(request,map);
	}
	
	/**
	 * @methodName	: queryItemsTest
	 * @description	: 根据条件分页查询组织机构对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
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
		List<Orgnization> itemList = orgnizationDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"orgId", "parentId", "orgType", "orgCode", "orgName", "orgFullname", "leader", "address", "district", "deleteFlag"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("orgIdList",null);
		map.put("pagenum",1);
		map.put("pagesize",10);
		
		System.out.println(map);
		
		// 调用服务类方法
		PageInfo<Orgnization> pageInfo = null;
		pageInfo = orgnizationManService.queryItems(request,map);
		System.out.println(pageInfo.getList());
	}
	
	/**
	 * @methodName	: getItemTest
	 * @description	: 根据key获取一个组织机构对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
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
		List<Orgnization> itemList = orgnizationDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"orgId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		Orgnization item = orgnizationManService.getItem(request,map);
		System.out.println(item);
	}
	
	/**
	 * @methodName	: getItemsTest
	 * @description	: 根据条件查询组织机构对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
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
		List<Orgnization> itemList = orgnizationDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"orgId", "parentId", "orgType", "orgCode", "orgName", "deleteFlag"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("offset",0);
		map.put("rows",20);
		map.put("orgIdList",null);
		
		System.out.println(map);
		
		// 调用服务类方法
		List<Orgnization> itemList2 = null;
		itemList2 = orgnizationManService.getItems(request,map);
		System.out.println(itemList2);
	}
	
	/**
	 * @methodName	: exportExcelFileTest
	 * @description	: 导出组织机构对象Excel数据文件
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=true)
	public void exportExcelFileTest() {
		Map<String,Object> params = new HashMap<String,Object>();
		// 调用服务类方法
		orgnizationManService.exportExcelFile(request,response,params);
	}
}
