package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 * @Description：手术登记表
 * @Author：ldl
 * @CreateDate：2016-04-11 
 * @Author：zhangjin
 * @version 1.0
 *
 */
public class OperAtionRecord extends Entity {
	
	/**
	 * 手术序列号
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
	 * 1门诊/2住院
	 */
	private String pasource;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 住院科室
	 */
	private String deptCode;
	/**
	 * 床号
	 */
	private String bedNo;
	/**
	 * 性别
	 */
	private String sexCode;
	/**
	 * 出生日期
	 */
	private Date birthday;
	/**
	 * 血型
	 */
	private String bloodCode;
	/**
	 * 手术分类1普通/2急诊/3感染
	 */
	private String opsKind;
	/**
	 * 手术医生编码
	 */
	private String opsDocd;
	/**
	 * 指导医生编码
	 */
	private String guiDocd;
	/**
	 * 预约手术时间
	 */
	private Date preDate;
	/**
	 * 预定手术时间
	 */
	private Integer duration;
	/**
	 * 麻醉方式
	 */
	private String anesType;
	/**
	 * 1 平台  2 加台  3 点台  4 急诊台
	 */
	private String consoleType;
	/**
	 * 执行科室
	 */
	private String execDept;
	/**
	 * 手术台代码
	 */
	private String consoleCode;
	/**
	 * 申请医生编码
	 */
	private String applyDocd;
	/**
	 * 申请时间
	 */
	private Date applyDate;
	/**
	 * 审批医生编码
	 */
	private String approveDocd;
	/**
	 * 手术规模
	 */
	private String degree;
	/**
	 * 有无细菌，1有/0无
	 */
	private String yngerm;
	/**
	 * 切口类型
	 */
	private String inciType;
	/**
	 * 1幕上 2 幕下
	 */
	private String screenup;
	/**
	 * 手术时间
	 */
	private Date operationdate;
	/**
	 * 接患者时间
	 */
	private Date receptDate;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 血液成分
	 */
	private String bloodType;
	/**
	 * 用血量
	 */
	private Double bloodNum;
	/**
	 * 单位
	 */
	private String bloodUnit;
	/**
	 * 入手术室时间
	 */
	private Date enterDate;
	/**
	 * 出手术室时间
	 */
	private Date outDate;
	/**
	 * 手术实际用时
	 */
	private Double realduation;
	/**
	 * 术前意识，1清醒/0不清醒
	 */
	private String foreynsober;
	/**
	 * 术后意识，1清醒/0不清醒
	 */
	private String stepynsober;
	/**
	 * 术前血压
	 */
	private String forepress;
	/**
	 * 术后血压
	 */
	private String steppress;
	/**
	 * 术前脉搏
	 */
	private Integer forepulse;
	/**
	 * 术后脉搏
	 */
	private Integer steppulse;
	/**
	 * 褥疮数量
	 */
	private Integer scarNum;
	/**
	 * 输液量
	 */
	private Integer transfusionQty;
	/**
	 * 标本数量
	 */
	private Integer sampleQty;
	/**
	 * 引流管个数
	 */
	private Integer guidtubeNum;
	/**
	 * 术前准备
	 */
	private String beforeReady;
	/**
	 * 工具核对
	 */
	private String toolExam;
	/**
	 * 是否隔离
	 */
	private String seperate;
	/**
	 * 是否危重
	 */
	private String danger;
	/**
	 * 抽血次数
	 */
	private Integer letBlood;
	/**
	 * 静注次数
	 */
	private Integer mainLine;
	/**
	 * 肌注次数
	 */
	private Integer muscleLine;
	/**
	 * 输液次数
	 */
	private Integer transfusion;
	/**
	 * 输氧次数
	 */
	private Integer transoxyen;
	/**
	 * 导尿次数
	 */
	private Integer stale;
	/**
	 * 是否感染 1 是  0 否
	 */
	private String question;
	/**
	 * 一类切口感染
	 */
	private String iinfection;
	/**
	 * 死亡
	 */
	private String die;
	/**
	 * 特殊说明
	 */
	private String specialComment;
	/**
	 * 是否有效 1有效    0无效
	 *  **/ 
	private String ynvalid;
	/**
	 * 是否收费
	 */
	private String ynfee;
	/**
	 * 体重
	 */
	private Double weight;
	/**
	 * 手术间ID
	 */
	private String roomId;
	/**
	 * 手术医生科室编码（医生可能会发生转科，所以此处记录当时医生科室
	 */
	private String docDpcd;
	

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
	public String getPasource() {
		return pasource;
	}
	public void setPasource(String pasource) {
		this.pasource = pasource;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getBloodCode() {
		return bloodCode;
	}
	public void setBloodCode(String bloodCode) {
		this.bloodCode = bloodCode;
	}
	public String getOpsKind() {
		return opsKind;
	}
	public void setOpsKind(String opsKind) {
		this.opsKind = opsKind;
	}
	public String getOpsDocd() {
		return opsDocd;
	}
	public void setOpsDocd(String opsDocd) {
		this.opsDocd = opsDocd;
	}
	public String getGuiDocd() {
		return guiDocd;
	}
	public void setGuiDocd(String guiDocd) {
		this.guiDocd = guiDocd;
	}
	public Date getPreDate() {
		return preDate;
	}
	public void setPreDate(Date preDate) {
		this.preDate = preDate;
	}
	
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getAnesType() {
		return anesType;
	}
	public void setAnesType(String anesType) {
		this.anesType = anesType;
	}
	public String getConsoleType() {
		return consoleType;
	}
	public void setConsoleType(String consoleType) {
		this.consoleType = consoleType;
	}
	public String getExecDept() {
		return execDept;
	}
	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}
	public String getConsoleCode() {
		return consoleCode;
	}
	public void setConsoleCode(String consoleCode) {
		this.consoleCode = consoleCode;
	}
	public String getApplyDocd() {
		return applyDocd;
	}
	public void setApplyDocd(String applyDocd) {
		this.applyDocd = applyDocd;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getApproveDocd() {
		return approveDocd;
	}
	public void setApproveDocd(String approveDocd) {
		this.approveDocd = approveDocd;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getYngerm() {
		return yngerm;
	}
	public void setYngerm(String yngerm) {
		this.yngerm = yngerm;
	}
	public String getInciType() {
		return inciType;
	}
	public void setInciType(String inciType) {
		this.inciType = inciType;
	}
	public String getScreenup() {
		return screenup;
	}
	public void setScreenup(String screenup) {
		this.screenup = screenup;
	}
	public Date getOperationdate() {
		return operationdate;
	}
	public void setOperationdate(Date operationdate) {
		this.operationdate = operationdate;
	}
	public Date getReceptDate() {
		return receptDate;
	}
	public void setReceptDate(Date receptDate) {
		this.receptDate = receptDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public Double getBloodNum() {
		return bloodNum;
	}
	public void setBloodNum(Double bloodNum) {
		this.bloodNum = bloodNum;
	}
	public String getBloodUnit() {
		return bloodUnit;
	}
	public void setBloodUnit(String bloodUnit) {
		this.bloodUnit = bloodUnit;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Double getRealduation() {
		return realduation;
	}
	public void setRealduation(Double realduation) {
		this.realduation = realduation;
	}
	public String getForeynsober() {
		return foreynsober;
	}
	public void setForeynsober(String foreynsober) {
		this.foreynsober = foreynsober;
	}
	public String getStepynsober() {
		return stepynsober;
	}
	public void setStepynsober(String stepynsober) {
		this.stepynsober = stepynsober;
	}
	public String getForepress() {
		return forepress;
	}
	public void setForepress(String forepress) {
		this.forepress = forepress;
	}
	public String getSteppress() {
		return steppress;
	}
	public void setSteppress(String steppress) {
		this.steppress = steppress;
	}
	public Integer getForepulse() {
		return forepulse;
	}
	public void setForepulse(Integer forepulse) {
		this.forepulse = forepulse;
	}
	public Integer getSteppulse() {
		return steppulse;
	}
	public void setSteppulse(Integer steppulse) {
		this.steppulse = steppulse;
	}
	public Integer getScarNum() {
		return scarNum;
	}
	public void setScarNum(Integer scarNum) {
		this.scarNum = scarNum;
	}
	public Integer getTransfusionQty() {
		return transfusionQty;
	}
	public void setTransfusionQty(Integer transfusionQty) {
		this.transfusionQty = transfusionQty;
	}
	public Integer getSampleQty() {
		return sampleQty;
	}
	public void setSampleQty(Integer sampleQty) {
		this.sampleQty = sampleQty;
	}
	public Integer getGuidtubeNum() {
		return guidtubeNum;
	}
	public void setGuidtubeNum(Integer guidtubeNum) {
		this.guidtubeNum = guidtubeNum;
	}
	public String getBeforeReady() {
		return beforeReady;
	}
	public void setBeforeReady(String beforeReady) {
		this.beforeReady = beforeReady;
	}
	public String getToolExam() {
		return toolExam;
	}
	public void setToolExam(String toolExam) {
		this.toolExam = toolExam;
	}
	public String getSeperate() {
		return seperate;
	}
	public void setSeperate(String seperate) {
		this.seperate = seperate;
	}
	public String getDanger() {
		return danger;
	}
	public void setDanger(String danger) {
		this.danger = danger;
	}
	public Integer getLetBlood() {
		return letBlood;
	}
	public void setLetBlood(Integer letBlood) {
		this.letBlood = letBlood;
	}
	public Integer getMainLine() {
		return mainLine;
	}
	public void setMainLine(Integer mainLine) {
		this.mainLine = mainLine;
	}
	public Integer getMuscleLine() {
		return muscleLine;
	}
	public void setMuscleLine(Integer muscleLine) {
		this.muscleLine = muscleLine;
	}
	public Integer getTransfusion() {
		return transfusion;
	}
	public void setTransfusion(Integer transfusion) {
		this.transfusion = transfusion;
	}
	public Integer getTransoxyen() {
		return transoxyen;
	}
	public void setTransoxyen(Integer transoxyen) {
		this.transoxyen = transoxyen;
	}
	public Integer getStale() {
		return stale;
	}
	public void setStale(Integer stale) {
		this.stale = stale;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getIinfection() {
		return iinfection;
	}
	public void setIinfection(String iinfection) {
		this.iinfection = iinfection;
	}
	public String getDie() {
		return die;
	}
	public void setDie(String die) {
		this.die = die;
	}
	public String getSpecialComment() {
		return specialComment;
	}
	public void setSpecialComment(String specialComment) {
		this.specialComment = specialComment;
	}
	public String getYnvalid() {
		return ynvalid;
	}
	public void setYnvalid(String ynvalid) {
		this.ynvalid = ynvalid;
	}
	public String getYnfee() {
		return ynfee;
	}
	public void setYnfee(String ynfee) {
		this.ynfee = ynfee;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getDocDpcd() {
		return docDpcd;
	}
	public void setDocDpcd(String docDpcd) {
		this.docDpcd = docDpcd;
	}
	
}
