package cn.honry.base.bean.business;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	/** * 显示节点的id */
	private String id;
	/** * 显示节点的名称 */
	private String text;
	/** * 显示节点的图标 */
	private String icon;
	/** * 显示节点的父节点 */
	private String parentId;
	/** * url */
	private String attributes;
	/** * 显示节点的子节点集合 */
	private List<TreeNode> children;

	/** * 空的构造函数 */
	public TreeNode() {
	}

	/**
	 * * 有参数的构造参数 * @param id 显示的节点ID * @param text 显示的节点名称 * @param icon
	 * 显示的节点图标 * @param parentId 显示的节点的父节点 * @param children 显示节点的子节点
	 */
	public TreeNode(String id, String text, String icon, String parentId,
			List<TreeNode> children) {
		super();
		this.id = id;
		this.text = text;
		this.icon = icon;
		this.parentId = parentId;
		this.children = children;
	}

	/** * 添加子节点的方法 * @param node 树节点实体 */
	public void addChild(TreeNode node) {
		if (this.children == null) {
			children = new ArrayList<TreeNode>();
			children.add(node);
		} else {
			children.add(node);
		}
	}
	
	/** *get/set方法 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	

}