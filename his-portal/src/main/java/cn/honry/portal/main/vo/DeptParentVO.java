package cn.honry.portal.main.vo;

import java.util.List;

public class DeptParentVO {
	private String id;//父级div的id
	private String attribute;//是否显示
	private List<DeptSubclassVO> deptSubclassVO;//部门的List
	
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
	public List<DeptSubclassVO> getDeptSubclassVO() {
		return deptSubclassVO;
	}
	public void setDeptSubclassVO(List<DeptSubclassVO> deptSubclassVO) {
		this.deptSubclassVO = deptSubclassVO;
	}
	
}
