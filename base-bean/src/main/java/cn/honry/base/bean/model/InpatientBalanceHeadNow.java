package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：InpatientBalanceHead.java 
 * @Description：住院结算头表   
 * @Author：hedong
 * @CreateDate：2015-08-17  
 * @version 1.0
 * 结算召回模块
 */
public class InpatientBalanceHeadNow extends Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;//String 13 Integer 11 Double18 Date 6
	private String invoiceNo;//  发票号码 结转时为流水号
	private Integer transType;// 交易类型,1正交易，2反交易   
	private String inpatientNo;//  住院流水号
	private Integer balanceNo;//   结算序号
	private String  paykindCode;// 结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干  
	private String  pactCode;//     合同代码  
	private Double  prepayCost;//   预交金额 
	private Double  changePrepaycost;//  转入预交金额 
	private Double  totCost;//   费用金额 
	private Double  ownCost;//   自费金额
	private Double  payCost;//   自付金额 
	private Double  pubCost;//   公费金额 
	private Double  ecoCost;//   优惠金额
	private Double  derCost;//   减免金额
	private Double  changeTotcost;//  转入费用金额 
	private Double  supplyCost;//  补收金额
	private Double  returnCost;//  返还金额
	private Double  foregiftCost;//  转押金 
	private Date  beginDate;//  起始日期 
	private Date  endDate;//    终止日期 
	private Integer  balanceType;//   结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6欠费结算
	private String  balanceOpercode;// 结算人代码  
	private Date  balanceDate;//  结算时间   
    private Double  accountPay;// 本次账户支付    
    private Double  officePay;//  公务员补助       
    private Double  largePay;//   大额补助       
    private Double  miltaryPay;//  老红军         
    private Double  cashPay;//   本次现金支付       
	private String fingrpCode;//  财务组代码       
	private Integer printTimes;//  打印次数          
	private String checkNo;//   审核序号            
	private Integer maininvoiceFlag;//   主发票标志 
	private Integer wasteFlag;//     扩展标志      
	private Integer ext1Flag;//  扩展标志1       
	private String extCode;//    扩展代码      
	private Integer lastFlag;//  生育保险最后一次结算标志   
	private String name;//   姓名               
    private String balanceoperDeptcode;// 结算员科室 
    private Double	  bursaryAdjustovertop;// 本次结算调整公费日限额超标金额  
	private String wasteOpercode;// 作废操作员  
	private Date  wasteDate;//  作废时间 
	private Integer checkFlag;// 0未核查/1已核查
	private String checkOpcd;// 核查人  
	private Date checkDate;//  核查时间
	private Integer daybalanceFlag;// 0未日结/1已日结
	private String daybalanceNo;//  日结标识号
	private String daybalanceOpcd;//  日结人
	private Date daybalanceDate;//  日结时间 
	
	//新加字段
	/**结算人名称**/
	private String balanceOpername;
	/**结算员科室名称**/
	private String balanceoperDeptname;
	/**作废操作员名称**/
	private String wasteOpername;
	/**核查人名称**/
	private String checkOpcdName;
	/**日结人名称**/
	private String daybalanceOpcdName;
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
	public String getBalanceoperDeptname() {
		return balanceoperDeptname;
	}
	public void setBalanceoperDeptname(String balanceoperDeptname) {
		this.balanceoperDeptname = balanceoperDeptname;
	}
	public String getWasteOpername() {
		return wasteOpername;
	}
	public void setWasteOpername(String wasteOpername) {
		this.wasteOpername = wasteOpername;
	}
	public String getCheckOpcdName() {
		return checkOpcdName;
	}
	public void setCheckOpcdName(String checkOpcdName) {
		this.checkOpcdName = checkOpcdName;
	}
	public String getDaybalanceOpcdName() {
		return daybalanceOpcdName;
	}
	public void setDaybalanceOpcdName(String daybalanceOpcdName) {
		this.daybalanceOpcdName = daybalanceOpcdName;
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
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getChangePrepaycost() {
		return changePrepaycost;
	}
	public void setChangePrepaycost(Double changePrepaycost) {
		this.changePrepaycost = changePrepaycost;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
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
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Double getDerCost() {
		return derCost;
	}
	public void setDerCost(Double derCost) {
		this.derCost = derCost;
	}
	public Double getChangeTotcost() {
		return changeTotcost;
	}
	public void setChangeTotcost(Double changeTotcost) {
		this.changeTotcost = changeTotcost;
	}
	public Double getSupplyCost() {
		return supplyCost;
	}
	public void setSupplyCost(Double supplyCost) {
		this.supplyCost = supplyCost;
	}
	public Double getReturnCost() {
		return returnCost;
	}
	public void setReturnCost(Double returnCost) {
		this.returnCost = returnCost;
	}
	public Double getForegiftCost() {
		return foregiftCost;
	}
	public void setForegiftCost(Double foregiftCost) {
		this.foregiftCost = foregiftCost;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
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
	public Double getAccountPay() {
		return accountPay;
	}
	public void setAccountPay(Double accountPay) {
		this.accountPay = accountPay;
	}
	public Double getOfficePay() {
		return officePay;
	}
	public void setOfficePay(Double officePay) {
		this.officePay = officePay;
	}
	public Double getLargePay() {
		return largePay;
	}
	public void setLargePay(Double largePay) {
		this.largePay = largePay;
	}
	public Double getMiltaryPay() {
		return miltaryPay;
	}
	public void setMiltaryPay(Double miltaryPay) {
		this.miltaryPay = miltaryPay;
	}
	public Double getCashPay() {
		return cashPay;
	}
	public void setCashPay(Double cashPay) {
		this.cashPay = cashPay;
	}
	public String getFingrpCode() {
		return fingrpCode;
	}
	public void setFingrpCode(String fingrpCode) {
		this.fingrpCode = fingrpCode;
	}
	public Integer getPrintTimes() {
		return printTimes;
	}
	public void setPrintTimes(Integer printTimes) {
		this.printTimes = printTimes;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public Integer getMaininvoiceFlag() {
		return maininvoiceFlag;
	}
	public void setMaininvoiceFlag(Integer maininvoiceFlag) {
		this.maininvoiceFlag = maininvoiceFlag;
	}
	public Integer getWasteFlag() {
		return wasteFlag;
	}
	public void setWasteFlag(Integer wasteFlag) {
		this.wasteFlag = wasteFlag;
	}
	public Integer getExt1Flag() {
		return ext1Flag;
	}
	public void setExt1Flag(Integer ext1Flag) {
		this.ext1Flag = ext1Flag;
	}
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	public Integer getLastFlag() {
		return lastFlag;
	}
	public void setLastFlag(Integer lastFlag) {
		this.lastFlag = lastFlag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBalanceoperDeptcode() {
		return balanceoperDeptcode;
	}
	public void setBalanceoperDeptcode(String balanceoperDeptcode) {
		this.balanceoperDeptcode = balanceoperDeptcode;
	}
	public Double getBursaryAdjustovertop() {
		return bursaryAdjustovertop;
	}
	public void setBursaryAdjustovertop(Double bursaryAdjustovertop) {
		this.bursaryAdjustovertop = bursaryAdjustovertop;
	}
	public String getWasteOpercode() {
		return wasteOpercode;
	}
	public void setWasteOpercode(String wasteOpercode) {
		this.wasteOpercode = wasteOpercode;
	}
	public Date getWasteDate() {
		return wasteDate;
	}
	public void setWasteDate(Date wasteDate) {
		this.wasteDate = wasteDate;
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
	public Integer getDaybalanceFlag() {
		return daybalanceFlag;
	}
	public void setDaybalanceFlag(Integer daybalanceFlag) {
		this.daybalanceFlag = daybalanceFlag;
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
	
	
}
