package cn.honry.base.bean.model;

import java.io.Serializable;

import cn.honry.base.bean.business.Entity;
@SuppressWarnings("serial")
public class PublicAddressBook extends Entity implements Serializable {
	/**节点名称**/
	private String name;
	/**父级节点**/
	private String parentCode;
	/**层级**/
	private Integer nodeLevel;
	/**路径**/
	private String path;
	/**父级路径**/
	private String superPath;
	/**顺序**/
	private Integer order;
	/**节点类型(00-院区，11-楼号，22-楼层，33-科室分类，44-科室，55-工作站)**/
	private String nodeType;
	/**状态(0：普通；1：常用)**/
	private Integer status=0;
	/**电话**/
	private String phone;
	/**分机电话**/
	private String minPhone;
	/**办公电话**/
	private String officePhone;
	
	//新加字段
	private String areaName;//院区名称
	private String buildingName;//楼名
	private String floorName;//楼层
	private String floorType;//楼层类型
	private String floorDept;//楼层科室
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public String getFloorType() {
		return floorType;
	}
	public void setFloorType(String floorType) {
		this.floorType = floorType;
	}
	public String getFloorDept() {
		return floorDept;
	}
	public void setFloorDept(String floorDept) {
		this.floorDept = floorDept;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public Integer getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(Integer nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSuperPath() {
		return superPath;
	}
	public void setSuperPath(String superPath) {
		this.superPath = superPath;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMinPhone() {
		return minPhone;
	}
	public void setMinPhone(String minPhone) {
		this.minPhone = minPhone;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	
}
