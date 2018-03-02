package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 发票明细表(费用分类表)
 */

public class FinanceInvoicedetailNow  extends Entity  {

	/**发票号 **/
	private String invoiceNo;
	/**交易类型,1正,2反 **/
	private Integer transType;
	/**发票内流水号 **/
	private String invoSequence;
	/**发票科目代码 **/
	private String invoCode;
	/**发票科目名称 **/
	private String invoName;
	/**可报效金额 **/
	private Double pubCost;
	/**不可报效金额  **/
	private Double ownCost;
	/**自付金额 **/
	private Double payCost;
	/**执行科室 **/
	private String deptCode;
	/**执行科室名称 **/
	private String deptName;
	/**操作时间 **/
	private Date operDate;
	/**操作员 **/
	private String operCode;
	/**0未日结/1已日结 **/
	private Integer balanceFlag;
	/**日结标识号 **/
	private String balanceNo;
	/**日结人 **/
	private String balanceOpcd;
	/**日结时间 **/
	private Date balanceDate;
	/**1正常，0作废，2重打，3注销 **/
	private Integer cancelFlag;
	/**发票序号，一次结算产生多张发票的combNo **/
	private String invoiceSeq;
	//新增字段  2016-11-15 11:12:55
	/**操作员姓名*/
	private String operName;
	/**日结人姓名*/
	private String balanceOpceName;
	//新增字段 2017-10-26
	/**所属医院**/
	private Integer hospitalID;
	/**所属院区**/
	private String areaCode;
	
	
	public Integer getHospitalID() {
		return hospitalID;
	}
	public void setHospitalID(Integer hospitalID) {
		this.hospitalID = hospitalID;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getInvoSequence() {
		return invoSequence;
	}
	public void setInvoSequence(String invoSequence) {
		this.invoSequence = invoSequence;
	}
	public String getInvoCode() {
		return invoCode;
	}
	public void setInvoCode(String invoCode) {
		this.invoCode = invoCode;
	}
	public String getInvoName() {
		return invoName;
	}
	public void setInvoName(String invoName) {
		this.invoName = invoName;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
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
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Integer getBalanceFlag() {
		return balanceFlag;
	}
	public void setBalanceFlag(Integer balanceFlag) {
		this.balanceFlag = balanceFlag;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getBalanceOpcd() {
		return balanceOpcd;
	}
	public void setBalanceOpcd(String balanceOpcd) {
		this.balanceOpcd = balanceOpcd;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Integer getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(Integer cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getInvoiceSeq() {
		return invoiceSeq;
	}
	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getBalanceOpceName() {
		return balanceOpceName;
	}
	public void setBalanceOpceName(String balanceOpceName) {
		this.balanceOpceName = balanceOpceName;
	}
	
	

}