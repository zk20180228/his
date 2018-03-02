package cn.honry.statistics.sys.reportForms.vo;


public class IncomeVo {
	private String dates; //有该医生的排班时间
	private String userId;//userID
	private String deptId;//deptID
	private Integer inspectNum;//检查费  开单数
	private Double inspectCost;//检查费  费用
	private Integer treatmentNum;//治疗费  开单数
	private Double treatmentCost;//治疗费  费用
	private Integer radiationNum;//放射费  开单数
	private Double radiationCost;//放射费  费用
	private Integer testNum;//化验费  开单数
	private Double testCost;//化验费  费用
	private Integer bloodNum;//输血费  开单数
	private Double bloodCost;//输血费  费用
	private Integer otherNum;//其他费  开单数
	private Double otherCost;//其他费  费用
	private Integer medicalNum;//医疗收入  开单总数
	private Double medicalCost;//医疗收入  费用
	private Integer westernNum;//西药费  开单总数
	private Double westernCost;//西药费  费用
	private Integer chineseNum;//中成药费  开单总数
	private Double chineseCost;//中成药费  费用
	private Integer herbalNum;//中草药费  开单总数
	private Double herbalCost;//中草药费  费用
	private Integer allNum;//药品收入  开单总数
	private Double allCost;//药品收入  费用
	private Double totle;//收入合计
	
	private String invoiceNo;//发票号
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public Integer getInspectNum() {
		return inspectNum;
	}
	public void setInspectNum(Integer inspectNum) {
		this.inspectNum = inspectNum;
	}
	public Double getInspectCost() {
		return inspectCost;
	}
	public void setInspectCost(Double inspectCost) {
		this.inspectCost = inspectCost;
	}
	public Integer getTreatmentNum() {
		return treatmentNum;
	}
	public void setTreatmentNum(Integer treatmentNum) {
		this.treatmentNum = treatmentNum;
	}
	public Double getTreatmentCost() {
		return treatmentCost;
	}
	public void setTreatmentCost(Double treatmentCost) {
		this.treatmentCost = treatmentCost;
	}
	public Integer getRadiationNum() {
		return radiationNum;
	}
	public void setRadiationNum(Integer radiationNum) {
		this.radiationNum = radiationNum;
	}
	public Double getRadiationCost() {
		return radiationCost;
	}
	public void setRadiationCost(Double radiationCost) {
		this.radiationCost = radiationCost;
	}
	public Integer getTestNum() {
		return testNum;
	}
	public void setTestNum(Integer testNum) {
		this.testNum = testNum;
	}
	public Double getTestCost() {
		return testCost;
	}
	public void setTestCost(Double testCost) {
		this.testCost = testCost;
	}
	public Integer getBloodNum() {
		return bloodNum;
	}
	public void setBloodNum(Integer bloodNum) {
		this.bloodNum = bloodNum;
	}
	public Double getBloodCost() {
		return bloodCost;
	}
	public void setBloodCost(Double bloodCost) {
		this.bloodCost = bloodCost;
	}
	public Integer getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(Integer otherNum) {
		this.otherNum = otherNum;
	}
	public Double getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(Double otherCost) {
		this.otherCost = otherCost;
	}
	public Integer getMedicalNum() {
		return medicalNum;
	}
	public void setMedicalNum(Integer medicalNum) {
		this.medicalNum = medicalNum;
	}
	public Double getMedicalCost() {
		return medicalCost;
	}
	public void setMedicalCost(Double medicalCost) {
		this.medicalCost = medicalCost;
	}
	public Integer getWesternNum() {
		return westernNum;
	}
	public void setWesternNum(Integer westernNum) {
		this.westernNum = westernNum;
	}
	public Double getWesternCost() {
		return westernCost;
	}
	public void setWesternCost(Double westernCost) {
		this.westernCost = westernCost;
	}
	public Integer getChineseNum() {
		return chineseNum;
	}
	public void setChineseNum(Integer chineseNum) {
		this.chineseNum = chineseNum;
	}
	public Double getChineseCost() {
		return chineseCost;
	}
	public void setChineseCost(Double chineseCost) {
		this.chineseCost = chineseCost;
	}
	public Integer getHerbalNum() {
		return herbalNum;
	}
	public void setHerbalNum(Integer herbalNum) {
		this.herbalNum = herbalNum;
	}
	public Double getHerbalCost() {
		return herbalCost;
	}
	public void setHerbalCost(Double herbalCost) {
		this.herbalCost = herbalCost;
	}
	public Integer getAllNum() {
		return allNum;
	}
	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}
	public Double getAllCost() {
		return allCost;
	}
	public void setAllCost(Double allCost) {
		this.allCost = allCost;
	}
	public Double getTotle() {
		return totle;
	}
	public void setTotle(Double totle) {
		this.totle = totle;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	
	

}
