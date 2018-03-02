package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 住院担保金 实体
 * @author  dh
 * @date 创建时间：2016年4月9日
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class InpatientSurety extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**住院流水号**/
	private String inpatientNo;
	/**发生序号**/
	private Integer happenNo;
	/**科室代码**/
	private String deptCode;
	/**担保人代码**/
	private String suretyCode;
	/**担保人姓名**/
	private String suretyName;
	/**担保金额**/
	private Double suretyCost;
	/**担保类型 E 人员担保 U 单位担保 F 财务担保**/
	private String suretyType;
	/**审批人代码**/
	private String applyCode;
	/**审批人姓名**/
	private String applyName;
	/**备注**/
	private String mark;
	/**支付方式CA现金CH支票CD信用卡DB借记卡AJ转押金PO汇票PS保险帐户YS院内账户**/
	private String payWay;
	/**预交金状态1:收取；0:作废;2:补打**/
	private Integer state;
	/**开户银行**/
	private String openBank;
	/**开户帐户**/
	private String openAccounts;
	/**工作单位**/
	private String workName;
	/**pos交易流水号或支票号或汇票号**/
	private String postransNo;
	/**发票号**/
	private String invoiceNo;
	/**旧发票号**/
	private String oldInvoiceNo;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getSuretyCode() {
		return suretyCode;
	}
	public void setSuretyCode(String suretyCode) {
		this.suretyCode = suretyCode;
	}
	public String getSuretyName() {
		return suretyName;
	}
	public void setSuretyName(String suretyName) {
		this.suretyName = suretyName;
	}
	public Double getSuretyCost() {
		return suretyCost;
	}
	public void setSuretyCost(Double suretyCost) {
		this.suretyCost = suretyCost;
	}
	public String getSuretyType() {
		return suretyType;
	}
	public void setSuretyType(String suretyType) {
		this.suretyType = suretyType;
	}
	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	public String getOpenAccounts() {
		return openAccounts;
	}
	public void setOpenAccounts(String openAccounts) {
		this.openAccounts = openAccounts;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getPostransNo() {
		return postransNo;
	}
	public void setPostransNo(String postransNo) {
		this.postransNo = postransNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getOldInvoiceNo() {
		return oldInvoiceNo;
	}
	public void setOldInvoiceNo(String oldInvoiceNo) {
		this.oldInvoiceNo = oldInvoiceNo;
	}
	
}
