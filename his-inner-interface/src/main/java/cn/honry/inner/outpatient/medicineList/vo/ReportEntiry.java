package cn.honry.inner.outpatient.medicineList.vo;

/**
 * @Description  打印收费发票实体
 * @author  marongbin
 * @createDate： 2016年12月12日 下午4:29:15 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public class ReportEntiry {
	private String invoiceNo;
	private Double payCost;
	private Double pubCost;
	private Double ownCost;
	private String chineseCost;
	private String sex;
	private String name;
	private String payKind;
	
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public String getChineseCost() {
		return chineseCost;
	}
	public void setChineseCost(String chineseCost) {
		this.chineseCost = chineseCost;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayKind() {
		return payKind;
	}
	public void setPayKind(String payKind) {
		this.payKind = payKind;
	}
	
}
