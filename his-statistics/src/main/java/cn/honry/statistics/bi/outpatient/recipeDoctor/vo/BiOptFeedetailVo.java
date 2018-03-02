package cn.honry.statistics.bi.outpatient.recipeDoctor.vo;

public class BiOptFeedetailVo{
	//科室
	private String deptName;
	//处方数量
	private String recipeCount;
	//处方金额
	private String recipeCose;
	//开方日期
	private String openAdviceDate;
	//搜索开始时间
	private String beginDate;
	//搜索结束时间
	private String endDate;
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getRecipeCount() {
		return recipeCount;
	}
	public void setRecipeCount(String recipeCount) {
		this.recipeCount = recipeCount;
	}
	public String getRecipeCose() {
		return recipeCose;
	}
	public void setRecipeCose(String recipeCose) {
		this.recipeCose = recipeCose;
	}
	public String getOpenAdviceDate() {
		return openAdviceDate;
	}
	public void setOpenAdviceDate(String openAdviceDate) {
		this.openAdviceDate = openAdviceDate;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
