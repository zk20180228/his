package cn.honry.statistics.sys.invoiceChecks.vo;

import java.util.Date;
import java.util.List;

public class VinpatirntInfoBalance {
	/**患者姓名**/
	private String patientName;
	/**住院号**/
	private String medicalrecordId;
	/**合同单位**/
	private String pactCode;
	/**入院时间**/
	private Date inDate;
	/**发票号**/
	private String invoiceNo;
	/**结算时间**/
	private Date balanceDate;
	/**预存金**/
	private Double prepayCost;
	/**返还金**/
	private Double returnCost;
	/**费用总额**/
	private Double totCost;
	/**性别**/
	private String sex;
	/**床号**/
	private String bedId;
	/**统计大类**/
	private String statName;
	/**统计大类费用**/
	private Double smallTot;
	/**报表需要用的的数组**/
	private List<VinpatirntInfoBalance> vinpatirntInfoBalance;
	
	public List<VinpatirntInfoBalance> getVinpatirntInfoBalance() {
		return vinpatirntInfoBalance;
	}
	public void setVinpatirntInfoBalance(
			List<VinpatirntInfoBalance> vinpatirntInfoBalance) {
		this.vinpatirntInfoBalance = vinpatirntInfoBalance;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getStatName() {
		return statName;
	}
	public void setStatName(String statName) {
		this.statName = statName;
	}
	public Double getSmallTot() {
		return smallTot;
	}
	public void setSmallTot(Double smallTot) {
		this.smallTot = smallTot;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public Double getReturnCost() {
		return returnCost;
	}
	public void setReturnCost(Double returnCost) {
		this.returnCost = returnCost;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	@Override
	public String toString() {
		return "VinpatirntInfoBalance [patientName=" + patientName
				+ ", medicalrecordId=" + medicalrecordId + ", pactCode="
				+ pactCode + ", inDate=" + inDate + ", invoiceNo=" + invoiceNo
				+ ", balanceDate=" + balanceDate + ", prepayCost=" + prepayCost
				+ ", returnCost=" + returnCost + ", totCost=" + totCost
				+ ", sex=" + sex + ", bedId=" + bedId + ", statName="
				+ statName + ", smallTot=" + smallTot
				+ ", vinpatirntInfoBalance=" + vinpatirntInfoBalance + "]";
	}
	
	
}
