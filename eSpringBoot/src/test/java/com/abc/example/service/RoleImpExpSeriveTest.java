package com.abc.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.abc.example.common.utils.Utility;
import com.abc.example.service.impl.RoleImpExpSeriveImpl;


/**
 * @className		: RoleImpExpSeriveTest
 * @description	: RoleImpExpSerive测试类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/12/28	1.0.0		sheng.zheng		初版
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoleImpExpSeriveTest {
	@Autowired
	MockHttpServletRequest request;
	
	@Autowired
	MockHttpServletResponse response;	
	
	@Autowired
	private RoleImpExpSeriveImpl roleImpExpSerive;
	
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
