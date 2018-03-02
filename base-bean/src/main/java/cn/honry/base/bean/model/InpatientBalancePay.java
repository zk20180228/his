package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：InpatientBalancePay.java 
 * @Description：住院收费结算实付表
 * @Author：hedong
 * @CreateDate：2015-08-17  
 * @version 1.0
 * 结算召回模块
 */
public class InpatientBalancePay extends Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String invoiceNo;// 发票号码 
	private Integer transType;// 交易类型,1正交易，2反交易  
	private Integer  transKind;// 交易种类 0 预交款 1 结算款
	private String  payWay;// 支付方式
	private Integer  balanceNo;//结算序号
	private Double  cost ;// 金额 
	private Integer  countNum;//张数
	private Double  changePrepaycost;// 转入金额（预交金）
	private String  bankCode;// 开户银行
	private String  bankName;// 开户银行名称
	private String  bankAccount;// 开户银行帐号 
	private String  bankAccoutname;// 开户银行单位
	private String  postransNo;//  支票号或交易流水号
	private Integer  reutrnorsupplyFlag;//返回或补收标志 1 补收 2 返还
	private String balanceOpercode;//结算人代码  
	private Date  balanceDate;// 结算日期
	//新加字段
	/**结算人名称**/
	private String balanceOpername;
	/**医院编码**/
	private Integer hospitalId;
	/**院区编码**/
	private String areaCode;
	
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
	public String getBalanceOpername() {
		return balanceOpername;
	}
	public void setBalanceOpername(String balanceOpername) {
		this.balanceOpername = balanceOpername;
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
	public Integer getTransKind() {
		return transKind;
	}
	public void setTransKind(Integer transKind) {
		this.transKind = transKind;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getCountNum() {
		return countNum;
	}
	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}
	public Double getChangePrepaycost() {
		return changePrepaycost;
	}
	public void setChangePrepaycost(Double changePrepaycost) {
		this.changePrepaycost = changePrepaycost;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankAccoutname() {
		return bankAccoutname;
	}
	public void setBankAccoutname(String bankAccoutname) {
		this.bankAccoutname = bankAccoutname;
	}
	public String getPostransNo() {
		return postransNo;
	}
	public void setPostransNo(String postransNo) {
		this.postransNo = postransNo;
	}
	public Integer getReutrnorsupplyFlag() {
		return reutrnorsupplyFlag;
	}
	public void setReutrnorsupplyFlag(Integer reutrnorsupplyFlag) {
		this.reutrnorsupplyFlag = reutrnorsupplyFlag;
	}
	public String getBalanceOpercode() {
		return balanceOpercode;
	}
	public void setBalanceOpercode(String balanceOpercode) {
		this.balanceOpercode = balanceOpercode;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
    
	
}
