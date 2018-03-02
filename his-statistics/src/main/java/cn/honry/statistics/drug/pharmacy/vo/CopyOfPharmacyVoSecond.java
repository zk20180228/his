package cn.honry.statistics.drug.pharmacy.vo;

import java.util.Date;
import java.util.List;



public class CopyOfPharmacyVoSecond {
	//药品编号
	private String drugCode;
	//药品名称
	private String drugName;
	//规格
	private String spec;
	//药品类别
	private String drugType;
	//当前状态
	private String outState;
	//摆药状态
	private String outType;
	//数量
	private Double outNum;
	//单位
	private String drugPackagingunit;
	//单价
	private Double drugRetailprice;
	//金额
	private Double outlCost;
	//药房
	private String drugDeptCode;
	/**建立时间**/
	private Date createTime;
	/**报表打印**/
	private List<CopyOfPharmacyVoSecond> pharmacyVoSecond;
	
	
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<CopyOfPharmacyVoSecond> getPharmacyVoSecond() {
		return pharmacyVoSecond;
	}
	public void setPharmacyVoSecond(List<CopyOfPharmacyVoSecond> pharmacyVoSecond) {
		this.pharmacyVoSecond = pharmacyVoSecond;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getOutState() {
		return outState;
	}
	public void setOutState(String outState) {
		this.outState = outState;
	}
	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public Double getOutNum() {
		return outNum;
	}
	public void setOutNum(Double outNum) {
		this.outNum = outNum;
	}
	public String getDrugPackagingunit() {
		return drugPackagingunit;
	}
	public void setDrugPackagingunit(String drugPackagingunit) {
		this.drugPackagingunit = drugPackagingunit;
	}
	public Double getDrugRetailprice() {
		return drugRetailprice;
	}
	public void setDrugRetailprice(Double drugRetailprice) {
		this.drugRetailprice = drugRetailprice;
	}
	public Double getOutlCost() {
		return outlCost;
	}
	public void setOutlCost(Double outlCost) {
		this.outlCost = outlCost;
	}
	@Override
	public String toString() {
		return "PharmacyVo [drugCode=" + drugCode + ", drugName=" + drugName
				+ ", spec=" + spec + ", drugType=" + drugType + ", outState="
				+ outState + ", outType=" + outType + ", outNum=" + outNum
				+ ", drugPackagingunit=" + drugPackagingunit
				+ ", drugRetailprice=" + drugRetailprice + ", outlCost="
				+ outlCost + ", drugDeptCode=" + drugDeptCode  + "]";
	}
	
	
}
