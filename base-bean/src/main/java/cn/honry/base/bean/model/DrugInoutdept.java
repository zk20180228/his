package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：DrugInoutdept.java 
 * @Description：药房药库出入库科室维护 实体
 * @Author：lt
 * @CreateDate：2015-7-6  
 * @version 1.0
 *
 */
public class DrugInoutdept extends Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields
	/**类别 1入库 2出库*/
	private Integer type;
	/**科室编号*/
	private String deptId;
	/**出库或入库科室*/
	private String objectDept;
	/**备注*/
	private String remark;
	/**排序*/
	private Integer dataorder;
	
	/**添加字段  科室编号*/
	private String deptCode;
	/**添加字段  科室名称*/
	private String deptName;

	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getObjectDept() {
		return this.objectDept;
	}

	public void setObjectDept(String objectDept) {
		this.objectDept = objectDept;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDataorder() {
		return this.dataorder;
	}

	public void setDataorder(Integer dataorder) {
		this.dataorder = dataorder;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}