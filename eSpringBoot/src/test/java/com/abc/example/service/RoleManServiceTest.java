package com.abc.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.constants.Constants;
import com.abc.example.common.utils.Utility;
import com.abc.example.dao.RoleDao;
import com.abc.example.entity.Role;
import com.abc.example.entity.UserDr;
import com.abc.example.service.impl.RoleManServiceImpl;

/**
 * @className	: RoleManServiceTest
 * @description	: 角色对象管理服务测试类
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
public class RoleManServiceTest{
	@Autowired
	private MockHttpServletRequest request;
	
	@Autowired
	private MockHttpServletResponse response;
//	
//	@Autowired
//	private MockHttpSession session;
	
	// 账户缓存服务类对象
	@Autowired
	private AccountCacheService accountCacheService;
	
	// 全局配置服务类对象
//	@Autowired
//	private GlobalConfigService gcs;
	
	// 单元测试服务类对象
	@Autowired
	private UnitTestService unitTestService;
	
	// 角色对象数据访问类对象
	@Autowired
	private RoleDao roleDao;
	
	// 角色对象管理服务类对象
	@Autowired
	private RoleManService roleManService;
	
	@Autowired
	private RoleManServiceImpl roleImpExpSerive;	
	
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
	 * @description	: 新增一个角色对象
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
		Role item = new Role();
		unitTestService.setItem(item);
//		item.setRoleName("测试角色");
//		item.setRemark("测试");
		System.out.println(item);
		
		// 调用服务类方法
		roleManService.addItem(request,item);
	}
	
	/**
	 * @methodName	: editItemTest
	 * @description	: 根据key修改一个角色对象
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
		Role item = new Role();
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Role> itemList = roleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getItemMapByFields(item,itemList,Fields);
		System.out.println(map);
		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("roleId", 1);
//		map.put("roleName", "新角色");
//		map.put("remark", "修改角色");
		
		// 调用服务类方法
		roleManService.editItem(request,map);
	}
	
	/**
	 * @methodName	: deleteItemTest
	 * @description	: 根据key禁用/启用一个角色对象
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
		List<Role> itemList = roleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		roleManService.deleteItem(request,map);
	}
	
	/**
	 * @methodName	: queryItemsTest
	 * @description	: 根据条件分页查询角色对象列表
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
		List<Role> itemList = roleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleName", "roleType", "deleteFlag"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("pagenum",1);
		map.put("pagesize",10);
		
		System.out.println(map);
		
		// 调用服务类方法
		PageInfo<Role> pageInfo = null;
		pageInfo = roleManService.queryItems(request,map);
		System.out.println(pageInfo.getList());
	}
	
	/**
	 * @methodName	: getItemTest
	 * @description	: 根据key获取一个角色对象
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
	public void getItemTest() {
		// 构造数据
		// 查询数据，最大rows条
		int rows = 20;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rows", rows);
		List<Role> itemList = roleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleId"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		System.out.println(map);
		
		// 调用服务类方法
		Role item = roleManService.getItem(request,map);
		System.out.println(item);
	}
	
	/**
	 * @methodName	: getItemsTest
	 * @description	: 根据条件查询角色对象列表
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
		List<Role> itemList = roleDao.selectItems(params);
		// 条件字段
		String[] Fields = new String[] {"roleType", "deleteFlag"};
		// 获取字段名到值的字典
		Map<String,Object> map = unitTestService.getFieldMapByFields(itemList,Fields);
		// 设置其它非对象属性参数
		map.put("offset",0);
		map.put("rows",20);
		
		System.out.println(map);
		
		// 调用服务类方法
		List<Role> itemList2 = null;
		itemList2 = roleManService.getItems(request,map);
		System.out.println(itemList2);
	}
	
	/**
	 * 
	 * @methodName		: importExcelFileTest
	 * @description	: importExcelFile测试方法
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/02/26	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	@Rollback(value=false)	
	public void importExcelFileTest() {
		String property = System.getProperty("user.dir");
		String filePath = property + "\\testdata\\角色数据导入.xlsx";
		File file = new File(filePath);
		FileInputStream fs = null;
		
		try {
			fs = new FileInputStream(file);
			MockMultipartFile upfile = new MockMultipartFile(file.getName(), file.getName(),
					MediaType.APPLICATION_OCTET_STREAM.toString(),fs);		
			
			roleImpExpSerive.setDebug(true);
			roleImpExpSerive.importExcelFile(request, upfile);
		}catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭文件
			Utility.closeStream(fs);
		}
		
	}
	
	@Test
	@Ignore
	public void batchImportExcelFileTest() {
		String property = System.getProperty("user.dir");
		File file = new File(property + "\\testdata\\角色数据导入.xlsx");
		FileInputStream fileInputStream = null;
		
		try {
			fileInputStream = new FileInputStream(file);
			MockMultipartFile upfile = new MockMultipartFile(file.getName(), file.getName(),
					MediaType.APPLICATION_OCTET_STREAM.toString(),fileInputStream);		
			
			roleImpExpSerive.setDebug(true);
			roleImpExpSerive.batchImportExcelFile(request, upfile);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			// 关闭文件
			Utility.closeStream(fileInputStream);
		}
		
	}	
	
	/**
	 * 
	 * @methodName		: exportExcelFileTest
	 * @description	: exportExcelFile测试方法
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/02/26	1.0.0		sheng.zheng		初版
	 *
	 */
	@Test
	@Ignore
	public void exportExcelFileTest() {
		roleImpExpSerive.setDebug(true);
		Map<String,Object> params = new HashMap<String,Object>();
		roleImpExpSerive.exportExcelFile(request, response, params);
	}	
	
	@Test
	@Ignore
	public void exportCsvFileTest() {
		roleImpExpSerive.setDebug(true);
		Map<String,Object> params = new HashMap<String,Object>();
		roleImpExpSerive.exportCsvFile(request, response, params);
		
//		BaseImportObj<Role> role = new BaseImportObj<Role>() {};
//		Type type = ((ParameterizedType)role.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        System.out.println(type);	
//        
//        System.out.println(role.getTClass());
	
	}		
	
	@Test
	@Ignore
	public void importDatasIntoDBTest() {	
//		List<String> dataRowList = new ArrayList<String>();
//		List<String> rowNoList = new ArrayList<String>();
//		for (int i = 0; i < 1000; i++) {
//			String str = "";
//			if (i % 64 == 0 && i != 0) {
//				str = i + ".1";
//			}else {
//				str = i + "";
//			}
//			dataRowList.add(str);
//			rowNoList.add(i + "");
//		}
//		try {
//			long time1 = System.currentTimeMillis();
//			roleImpExpSerive.importDatasIntoDB(request, dataRowList, rowNoList, 0);
//			long time2 = System.currentTimeMillis();
//			System.out.println(" time elapsed ." + (time2 - time1));
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
	}	
}
