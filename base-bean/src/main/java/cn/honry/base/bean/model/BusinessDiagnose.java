package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class BusinessDiagnose extends Entity  implements java.io.Serializable{
	
	
	//住院流水号
	private String inpatientNo;
	//发生序号
	private Integer happenNo;
	//住院诊断类型 1 主要诊断 2 其他诊断 3 并发症 4 院内感染 5 损伤 6 病理诊断 7 过敏药 8 新生儿疾病 9 新生儿院感
	private String diagKind;
	//诊断级别
	private String levelCode;
	//诊断分期
	private String periorCode;
	//诊断ICD码
	private String icdCode;
	//诊断名称
	private String diagName;
	//诊断日期
	private Date diagDate;
	//医师代号
	private String doctCode;
	//医师姓名(诊断)
	private String doctName;
	//入院日期
	private Date inDate;
	//出院日期
	private Date outDate;
	//治疗情况 0 治愈1 好转 2 未愈3 死亡 4 其他
	private Integer diagOutState;
	//第二ICD
	private String secondIcd;
	//并发症类别
	private String syndromeId;
	//病理符合
	private String clPa;
	//是否疑诊
	private Integer dubdiagFlag;
	//是否主诊断
	private Integer mainFlay;
	//备注
	private String remark;
	//类别 1 医生站录入诊断  2 病案室录入诊断
	private Integer operType;
	//手术标志
	private String operation;
	//是否是30种疾病
	private Integer is30Disease;
	//DIAG_ZG
	private Integer diagZg;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getDiagKind() {
		return diagKind;
	}
	public void setDiagKind(String diagKind) {
		this.diagKind = diagKind;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getPeriorCode() {
		return periorCode;
	}
	public void setPeriorCode(String periorCode) {
		this.periorCode = periorCode;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public Date getDiagDate() {
		return diagDate;
	}
	public void setDiagDate(Date diagDate) {
		this.diagDate = diagDate;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Integer getDiagOutState() {
		return diagOutState;
	}
	public void setDiagOutState(Integer diagOutState) {
		this.diagOutState = diagOutState;
	}
	public String getSecondIcd() {
		return secondIcd;
	}
	public void setSecondIcd(String secondIcd) {
		this.secondIcd = secondIcd;
	}
	public String getSyndromeId() {
		return syndromeId;
	}
	public void setSyndromeId(String syndromeId) {
		this.syndromeId = syndromeId;
	}
	public String getClPa() {
		return clPa;
	}
	public void setClPa(String clPa) {
		this.clPa = clPa;
	}
	public Integer getDubdiagFlag() {
		return dubdiagFlag;
	}
	public void setDubdiagFlag(Integer dubdiagFlag) {
		this.dubdiagFlag = dubdiagFlag;
	}
	public Integer getMainFlay() {
		return mainFlay;
	}
	public void setMainFlay(Integer mainFlay) {
		this.mainFlay = mainFlay;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOperType() {
		return operType;
	}
	public void setOperType(Integer operType) {
		this.operType = operType;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Integer getIs30Disease() {
		return is30Disease;
	}
	public void setIs30Disease(Integer is30Disease) {
		this.is30Disease = is30Disease;
	}
	public Integer getDiagZg() {
		return diagZg;
	}
	public void setDiagZg(Integer diagZg) {
		this.diagZg = diagZg;
	}
	
	
}
