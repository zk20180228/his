package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 住院：结算员日结  实体
 * @author  tcj
 * @date 创建时间：2016年4月12日
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class InpatientScDreport extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 统计序号 **/
	private  Integer statNo;
	/** 开始时间 **/
	private  Date beginDate;
	/** 结束时间 **/
	private  Date endDate;
	/** 操作员代码 **/
	private  String operCode;
	/** 统计时间 **/
	private  Date operDate;
	/** 医疗预收款贷方 **/
	private  double prepayCost;
	/** 借方支票(银行存款借方) **/
	private  double debitCheck;
	/** 借方银行卡 **/
	private  double debitBank;
	/** 医疗预收款借方 **/
	private  double balancePrepaycost;
	/** 贷方支票(银行存款贷方) **/
	private  double lenderCheck;
	/** 贷方银行卡**/
	private  double lenderBank;
	/** 公费记帐金额 **/
	private  double busaryPubCost;
	/** 市医保帐户支付金额 **/
	private  double cmedicarePayCost;
	/** 市医保统筹金额 **/
	private  double cmedicarePubCost;
	/** 省医保帐户支付金额 **/
	private  double pmedicarePaycost;
	/** 省医保统筹金额 **/
	private  double pmedicarePubcost;
	/** 上缴现金DEBIT_CASH-LENDER_CASH **/
	private  double turninCash;
	/** 预交金发票张数 **/
	private  double prepayinvNum;
	/** 结算发票张数 **/
	private  double balanceinvNum;
	/** 作废预交金发票号码 **/
	private  String wasteprepayInvno;
	/** 作废结算发票号码 **/
	private  String wastebalanceInvno;
	/** 作废预交金发票张数 **/
	private  double wasteprepayinvNum;
	/** 作废结算发票张数 **/
	private  double wastebalanceinvNum;
	/** 预交金发票区间 **/
	private  String prepayinvZone;
	/** 结算发票区间 **/
	private  String balanceinvZone;
	/** 收费员科室 **/
	private  String operDept;
	/** 医疗应收款(结算总金额) **/
	private  double balanceCost;
	/** 院内账户借方 **/
	private  double debitHos;
	/** 院内账户贷方 **/
	private  double lenderHos;
	/** 借方其它 **/
	private  double debitOther;
	/** 贷方其它 **/
	private  double lenderOther;
	/** 减免金额 **/
	private  double derateCost;
	/** 市保大额 **/
	private  double coverCost;
	/** 省保大额**/
	private  double poverCost;
	/** 省保公务员 **/
	private  double poffiCost;
	/** 1审核0未审核 **/
	private  double checkFlag;
	/** 审核人 **/
	private String checkOper;
	/** 审核时间 **/
	private Date checkDate;
	/** 库存现金借方 **/
	private  double debitCash;
	/** 库存现金贷方 **/
	private  double lenderCash;
