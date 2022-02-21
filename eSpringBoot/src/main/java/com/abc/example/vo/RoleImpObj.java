package com.abc.example.vo;

import com.abc.example.common.impexp.BaseImportObj;
import com.abc.example.entity.Role;


/**
 * @className		: RoleImpObj
 * @description	: 角色数据数据导入类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class RoleImpObj extends BaseImportObj<Role>{	
	    
    /**
     * 
     * @methodName		: fillData
     * @description	: 将数据设置到属性字段中，子类可重载此方法
     * @param fieldName	: 字段名称
     * @param cellData	: 对应单元格数据
     * @param rowObj	: Role类型对象
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
	@Override
    public void fillData(String fieldName, String cellData,Role rowObj) throws Exception{  
		switch(fieldName) {
		case "roleId":
			Integer roleId = 0;
			try {
				roleId = Integer.valueOf(cellData);				
			}catch(Exception e) {
				// 公式处理
				roleId = Double.valueOf(cellData).intValue();
			}
			rowObj.setRoleId(roleId);
			break;
		default:
			super.fillData(fieldName, cellData, rowObj);
			break;
		}      	    	
    }        
	
}
