package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiInpatientBalancelist entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientBalancelist implements java.io.Serializable {

	// Fields

	private BiInpatientBalancelistId id;
	private Boolean transType;
	private String inpatientNo;
	private String name;
	private Boolean paykindCode;
	private String pactCode;
	private String deptCode;
	private String statCode;
	private String statName;
	private Double totCost;
	private Double ownCost;
	private Double payCost;
	private Double pubCost;
	private Double ecoCost;
	private String balanceOpercode;
	private Date balanceDate;
	private Boolean balanceType;
	private Boolean babyFlag;
	private String checkNo;
	private String balanceoperDeptcode;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiInpatientBalancelist() {
	}

	/** minimal constructor */
	public BiInpatientBalancelist(BiInpatientBalancelistId id) {
		this.id = id;
	}

	/** full constructor */
	public BiInpatientBalancelist(BiInpatientBalancelistId id,
			Boolean transType, String inpatientNo, String name,
			Boolean paykindCode, String pactCode, String deptCode,
			String statCode, String statName, Double totCost, Double ownCost,
			Double payCost, Double pubCost, Double ecoCost,
			String balanceOpercode, Date balanceDate, Boolean balanceType,
			Boolean babyFlag, String checkNo, String balanceoperDeptcode,
			Date createTime, Date updateTime) {
		this.id = id;
		this.transType = transType;
		this.inpatientNo = inpatientNo;
		this.name = name;
		this.paykindCode = paykindCode;
		this.pactCode = pactCode;
		this.deptCode = deptCode;
		this.statCode = statCode;
		this.statName = statName;
		this.totCost = totCost;
		this.ownCost = ownCost;
		this.payCost = payCost;
		this.pubCost = pubCost;
		this.ecoCost = ecoCost;
		this.balanceOpercode = balanceOpercode;
		this.balanceDate = balanceDate;
		this.balanceType = balanceType;
		this.babyFlag = babyFlag;
		this.checkNo = checkNo;
		this.balanceoperDeptcode = balanceoperDeptcode;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public BiInpatientBalancelistId getId() {
		return this.id;
	}

	public void setId(BiInpatientBalancelistId id) {
		this.id = id;
	}

	public Boolean getTransType() {
		return this.transType;
	}

	public void setTransType(Boolean transType) {
		this.transType = transType;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getPaykindCode() {
		return this.paykindCode;
	}

	public void setPaykindCode(Boolean paykindCode) {
		this.paykindCode = paykindCode;
	}

	public String getPactCode() {
		return this.pactCode;
	}

	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getStatCode() {
		return this.statCode;
	}

	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}

	public String getStatName() {
		return this.statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public Double getTotCost() {
		return this.totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getOwnCost() {
		return this.ownCost;
	}

	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}

	public Double getPayCost() {
		return this.payCost;
	}

	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}

	public Double getPubCost() {
		return this.pubCost;
	}

	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}

	public Double getEcoCost() {
		return this.ecoCost;
	}

	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}

	public String getBalanceOpercode() {
		return this.balanceOpercode;
	}

	public void setBalanceOpercode(String balanceOpercode) {
		this.balanceOpercode = balanceOpercode;
	}

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public Boolean getBalanceType() {
		return this.balanceType;
	}

	public void setBalanceType(Boolean balanceType) {
		this.balanceType = balanceType;
	}

	public Boolean getBabyFlag() {
		return this.babyFlag;
	}

	public void setBabyFlag(Boolean babyFlag) {
		this.babyFlag = babyFlag;
	}

	public String getCheckNo() {
		return this.checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getBalanceoperDeptcode() {
		return this.balanceoperDeptcode;
	}

	public void setBalanceoperDeptcode(String balanceoperDeptcode) {
		this.balanceoperDeptcode = balanceoperDeptcode;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}