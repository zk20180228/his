package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * ClassName: DrugCheckstatic 
 * @Description: 盘点记录表
 * @author lt
 * @date 2015-8-17
 */
@SuppressWarnings("serial")
public class DrugCheckstatic extends Entity implements java.io.Serializable {

	/**盘点单号*/
	private String checkCode;
	/**盘点单名称*/
	private String checkName;
	/**库存单位编码*/
	private String drugDeptCode;
	/**盘点状态标志（0封帐；1结存；2取消）*/
	private Integer checkState;
	/**封帐人*/
	private String foperCode;
	/**封帐时间*/
	private Date foperTime;
	/**结存人*/
	private String coperCode;
	/**结存时间*/
	private Date coperTime;
	/**盘亏金额*/
	private Double checkLossCost;
	/**盘盈金额*/
	private Double checkProfitCost;

	/** 
	* @Fields hospitalId : 所属医院 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 所属院区
	*/ 
	private String areaCode;

	public String getCheckCode() {
		return this.checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getCheckName() {
		return this.checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public String getDrugDeptCode() {
		return this.drugDeptCode;
	}

	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}

	public Integer getCheckState() {
		return this.checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getFoperCode() {
		return this.foperCode;
	}

	public void setFoperCode(String foperCode) {
		this.foperCode = foperCode;
	}

	public Date getFoperTime() {
		return this.foperTime;
	}

	public void setFoperTime(Date foperTime) {
		this.foperTime = foperTime;
	}

	public String getCoperCode() {
		return this.coperCode;
	}

	public void setCoperCode(String coperCode) {
		this.coperCode = coperCode;
	}

	public Date getCoperTime() {
		return this.coperTime;
	}

	public void setCoperTime(Date coperTime) {
		this.coperTime = coperTime;
	}

	public Double getCheckLossCost() {
		return this.checkLossCost;
	}

	public void setCheckLossCost(Double checkLossCost) {
		this.checkLossCost = checkLossCost;
	}

	public Double getCheckProfitCost() {
		return this.checkProfitCost;
	}

	public void setCheckProfitCost(Double checkProfitCost) {
		this.checkProfitCost = checkProfitCost;
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