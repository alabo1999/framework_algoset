package com.abc.example.common.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @className		: TreeNode
 * @description	: 树节点
 * @summary		: 节点数据类型，必须实现ITreeNodeData接口类的接口
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
@Data
public class TreeNode<T extends ITreeNodeData> implements Serializable {
	private static final long serialVersionUID = 1L;

	// 节点数据
	private T nodeData;
	
	// 父节点对象
	private TreeNode<T> parent;
	
	// 子节点
	private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();
	
	// 是否包含在树中，1表示包含，0表示不包含，此属性为附加属性，在完整树剪枝时使用
	private Integer isIncluded = 0;
	
	/**
	 * 
	 * @methodName		: addChildNode
	 * @description	: 添加子节点 
	 * @param childNode	: 子节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void addChildNode(TreeNode<T> childNode) {
		childNode.setParent(this);
		children.add(childNode);
	}
	
	/**
	 * 
	 * @methodName		: removeChildNode
	 * @description	: 移除子节点，如果子节点在子节点列表中，则移除，否则无影响
	 * @param childNode	: 子节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void removeChildNode(TreeNode<T> childNode) {
		children.remove(childNode);
	}
	
	/**
	 * 
	 * @methodName		: clear
	 * @description	: 移除所有子节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void clear() {
		children.clear();
	}
	
	/**
	 * 
	 * @methodName		: getPrevSibling
	 * @description	: 取得左邻节点
	 * @return			: 如果当前节点为第一个节点，则返回null，否则为前一个节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<T> getPrevSibling(){
		if (parent == null) {
			// 如果为根节点，则返回null
			return null;
		}
		
		List<TreeNode<T>> siblingList = parent.getChildren();
		TreeNode<T> node = null;
		for (int i = 0; i < siblingList.size(); i++) {
			TreeNode<T> item = siblingList.get(i);
			if (item == this) {
				// 找到当前节点
				if (i > 0) {
					// 当前节点不是第一个子节点
					// 取得前一个节点
					node = siblingList.get(i-1);
				}
				break;
			}
		}
		return node;		
	}
	
	/**
	 * 
	 * @methodName		: getNextSibling
	 * @description	: 取得右邻节点
	 * @return			: 如果当前节点为最后一个节点，则返回null，否则为后一个节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<T> getNextSibling(){
		if (parent == null) {
			// 如果为根节点，则返回null
			return null;
		}
		
		List<TreeNode<T>> siblingList = parent.getChildren();
		TreeNode<T> node = null;
		for (int i = 0; i < siblingList.size(); i++) {
			TreeNode<T> item = siblingList.get(i);
			if (item == this) {
				// 找到当前节点
				if (i < siblingList.size()-1) {
					// 当前节点不是最后一个子节点
					// 取得后一个节点
					node = siblingList.get(i+1);
				}
				break;
			}
		}
		return node;		
	}	
	
	/**
	 * 
	 * @methodName		: lookUpSubNode
	 * @description	: 在当前节点及下级节点中查找指定节点ID的节点
	 * @param nodeId	: 节点ID
	 * @return			: 如果找到，返回对应树节点，否则返回null
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<T> lookUpSubNode(int nodeId){
		TreeNode<T> node = null;

		// 检查当前节点
		if (nodeData.getNodeId() == nodeId) {
			node = this;
			return node;
		}
		
		// 遍历子节点
		for(TreeNode<T> item : children) {			
			node = item.lookUpSubNode(nodeId);
			if (node != null) {
				// 如果节点非空，表示查找到了
				break;
			}
		}
		return node;
	}
	
	/**
	 * 
	 * @methodName		: lookUpSubNode
	 * @description	: 在当前节点及下级节点中查找指定节点名称的节点
	 * @param nodeName	: 节点名称
	 * @return			: 如果找到，返回对应树节点，否则返回null
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<T> lookUpSubNode(String nodeName){
		TreeNode<T> node = null;

		// 检查当前节点
		if (nodeData.getNodeName().equals(nodeName)) {
			node = this;
			return node;
		}
		
		// 遍历子节点
		for(TreeNode<T> item : children) {			
			node = item.lookUpSubNode(nodeName);
			if (node != null) {
				// 如果节点非空，表示查找到了
				break;
			}
		}
		return node;		
	}	
	
	/**
	 * 
	 * @methodName		: lookUpSuperNode
	 * @description	: 在当前节点及上级节点中查找指定节点ID的节点
	 * @param nodeId	: 节点ID
	 * @return			: 如果找到，返回对应树节点，否则返回null
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<T> lookUpSuperNode(int nodeId){
		TreeNode<T> node = null;

		// 检查当前节点
		if (nodeData.getNodeId() == nodeId) {
			node = this;
			return node;
		}
		
		// 查找父节点
		if (parent != null) {
			node = parent.lookUpSuperNode(nodeId);
		}
		
		return node;
	}
	
	/**
	 * 
	 * @methodName		: lookUpSuperNode
	 * @description	: 在当前节点及上级节点中查找指定节点名称的节点
	 * @param nodeName	: 节点名称
	 * @return			: 如果找到，返回对应树节点，否则返回null
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public TreeNode<T> lookUpSuperNode(String nodeName){
		TreeNode<T> node = null;

		// 检查当前节点
		if (nodeData.getNodeName().equals(nodeName)) {
			node = this;
			return node;
		}
		
		// 查找父节点
		if (parent != null) {
			node = parent.lookUpSuperNode(nodeName);
		}
		
		return node;
	}

	
	/**
	 * 
	 * @methodName		: clone
	 * @description	: 复制树节点，包括所有子节点
	 * @return			: 复制后的树节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@SuppressWarnings("unchecked")
	public TreeNode<T> clone(){
		// 复制当前节点数据信息
		TreeNode<T> treeNode = new TreeNode<T>();
		// 节点数据
		treeNode.setNodeData((T)this.nodeData.clone());
		// 是否包含
		treeNode.setIsIncluded(this.isIncluded);
		// 复制所有子节点
		for(TreeNode<T> item : this.children) {
			// 复制子节点
			TreeNode<T> childNode = item.clone();
			// 加入子节点列表中
			treeNode.addChildNode(childNode);
		}
		return treeNode;
	}
	
	/**
	 * 
	 * @methodName		: setChildrenIsIncluded
	 * @description	: 设置所有子节点的是否包含属性 
	 * @param isIncluded	: 节点是否包含
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void setChildrenIsIncluded(Integer isIncluded) {
		// 遍历所有子节点
		for(TreeNode<T> item : this.children) {
			item.setIsIncluded(isIncluded);
			// 子节点的子节点
			item.setChildrenIsIncluded(isIncluded);
		}
	}
	
	/**
	 * 
	 * @methodName		: isAllChildrenIncluded
	 * @description	: 获取子节点是否全包含信息
	 * @return			: 包含全部子节点，则返回true，否则返回false
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public boolean isAllChildrenIncluded() {
		// 遍历所有子节点
		for(TreeNode<T> item : this.children) {
			Integer subIncluded = item.getIsIncluded();
			if (subIncluded == 0) {
				// 存在未包含的子节点
				return false;
			}
			// 子节点的子节点
			boolean bIncluded = item.isAllChildrenIncluded();
			if (bIncluded == false) {
				// 存在未包含的子节点
				return false;
			}
		}	
		return true;
	}
	
	/**
	 * 
	 * @methodName		: combineTreeNode
	 * @description	: 将结构完全相同的节点合并到本节点中，合并后的节点的isIncluded属性位|操作
	 * @param combineNode: 并入的节点
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void combineTreeNode(TreeNode<T> combineNode) {
		// 当前节点数据的isIncluded属性，使用位|操作
		this.setIsIncluded(this.getIsIncluded() | combineNode.getIsIncluded());
		// 合并子节点
		for (int i = 0; i < children.size(); i++) {
			TreeNode<T> item = children.get(i);
			TreeNode<T> combineItem = combineNode.getChildren().get(i);
			// 合并子节点
			item.combineTreeNode(combineItem);
		}
	}
	
	/**
	 * 
	 * @methodName		: arrange
	 * @description	: 对树进行剪枝处理，即所有isIncluded为0的节点，都被移除
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public void arrange() {
		// 遍历子节点列表，如果子节点的isIncluded为0，则剪枝
		// 倒序遍历
		for (int i = children.size() -1; i >=0; i--) {
			TreeNode<T> item = children.get(i);
			if (item.getIsIncluded() == 0) {
				// 不包含，需要移除
				children.remove(i);
			}else {
				// 包含，当前节点不需要移除，处理其子节点列表
				item.arrange();
			}			
		}
	}
	
	/**
	 * 
	 * @methodName		: getNodeList
	 * @description	: 获取包括自身及所有子节点的列表
	 * @param nodeList	: 树节点列表,入口参数为null
	 * @return			: 树节点列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<TreeNode<T>> getNodeList(List<TreeNode<T>> nodeList){
		
		if (nodeList == null) {
			// 如果入口节点，则参数为null，需要创建
			nodeList = new ArrayList<TreeNode<T>>();
		}
		
		// 加入自身节点
		nodeList.add(this);
		
		// 加入所有子节点
		for(int i = 0; i < children.size(); i++) {
			TreeNode<T> childNode = children.get(i);
			childNode.getNodeList(nodeList);
		}
		
		return nodeList;
	}
	
	/**
	 * 
	 * @methodName		: loadData
	 * @description	: 将T类型对象的列表加载到树中，调用之前应确保节点的数据对象已创建，
	 * 						且节点ID设置为0
	 * @param inputList	: T类型对象的列表
	 * @return			: 错误的T类型对象的列表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public List<T> loadData(List<T> inputList){
		// 错误的数据对象列表
		List<T> errorList = new ArrayList<T>();
		
		// 建立节点ID与节点对象的映射表，表示节点加载过程当前已加载的节点集合
		Map<Integer,TreeNode<T>> nodeMap = new HashMap<Integer,TreeNode<T>>();

		//==================================================================
		// 要考虑数据次序不一定保证父节点已先加载的情况
		
		// 清除数据
		clear();
		// 先加入根节点
		nodeMap.put(this.nodeData.getNodeId(), this);
		
		// 父节点
		TreeNode<T> parentNode = null;
		
		// 遍历inputList，加载树				
		for(T item : inputList) {
			Integer parentId = item.getParentId();
								
			if (nodeMap.containsKey(parentId)) {
				// 如果父节点已加载，取得父节点对象
				parentNode = nodeMap.get(parentId);
				// 加载树节点
				addTreeNode(parentNode,item,nodeMap);							
			}else {
				// 如果父节点未加载，则暂时作为游离的独立节点或子树
				// 加载树节点
				addTreeNode(null,item,nodeMap);							
			}											
		}
		
		// 处理游离的节点
		for(TreeNode<T> node : nodeMap.values()) {
			if (node.getParent() == null && node.getNodeData().getNodeId() != 0) {
				// 父节点为空，且非根节点
				// 取得父节点ID
				Integer parentId = node.getNodeData().getParentId();
				if (nodeMap.containsKey(parentId)) {
					// 如果父节点存在，,取得父节点对象
					parentNode = nodeMap.get(parentId);
					// 加入父节点中
					parentNode.addChildNode(node);
				}else {
					// parentId对应的节点不存在，说明数据配置错误
					errorList.add(node.getNodeData());
				}
			}
		}
		return errorList;
	}
	
	/**
	 * 
	 * @methodName		: addTreeNode
	 * @description	: 加入树节点
	 * @param parentNode	: 父节点
	 * @param dataInfo	: 节点信息对象
	 * @param nodeMap	: 节点ID与节点对象的映射表
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	private void addTreeNode(TreeNode<T> parentNode, T dataInfo, Map<Integer,TreeNode<T>> nodeMap) {
		// 生成树节点
		TreeNode<T> treeNode = new TreeNode<T>();
		// 设置节点数据
		treeNode.setNodeData((T)dataInfo);
		if(parentNode != null) {
			// 父节点非空，加入父节点中
			parentNode.addChildNode(treeNode);
		}
		
		// 加入nodeMap中
		nodeMap.put(dataInfo.getNodeId(), treeNode);		
	}
	
	/**
	 * 
	 * @methodName		: toString
	 * @description	: 重载toString方法
	 * @return			: 返回序列化输出的字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	@Override
	public String toString() {
		String sRet = "";
		
		// 输出节点开始符号
		sRet = "{";			
		// 输出isIncluded值
		sRet += "\"isIncluded\":" + isIncluded + ",";
		// 输出当前节点数据
		sRet += "\"nodeData\":" + nodeData.toString();
		// 与前一个节点分隔
		sRet += ",";			
		sRet += "\"children\":";
		
		// 输出子节点
		// 子节点列表
		sRet += "[";
		String sChild = "";
		// 遍历子节点
		for(TreeNode<T> item : children) {
			// 输出子节点数据
			if (sChild.equals("")) {
				sChild = item.toString();
			}else {
				sChild += "," + item.toString();
			}
		}
		sRet += sChild;
		// 结束列表
		sRet += "]";
		// 输出节点结束符号
		sRet += "}";
		
		return sRet;
	}	
}
