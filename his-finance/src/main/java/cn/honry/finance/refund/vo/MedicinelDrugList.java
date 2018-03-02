package cn.honry.finance.refund.vo;

public class MedicinelDrugList {
	/**药品名称**/
	private String itemName;
	/**组**/
	private String combNo;
	/**规格**/
	private String specs;
	/**数量**/
	private Double qty;
	/**单位**/
	private String priceUnit;
	/**可退数量**/
	private Double nobackNum;
	/**金额**/
	private Double totCost;
	/**每次用量**/
	private Double doseOnce;
	/**收费人**/
	private String feeCpcd;
	/**ID**/
	private String id;
	/**处方号**/
	private String recipeNo;
	/**药品标志**/
	private String drugFlag;
	/**执行科室**/
	private String execDpcd;
	/**单价**/
	private Double unitPrice;
	/**药品ID**/
	private String itemCode;
	/**包装**/
	private Integer extFlag3;
	/**处方内流水号**/
	private Integer sequenceNo;
	/**虚拟可退数量**/
	private Double nobackNums = 0.00;
	
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
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
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
	public Double getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}
	public String getFeeCpcd() {
		return feeCpcd;
	}
	public void setFeeCpcd(String feeCpcd) {
		this.feeCpcd = feeCpcd;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Integer getExtFlag3() {
		return extFlag3;
	}
	public void setExtFlag3(Integer extFlag3) {
		this.extFlag3 = extFlag3;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Double getNobackNums() {
		return nobackNums;
	}
	public void setNobackNums(Double nobackNums) {
		this.nobackNums = nobackNums;
	}
	
	
	
}
