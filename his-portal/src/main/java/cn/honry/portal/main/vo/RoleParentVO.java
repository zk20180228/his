package cn.honry.portal.main.vo;

import java.util.List;

public class RoleParentVO {
	private String id;//父级div的id
	private String attribute;//是否显示
	private List<RoleSubclassVO> roleSubclassVO;//角色的List
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public List<RoleSubclassVO> getRoleSubclassVO() {
		return roleSubclassVO;
	}
	public void setRoleSubclassVO(List<RoleSubclassVO> roleSubclassVO) {
		this.roleSubclassVO = roleSubclassVO;
	}
	
}
