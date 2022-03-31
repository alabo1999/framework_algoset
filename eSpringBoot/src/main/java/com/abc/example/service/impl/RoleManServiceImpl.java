package com.abc.example.service.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.abc.example.common.impexp.BaseExportObj;
import com.abc.example.common.impexp.BaseImportObj;
import com.abc.example.common.impexp.CsvExportHandler;
import com.abc.example.common.impexp.ExcelExportHandler;
import com.abc.example.common.impexp.ExcelImportHandler;
import com.abc.example.common.impexp.ImpExpFieldDef;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.common.utils.ObjListUtil;
import com.abc.example.common.utils.Utility;
import com.abc.example.config.UploadConfig;
import com.abc.example.dao.RoleDao;
import com.abc.example.entity.Role;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
//import com.abc.example.service.DataRightsService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.TableCodeConfigService;
import com.abc.example.vo.RoleExpObj;
import com.abc.example.service.RoleManService;

/**
 * @className	: RoleManServiceImpl
 * @description	: 角色对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/20	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class RoleManServiceImpl extends BaseService implements RoleManService{
	// 角色对象数据访问类对象
	@Autowired
	private RoleDao roleDao;
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;
	
	@Autowired
	private UploadConfig uploadConfig;	
	
	// 数据权限服务类对象
//	@Autowired
//	private DataRightsService drs;
	
	// 公共配置数据服务类对象
	@Autowired
	private GlobalConfigService gcs;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个角色对象
	 * @param request	: request对象
	 * @param item		: 角色对象
	 * @return			: 新增的角色对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Map<String,Object> addItem(HttpServletRequest request, Role item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);
		
		// 获取全局记录ID
		TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
		Long globalRecId = tccs.getTableRecId("exa_roles");
		Integer roleId = globalRecId.intValue();
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		item.setRoleId(roleId);
		item.setOperatorName(operatorName);
		
		// 插入数据
		try {
			roleDao.insertItem(item);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotRoleE, null, item, EDataOperationType.dotAddE);
		
		// 构造返回值
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId", roleId);
		
		return map;
	}
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个角色对象
	 * @param request	: request对象
	 * @param params	: 角色对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void editItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "editItem", params);
		
		// 获取对象
		Integer roleId = (Integer)params.get("roleId");
		Role oldItem = roleDao.selectItemByKey(roleId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			roleDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		// Role newItem = roleDao.selectItemByKey(roleId);
		// cdcs.cacheObjectChanged(ECacheObjectType.cotRoleE, oldItem, newItem, EDataOperationType.dotUpdateE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个角色对象
	 * @param request	: request对象
	 * @param params	: 角色对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItem", params);
		
		// 获取对象
		Integer roleId = (Integer)params.get("roleId");
		Role oldItem = roleDao.selectItemByKey(roleId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}
		
		// 检查删除标记
		Integer deleteFlag = (Integer)params.get("deleteFlag");
		if (deleteFlag == null) {
			// 默认为停用
			deleteFlag = EDeleteFlag.dfDeletedE.getCode();
			params.put("deleteFlag", deleteFlag);
		}
		if (oldItem.getDeleteFlag() == deleteFlag.byteValue()) {
			// 不变
			return;
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			roleDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		// if (deleteFlag == EDeleteFlag.dfDeletedE.getCode()){
		// 	cdcs.cacheObjectChanged(ECacheObjectType.cotRoleE, oldItem, null, EDataOperationType.dotRemoveE);
		// }else{
		// 	oldItem.setDeleteFlag(deleteFlag.byteValue());
		// 	cdcs.cacheObjectChanged(ECacheObjectType.cotRoleE, null, oldItem, EDataOperationType.dotAddE);
		// }
		
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询角色对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"roleName"		: "",	// 角色名称，like，可选
	 * 		"roleType"		: 0,	// 角色类型，参见系统参数表，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 角色对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/21	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<Role> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		PageInfo<Role> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<Role> roleList = roleDao.selectItemsByCondition(params);
			// 分页对象
			pageInfo = new PageInfo<Role>(roleList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个角色对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"roleId"	: 0,	// 角色ID，必选
	 * 	}
	 * @return			: 角色对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Role getItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Integer roleId = (Integer)params.get("roleId");
		// 查询数据
		try {
			// 查询记录
			Role item = roleDao.selectItemByKey(roleId);
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询角色对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"roleType"		: 0,	// 角色类型，参见系统参数表，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，0-正常、1-已删除，可选
	 * 	}
	 * @return			: 角色对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/22	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<Role> getItems(HttpServletRequest request, Map<String, Object> params) {
		// 查询数据
		try {
			// 查询记录
			List<Role> itemList = roleDao.selectItems(params);
			return itemList;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName			: checkValidForParams
	 * @description			: 输入参数校验
	 * @param request		: request对象
	 * @param methodName	: 方法名称
	 * @param params		: 输入参数
	 * @history				:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2021/01/20	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// Role item = (Role)params;
			
			// 检查项: TBD
		}
		break;
		case "editItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId
			checkKeyFields(map,new String[] {"roleId"});
		}
		break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId
			checkKeyFields(map,new String[] {"roleId"});
		}
		break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: roleId
			checkKeyFields(map,new String[] {"roleId"});
		}
		break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @methodName		: importExcelFile
	 * @description	: 导入Excel文件
	 * @param request	: request对象
	 * @param upfile	: 上传文件对象
	 * @return			: 错误提示字符串列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String> importExcelFile(HttpServletRequest request,MultipartFile upfile){
		if (upfile == null) {
			throw new BaseException(ExceptionCodes.UPLOAD_NULL_FILE);
		}
		
		// 获取文件名
		String fileName = upfile.getOriginalFilename();
		// 获取文件输入流
		InputStream in = null;
		List<String> errorList = null;
		List<String[]> dataRows = null;
		List<Role> roleList = null;
		
		try {
			try {
				in = upfile.getInputStream();
			} catch(Exception e) {
				LogUtil.error(e);
				throw new BaseException(ExceptionCodes.UPLOAD_READFILE_FAILED);
			}

			// Excel文件导入处理类
			ExcelImportHandler excelImportHandler = new ExcelImportHandler();
			// 数据导入对象基类，注意，new必须加上{}，这样可以访问匿名内部类
			BaseImportObj<Role> roleImpObj = new BaseImportObj<Role>() {};	
			// 派生类
			// RoleImpObj roleImpObj = new RoleImpObj();
			
			// 设置标题行数，如为一行，使用默认值，可不设置
			// roleImpObj.setTitleRowNums(1);
			
			// 设置异常处理策略，如为异常发生时继续，使用默认值，可不设置
			// excelImportHandler.setErrorProcFalg(ImpExpConstants.CONTINUE_ON_ERROR);
			// roleImpObj.setErrorProcFlag(ImpExpConstants.CONTINUE_ON_ERROR);
			
			// 设置导入字段定义对象列表
			List<ImpExpFieldDef> fieldList = new ArrayList<ImpExpFieldDef>();
			fieldList.add(new ImpExpFieldDef("roleId","角色ID",1));
			fieldList.add(new ImpExpFieldDef("roleName","角色名称",1));
			fieldList.add(new ImpExpFieldDef("remark","备注",0));
			roleImpObj.setFieldList(fieldList);			
			
			// 读取Excel数据，一次性读入
			dataRows = excelImportHandler.importFile(in,fileName,roleImpObj.getTitleRowNums(),0);	 					
			
			// 导入数据行，一次性导入	   
			roleList = roleImpObj.importAllDatas(dataRows);
			// 汇总异常信息列表，读取阶段的异常日志放置在前部位置
			roleImpObj.insertErrorList(excelImportHandler.getErrorLog());
			// 输出的异常日志列表
			// errorList = roleImpObj.getErrorLogList();
			
			// 调试输出
			if (debug) {
				System.out.println("--读取全部数据--");									
				for(int i = 0; i < dataRows.size(); i++) {
					String[] item = dataRows.get(i);
					String strItem = LogUtil.getString(item);					
					System.out.println(strItem);									
				}
				for(int i = 0; i < roleList.size(); i++) {
					Role item = roleList.get(i);
					System.out.println(item.toString());
				}
				System.out.println("--读取Excel文件的异常信息列表--");									
				System.out.println(excelImportHandler.getErrorLog());							
				System.out.println("--导入的异常信息列表--");									
				System.out.println(errorList);				
			}
			
			
			// 下面进行入库操作
	    	// 导入对象数据去重处理，相同特征的对象只保留最后一个
	    	// fieldMap为字段名到特征字段标志值的映射表
			// 特征字段用于识别对象的字段，如对象名称、编号、手机号码等，其标志值为1，非特征字段值为0
	    	Map<String,Integer> fieldMap = new HashMap<String,Integer>();
	    	fieldMap.put("roleName", 1);
	    	fieldMap.put("remark", 0);	
	    	roleImpObj.checkAndRemoveDuplicate(roleList, fieldMap);
	    	
	    	// 批量插入对象的方法
	        Method method1 = Utility.getMethodByName(this,"insertItems");
	        // 根据特征字段修改对象的方法
	        Method method2 = Utility.getMethodByName(this,"updateItem");
	        // 新增的对象列表        
	        List<Role> addList = new ArrayList<Role>();
	        // 修改的对象列表
	        List<Role> updateList = new ArrayList<Role>();
	        
			roleImpObj.datasIntoDBMethod1(this, request,roleList, addList,updateList, method1, method2);
			// System.out.println(roleImpObj.getErrorLogList());			
			
			// 缓存数据一致性检查
			if(addList.size() + updateList.size() > 0) {
				// cdcs.cacheObjectChanged(ECacheObjectType.cotPointE, updateList, addList,EDataOperationType.dotLoadE);
			}
			
			// 最后输出错误信息列表
			return errorList;						
		}catch(Exception e) {
			// 传递异常
			throw new RuntimeException(e);
		}finally {
			// 关闭workbook和in
			Utility.closeStream(in);			
		}
	}	
	
	/**
	 * 
	 * @methodName		: batchImportExcelData
	 * @description		: 分批导入Excel数据
	 * @param request	: request对象
	 * @param upfile	: 上传文件对象
	 * @return			: 错误提示字符串列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<String> batchImportExcelFile(HttpServletRequest request,MultipartFile upfile){
		if (upfile == null) {
			throw new BaseException(ExceptionCodes.UPLOAD_NULL_FILE);
		}
		
		// 获取文件名
		// String fileName = upfile.getOriginalFilename();
		// 获取文件输入流
		InputStream in = null;
		List<String> errorList = null;
		// List<Role> roleList = null;
		
		try {
//			try {
//				in = upfile.getInputStream();
//			} catch(Exception e) {
//				LogUtil.error(e);
//				throw new BaseException(ExceptionCodes.UPLOAD_READFILE_FAILED);
//			}
//
//			// Excel文件导入处理类
//			ExcelImportHandler excelImportHandler = new ExcelImportHandler();
//			// 数据导入对象基类，注意，new必须加上{}，这样可以访问匿名内部类
//			// BaseImportObj<Role> roleImpObj = new BaseImportObj<Role>() {};	
//			// 派生类
//			RoleImpObj roleImpObj = new RoleImpObj();
//			
//			// 设置导入字段定义对象列表
//			List<ImpExpFieldDef> fieldList = new ArrayList<ImpExpFieldDef>();
//			fieldList.add(new ImpExpFieldDef("roleId","角色ID",0));
//			fieldList.add(new ImpExpFieldDef("roleName","角色名称",0));
//			fieldList.add(new ImpExpFieldDef("remark","备注",0));
//			roleImpObj.setFieldList(fieldList);
//			
//			List<String[]> dataRows = null;
//			// 读取Excel数据，分批读入
//			// 打开文件
//			excelImportHandler.openFile(in, fileName);
//
//			// 读取标题
//			Integer titleRowNums = roleImpObj.getTitleRowNums();
//			Integer sheetIdx = 0;
//			// 参数1：titleRowNums，参数2：sheetIdx
//			List<String[]> titleDatas = excelImportHandler.readTitles(titleRowNums, sheetIdx) ;
//			 if (debug) {
//				System.out.println("--读取标题行数据--");									
// 				for(int i = 0; i < titleDatas.size(); i++) {
//					String[] item = titleDatas.get(i);
//					String strItem = LogUtil.getString(item);						
//					System.out.println(strItem);									
//				}
// 			}				 
//			// 加载标题
//			roleImpObj.importTitles(titleDatas);			
//			
//			// 读取数据，每次读3行
//			Integer readRowNum = 3;
//			while(true) {
//				// 读取一批数据
//				dataRows = excelImportHandler.readNextDatas(readRowNum, sheetIdx);
//				// 导入一批数据
//				roleList = roleImpObj.importDatas(dataRows);
//				
//				// 调试输出
//				if (debug) {
//					System.out.println("--读取数据行数据--");									
//					for(int i = 0; i < dataRows.size(); i++) {
//						String[] item = dataRows.get(i);
//						String strItem = LogUtil.getString(item);		    			
//						System.out.println(strItem);	    			    			
//		    		}
//					System.out.println("--读取Excel文件的异常信息列表--");	    			    			
//					System.out.println(excelImportHandler.getErrorLog());	    		
//					System.out.println("--导入的Role对象列表--");	    			    			
//					System.out.println(roleList);	    		
//					System.out.println("--导入的异常信息列表--");	    			    			
//					System.out.println(roleImpObj.getErrorLogList());	    		
//				}
//	    		
//	    		// 处理roleList数据，如入库等
//				
//	    		// 检查是否已读完
//	    		if (dataRows.size() < readRowNum) {
//	    			// 读取的数据行数不足期望的行数，表示读完
//	    			break;
//	    		}		
//    		}
//    		// 关闭文件
//    		excelImportHandler.closeFile();
//    		
//    		// 合并异常日志列表
//    		roleImpObj.addErrorLogList(excelImportHandler.getErrorLog());
//    		errorList = roleImpObj.getErrorLogList();
			
			// 最后输出错误信息列表
			return errorList;	    				
		}catch(Exception e) {
			// 传递异常
			throw new RuntimeException(e);
		}finally {
			// 关闭workbook和in
			Utility.closeStream(in);			
		}						
	}	
	
	/**
	 * 
	 * @methodName		: exportCsvFile
	 * @description	: 导出Csv文件
	 * @param request	: request对象
	 * @param response	: response对象
	 * @param params	: 请求参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void exportCsvFile(HttpServletRequest request,HttpServletResponse response,
			Map<String,Object> params) {
		List<Role> itemList = null;
		
		// 根据查询条件params，查询数据库，获取Role对象列表
		itemList = roleDao.selectItemsByCondition(params);
		
		// 构造导出列表
		List<ImpExpFieldDef> fieldList = new ArrayList<ImpExpFieldDef>();
		fieldList.add(new ImpExpFieldDef("roleId","角色ID",0));
		fieldList.add(new ImpExpFieldDef("roleName","角色名称",0));
		fieldList.add(new ImpExpFieldDef("remark","备注",0));
		
		// 使用导出类
		BaseExportObj expObj = new BaseExportObj();
		// 设置标题数据
		expObj.setTitles(fieldList);
		// 输出标题行
		String[] arrTitles = expObj.exportTitleList();
		// 输出导出数据行
		List<String[]> dataRowList = expObj.exportDataList(itemList);
		
		// 使用Csv导出处理类
		CsvExportHandler csvExpHandler = new CsvExportHandler();
		// 导出处理
		// 临时文件路径，文件名随机
		String filename = RandomStringUtils.randomAlphanumeric(16) + ".csv";
		String csvFilePath = uploadConfig.getUploadPath() + "/" + filename;
		try {
			// 生成csv临时文件
			csvExpHandler.exportCsvFile(arrTitles, dataRowList, csvFilePath);	
			// 调用基类下载方法
			this.download(response, csvFilePath, "角色数据.csv", "octet-stream");
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.EXPORT_EXCEL_FILE_FAILED);
		}finally {
			if (!debug) {
				// 删除临时文件
				Utility.deleteFile(csvFilePath);
			}			
		}
	}
	
	/**
	 * 
	 * @methodName		: exportExcelFile
	 * @description	: 导出Excel文件
	 * @param request	: request对象
	 * @param params	: 请求参数
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void exportExcelFile(HttpServletRequest request,HttpServletResponse response,
			Map<String,Object> params) {
		List<Role> itemList = null;
		
		// 根据查询条件params，查询数据库，获取Role对象列表
		itemList = roleDao.selectItemsByCondition(params);
		
		// 构造导出列表
		List<ImpExpFieldDef> fieldList = new ArrayList<ImpExpFieldDef>();
		fieldList.add(new ImpExpFieldDef("roleId","角色ID",0));
		fieldList.add(new ImpExpFieldDef("roleName","角色名称",0));
		fieldList.add(new ImpExpFieldDef("remark","备注",0));
		
		// 使用导出类
		//BaseExportObj expObj = new BaseExportObj();
		RoleExpObj expObj = new RoleExpObj();
		// 设置标题数据
		expObj.setTitles(fieldList);
		// 输出标题行
		String[] arrTitles = expObj.exportTitleList();
		// 输出导出数据行
		List<String[]> dataRowList = expObj.exportDataList(itemList);
		
		// 使用Excel导出处理类
		ExcelExportHandler excelExpHandler = new ExcelExportHandler();
		// 导出处理
		// 临时文件路径，文件名随机
		String filename = RandomStringUtils.randomAlphanumeric(16) + ".xlsx";
		String excelFilePath = uploadConfig.getUploadPath() + "/" + filename;
		String sheetName = "角色数据";
		try {
			// 生成excel临时文件
			excelExpHandler.exportExcelFile(arrTitles, dataRowList, excelFilePath, sheetName);	
			// 调用基类下载方法
			this.download(response, excelFilePath, "角色数据.xlsx", "application/vnd.ms-excel");
		}catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.EXPORT_EXCEL_FILE_FAILED);
		}finally {
			if (!debug) {
				// 删除临时文件
				Utility.deleteFile(excelFilePath);
			}			
		}
	}	
	
	/**
	 * 
	 * @methodName		: insertItems
	 * @description	: 文件导入调用的批量插入角色对象列表方法
	 * @param <T>		: 泛型类型
	 * @param insertList: 批量插入的角色对象列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public <T> void insertItems(HttpServletRequest request,List<T> insertList) {
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 获取一批记录ID
		TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
		Long recId = tccs.getTableRecIds("exa_roles",insertList.size());
		Integer startId = Integer.valueOf(recId.intValue());
		for (int i = 0; i < insertList.size(); i++) {
			Role item = (Role)insertList.get(i);
			// 设置信息：记录ID、操作者账号
			item.setRoleId(startId + i);
			item.setOperatorName(operatorName);
		}        			
		//roleDao.insertItems((List<Role>)insertList);		
	}
	
	/**
	 * 
	 * @methodName		: updateItem
	 * @description	: 文件导入需调用的修改一个对象的方法
	 * @param <T>		: 泛型类型
	 * @param item		: 角色对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/01/09	1.0.0		sheng.zheng		初版
	 *
	 */
	public <T> void updateItem(HttpServletRequest request,T item, List<String> fieldnameList) {
		// 获取操作人账号
		String operatorName = getUserName(request);

		Map<String,Object> map = ObjListUtil.getValuesMap(item, fieldnameList);
		map.put("operatorName", operatorName);
		//roleDao.updateItems(map);
	}		
}
