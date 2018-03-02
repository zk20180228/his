package cn.honry.finance.refund.vo;

public class RefundDrugList {
	/**药品名称**/
	private String itemName;
	/**组**/
	private String combNo;
	/**规格**/
	private String specs;
	/**数量**/
	private Double qty;
	/**单位**/
	private String unitPrice;
	/**可退数量**/
	private Double nobackNum;
	/**金额**/
	private Double totCost;
	/**单位**/
	private String doseUnit;
	/**ID**/
	private String id;
	/**标志**/
	private String confirmFlag;
	
	
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Double getNobackNum() {
		return nobackNum;
	}
	public void setNobackNum(Double nobackNum) {
		this.nobackNum = nobackNum;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(String confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	
	
}
