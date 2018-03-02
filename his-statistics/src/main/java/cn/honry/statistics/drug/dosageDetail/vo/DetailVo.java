package cn.honry.statistics.drug.dosageDetail.vo;

public class DetailVo {
	//工作号
	private String jobNo;
	//配药人
	private String drugedOper;
	//配药终端
	private String drugedTerminal;
	//配药终端Id
	private String drugedTerminalID;
	//处方数
	private Double recipeCount;
	//处方中药品数量
	private Double recipeQty;
	//处方金额(零售金额)
	private Double recipeCost;
	//处方内药品剂数合计
	private Double sumDays;
	
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getDrugedOper() {
		return drugedOper;
	}
	public void setDrugedOper(String drugedOper) {
		this.drugedOper = drugedOper;
	}
	public String getDrugedTerminal() {
		return drugedTerminal;
	}
	public void setDrugedTerminal(String drugedTerminal) {
		this.drugedTerminal = drugedTerminal;
	}
	public Double getRecipeCount() {
		return recipeCount;
	}
	public void setRecipeCount(Double recipeCount) {
		this.recipeCount = recipeCount;
	}
	public Double getRecipeQty() {
		return recipeQty;
	}
	public void setRecipeQty(Double recipeQty) {
		this.recipeQty = recipeQty;
	}
	public Double getRecipeCost() {
		return recipeCost;
	}
	public void setRecipeCost(Double recipeCost) {
		this.recipeCost = recipeCost;
	}
	public Double getSumDays() {
		return sumDays;
	}
	public void setSumDays(Double sumDays) {
		this.sumDays = sumDays;
	}
	public String getDrugedTerminalID() {
		return drugedTerminalID;
	}
	public void setDrugedTerminalID(String drugedTerminalID) {
		this.drugedTerminalID = drugedTerminalID;
	}
	
}
