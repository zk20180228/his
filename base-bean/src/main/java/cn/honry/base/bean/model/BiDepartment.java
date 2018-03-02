package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BiDepartment entity. @author MyEclipse Persistence Tools
 */

public class BiDepartment implements java.io.Serializable {

	// Fields

	private String deptId;
	private BigDecimal hospitalId;
	private String deptCode;
	private String deptName;
	private String deptAddress;
	private BigDecimal deptOrder;
	private String deptType;
	private String deptProperty;
	private Boolean deptIsforregister;
	private String deptRegisterno;
	private Boolean deptIsforaccounting;
	private String deptRemark;
	private String deptStatus;
	private Date createDate;
	private Date updateDate;

	// Constructors

	/** default constructor */
	public BiDepartment() {
	}

	/** full constructor */
	public BiDepartment(BigDecimal hospitalId, String deptCode,
			String deptName, String deptAddress, BigDecimal deptOrder,
			String deptType, String deptProperty, Boolean deptIsforregister,
			String deptRegisterno, Boolean deptIsforaccounting,
			String deptRemark, String deptStatus, Date createDate,
			Date updateDate) {
		this.hospitalId = hospitalId;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.deptAddress = deptAddress;
		this.deptOrder = deptOrder;
		this.deptType = deptType;
		this.deptProperty = deptProperty;
		this.deptIsforregister = deptIsforregister;
		this.deptRegisterno = deptRegisterno;
		this.deptIsforaccounting = deptIsforaccounting;
		this.deptRemark = deptRemark;
		this.deptStatus = deptStatus;
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

	public String getDeptAddress() {
		return this.deptAddress;
	}

	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
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

	public String getDeptProperty() {
		return this.deptProperty;
	}

	public void setDeptProperty(String deptProperty) {
		this.deptProperty = deptProperty;
	}

	public Boolean getDeptIsforregister() {
		return this.deptIsforregister;
	}

	public void setDeptIsforregister(Boolean deptIsforregister) {
		this.deptIsforregister = deptIsforregister;
	}

	public String getDeptRegisterno() {
		return this.deptRegisterno;
	}

	public void setDeptRegisterno(String deptRegisterno) {
		this.deptRegisterno = deptRegisterno;
	}

	public Boolean getDeptIsforaccounting() {
		return this.deptIsforaccounting;
	}

	public void setDeptIsforaccounting(Boolean deptIsforaccounting) {
		this.deptIsforaccounting = deptIsforaccounting;
	}

	public String getDeptRemark() {
		return this.deptRemark;
	}

	public void setDeptRemark(String deptRemark) {
		this.deptRemark = deptRemark;
	}

	public String getDeptStatus() {
		return this.deptStatus;
	}

	public void setDeptStatus(String deptStatus) {
		this.deptStatus = deptStatus;
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