package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class BusinessAnaerecord extends Entity  implements java.io.Serializable{

	//手术序列号
	private String operationNo;
	//门诊号/住院流水号
	private String clinicCode;
	//病历号/住院号
	private String patientNo;
	//患者姓名
	private String name;
	//性别
	private String sexCode;
	//1门诊/2住院
	private Integer psource;
	//麻醉方式
	private String anesType;
	//麻醉时间
	private Date anaeDate;
	//麻醉医生(主）
	private String anaeDocd;
	//麻醉医生（助手）
	private String anaeHelper;
	//麻醉效果
	private Integer anaeResult;
	//是否入PACU,1是 0否
	private Integer isPacu;
	//入室时间
	private Date inpacuDate;
	//入室状态
	private Integer inpacuStatus;
	//出室时间
	private Date outpacuDate;
	//出室状态
	private Integer outpacuStatus;
	//备注
	private String remark;
	//是否术后镇痛，1是 0否
	private Integer isDemulcent;
	//镇痛方式
	private String demuKind;
	//泵型
	private String demuModel;
	//镇痛天数
	private Integer demuDays;
	//拔管时间
	private Date pulloutDate;
	//拔管人
	private String pulloutOpcd;
	//镇痛效果
	private Integer demuResult;
	//1术前/2术后
	private Integer foreFlag;
	//0未记帐/1已记帐
	private Integer chargeFlag;
	//1有效/0无效
	private Integer ynvalid;
	public String getOperationNo() {
		return operationNo;
	}
	public void setOperationNo(String operationNo) {
		this.operationNo = operationNo;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
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
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public Integer getPsource() {
		return psource;
	}
	public void setPsource(Integer psource) {
		this.psource = psource;
	}
	public String getAnesType() {
		return anesType;
	}
	public void setAnesType(String anesType) {
		this.anesType = anesType;
	}
	public Date getAnaeDate() {
		return anaeDate;
	}
	public void setAnaeDate(Date anaeDate) {
		this.anaeDate = anaeDate;
	}
	public String getAnaeDocd() {
		return anaeDocd;
	}
	public void setAnaeDocd(String anaeDocd) {
		this.anaeDocd = anaeDocd;
	}
	public String getAnaeHelper() {
		return anaeHelper;
	}
	public void setAnaeHelper(String anaeHelper) {
		this.anaeHelper = anaeHelper;
	}
	public Integer getAnaeResult() {
		return anaeResult;
	}
	public void setAnaeResult(Integer anaeResult) {
		this.anaeResult = anaeResult;
	}
	public Integer getIsPacu() {
		return isPacu;
	}
	public void setIsPacu(Integer isPacu) {
		this.isPacu = isPacu;
	}
	public Date getInpacuDate() {
		return inpacuDate;
	}
	public void setInpacuDate(Date inpacuDate) {
		this.inpacuDate = inpacuDate;
	}
	public Integer getInpacuStatus() {
		return inpacuStatus;
	}
	public void setInpacuStatus(Integer inpacuStatus) {
		this.inpacuStatus = inpacuStatus;
	}
	public Date getOutpacuDate() {
		return outpacuDate;
	}
	public void setOutpacuDate(Date outpacuDate) {
		this.outpacuDate = outpacuDate;
	}
	public Integer getOutpacuStatus() {
		return outpacuStatus;
	}
	public void setOutpacuStatus(Integer outpacuStatus) {
		this.outpacuStatus = outpacuStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsDemulcent() {
		return isDemulcent;
	}
	public void setIsDemulcent(Integer isDemulcent) {
		this.isDemulcent = isDemulcent;
	}
	public String getDemuKind() {
		return demuKind;
	}
	public void setDemuKind(String demuKind) {
		this.demuKind = demuKind;
	}
	public String getDemuModel() {
		return demuModel;
	}
	public void setDemuModel(String demuModel) {
		this.demuModel = demuModel;
	}
	public Integer getDemuDays() {
		return demuDays;
	}
	public void setDemuDays(Integer demuDays) {
		this.demuDays = demuDays;
	}
	public Date getPulloutDate() {
		return pulloutDate;
	}
	public void setPulloutDate(Date pulloutDate) {
		this.pulloutDate = pulloutDate;
	}
	public String getPulloutOpcd() {
		return pulloutOpcd;
	}
	public void setPulloutOpcd(String pulloutOpcd) {
		this.pulloutOpcd = pulloutOpcd;
	}
	public Integer getDemuResult() {
		return demuResult;
	}
	public void setDemuResult(Integer demuResult) {
		this.demuResult = demuResult;
	}
	public Integer getForeFlag() {
		return foreFlag;
	}
	public void setForeFlag(Integer foreFlag) {
		this.foreFlag = foreFlag;
	}
	public Integer getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public Integer getYnvalid() {
		return ynvalid;
	}
	public void setYnvalid(Integer ynvalid) {
		this.ynvalid = ynvalid;
	}
	
	
	  
}
