package cn.honry.base.bean.model;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.business.Entity;
/**
 * hedong 预交金表
 */
public class InpatientInPrepay  extends Entity implements java.io.Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer transType;//交易类型,1正交易，2反交易
	  private String inpatientNo;//住院号 inpatient_no     VARCHAR2(50),
	  private Integer happenNo;//发生序号 happen_no         NUMBER(5),
	  private String name;//姓名 name              VARCHAR2(20),
	  private Double prepayCost;//预交金额 prepay_cost       NUMBER(18,4),
	  private String payWay;//支付方式CA现金CH支票CD信用卡DB借记卡AJ转押金PO汇票PS保险帐户YS院内账户   pay_way          VARCHAR2(2),
	  private String deptCode;//科室代码 dept_code         VARCHAR2(50),
	  private String receiptNo;//预交金收据号码  receipt_no       VARCHAR2(14),
	  private Date statDate;//统计日期 stat_date        DATE,
	  private Date balanceDate ;//结算时间 balance_date     DATE,
	  private Integer balanceState;//结算标志 0:未结算；1:已结算 2:已结转 balance_state     NUMBER(1),
	  private Integer prepayState;//预交金状态0:收取；1:作废;2:补打,3结算召回作废 prepay_state      NUMBER(1),
	  private String oldRecipeno;//原票据号 old_recipeno      VARCHAR2(14),
	  private String openBank  ;//开户银行 open_bank       VARCHAR2(50),
	  private String openAccounts;//开户帐户 open_accounts     VARCHAR2(50),
	  private String invoiceNo ;//结算发票号 invoice_no       VARCHAR2(14),
	  private Integer balanceNo;//结算序号 balance_no        NUMBER(2),
	  private String balanceOpercode;//结算人代码 balance_opercode  VARCHAR2(50),
	  private Integer reportFlag ;//上缴标志（1是 0否）report_flag      NUMBER(1),
	  private String checkNo;//审核序号 check_no          VARCHAR2(20),
	  private String fingrpCode;//财务组代码 fingrp_code       VARCHAR2(6),
	  private String workName ;//工作单位  work_name       VARCHAR2(50),
	  private Integer transFlag ;//0非转押金，1转押金，2转押金已打印 trans_flag       NUMBER(1),
	  private Integer changeBalanceNo;//转押金时结算序号 change_balance_no NUMBER(2), 已与sun商量改为字符串类型
	  private String transCode;//转押金结算员 trans_code       VARCHAR2(50),
	  private Date transDate ;//转押金时间 trans_date        DATE,
	  private Integer printFlag;//打印标志 print_flag        NUMBER(1),
	  private Integer extFlag ;//正常收取 1 结算召回 2  ext_flag        NUMBER(1),
	  private Integer ext1Flag ;//扩展标志1 ext1_flag        NUMBER(1) default 0,
	  private String postransNo;//pos交易流水号或支票号或汇票号 postrans_no       VARCHAR2(30),
	  private String daybalanceNo;//日结标识号  daybalance_no    VARCHAR2(10),
	  private String daybalanceOpcd;//日结人 daybalance_opcd   VARCHAR2(50),
	  private Date daybalanceDate;//日结时间 daybalance_date   DATE,
	  
	  	/**医院编码**/
		private Integer hospitalId;
		/**院区编码**/
		private String areaCode;
	  
	  //新加字段
	  /**科室名称**/
	  private String deptName;
	  /**结算人名称**/
	  private String balanceOpername;
	  /**财务组名称**/
	  private String fingrpName;
	  /**报表所用字段**/
	  private List<InpatientInPrepay> inpatientInPrepay;
	  
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
	public List<InpatientInPrepay> getInpatientInPrepay() {
		return inpatientInPrepay;
	}
	public void setInpatientInPrepay(List<InpatientInPrepay> inpatientInPrepay) {
		this.inpatientInPrepay = inpatientInPrepay;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBalanceOpername() {
		return balanceOpername;
	}
	public void setBalanceOpername(String balanceOpername) {
		this.balanceOpername = balanceOpername;
	}
	public String getFingrpName() {
		return fingrpName;
	}
	public void setFingrpName(String fingrpName) {
		this.fingrpName = fingrpName;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Integer getBalanceState() {
		return balanceState;
	}
	public void setBalanceState(Integer balanceState) {
		this.balanceState = balanceState;
	}
	public Integer getPrepayState() {
		return prepayState;
	}
	public void setPrepayState(Integer prepayState) {
		this.prepayState = prepayState;
	}
	public String getOldRecipeno() {
		return oldRecipeno;
	}
	public void setOldRecipeno(String oldRecipeno) {
		this.oldRecipeno = oldRecipeno;
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
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getBalanceOpercode() {
		return balanceOpercode;
	}
	public void setBalanceOpercode(String balanceOpercode) {
		this.balanceOpercode = balanceOpercode;
	}
	public Integer getReportFlag() {
		return reportFlag;
	}
	public void setReportFlag(Integer reportFlag) {
		this.reportFlag = reportFlag;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getFingrpCode() {
		return fingrpCode;
	}
	public void setFingrpCode(String fingrpCode) {
		this.fingrpCode = fingrpCode;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public Integer getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(Integer transFlag) {
		this.transFlag = transFlag;
	}
	
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public Integer getPrintFlag() {
		return printFlag;
	}
	public void setPrintFlag(Integer printFlag) {
		this.printFlag = printFlag;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public Integer getExt1Flag() {
		return ext1Flag;
	}
	public void setExt1Flag(Integer ext1Flag) {
		this.ext1Flag = ext1Flag;
	}
	public String getPostransNo() {
		return postransNo;
	}
	public void setPostransNo(String postransNo) {
		this.postransNo = postransNo;
	}
	public String getDaybalanceNo() {
		return daybalanceNo;
	}
	public void setDaybalanceNo(String daybalanceNo) {
		this.daybalanceNo = daybalanceNo;
	}
	public String getDaybalanceOpcd() {
		return daybalanceOpcd;
	}
	public void setDaybalanceOpcd(String daybalanceOpcd) {
		this.daybalanceOpcd = daybalanceOpcd;
	}
	public Date getDaybalanceDate() {
		return daybalanceDate;
	}
	public void setDaybalanceDate(Date daybalanceDate) {
		this.daybalanceDate = daybalanceDate;
	}
	public void setChangeBalanceNo(Integer changeBalanceNo) {
		this.changeBalanceNo = changeBalanceNo;
	}
	public Integer getChangeBalanceNo() {
		return changeBalanceNo;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	
	
}
