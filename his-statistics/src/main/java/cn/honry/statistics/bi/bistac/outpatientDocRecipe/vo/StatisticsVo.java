package cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo;

public class StatisticsVo {
	
	private String docCode;
	private String deptCode;
	private Integer peopleNum;
	private Integer recipeNum;
	
	private Double westernCost = 0.0;// 西药费 费用
	private Double chineseCost = 0.0;// 中成药费 费用
	private Double herbalCost = 0.0;// 中草药费 费用

	private Double chuangweiCost = 0.0;// 床位费 费用
	private Double treatmentCost = 0.0;// 治疗费 费用
	private Double inspectCost = 0.0;// 检查费 费用
	private Double radiationCost = 0.0;// 放射费 费用
	private Double testCost = 0.0;// 化验费 费用
	private Double shoushuCost = 0.0;// 手术费 费用
	private Double bloodCost = 0.0;// 输血费 费用
	private Double o2Cost = 0.0;// 输氧费 费用
	private Double cailiaoCost = 0.0;// 材料费 费用
	private Double yimiaoCost = 0.0;// 疫苗费 费用
	private Double otherCost = 0.0;// 其他费 费用
	private Double deptCost = 0.0;// 其他费 费用
	private Double totleCost = 0.0;// 收入合计

	
	public Double getDeptCost() {
		return deptCost;
	}

	public void setDeptCost(Double deptCost) {
		this.deptCost = deptCost;
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

	public Double getWesternCost() {
		return westernCost;
	}

	public void setWesternCost(Double westernCost) {
		this.westernCost = westernCost;
	}

	public Double getChineseCost() {
		return chineseCost;
	}

	public void setChineseCost(Double chineseCost) {
		this.chineseCost = chineseCost;
	}

	public Double getHerbalCost() {
		return herbalCost;
	}

	public void setHerbalCost(Double herbalCost) {
		this.herbalCost = herbalCost;
	}

	public Double getChuangweiCost() {
		return chuangweiCost;
	}

	public void setChuangweiCost(Double chuangweiCost) {
		this.chuangweiCost = chuangweiCost;
	}

	public Double getTreatmentCost() {
		return treatmentCost;
	}

	public void setTreatmentCost(Double treatmentCost) {
		this.treatmentCost = treatmentCost;
	}

	public Double getInspectCost() {
		return inspectCost;
	}

	public void setInspectCost(Double inspectCost) {
		this.inspectCost = inspectCost;
	}

	public Double getRadiationCost() {
		return radiationCost;
	}

	public void setRadiationCost(Double radiationCost) {
		this.radiationCost = radiationCost;
	}

	public Double getTestCost() {
		return testCost;
	}

	public void setTestCost(Double testCost) {
		this.testCost = testCost;
	}

	public Double getShoushuCost() {
		return shoushuCost;
	}

	public void setShoushuCost(Double shoushuCost) {
		this.shoushuCost = shoushuCost;
	}

	public Double getBloodCost() {
		return bloodCost;
	}

	public void setBloodCost(Double bloodCost) {
		this.bloodCost = bloodCost;
	}

	public Double getO2Cost() {
		return o2Cost;
	}

	public void setO2Cost(Double o2Cost) {
		this.o2Cost = o2Cost;
	}

	public Double getCailiaoCost() {
		return cailiaoCost;
	}

	public void setCailiaoCost(Double cailiaoCost) {
		this.cailiaoCost = cailiaoCost;
	}

	public Double getYimiaoCost() {
		return yimiaoCost;
	}

	public void setYimiaoCost(Double yimiaoCost) {
		this.yimiaoCost = yimiaoCost;
	}

	public Double getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(Double otherCost) {
		this.otherCost = otherCost;
	}

	public Double getTotleCost() {
		return totleCost;
	}

	public void setTotleCost(Double totleCost) {
		this.totleCost = totleCost;
	}
	


}
