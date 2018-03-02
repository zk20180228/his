package cn.honry.statistics.bi.bistac.toListView.vo;

public class RegisterWorkVo {
	private String deptCode;
	private String deptName;
	private String docCode;
	private String docName;
	
	private Integer ghzj=0;//挂号总计
	private Integer jzgh=0;//急诊挂号
	private Integer mzgh=0;//门诊挂号
	private Double feeCost=0.0;//挂号费
	private Integer cfs=0;//处方数
	
	
	private Double westernCost = 0.0;// 西药费 费用
	private Double chineseCost = 0.0;// 中成药费 费用
	private Double herbalCost = 0.0;// 中草药费 费用
	private Double allCost = 0.0;// 药品收入 费用
	
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
	private Double huliCost=0.0;//护理费
	private Double zhenchaCost=0.0;//诊查费
	private Double otherCost = 0.0;// 其他费 费用
	private Double medicalCost = 0.0;// 医疗收入 费用
	private Double totle = 0.0;// 收入合计
	
	
	public Double getHuliCost() {
		return huliCost;
	}
	public void setHuliCost(Double huliCost) {
		this.huliCost = huliCost;
	}
	public Double getZhenchaCost() {
		return zhenchaCost;
	}
	public void setZhenchaCost(Double zhenchaCost) {
		this.zhenchaCost = zhenchaCost;
	}
	public Integer getGhzj() {
		return ghzj;
	}
	public void setGhzj(Integer ghzj) {
		this.ghzj = ghzj;
	}
	public Integer getJzgh() {
		return jzgh;
	}
	public void setJzgh(Integer jzgh) {
		this.jzgh = jzgh;
	}
	public Integer getMzgh() {
		return mzgh;
	}
	public void setMzgh(Integer mzgh) {
		this.mzgh = mzgh;
	}
	public Double getFeeCost() {
		return feeCost;
	}
	public void setFeeCost(Double feeCost) {
		this.feeCost = feeCost;
	}
	public Integer getCfs() {
		return cfs;
	}
	public void setCfs(Integer cfs) {
		this.cfs = cfs;
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
	public Double getAllCost() {
		return allCost;
	}
	public void setAllCost(Double allCost) {
		this.allCost = allCost;
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
	public Double getMedicalCost() {
		return medicalCost;
	}
	public void setMedicalCost(Double medicalCost) {
		this.medicalCost = medicalCost;
	}
	public Double getTotle() {
		return totle;
	}
	public void setTotle(Double totle) {
		this.totle = totle;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	
}
