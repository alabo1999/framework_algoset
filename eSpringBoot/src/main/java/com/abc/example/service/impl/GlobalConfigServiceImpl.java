package com.abc.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.example.common.utils.LogUtil;
import com.abc.example.dao.SysParameterDao;
import com.abc.example.entity.SysParameter;
import com.abc.example.service.ChangeNotifyService;
import com.abc.example.service.FunctionTreeService;
import com.abc.example.service.GlobalConfigService;
import com.abc.example.service.RoleFuncRightsService;
import com.abc.example.service.RoleService;
import com.abc.example.service.SysParameterService;
import com.abc.example.service.TableCodeConfigService;
import com.abc.example.service.UnitTestService;


/**
 * @className		: GlobalConfigServiceImpl
 * @description	: GlobalConfigService实现类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Service
public class GlobalConfigServiceImpl implements GlobalConfigService{
			
	// 数据表ID配置信息对象服务
	@Autowired	
	private TableCodeConfigService tableCodeConfigService;
	
	// 系统参数地下服务
	@Autowired
	private SysParameterService sysParameterService;
		
	// 角色对象服务
	@Autowired
	private RoleService roleService; 
	
	// 功能对象服务
	@Autowired
	private FunctionTreeService functionService;
	
	// 角色功能树服务
	@Autowired 
	private RoleFuncRightsService roleFuncRightsService;
		
	// 变更通知服务
	@Autowired 	
	private ChangeNotifyService changeNotifyService;	
	
	// 单元测试服务类对象
	@Autowired
	private UnitTestService unitTestService;
	@Autowired
	private SysParameterDao sysParameterDao;
			
	
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 加载数据 
	 * @return			: 成功返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public boolean loadData() {
		boolean bRet = true;

		try {
			// 加载数据表ID配置数据
			bRet = tableCodeConfigService.loadData();
			
			// 加载系统参数数据
			bRet = sysParameterService.loadData();
			
			// 加载角色数据
			bRet = roleService.loadData();
			
			// 加载功能树数据
			bRet = functionService.loadData();
			
			// 加载角色功能树数据
			roleFuncRightsService.setFunctionTree(functionService.getFunctionTree());
			bRet = roleFuncRightsService.loadData();
			
			// 初始化单元测试服务对象
			unitTestService.init();			
			// 查询全部系统参数
			List<SysParameter> sysParamList = sysParameterDao.selectAllItems();
			unitTestService.setEnums(sysParamList);
			
									
		}catch(Exception e) {
			LogUtil.error(e);
			bRet = false;
		}
		
		return bRet;
	}
	
	/**
	 * 
	 * @methodName		: getDataServiceObject
	 * @description	: 根据数据服务对象key，获取数据服务对象
	 * @param dsoKey	: 数据服务对象key
	 * @return			: 数据服务对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */	
	@Override
	public Object getDataServiceObject(String dsoKey) {
		Object retObj = null;
		switch(dsoKey) {
		case "SysParameterService":
			retObj = sysParameterService;
			break;	
		case "TableCodeConfigService":
			retObj = tableCodeConfigService;
			break;
		case "FunctionTreeService":
			retObj = functionService;
			break;
		case "RoleService":
			retObj = roleService;
			break;
		case "RoleFuncRightsService":
			retObj = roleFuncRightsService;
			break;	
		case "ChangeNotifyService":
			retObj = changeNotifyService;
			break;
		case "UnitTestService":
			retObj = unitTestService;
			break;
		default:
			break;
		}
		return retObj;
	}	

}
