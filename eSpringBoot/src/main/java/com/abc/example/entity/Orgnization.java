package com.abc.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.abc.example.common.tree.ITreeNodeData;
import com.abc.example.common.utils.LogUtil;

import lombok.Data;

/**
 * @className	: Orgnization
 * @description	: 组织机构对象实体类
 * @summary		: 
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/02/14	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class Orgnization implements Serializable,ITreeNodeData{
	private static final long serialVersionUID = 1L;
	
	// 组织ID
	@Column(name = "org_id")
	private Integer orgId = 0;
	
	// 组织机构编号
	@Column(name = "org_code")
	private String orgCode = "";
	
	// 组织机构名称
	@Column(name = "org_name")
	private String orgName = "";
	
	// 组织机构全称
	@Column(name = "org_fullname")
	private String orgFullname = "";
	
	// 机构类型，1-本公司，2-政府管理部门，3-学院
	@Column(name = "org_type")
	private Byte orgType = 3;
	
	// 组织机构分类，保留
	@Column(name = "org_category")
	private Byte orgCategory = 0;
	
	// 负责人名称
	@Column(name = "leader")
	private String leader = "";
	
	// 联系人
	@Column(name = "contacts")
	private String contacts = "";
	
	// 联系电话
	@Column(name = "phone_number")
	private String phoneNumber = "";
	
	// Email
	@Column(name = "email")
	private String email = "";
	
	// 地址
	@Column(name = "address")
	private String address = "";
	
	// 邮编
	@Column(name = "zipcode")
	private String zipcode = "";
	
	// 行政区省、市、区县名称
	@Column(name = "district")
	private String district = "";
	
	// ,1-国家级、2-副国级、3-省部级、4、副省级、5-市级、6、副市级、7-区县、8-副县级、9-街道乡镇级'
	@Column(name = "district_level")
	private Byte districtLevel = 5;
	
	// 父组织ID
	@Column(name = "parent_id")
	private Integer parentId = 0;
	
	// 父组织名称
	private String parentOrgName;	
	
	// 经度
	@Column(name = "lon")
	private Double lon;
	
	// 纬度
	@Column(name = "lat")
	private Double lat;
	
	// 备注
	@Column(name = "remark")
	private String remark = "";
	
	// 操作人账号
	@Column(name = "operator_name")
	private String operatorName = "";
	
	// 记录删除标记，1-已删除
	@Column(name = "delete_flag")
	private Byte deleteFlag = 0;
	
	// 创建时间
	@Column(name = "create_time")
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	
	// 更新时间
	@Column(name = "update_time")
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;
	
	// ================ 接口重载 ======================
	
	//获取节点ID
	@Override
	public int getNodeId() {
		return orgId;
	}

	//获取节点名称
	@Override
	public String getNodeName() {
		return orgName;
	}
			
	//获取父节点ID
	@Override
	public int getParentId() {
		return parentId;
	}
	
	//对象克隆
	@Override
	public Object clone(){
		Orgnization obj = null;
        try{
        	obj = (Orgnization)super.clone();
        }catch(CloneNotSupportedException e){
        	LogUtil.error(e);
        }
        return obj;
    }
	
	@Override
	public String toString() {
		String sRet = "{"
				+ "\"orgId\":" + orgId + ","
				+ "\"orgCode\":\"" + orgCode + "\","
				+ "\"orgName\":\"" + orgName + "\","
				+ "\"orgFullname\":\"" + orgFullname + "\","
				+ "\"orgType\":" + orgType + ","
				+ "\"orgCategory\":" + orgCategory + ","
				+ "\"leader\":\"" + leader + "\","
				+ "\"contacts\":\"" + contacts + "\","
				+ "\"phoneNumber\":\"" + phoneNumber + "\","
				+ "\"email\":\"" + email + "\","
				+ "\"address\":\"" + address + "\","
				+ "\"zipcode\":\"" + zipcode + "\","
				+ "\"district\":\"" + district + "\","
				+ "\"districtLevel\":" + districtLevel + ","
				+ "\"parentId\":" + parentId + ",";
		if (lon != null) {
			sRet += "\"lon\":" + lon + ",";
		}
		if (lat != null) {
			sRet += "\"lat\":" + lat + ",";			
		}				
		sRet +=  "\"deleteFlag\":" + deleteFlag + ""
				+ "}";
		return sRet;
	}		
}
