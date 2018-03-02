package cn.honry.inner.statistics.outpatientDocRecipe.vo;

public class OutpatientDocRecipeVo {
	/** 
	* 医生凑得
	*/ 
	private String docCode;
	/** 
	* 科室code
	*/ 
	private String deptCode;
	/** 
	* 看诊数 
	*/ 
	private Integer peopleNum;
	/** 
	*开单数
	*/ 
	private Integer recipeNum;
	/** 
	* 费别
	*/ 
	private String feeName;
	/** 
	*费别输入
	*/ 
	private Double feeCost;
	/** 
	*总收入 
	*/ 
	private Double totalCost;
	/** 
	*院区
	*/ 
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
