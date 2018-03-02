package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BiWard entity. @author MyEclipse Persistence Tools
 */

public class BiWard implements java.io.Serializable {

	// Fields

	private String deptId;
	private BigDecimal hospitalId;
	private String deptCode;
	private String deptName;
	private String wardCode;
	private String wardName;
	private BigDecimal deptOrder;
	private String deptType;
	private String deptRemark;
	private String status;
	private Date createDate;
	private Date updateDate;

	// Constructors

	/** default constructor */
	public BiWard() {
	}

	/** full constructor */
	public BiWard(BigDecimal hospitalId, String deptCode, String deptName,
			String wardCode, String wardName, BigDecimal deptOrder,
			String deptType, String deptRemark, String status, Date createDate,
			Date updateDate) {
		this.hospitalId = hospitalId;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.wardCode = wardCode;
		this.wardName = wardName;
		this.deptOrder = deptOrder;
		this.deptType = deptType;
		this.deptRemark = deptRemark;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	// Property accessors

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public BigDecimal getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(BigDecimal hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getWardCode() {
		return this.wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getWardName() {
		return this.wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public BigDecimal getDeptOrder() {
		return this.deptOrder;
	}

	public void setDeptOrder(BigDecimal deptOrder) {
		this.deptOrder = deptOrder;
	}

	public String getDeptType() {
		return this.deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getDeptRemark() {
		return this.deptRemark;
	}

	public void setDeptRemark(String deptRemark) {
		this.deptRemark = deptRemark;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}