//	/** 新添加字段 医院编码  **/
//	private Integer hospitalId;
//	/** 新添加字段 院区编码  **/
//	private String areaCode;
//	
//	public Integer getHospitalId() {
//		return hospitalId;
//	}
//	public void setHospitalId(Integer hospitalId) {
//		this.hospitalId = hospitalId;
//	}
//	public String getAreaCode() {
//		return areaCode;
//	}
//	public void setAreaCode(String areaCode) {
//		this.areaCode = areaCode;
//	}
	
	public Integer getStatNo() {
		return statNo;
	}
	public void setStatNo(Integer statNo) {
		this.statNo = statNo;
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
	public double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public double getDebitCheck() {
		return debitCheck;
	}
	public void setDebitCheck(double debitCheck) {
		this.debitCheck = debitCheck;
	}
	public double getDebitBank() {
		return debitBank;
	}
	public void setDebitBank(double debitBank) {
		this.debitBank = debitBank;
	}
	public double getBalancePrepaycost() {
		return balancePrepaycost;
	}
	public void setBalancePrepaycost(double balancePrepaycost) {
		this.balancePrepaycost = balancePrepaycost;
	}
	public double getLenderCheck() {
		return lenderCheck;
	}
	public void setLenderCheck(double lenderCheck) {
		this.lenderCheck = lenderCheck;
	}
	public double getLenderBank() {
		return lenderBank;
	}
	public void setLenderBank(double lenderBank) {
		this.lenderBank = lenderBank;
	}
	public double getBusaryPubCost() {
		return busaryPubCost;
	}
	public void setBusaryPubCost(double busaryPubCost) {
		this.busaryPubCost = busaryPubCost;
	}
	public double getCmedicarePayCost() {
		return cmedicarePayCost;
	}
	public void setCmedicarePayCost(double cmedicarePayCost) {
		this.cmedicarePayCost = cmedicarePayCost;
	}
	public double getCmedicarePubCost() {
		return cmedicarePubCost;
	}
	public void setCmedicarePubCost(double cmedicarePubCost) {
		this.cmedicarePubCost = cmedicarePubCost;
	}
	public double getPmedicarePaycost() {
		return pmedicarePaycost;
	}
	public void setPmedicarePaycost(double pmedicarePaycost) {
		this.pmedicarePaycost = pmedicarePaycost;
	}
	public double getPmedicarePubcost() {
		return pmedicarePubcost;
	}
	public void setPmedicarePubcost(double pmedicarePubcost) {
		this.pmedicarePubcost = pmedicarePubcost;
	}
	public double getTurninCash() {
		return turninCash;
	}
	public void setTurninCash(double turninCash) {
		this.turninCash = turninCash;
	}
	public double getPrepayinvNum() {
		return prepayinvNum;
	}
	public void setPrepayinvNum(double prepayinvNum) {
		this.prepayinvNum = prepayinvNum;
	}
	public double getBalanceinvNum() {
		return balanceinvNum;
	}
	public void setBalanceinvNum(double balanceinvNum) {
		this.balanceinvNum = balanceinvNum;
	}
	public String getWasteprepayInvno() {
		return wasteprepayInvno;
	}
	public void setWasteprepayInvno(String wasteprepayInvno) {
		this.wasteprepayInvno = wasteprepayInvno;
	}
	public String getWastebalanceInvno() {
		return wastebalanceInvno;
	}
	public void setWastebalanceInvno(String wastebalanceInvno) {
		this.wastebalanceInvno = wastebalanceInvno;
	}
	public double getWasteprepayinvNum() {
		return wasteprepayinvNum;
	}
	public void setWasteprepayinvNum(double wasteprepayinvNum) {
		this.wasteprepayinvNum = wasteprepayinvNum;
	}
	public double getWastebalanceinvNum() {
		return wastebalanceinvNum;
	}
	public void setWastebalanceinvNum(double wastebalanceinvNum) {
		this.wastebalanceinvNum = wastebalanceinvNum;
	}
	public String getPrepayinvZone() {
		return prepayinvZone;
	}
	public void setPrepayinvZone(String prepayinvZone) {
		this.prepayinvZone = prepayinvZone;
	}
	public String getBalanceinvZone() {
		return balanceinvZone;
	}
	public void setBalanceinvZone(String balanceinvZone) {
		this.balanceinvZone = balanceinvZone;
	}
	public String getOperDept() {
		return operDept;
	}
	public void setOperDept(String operDept) {
		this.operDept = operDept;
	}
	public double getBalanceCost() {
		return balanceCost;
	}
	public void setBalanceCost(double balanceCost) {
		this.balanceCost = balanceCost;
	}
	public double getDebitHos() {
		return debitHos;
	}
	public void setDebitHos(double debitHos) {
		this.debitHos = debitHos;
	}
	public double getLenderHos() {
		return lenderHos;
	}
	public void setLenderHos(double lenderHos) {
		this.lenderHos = lenderHos;
	}
	public double getDebitOther() {
		return debitOther;
	}
	public void setDebitOther(double debitOther) {
		this.debitOther = debitOther;
	}
	public double getLenderOther() {
		return lenderOther;
	}
	public void setLenderOther(double lenderOther) {
		this.lenderOther = lenderOther;
	}
	public double getDerateCost() {
		return derateCost;
	}
	public void setDerateCost(double derateCost) {
		this.derateCost = derateCost;
	}
	public double getCoverCost() {
		return coverCost;
	}
	public void setCoverCost(double coverCost) {
		this.coverCost = coverCost;
	}
	public double getPoverCost() {
		return poverCost;
	}
	public void setPoverCost(double poverCost) {
		this.poverCost = poverCost;
	}
	public double getPoffiCost() {
		return poffiCost;
	}
	public void setPoffiCost(double poffiCost) {
		this.poffiCost = poffiCost;
	}
	public double getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(double checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getCheckOper() {
		return checkOper;
	}
	public void setCheckOper(String checkOper) {
		this.checkOper = checkOper;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public double getDebitCash() {
		return debitCash;
	}
	public void setDebitCash(double debitCash) {
		this.debitCash = debitCash;
	}
	public double getLenderCash() {
		return lenderCash;
	}
	public void setLenderCash(double lenderCash) {
		this.lenderCash = lenderCash;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
