package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


/**  
 *  
 * @className：OutpatientMedicalrecord 
 * @Description：  门诊病历表
 * @Author：aizhonghua
 * @CreateDate：2015-7-6 下午03:13:01  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-7-6 下午03:13:01  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class OutpatientMedicalrecord extends Entity{

	private static final long serialVersionUID = 4004547423343863134L;
	
	/**就诊号**/
	private String clinicCode;
	/**来源**/
	private Integer pasource;
	/**病历号**/
	private String patientNo;
	/**患者姓名**/
	private String name;
	/**性别**/
	private String sex;
	/**年龄**/
	private Integer age;
	/**出生日期**/
	private Date birthday;
	/**体重**/
	private Double weight;
	/**联系电话**/
	private String telephone; 
	/**项目编码**/
	private String itemCode;
	/**检查部位**/
	private String checkpart;
	/**申请日期**/
	private Date applyDate;
	/**申请医生编码**/
	private String applyDoc;
	/**主诉**/
	private String maindesc;
	/**家族遗传史**/
	private String heredityHis;
	/**1 初诊2复诊3急症**/
	private Integer diagnoseType;
	/**就诊时间**/
	private Date diagnoseDate;
	/**过敏史**/
	private String allergichistory;
	/**病史和特征**/
	private String historyspecil;
	/**现病史**/
	private String presentillness;
	/**体温**/
	private Double temperature;
	/**脉搏**/
	private Double pulse;
	/**呼吸**/
	private Double breathing;
	/**血压**/
	private String bloodPressure;
	/**体格检查**/
	private String physicalExamination;
	/**检查检验结果**/
	private String checkresult;
	/**诊断1**/
	private String diagnose1;
	/**诊断2**/
	private String diagnose2;
	/**诊断3**/
	private String diagnose3;
	/**诊断4**/
	private String diagnose4;
	/**备注**/
	private String remark;
	/**检查目的要求**/
	private String purpose;
	/**就诊地址**/
	private String address;
	/**医嘱建议**/
	private String advice;
	
	private String type;//药品种类
	private String typeChi;//药品种类中文
	private Integer stackType;//组套类别
	private String stackName;//组套名称
	private String stackSource;//组套来源
	private String stackInputCode;//组套自定义码
	private Integer stackFlag;//组套是否共享
	private String stackRemark;//组套是否共享
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public Integer getPasource() {
		return pasource;
	}
	public void setPasource(Integer pasource) {
		this.pasource = pasource;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getCheckpart() {
		return checkpart;
	}
	public void setCheckpart(String checkpart) {
		this.checkpart = checkpart;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyDoc() {
		return applyDoc;
	}
	public void setApplyDoc(String applyDoc) {
		this.applyDoc = applyDoc;
	}
	public String getAllergichistory() {
		return allergichistory;
	}
	public void setAllergichistory(String allergichistory) {
		this.allergichistory = allergichistory;
	}
	public String getHistoryspecil() {
		return historyspecil;
	}
	public void setHistoryspecil(String historyspecil) {
		this.historyspecil = historyspecil;
	}
	public String getPresentillness() {
		return presentillness;
	}
	public void setPresentillness(String presentillness) {
		this.presentillness = presentillness;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getPulse() {
		return pulse;
	}
	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}
	public Double getBreathing() {
		return breathing;
	}
	public void setBreathing(Double breathing) {
		this.breathing = breathing;
	}
	public String getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public String getPhysicalExamination() {
		return physicalExamination;
	}
	public void setPhysicalExamination(String physicalExamination) {
		this.physicalExamination = physicalExamination;
	}
	public String getCheckresult() {
		return checkresult;
	}
	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}
	public String getDiagnose1() {
		return diagnose1;
	}
	public void setDiagnose1(String diagnose1) {
		this.diagnose1 = diagnose1;
	}
	public String getDiagnose2() {
		return diagnose2;
	}
	public void setDiagnose2(String diagnose2) {
		this.diagnose2 = diagnose2;
	}
	public String getDiagnose3() {
		return diagnose3;
	}
	public void setDiagnose3(String diagnose3) {
		this.diagnose3 = diagnose3;
	}
	public String getDiagnose4() {
		return diagnose4;
	}
	public void setDiagnose4(String diagnose4) {
		this.diagnose4 = diagnose4;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getMaindesc() {
		return maindesc;
	}
	public void setMaindesc(String maindesc) {
		this.maindesc = maindesc;
	}
	public String getHeredityHis() {
		return heredityHis;
	}
	public void setHeredityHis(String heredityHis) {
		this.heredityHis = heredityHis;
	}
	public Integer getDiagnoseType() {
		return diagnoseType;
	}
	public void setDiagnoseType(Integer diagnoseType) {
		this.diagnoseType = diagnoseType;
	}
	public Date getDiagnoseDate() {
		return diagnoseDate;
	}
	public void setDiagnoseDate(Date diagnoseDate) {
		this.diagnoseDate = diagnoseDate;
	}
	public Integer getStackType() {
		return stackType;
	}
	public void setStackType(Integer stackType) {
		this.stackType = stackType;
	}
	public String getStackName() {
		return stackName;
	}
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}
	public String getStackSource() {
		return stackSource;
	}
	public void setStackSource(String stackSource) {
		this.stackSource = stackSource;
	}
	public String getStackInputCode() {
		return stackInputCode;
	}
	public void setStackInputCode(String stackInputCode) {
		this.stackInputCode = stackInputCode;
	}
	public Integer getStackFlag() {
		return stackFlag;
	}
	public void setStackFlag(Integer stackFlag) {
		this.stackFlag = stackFlag;
	}
	public String getStackRemark() {
		return stackRemark;
	}
	public void setStackRemark(String stackRemark) {
		this.stackRemark = stackRemark;
	}
	public String getTypeChi() {
		return typeChi;
	}
	public void setTypeChi(String typeChi) {
		this.typeChi = typeChi;
	}
}