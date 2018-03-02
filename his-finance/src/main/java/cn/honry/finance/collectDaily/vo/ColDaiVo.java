package cn.honry.finance.collectDaily.vo;

public class ColDaiVo {
	/** 统计大类信息  **/
	private String code;
	/** 费用金额总和  **/
	private double cost;
	/** 自费金额  总和  **/
	private double ownCost;
	/** 自付金额 总和  **/
	private double payCost;
	/** 公费金额  总和  **/
	private double pubCost;
	/** 数据1**/
	private double date1;
	/** 数据2**/
	private double date2;
	/** 正反交易标志 */
	private Integer transType;
	/** 医疗预收款（借方） **/
	private double balancePrepaycost;
	/** 医疗预收款（贷方） **/
	private double prepayCost;
	/** 医疗应收款（贷方） **/
	private double balanceCost;
	/** 银行存款借方金额，借方支票(银行存款借方)  **/
	private double debitCheck;
	/** 贷方支票(银行存款贷方)  **/
	private double lenderCheck;
	/** 现金（借方）  **/
	private double cashj;
	/** 现金（贷方）  **/
	private double cashd;
	/** 借方银行卡  **/
	private double debitBank;
	/** 贷方银行卡  **/
	private double lenderBank;
	/** 院内账户借方  **/
	private double debitHos;
	/** 院内账户贷方  **/
	private double lenderHos;
	/** 借方其它  **/
	private double debitOther;
	/** 贷方其它  **/
	private double lenderOther;
	/** 减免金额(借方)  **/
	private double derateCost;
	/** 公费记帐金额（借方）  **/
	private double busaryPubcost;
	/** 预交金发票张数  **/
	private double prepayinvNum;
	/** 预交金作废张数  **/
	private double wasteprepayinvNum;
	/** 预交金票据区间  **/
	private String prepayinvZone ;
	/** 作废预交金发票号码  **/
	private String wasteprepayInvno;
	/** 结算发票张数  **/
	private double balanceinvNum;
	/** 作废结算发票张数  **/
	private double wastebalanceinvNum;
	/** 结算发票区间  **/
	private String balanceinvZone;
	/** 作废结算发票号码  **/
	private String wastebalanceInvno;
	/** 上缴现金 **/
	private double turninCash;
	
	public double getTurninCash() {
		return turninCash;
	}
	public void setTurninCash(double turninCash) {
		this.turninCash = turninCash;
	}
	public double getBalanceCost() {
		return balanceCost;
	}
	public void setBalanceCost(double balanceCost) {
		this.balanceCost = balanceCost;
	}
	public double getDebitCheck() {
		return debitCheck;
	}
	public void setDebitCheck(double debitCheck) {
		this.debitCheck = debitCheck;
	}
	public double getLenderCheck() {
		return lenderCheck;
	}
	public void setLenderCheck(double lenderCheck) {
		this.lenderCheck = lenderCheck;
	}
	public double getCashj() {
		return cashj;
	}
	public void setCashj(double cashj) {
		this.cashj = cashj;
	}
	public double getCashd() {
		return cashd;
	}
	public void setCashd(double cashd) {
		this.cashd = cashd;
	}
	public double getDebitBank() {
		return debitBank;
	}
	public void setDebitBank(double debitBank) {
		this.debitBank = debitBank;
	}
	public double getLenderBank() {
		return lenderBank;
	}
	public void setLenderBank(double lenderBank) {
		this.lenderBank = lenderBank;
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
	public double getBusaryPubcost() {
		return busaryPubcost;
	}
	public void setBusaryPubcost(double busaryPubcost) {
		this.busaryPubcost = busaryPubcost;
	}
	public double getPrepayinvNum() {
		return prepayinvNum;
	}
	public void setPrepayinvNum(double prepayinvNum) {
		this.prepayinvNum = prepayinvNum;
	}
	public double getWasteprepayinvNum() {
		return wasteprepayinvNum;
	}
	public void setWasteprepayinvNum(double wasteprepayinvNum) {
		this.wasteprepayinvNum = wasteprepayinvNum;
	}
	public String getPrepayinvZone() {
		return prepayinvZone;
	}
	public void setPrepayinvZone(String prepayinvZone) {
		this.prepayinvZone = prepayinvZone;
	}
	public String getWasteprepayInvno() {
		return wasteprepayInvno;
	}
	public void setWasteprepayInvno(String wasteprepayInvno) {
		this.wasteprepayInvno = wasteprepayInvno;
	}
	public double getBalanceinvNum() {
		return balanceinvNum;
	}
	public void setBalanceinvNum(double balanceinvNum) {
		this.balanceinvNum = balanceinvNum;
	}
	public double getWastebalanceinvNum() {
		return wastebalanceinvNum;
	}
	public void setWastebalanceinvNum(double wastebalanceinvNum) {
		this.wastebalanceinvNum = wastebalanceinvNum;
	}
	public String getBalanceinvZone() {
		return balanceinvZone;
	}
	public void setBalanceinvZone(String balanceinvZone) {
		this.balanceinvZone = balanceinvZone;
	}
	public String getWastebalanceInvno() {
		return wastebalanceInvno;
	}
	public void setWastebalanceInvno(String wastebalanceInvno) {
		this.wastebalanceInvno = wastebalanceInvno;
	}
	public double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public double getBalancePrepaycost() {
		return balancePrepaycost;
	}
	public void setBalancePrepaycost(double balancePrepaycost) {
		this.balancePrepaycost = balancePrepaycost;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public double getDate1() {
		return date1;
	}
	public void setDate1(double date1) {
		this.date1 = date1;
	}
	public double getDate2() {
		return date2;
	}
	public void setDate2(double date2) {
		this.date2 = date2;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(double ownCost) {
		this.ownCost = ownCost;
	}
	public double getPayCost() {
		return payCost;
	}
	public void setPayCost(double payCost) {
		this.payCost = payCost;
	}
	public double getPubCost() {
		return pubCost;
	}
	public void setPubCost(double pubCost) {
		this.pubCost = pubCost;
	}
	
	
}
