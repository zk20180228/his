package cn.honry.outpatient.advice.vo;

public class OutpatientVo {
	private String name;//项目名称
	private String spces;//规格
	private String dose;//基本剂量
	private String usage;//用法名称
	private String freq;//频次
	private String qty;//开立数量,每次用量单位名称
	private String rema;//备注
	private String recipeNo;//处方号
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpces() {
		return spces;
	}
	public void setSpces(String spces) {
		this.spces = spces;
	}
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getRema() {
		return rema;
	}
	public void setRema(String rema) {
		this.rema = rema;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	
}
