package com.abc.example.vo;

import com.abc.example.common.impexp.BaseExportObj;
import com.abc.example.entity.Role;

/**
 * @className		: RoleExpObj
 * @description	: 角色导出对象
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class RoleExpObj extends BaseExportObj {

    /**
     * 
     * @methodName		: getFieldValue
     * @description	: 取得指定字段名称的字段值
     * @param <T>		: 泛型T
     * @param item		: 输入Role类型对象
     * @param fieldName	: 字段名称
     * @return			: 字段值
     * @history		:
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks                   
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     *
     */
	@Override
	public <T> Object getFieldValue(T item,String fieldName) {
		Role role = (Role)item;
		Object retValue = null;
		switch(fieldName) {
		case "roleId":
			// 计算字段示例
			retValue = role.getRoleId() + 10;
			break;
		default:
			// 其它字段，调用父类方法
			retValue = super.getFieldValue(item, fieldName);
			break;
		}
		return retValue;
	}
}
