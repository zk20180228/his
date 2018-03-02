package cn.honry.statistics.sys.reportForms.vo;

public class StatisticsVo {

	private String name;// 姓名
	private String nameId;// 姓名
	private String userid;// 用户id
	private String userName;// 用户
	private String dept;// 科室
	private String deptId;// 科室
	private Integer westernNum = 0;// 西药费 开单总数
	private Double westernCost = 0.0;// 西药费 费用
	private Integer chineseNum = 0;// 中成药费 开单总数
	private Double chineseCost = 0.0;// 中成药费 费用
	private Integer herbalNum = 0;// 中草药费 开单总数
	private Double herbalCost = 0.0;// 中草药费 费用
	private Integer allNum = 0;// 药品收入 开单总数
	private Double allCost = 0.0;// 药品收入 费用

	private Integer chuangweiNum = 0;// 床位费 开单总数
	private Double chuangweiCost = 0.0;// 床位费 费用
	private Integer treatmentNum = 0;// 治疗费 开单数
	private Double treatmentCost = 0.0;// 治疗费 费用
	private Integer inspectNum = 0;// 检查费 开单数
	private Double inspectCost = 0.0;// 检查费 费用
	private Integer radiationNum = 0;// 放射费 开单数
	private Double radiationCost = 0.0;// 放射费 费用
	private Integer testNum = 0;// 化验费 开单数
	private Double testCost = 0.0;// 化验费 费用
	private Integer shoushuNum = 0;// 手术费 开单数
	private Double shoushuCost = 0.0;// 手术费 费用
	private Integer bloodNum = 0;// 输血费 开单数
	private Double bloodCost = 0.0;// 输血费 费用
	private Integer o2Num = 0;// 输氧费 开单数
	private Double o2Cost = 0.0;// 输氧费 费用
	private Integer cailiaoNum = 0;// 材料费 开单数
	private Double cailiaoCost = 0.0;// 材料费 费用
	private Integer yimiaoNum = 0;// 疫苗费 开单数
	private Double yimiaoCost = 0.0;// 疫苗费 费用
	private Integer otherNum = 0;// 其他费 开单数
	private Double otherCost = 0.0;// 其他费 费用
	private Integer medicalNum = 0;// 医疗收入 开单总数
	private Double medicalCost = 0.0;// 医疗收入 费用

	private Double totle = 0.0;// 收入合计
	private String regDate;// 挂号日期
	private String deptCode;// 科室的编号
	private String docterCode;// 医生的编号

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameId() {
		return nameId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	public Integer getChuangweiNum() {
		return chuangweiNum;
	}

	public void setChuangweiNum(Integer chuangweiNum) {
		this.chuangweiNum = chuangweiNum;
	}

	public Double getChuangweiCost() {
		return chuangweiCost;
	}

	public void setChuangweiCost(Double chuangweiCost) {
		this.chuangweiCost = chuangweiCost;
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

	public Integer getShoushuNum() {
		return shoushuNum;
	}

	public void setShoushuNum(Integer shoushuNum) {
		this.shoushuNum = shoushuNum;
	}

	public Double getShoushuCost() {
		return shoushuCost;
	}

	public void setShoushuCost(Double shoushuCost) {
		this.shoushuCost = shoushuCost;
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

	public Integer getO2Num() {
		return o2Num;
	}

	public void setO2Num(Integer o2Num) {
		this.o2Num = o2Num;
	}

	public Double getO2Cost() {
		return o2Cost;
	}

	public void setO2Cost(Double o2Cost) {
		this.o2Cost = o2Cost;
	}

	public Integer getCailiaoNum() {
		return cailiaoNum;
	}

	public void setCailiaoNum(Integer cailiaoNum) {
		this.cailiaoNum = cailiaoNum;
	}

	public Double getCailiaoCost() {
		return cailiaoCost;
	}

	public void setCailiaoCost(Double cailiaoCost) {
		this.cailiaoCost = cailiaoCost;
	}

	public Integer getYimiaoNum() {
		return yimiaoNum;
	}

	public void setYimiaoNum(Integer yimiaoNum) {
		this.yimiaoNum = yimiaoNum;
	}

	public Double getYimiaoCost() {
		return yimiaoCost;
	}

	public void setYimiaoCost(Double yimiaoCost) {
		this.yimiaoCost = yimiaoCost;
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

	public Double getTotle() {
		return totle;
	}

	public void setTotle(Double totle) {
		this.totle = totle;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDocterCode() {
		return docterCode;
	}

	public void setDocterCode(String docterCode) {
		this.docterCode = docterCode;
	}

}
