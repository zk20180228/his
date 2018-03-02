package cn.honry.statistics.operation.recipelStat.vo;

public class RecipelInfoVo{
	
	/**商品名**/
	private String goodsName;
	/**每次量**/
	private String oneDosage;
	/**用法**/
	private String usage;
	/**频次**/
	private String frequency;
	/**总量**/
	private String gross;
	/**零售价**/
	private Double retailPrice;
	/**金额**/
	private Double money;
	/**剂数**/
	private Integer dosageNum;
	/**有效性**/
	private String validity;
	/**处方号**/
	private String recipeNo;
	
	/**getters and setters**/
	
	public String getGoodsName() {
		return goodsName;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getOneDosage() {
		return oneDosage;
	}
	public void setOneDosage(String oneDosage) {
		this.oneDosage = oneDosage;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getGross() {
		return gross;
	}
	public void setGross(String gross) {
		this.gross = gross;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getDosageNum() {
		return dosageNum;
	}
	public void setDosageNum(Integer dosageNum) {
		this.dosageNum = dosageNum;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	
}
