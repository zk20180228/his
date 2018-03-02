package cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo;

public class OutpatientDocRecipeVo {
	private String docCode;
	private String deptCode;
	private Integer peopleNum;
	private Integer recipeNum;
	private String feeName;
	private Double feeCost;
	private Double totalCost;
	private String areaCode;
	
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(Integer peopleNum) {
		this.peopleNum = peopleNum;
	}
	public Integer getRecipeNum() {
		return recipeNum;
	}
	public void setRecipeNum(Integer recipeNum) {
		this.recipeNum = recipeNum;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public Double getFeeCost() {
		return feeCost;
	}
	public void setFeeCost(Double feeCost) {
		this.feeCost = feeCost;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	
}
