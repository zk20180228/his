package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 麻醉记录
 * @author zhangjin
 * @CreateDate：2016-04-11 
 *
 */
public class OperationAnaerecord extends Entity{

	private static final long serialVersionUID = 1L;
    /**
     * 手术序号
     */
	private String operationId;
	/**
     * 门诊号/住院流水号
     */
	private String clinicCode;
	/**
     * 病历号/住院号
     */
	private String patientNo;
	/**
     * 患者姓名
     */
	private String name;
	/**
     * 性别
     */
	private String sexCode;
	/**
     * 1门诊/2住院
     */
	private String psource;
	/**
     * 麻醉方式
     */
	private String anesType;
	/**
     * 麻醉时间
     */
	private Date anaeDate;
	/**
     * 麻醉医生(主）
     */
	private String anaeDocd;
	/**
     * 麻醉医生（助手）
     */
	private String anaeHelper;
	/**
     * 麻醉效果
     */
	private String anaeResult;
	/**
     * 是否入PACU,1是 0否
     */
	private String isPacu;
	/**
     *入室时间 
     */
	private Date inpacuDate;
	/**
     * 入室状态
     */
	private String inpacuStatus;
	/**
     * 出室时间
     */
	private Date outpacuDate;
	/**
     * 出室状态
     */
	private String outpacuStatus;
	/**
     * 备注
     */
	private String remark;
	/**
     * 是否术后镇痛，1是 0否
     */
	private String isDemulcent;
	/**
     *镇痛方式 
     */
	private String demuKind;
	/**
     * 泵型
     */
	private String demuModel;
	/**
     * 镇痛天数
     */
	private Integer demuDays;
	/**
     * 拔管时间
     */
	private Date pulloutDate;
	/**
     * 拔管人
     */
	private String pulloutOpcd;
	/**
     * 镇痛效果
     */
	private String demuResult;
	/**
     * 1术前/2术后
     */
	private String foreFlag;
	/**
     * 0未记帐/1已记帐
     */
	private String chargeFlag;
	/**
     * 1有效/0无效
     */
	private String ynvalid;
	/**
     * 操作员
     */
	private String operCode;
	/**
     * 执行科室
     */
	private String deptCode;
	/**
     * 操作时间
     */
	private Date operDate;
	
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
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
	public String getPsource() {
		return psource;
	}
	public void setPsource(String psource) {
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
	public String getAnaeResult() {
		return anaeResult;
	}
	public void setAnaeResult(String anaeResult) {
		this.anaeResult = anaeResult;
	}
	public String getIsPacu() {
		return isPacu;
	}
	public void setIsPacu(String isPacu) {
		this.isPacu = isPacu;
	}
	public Date getInpacuDate() {
		return inpacuDate;
	}
	public void setInpacuDate(Date inpacuDate) {
		this.inpacuDate = inpacuDate;
	}
	public String getInpacuStatus() {
		return inpacuStatus;
	}
	public void setInpacuStatus(String inpacuStatus) {
		this.inpacuStatus = inpacuStatus;
	}
	public Date getOutpacuDate() {
		return outpacuDate;
	}
	public void setOutpacuDate(Date outpacuDate) {
		this.outpacuDate = outpacuDate;
	}
	public String getOutpacuStatus() {
		return outpacuStatus;
	}
	public void setOutpacuStatus(String outpacuStatus) {
		this.outpacuStatus = outpacuStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsDemulcent() {
		return isDemulcent;
	}
	public void setIsDemulcent(String isDemulcent) {
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
	public String getDemuResult() {
		return demuResult;
	}
	public void setDemuResult(String demuResult) {
		this.demuResult = demuResult;
	}
	public String getForeFlag() {
		return foreFlag;
	}
	public void setForeFlag(String foreFlag) {
		this.foreFlag = foreFlag;
	}
	public String getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(String chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public String getYnvalid() {
		return ynvalid;
	}
	public void setYnvalid(String ynvalid) {
		this.ynvalid = ynvalid;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
	
	
	
}
