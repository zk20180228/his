package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;



/** 
* @ClassName: Wages 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author zx
* @date 2017年7月18日
*  
*/
public class OaWages extends Entity {
	
	/** 
	*/ 
	private static final long serialVersionUID = 1L;
	/** * id */ 
	private String wagesId;
	/** * 工资月份 */
	private Date wagesTime;
	/** * 工资号 */ 
	private String wagesAccount;
	/** * 姓名 */ 
	private String name;
	/** * 部门*/ 
	private String deptName;
	/** * 人员类别 */ 
	private String category;
	/** * 岗位工资 */ 
	private String postPay;
	/** * 薪级工资 */ 
	private String basePay;
	/** * 救护10*/ 
	private String nursinTeach;
	/** * 绩效*/ 
	private String achievements;
	/** * 救护*/ /** *  */ 
	private String nursinTeaching;
	/**保留项**/
	private String keepThink;
	/**卫津**/
	private String healthAllowance;
	/** 独子费**/
	private String onlyChildFee;
	/**卫生费 **/
	private String hygieneFee;
	/** 博导贴**/
	private String PHDFee;
	/** 补工资**/
	private String subsidyFee;
	/** 预增资201410**/
	private String increased;
	/** 预增资20160607**/
	private String increasing;
	/**应发合计**/
	private String totalShould;
	/**扣房租**/
	private String deductRent;
	/**公积金**/
	private String housingFund;
	/**扣托费**/
	private String boardingFee;
	/** * 医疗保险 */
	private String medicalInsurance;
	/**统筹金**/
	private String overallPlanning;
	/**失业险**/
	private String unemploymentInsurance;
	/**扣工资**/
	private String deductWages;
	/**代扣暖气费**/
	private String heatingCosts;
	/**收账**/
	private String accountEeceivable;
	/**实发工资**/
	private String totalActual;
	/**公积金账号**/
	private String providentFundAccount;
	/**身份证号码**/
	private String IDCard;
	public String getWagesId() {
		return wagesId;
	}
	public void setWagesId(String wagesId) {
		this.wagesId = wagesId;
	}
	public Date getWagesTime() {
		return wagesTime;
	}
	public void setWagesTime(Date wagesTime) {
		this.wagesTime = wagesTime;
	}
	public String getWagesAccount() {
		return wagesAccount;
	}
	public void setWagesAccount(String wagesAccount) {
		this.wagesAccount = wagesAccount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPostPay() {
		return postPay;
	}
	public void setPostPay(String postPay) {
		this.postPay = postPay;
	}
	public String getBasePay() {
		return basePay;
	}
	public void setBasePay(String basePay) {
		this.basePay = basePay;
	}
	public String getNursinTeach() {
		return nursinTeach;
	}
	public void setNursinTeach(String nursinTeach) {
		this.nursinTeach = nursinTeach;
	}
	public String getAchievements() {
		return achievements;
	}
	public void setAchievements(String achievements) {
		this.achievements = achievements;
	}
	public String getNursinTeaching() {
		return nursinTeaching;
	}
	public void setNursinTeaching(String nursinTeaching) {
		this.nursinTeaching = nursinTeaching;
	}
	public String getKeepThink() {
		return keepThink;
	}
	public void setKeepThink(String keepThink) {
		this.keepThink = keepThink;
	}
	public String getHealthAllowance() {
		return healthAllowance;
	}
	public void setHealthAllowance(String healthAllowance) {
		this.healthAllowance = healthAllowance;
	}
	public String getOnlyChildFee() {
		return onlyChildFee;
	}
	public void setOnlyChildFee(String onlyChildFee) {
		this.onlyChildFee = onlyChildFee;
	}
	public String getHygieneFee() {
		return hygieneFee;
	}
	public void setHygieneFee(String hygieneFee) {
		this.hygieneFee = hygieneFee;
	}
	public String getPHDFee() {
		return PHDFee;
	}
	public void setPHDFee(String pHDFee) {
		PHDFee = pHDFee;
	}
	public String getSubsidyFee() {
		return subsidyFee;
	}
	public void setSubsidyFee(String subsidyFee) {
		this.subsidyFee = subsidyFee;
	}
	public String getIncreased() {
		return increased;
	}
	public void setIncreased(String increased) {
		this.increased = increased;
	}
	public String getIncreasing() {
		return increasing;
	}
	public void setIncreasing(String increasing) {
		this.increasing = increasing;
	}
	public String getTotalShould() {
		return totalShould;
	}
	public void setTotalShould(String totalShould) {
		this.totalShould = totalShould;
	}
	public String getDeductRent() {
		return deductRent;
	}
	public void setDeductRent(String deductRent) {
		this.deductRent = deductRent;
	}
	public String getHousingFund() {
		return housingFund;
	}
	public void setHousingFund(String housingFund) {
		this.housingFund = housingFund;
	}
	public String getBoardingFee() {
		return boardingFee;
	}
	public void setBoardingFee(String boardingFee) {
		this.boardingFee = boardingFee;
	}
	public String getMedicalInsurance() {
		return medicalInsurance;
	}
	public void setMedicalInsurance(String medicalInsurance) {
		this.medicalInsurance = medicalInsurance;
	}
	public String getOverallPlanning() {
		return overallPlanning;
	}
	public void setOverallPlanning(String overallPlanning) {
		this.overallPlanning = overallPlanning;
	}
	public String getUnemploymentInsurance() {
		return unemploymentInsurance;
	}
	public void setUnemploymentInsurance(String unemploymentInsurance) {
		this.unemploymentInsurance = unemploymentInsurance;
	}
	public String getDeductWages() {
		return deductWages;
	}
	public void setDeductWages(String deductWages) {
		this.deductWages = deductWages;
	}
	public String getHeatingCosts() {
		return heatingCosts;
	}
	public void setHeatingCosts(String heatingCosts) {
		this.heatingCosts = heatingCosts;
	}
	public String getAccountEeceivable() {
		return accountEeceivable;
	}
	public void setAccountEeceivable(String accountEeceivable) {
		this.accountEeceivable = accountEeceivable;
	}
	public String getTotalActual() {
		return totalActual;
	}
	public void setTotalActual(String totalActual) {
		this.totalActual = totalActual;
	}
	public String getProvidentFundAccount() {
		return providentFundAccount;
	}
	public void setProvidentFundAccount(String providentFundAccount) {
		this.providentFundAccount = providentFundAccount;
	}
	public String getIDCard() {
		return IDCard;
	}
	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}
	
	

}