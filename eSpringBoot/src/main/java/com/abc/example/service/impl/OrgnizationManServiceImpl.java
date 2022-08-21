package com.abc.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.abc.example.common.impexp.BaseExportObj;
import com.abc.example.common.impexp.ExcelExportHandler;
import com.abc.example.common.impexp.ImpExpFieldDef;
import com.abc.example.common.utils.LogUtil;
import com.abc.example.common.utils.Utility;
import com.abc.example.config.UploadConfig;
import com.abc.example.dao.OrgnizationDao;
import com.abc.example.entity.Orgnization;
import com.abc.example.entity.SysParameter;
import com.abc.example.enumeration.ECacheObjectType;
import com.abc.example.enumeration.EDataOperationType;
import com.abc.example.enumeration.EDeleteFlag;
import com.abc.example.exception.BaseException;
import com.abc.example.exception.ExceptionCodes;
import com.abc.example.service.BaseService;
import com.abc.example.service.CacheDataConsistencyService;
import com.abc.example.service.DataRightsService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.TableCodeConfigService;
import com.abc.example.service.OrgnizationManService;
import com.abc.example.service.OrgnizationService;
import com.abc.example.service.SysParameterService;

/**
 * @className	: OrgnizationManServiceImpl
 * @description	: 组织机构对象管理服务实现类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/02/14	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class OrgnizationManServiceImpl extends BaseService implements OrgnizationManService{
	// 组织机构对象数据访问类对象
	@Autowired
	private OrgnizationDao orgnizationDao;
	
	// 文件上传配置类对象
	@Autowired
	private UploadConfig uploadConfig;
	
	// 缓存数据一致性服务类对象
	@Autowired
	private CacheDataConsistencyService cdcs;
	
	// 数据权限服务类对象
	@Autowired
	private DataRightsService drs;
	
	// 公共配置数据服务类对象
	@Autowired
	private GlobalConfigService gcs;
	
	/**
	 * @methodName		: addItem
	 * @description		: 新增一个组织机构对象
	 * @param request	: request对象
	 * @param item		: 组织机构对象
	 * @return			: 新增的组织机构对象key
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Map<String,Object> addItem(HttpServletRequest request, Orgnization item) {
		// 输入参数校验
		checkValidForParams(request, "addItem", item);
		
		// 检查数据权限
		drs.checkUserDrByOrgId(request, item.getParentId());
		
		// 检查orgName唯一性
		checkUniqueInfo(item.getOrgName());		
		
		// 获取全局记录ID
		TableCodeConfigService tccs = (TableCodeConfigService)gcs.getDataServiceObject("TableCodeConfigService");
		Long globalRecId = tccs.getTableRecId("exa_orgnizations");
		Integer orgId = globalRecId.intValue();
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		
		// 设置信息
		item.setOrgId(orgId);
		item.setOperatorName(operatorName);
		
		// 插入数据
		try {
			orgnizationDao.insertItem(item);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotOrgnizationE, null, orgId, EDataOperationType.dotAddE);
		
		// 构造返回值
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		
		return map;
	}
	
	/**
	 * 
	 * @methodName		: checkUniqueInfo
	 * @description	: 检查名称的唯一性
	 * @param orgName	: 组织名称
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/27	1.0.0		sheng.zheng		初版
	 *
	 */
	private void checkUniqueInfo(String orgName) {
		Map<String,Object> dbParams = new HashMap<String,Object>();
		dbParams.put("orgName", orgName);
		dbParams.put("deleteFlag", (byte)0);
		List<Orgnization> itemList = orgnizationDao.selectItems(dbParams);
		if (itemList.size() > 0) {
			throw new BaseException(ExceptionCodes.UNIQUE_KEY_FAILED,"orgName="+orgName);
		}
	}		
	
	/**
	 * @methodName		: editItem
	 * @description		: 根据key修改一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void editItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "editItem", params);
		
		// 检查数据权限
		// 本组织对象
		drs.checkUserDr(request, params);
		
		// 获取对象
		Integer orgId = (Integer)params.get("orgId");
		Orgnization oldItem = orgnizationDao.selectItemByKey(orgId);
		if (oldItem == null) {
			throw new BaseException(ExceptionCodes.OBJECT_DOES_NOT_EXIST);
		}			
		
		// 父组织对象
		Integer parentId = (Integer)params.get("parentId");
		if(parentId == null) {
			parentId = oldItem.getParentId();
		}
		if (parentId.intValue() != oldItem.getParentId()) {
			// 如果父组织对象有变化，需检查对父对象的数据权限
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgId", parentId);
			drs.checkUserDr(request, map);			
		}
		Integer orgType = (Integer)params.get("orgType");
		if (orgType == null) {
			orgType = oldItem.getOrgType().intValue();
		}
		
		// 检查orgName唯一性
		String orgName = (String)params.get("orgName");
		if (orgName != null && !oldItem.getOrgName().equals(orgName)) {
			// 如果组织名称有修改
			checkUniqueInfo(orgName);			
		}
		
		// 获取操作人账号
		String operatorName = getUserName(request);
		// 设置信息
		params.put("operatorName", operatorName);
		
		// 修改数据
		try {
			orgnizationDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		cdcs.cacheObjectChanged(ECacheObjectType.cotOrgnizationE, orgId, null, EDataOperationType.dotUpdateE);
		
	}
	
	/**
	 * @methodName		: deleteItem
	 * @description		: 根据key禁用/启用一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 组织机构对象相关字段字典，除key字段必选外，其它字段均可选
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void deleteItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "deleteItem", params);
		
		// 检查数据权限
		drs.checkUserDr(request, params);
		
		// 获取对象
		Integer orgId = (Integer)params.get("orgId");
		Orgnization oldItem = orgnizationDao.selectItemByKey(orgId);
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
			orgnizationDao.updateItemByKey(params);
			
			 // 可能的数据库一致性处理
			
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.UPDATE_OBJECT_FAILED);
		}
		
		// 缓存一致性检查
		 if (deleteFlag == EDeleteFlag.dfDeletedE.getCode()){
		 	cdcs.cacheObjectChanged(ECacheObjectType.cotOrgnizationE, orgId, null, EDataOperationType.dotRemoveE);
		 }else{
		 	oldItem.setDeleteFlag(deleteFlag.byteValue());
		 	cdcs.cacheObjectChanged(ECacheObjectType.cotOrgnizationE, null, orgId, EDataOperationType.dotAddE);
		 }
		
	}
	
	/**
	 * @methodName		: queryItems
	 * @description		: 根据条件分页查询组织机构对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"parentId"		: 0,	// 父组织ID，可选
	 * 		"orgType"		: 3,	// 机构类型，1-本公司，2-政府管理部门，3-学院，可选
	 * 		"orgCode"		: "",	// 组织机构编号，like，可选
	 * 		"orgName"		: "",	// 组织机构名称，like，可选
	 * 		"orgFullname"	: "",	// 组织机构全称，like，可选
	 * 		"leader"		: "",	// 负责人名称，like，可选
	 * 		"address"		: "",	// 地址，like，可选
	 * 		"district"		: "",	// 行政区省、市、区县名称，like，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，1-已删除，可选
	 * 		"orgIdList"		: [],	// 组织ID列表，可选
	 * 		"pagenum"		: 1,	// 当前页码，可选
	 * 		"pagesize"		: 10,	// 每页记录数，可选
	 * 	}
	 * @return			: 组织机构对象分页列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/15	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public PageInfo<Orgnization> queryItems(HttpServletRequest request,
			 Map<String, Object> params) {
		PageInfo<Orgnization> pageInfo = null;
		// 查询数据
		try {
			// 分页处理
			processPageInfo(params);
			// 查询记录
			List<Orgnization> orgnizationList = orgnizationDao.selectItemsByCondition(params);
			fillRefValue(orgnizationList);
			// 分页对象
			pageInfo = new PageInfo<Orgnization>(orgnizationList);
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
		
		return pageInfo;
	}
	
	/**
	 * @methodName		: getItem
	 * @description		: 根据key获取一个组织机构对象
	 * @param request	: request对象
	 * @param params	: 请求参数，形式如下：
	 * 	{
	 * 		"orgId"	: 0,	// 组织ID，必选
	 * 	}
	 * @return			: 组织机构对象
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public Orgnization getItem(HttpServletRequest request, Map<String, Object> params) {
		// 输入参数校验
		checkValidForParams(request, "getItem", params);
		
		Integer orgId = (Integer)params.get("orgId");
		
		// 查询数据
		try {
			// 查询记录
			Orgnization item = orgnizationDao.selectItemByKey(orgId);
			fillRefValue(item);
			return item;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: getItems
	 * @description		: 根据条件查询组织机构对象列表
	 * @param request	: request对象
	 * @param params	: 查询参数，形式如下：
	 * 	{
	 * 		"offset"		: 0,	// limit记录偏移量，可选
	 * 		"rows"			: 20,	// limit最大记录条数，可选
	 * 		"orgId"			: 0,	// 组织ID，可选
	 * 		"parentId"		: 0,	// 父组织ID，可选
	 * 		"orgType"		: 3,	// 机构类型，1-本公司，2-政府管理部门，3-学院，可选
	 * 		"orgCode"		: "",	// 组织机构编号，可选
	 * 		"orgName"		: "",	// 组织机构名称，可选
	 * 		"deleteFlag"	: 0,	// 记录删除标记，1-已删除，可选
	 * 		"orgIdList"		: [],	// 组织ID列表，可选
	 * 	}
	 * @return			: 组织机构对象列表
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/16	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public List<Orgnization> getItems(HttpServletRequest request,
			 Map<String, Object> params) {
		// 查询数据
		try {
			// 查询记录
			List<Orgnization> itemList = orgnizationDao.selectItems(params);
			return itemList;
		} catch(Exception e) {
			LogUtil.error(e);
			throw new BaseException(ExceptionCodes.QUERY_OBJECT_FAILED);
		}
	}
	
	/**
	 * @methodName		: exportExcelFile
	 * @description		: 导出组织机构对象Excel数据文件
	 * @param request	: request对象
	 * @param response	: response对象
	 * @param params	: 查询参数
	 * @history			:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public void exportExcelFile(HttpServletRequest request,
			 HttpServletResponse response,Map<String, Object> params) {
		List<Orgnization> itemList = null;
		
		// 根据查询条件，查询数据库，获取组织机构对象列表
		itemList = orgnizationDao.selectItemsByCondition(params);
		if (itemList.size() == 0) {
			throw new BaseException(ExceptionCodes.EXPORT_FILE_NO_DATA);
		}	
		fillRefValue(itemList);			
		
		// 构造导出标题列表
		List<ImpExpFieldDef> fieldList = new ArrayList<ImpExpFieldDef>();
		fieldList.add(new ImpExpFieldDef("orgId","组织ID",0));
		fieldList.add(new ImpExpFieldDef("orgCode","组织编号",0));
		fieldList.add(new ImpExpFieldDef("orgName","组织名称",0));
		fieldList.add(new ImpExpFieldDef("orgFullname","组织全称",0));
		fieldList.add(new ImpExpFieldDef("orgType","组织类型",0));
		//fieldList.add(new ImpExpFieldDef("orgCategory","组织机构分类",0));
		fieldList.add(new ImpExpFieldDef("leader","负责人名称",0));
		fieldList.add(new ImpExpFieldDef("contacts","联系人",0));
		fieldList.add(new ImpExpFieldDef("phoneNumber","联系电话",0));
		fieldList.add(new ImpExpFieldDef("email","Email",0));
		fieldList.add(new ImpExpFieldDef("address","地址",0));
		fieldList.add(new ImpExpFieldDef("zipcode","邮编",0));
		fieldList.add(new ImpExpFieldDef("district","行政区",0));
		fieldList.add(new ImpExpFieldDef("districtLevel","行政级别",0));
		fieldList.add(new ImpExpFieldDef("parentId","父组织ID",0));
		fieldList.add(new ImpExpFieldDef("parentOrgName","父组织名称",0));
		fieldList.add(new ImpExpFieldDef("appId","平台应用ID",0));
		fieldList.add(new ImpExpFieldDef("lon","经度",0));
		fieldList.add(new ImpExpFieldDef("lat","纬度",0));
		fieldList.add(new ImpExpFieldDef("remark","备注",0));
		// 使用导出类
		BaseExportObj expObj = new BaseExportObj();
		// 设置标题数据
		expObj.setTitles(fieldList);
		// 输出标题行
		String[] arrTitles = expObj.exportTitleList();
		// 输出导出数据行
		List<String[]> dataRowList = expObj.exportDataList(itemList);
		
		// 进行字段值翻译
		SysParameterService sps = (SysParameterService)gcs.getDataServiceObject("SysParameterService");
		String trans = "";
		for(String[] dataRow : dataRowList) {
			String orgType = dataRow[4];
			SysParameter spItem = sps.getItemByKey("org_type", orgType, false);
			if (spItem != null) {
				trans = spItem.getItemValue();							
			}else {
				trans = "";
			}
			dataRow[4] = trans;
			
			String districtLevel = dataRow[12];
			spItem = sps.getItemByKey("district_level", districtLevel, false);
			if (spItem != null) {
				trans = spItem.getItemValue();							
			}else {
				trans = "";
			}
			dataRow[12] = trans;
		}
		
		// 使用Excel导出处理类
		ExcelExportHandler excelExpHandler = new ExcelExportHandler();
		// 导出处理
		// 临时文件路径，文件名随机
		String filename = RandomStringUtils.randomAlphanumeric(16) + ".xlsx";
		String excelFilePath = uploadConfig.getUploadPath() + "/" + filename;
		String sheetName = "组织机构对象数据";
		try {
			// 生成excel临时文件
			excelExpHandler.exportExcelFile(arrTitles, dataRowList, excelFilePath, sheetName);
			// 调用基类下载方法
			this.download(response, excelFilePath, "组织机构对象数据.xlsx", "application/vnd.ms-excel");
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
	 * @methodName			: checkValidForParams
	 * @description			: 输入参数校验
	 * @param request		: request对象
	 * @param methodName	: 方法名称
	 * @param params		: 输入参数
	 * @history				:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks
	 * ------------------------------------------------------------------------------
	 * 2022/02/14	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidForParams(HttpServletRequest request, String methodName,
			 Object params) {
		switch(methodName) {
		case "addItem":
		{
			// Orgnization item = (Orgnization)params;
			
			// 检查项: TBD
		}
		break;
		case "editItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: orgId
			checkKeyFields(map,new String[] {"orgId"});
		}
		break;
		case "deleteItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: orgId
			checkKeyFields(map,new String[] {"orgId"});
		}
		break;
		case "getItem":
		{
			Map<String,Object> map = (Map<String,Object>)params;
			
			// 检查项: orgId
			checkKeyFields(map,new String[] {"orgId"});
		}
		break;
		default:
			break;
		}
	}	
	
	// 填充itemList中的参照信息
	private void fillRefValue(List<Orgnization> itemList) {
		// 填充参照信息
		for(Orgnization item : itemList) {
			fillRefValue(item);
		}
	}
	
	// 填充item中的参照信息
	private void fillRefValue(Orgnization item) {
		// 填充参照信息
		if(item.getParentId() != 0) {
			OrgnizationService os = (OrgnizationService)gcs.getDataServiceObject("OrgnizationService");
			Orgnization orgObj = os.getItemByKey(item.getParentId(), false);
			if (orgObj != null) {
				item.setParentOrgName(orgObj.getOrgName());
			}							
		}
	}	
}
