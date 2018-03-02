package cn.honry.inpatient.admission.vo;

import cn.honry.base.bean.model.BusinessBedward;

public class AdmissionVO {
    //住院主表信息
	private String inState;//住院状态  用于页面判断是否可点击保存操作 只有登记状态时才能做接诊
	private String oldMedicalrecordId;//病例号 用于更新患者信息表
    private String patientInfoId;//住院主表id 非页面显示内容
    private String patientName;//患者姓名
    private String sex;//性别  下拉
    private String blNumber;//病例号
    private String settlement;//结算方式 只读 不更新
    private String birthArea;//出生地
    private String country;//国籍 下拉 
    private String nation;//民族 下拉
    private String reportBirthday;//出生日期
    private Integer reportAge;//年龄
    private String dist;//籍贯
    private String occupation;//职业  下拉
    private String certificatesType;//证件类型
    private String certificatesNo;//身份证号码
    private String workName;//工作单位
    private String workTel;//单位电话
    private String marry;//婚姻状况 下拉
    private String home;//家庭地址
    private String homeTel;//家庭电话
    //入院登记信息
	/**住院流水号*/
	private String inpatientNo;
    private String source;//入院来源 下拉
    private String inAvenue;//入院途径
    private String inCircs;//入院情况
    private Integer feeInterval;//床费间隔天数  默认为1天 
    private String inDate;//入院日期   只读 不更新
    private String inDoctor;//收住医师
    private String remark;//备注
    private String diagName;//入院诊断
    private Integer haveBabyFlag;//是否有婴儿
    //病床使用记录表
    /**医师代码(住院)*/
	private String houseDocCode;
	/**医师代码(主治)*/
	private String chargeDocCode;
	/**医师代码(主任)*/
	private String chiefDocCode;
	/**护士代码(责任)*/
	private String dutyNurseCode;
    //生命体征信息
    private String temperature;//体温
    private String pulse;//脉搏
    private String breath;//呼吸
    private String pressure;//血压
    private Double weight;//体重
    private String postureDate;//录入时间
    private String bedId;//病床使用记录表主键ID
    private String bednumber;//病床表主键ID
    
    /**医师代码(住院)*/
   	private String houseDocName;
   	/**医师代码(主治)*/
   	private String chargeDocName;
   	/**医师代码(主任)*/
   	private String chiefDocName;
   	/**护士代码(责任)*/
   	private String dutyNurseName;
    
    public String getBednumber() {
		return bednumber;
	}
	public void setBednumber(String bednumber) {
		this.bednumber = bednumber;
	}
	/**病房编号   **/
	private BusinessBedward businessBedward;
    private String bedName;//床位名称 只用作显示
    private String bedNo;//病床ID
    private String bingqu;//病区
    
	
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public Integer getHaveBabyFlag() {
		return haveBabyFlag;
	}
	public void setHaveBabyFlag(Integer haveBabyFlag) {
		this.haveBabyFlag = haveBabyFlag;
	}
	public String getBingqu() {
		return bingqu;
	}
	public void setBingqu(String bingqu) {
		this.bingqu = bingqu;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getHouseDocCode() {
		return houseDocCode;
	}
	public void setHouseDocCode(String houseDocCode) {
		this.houseDocCode = houseDocCode;
	}
	public String getChargeDocCode() {
		return chargeDocCode;
	}
	public void setChargeDocCode(String chargeDocCode) {
		this.chargeDocCode = chargeDocCode;
	}
	public String getChiefDocCode() {
		return chiefDocCode;
	}
	public void setChiefDocCode(String chiefDocCode) {
		this.chiefDocCode = chiefDocCode;
	}
	public String getDutyNurseCode() {
		return dutyNurseCode;
	}
	public void setDutyNurseCode(String dutyNurseCode) {
		this.dutyNurseCode = dutyNurseCode;
	}
	public BusinessBedward getBusinessBedward() {
		return businessBedward;
	}
	public void setBusinessBedward(BusinessBedward businessBedward) {
		this.businessBedward = businessBedward;
	}
	public String getPatientInfoId() {
		return patientInfoId;
	}
	public void setPatientInfoId(String patientInfoId) {
		this.patientInfoId = patientInfoId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBlNumber() {
		return blNumber;
	}
	public void setBlNumber(String blNumber) {
		this.blNumber = blNumber;
	}
	public String getSettlement() {
		return settlement;
	}
	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}
	public String getBirthArea() {
		return birthArea;
	}
	public void setBirthArea(String birthArea) {
		this.birthArea = birthArea;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getReportBirthday() {
		return reportBirthday;
	}
	public void setReportBirthday(String reportBirthday) {
		this.reportBirthday = reportBirthday;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getCertificatesNo() {
		return certificatesNo;
	}
	public void setCertificatesNo(String certificatesNo) {
		this.certificatesNo = certificatesNo;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getWorkTel() {
		return workTel;
	}
	public void setWorkTel(String workTel) {
		this.workTel = workTel;
	}
	public String getMarry() {
		return marry;
	}
	public void setMarry(String marry) {
		this.marry = marry;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getHomeTel() {
		return homeTel;
	}
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getInAvenue() {
		return inAvenue;
	}
	public void setInAvenue(String inAvenue) {
		this.inAvenue = inAvenue;
	}
	public String getInCircs() {
		return inCircs;
	}
	public void setInCircs(String inCircs) {
		this.inCircs = inCircs;
	}
	
	public String getInDoctor() {
		return inDoctor;
	}
	public void setInDoctor(String inDoctor) {
		this.inDoctor = inDoctor;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	public String getBreath() {
		return breath;
	}
	public void setBreath(String breath) {
		this.breath = breath;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getPostureDate() {
		return postureDate;
	}
	public void setPostureDate(String postureDate) {
		this.postureDate = postureDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInState(String inState) {
		this.inState = inState;
	}
	public String getInState() {
		return inState;
	}
	public void setFeeInterval(Integer feeInterval) {
		this.feeInterval = feeInterval;
	}
	public Integer getFeeInterval() {
		return feeInterval;
	}
	public void setReportAge(Integer reportAge) {
		this.reportAge = reportAge;
	}
	public Integer getReportAge() {
		return reportAge;
	}
	public void setOldMedicalrecordId(String oldMedicalrecordId) {
		this.oldMedicalrecordId = oldMedicalrecordId;
	}
	public String getOldMedicalrecordId() {
		return oldMedicalrecordId;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getWeight() {
		return weight;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getBedId() {
		return bedId;
	}
	public String getCertificatesType() {
		return certificatesType;
	}
	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}
	public String getHouseDocName() {
		return houseDocName;
	}
	public void setHouseDocName(String houseDocName) {
		this.houseDocName = houseDocName;
	}
	public String getChargeDocName() {
		return chargeDocName;
	}
	public void setChargeDocName(String chargeDocName) {
		this.chargeDocName = chargeDocName;
	}
	public String getChiefDocName() {
		return chiefDocName;
	}
	public void setChiefDocName(String chiefDocName) {
		this.chiefDocName = chiefDocName;
	}
	public String getDutyNurseName() {
		return dutyNurseName;
	}
	public void setDutyNurseName(String dutyNurseName) {
		this.dutyNurseName = dutyNurseName;
	}
	
}
