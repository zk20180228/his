package cn.honry.inpatient.inprePay.vo;
/**
 * @see 住院预交金打印
 * @author conglin
 *
 */
public class AcceptingGold {
	private String hospitalName;
	private String createTime;
	private String receiptNo;
	private String name;
	private String deptName;
	private String inpatientNo;
	private String costd;
	private String prepayCost;
	private String createUser;
	private String payWay;
	private String pactCode;
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
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
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getCostd() {
		return costd;
	}
	public void setCostd(String costd) {
		this.costd = costd;
	}
	public String getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(String prepayCost) {
		this.prepayCost = prepayCost;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	@Override
	public String toString() {
		return "AcceptingGold [hospitalName=" + hospitalName + ", createTime="
				+ createTime + ", receiptNo=" + receiptNo + ", name=" + name
				+ ", deptName=" + deptName + ", inpatientNo=" + inpatientNo
				+ ", costd=" + costd + ", prepayCost=" + prepayCost
				+ ", createUser=" + createUser + ", payWay=" + payWay
				+ ", pactCode=" + pactCode + "]";
	}
	

}
