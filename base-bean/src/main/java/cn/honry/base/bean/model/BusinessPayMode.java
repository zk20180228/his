package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 结算支付情况表
 */

public class BusinessPayMode extends Entity  {

	/**发票号**/
	private String invoiceNo;
	/**交易类型,1正，2反**/
	private Integer transType;
	/**交易流水号**/
	private Integer sequenceNo;
	/**支付方式   1 现金   2 银行卡  3 支票 4 用户账户**/
	private String modeCode;
	/**应付金额**/
	private Double totCost;
	/**实付金额**/
	private Double realCost;
	/**开户银行代码**/
	private String bankCode;
	/**开户银行名称**/
	private String bankName;
	/**账号**/
	private String account;
	/**pos机号**/
	private String posNo;
	/**支票号**/
	private String checkNo;
	/**结算人**/
	private String operCode;
	/**结算时间**/
	private Date operDate;
	/**0未核查/1已核查**/
	private Integer checkFlag;
	/**核查人**/
	private String checkOpcd;
	/**核查时间**/
	private Date checkDate;
	/**0未日结/1已日结**/
	private Integer balanceFlag;
	/**日结标识号**/
	private String balanceNo;
	/**日结人**/
	private String balanceOpcd;
	/**0未对帐/1已对帐**/
	private Integer correctFlag;
	/**对帐人**/
	private String correctOpcd;
	/**对帐时间**/
	private Date correctDate;
	/**日结时间**/
	private Date balanceDate;
	/**发票序号，一次结算产生多张发票的combNo**/
	private String invoiceSeq;
	/**1正常，0作废，2重打，3注销**/
	private Integer cancelFlag;
	/**发票组号**/
	private String invoiceComb;
	//新增字段 2016-11-15 11:43:08
	/**支付方式名称*/
	private String modeName;
	/**结算人姓名*/
	private String operName;
	/**核查人姓名*/
	private String checkOpcdName;
	/**日结人姓名*/
	private String balanceOpcdName;
	/**对帐人姓名*/
	private String correctOpceName;
	
	
	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getCheckOpcdName() {
		return checkOpcdName;
	}

	public void setCheckOpcdName(String checkOpcdName) {
		this.checkOpcdName = checkOpcdName;
	}

	public String getBalanceOpcdName() {
		return balanceOpcdName;
	}

	public void setBalanceOpcdName(String balanceOpcdName) {
		this.balanceOpcdName = balanceOpcdName;
	}

	public String getCorrectOpceName() {
		return correctOpceName;
	}

	public void setCorrectOpceName(String correctOpceName) {
		this.correctOpceName = correctOpceName;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getModeCode() {
		return modeCode;
	}

	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}

	public Double getTotCost() {
		return totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Double getRealCost() {
		return realCost;
	}

	public void setRealCost(Double realCost) {
		this.realCost = realCost;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getOperDate() {
		return operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public Integer getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getCheckOpcd() {
		return checkOpcd;
	}

	public void setCheckOpcd(String checkOpcd) {
		this.checkOpcd = checkOpcd;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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

	public Integer getBalanceFlag() {
		return balanceFlag;
	}

	public void setBalanceFlag(Integer balanceFlag) {
		this.balanceFlag = balanceFlag;
	}

	public Integer getCorrectFlag() {
		return correctFlag;
	}

	public void setCorrectFlag(Integer correctFlag) {
		this.correctFlag = correctFlag;
	}

	public String getCorrectOpcd() {
		return correctOpcd;
	}

	public void setCorrectOpcd(String correctOpcd) {
		this.correctOpcd = correctOpcd;
	}

	public Date getCorrectDate() {
		return correctDate;
	}

	public void setCorrectDate(Date correctDate) {
		this.correctDate = correctDate;
	}

	public Date getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public String getInvoiceComb() {
		return invoiceComb;
	}

	public void setInvoiceComb(String invoiceComb) {
		this.invoiceComb = invoiceComb;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceSeq() {
		return invoiceSeq;
	}

	public void setInvoiceSeq(String invoiceSeq) {
		this.invoiceSeq = invoiceSeq;
	}

	public Integer getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(Integer cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	

}