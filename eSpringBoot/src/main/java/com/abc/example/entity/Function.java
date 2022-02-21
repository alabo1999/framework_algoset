package com.abc.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;

import com.abc.example.common.tree.ITreeNodeData;
import com.abc.example.common.utils.LogUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @className		: Function
 * @description	: 操作功能对象实体类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class Function  implements Serializable,ITreeNodeData{
	private static final long serialVersionUID = 1L;

	// 功能ID
	@Column(name = "func_id")
	private Integer funcId = 0;
	
	// 功能名称
	@Column(name = "func_name")
	private String funcName = "";
	
	// 父功能ID
	@Column(name = "parent_id")
	private Integer parentId = 0;
	
	// 功能所在层级
	@Column(name = "level")
	private Byte level = 0;
	
	// 显示顺序
	@Column(name = "order_no")
	private Integer orderNo = 0;
	
	// 访问接口url
	@Column(name = "url")
	private String url = "";
	
	// dom对象的ID
	@Column(name = "dom_key")
	private String domKey = "";
	
	// 节点icon名称
	@Column(name = "img_tag")
	private String imgTag = "";
	
	// 备注
	@Column(name = "remark")
	private String remark = "";
	
	// 操作人账号
	@Column(name = "operator_name")
	private String operatorName = "";
	
	// 记录删除标记，0-正常、1-已删除
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
		return funcId;
	}

	//获取节点名称
	@Override
	public String getNodeName() {
		return funcName;
	}
			
	//获取父节点ID
	@Override
	public int getParentId() {
		return parentId;
	}
	
	//对象克隆
	@Override
	public Object clone(){
		Function obj = null;
        try{
        	obj = (Function)super.clone();
        }catch(CloneNotSupportedException e){
        	LogUtil.error(e);
        }
        return obj;
    }
	
	@Override
	public String toString() {
		return "{"
				+ "\"funcId\":" + funcId + ","
				+ "\"funcName\":\"" + funcName + "\","
				+ "\"parentId\":" + parentId + ","
				+ "\"level\":" + level + ","
				+ "\"orderNo\":" + orderNo + ","
				+ "\"url\":\"" + url + "\","
				+ "\"domKey\":\"" + domKey + "\","
				+ "\"imgTag\":\"" + imgTag + "\""
				+ "}";
	}
}
