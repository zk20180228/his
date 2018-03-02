package cn.honry.base.bean.model;

import java.util.Date;


import cn.honry.base.bean.business.Entity;


/**患者电子病历实体
 * @author dtl
 *
 */
public class EmrFirst extends Entity{

	// Fields
	/**
	 * 病历编号
	 */
	private String emrId;
	/**
	 * 患者病历号
	 */
	private String patId;
	/**
	 * 医疗保险号
	 */
	private String siNo;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 出生日期
	 */
	private Date birth;
	/**
	 * 婚姻状态
	 */
	private String marriage;
	/**
	 * 职业
	 */
	private String profeesion;
	/**
	 * 出生地
	 */
	private String birthPlace;
	/**
	 * 民族
	 */
	private String folk;
	/**
	 * 国籍
	 */
	private String nation;
	/**
	 * 身份证
	 */
	private String idNo;
	/**
	 * 工作单位和地址
	 */
	private String jobAddress;
	/**
	 * 工作电话
	 */
	private String jobPhone;
	/**
	 * 工作地点邮编
	 */	
	private String jobPost;
	/**
	 * 户口地址
	 */	
	private String houseHold;
	/**
	 * 家庭电话
	 */	
	private String homePhone;
	/**
	 * 户口所在地邮编
	 */	
	private String homePost;
	/**
	 * 联系人
	 */	
	private String link;
	/**
	 * 联系人关系
	 */	
	private Integer linkRelation;
	/**
	 * 联系人地址
	 */	
	private String linkAddress;
	/**
	 * 联系人电话
	 */	
	private String linkPhone;
	/**
	 * 入院日期
	 */	
	private Date inTime;
	/**
	 * 入院科别
	 */	
	private String inDept;
	/**
	 * 所在病区
	 */	
	private String inNation;
	/**
	 * 术前住院天数
	 */	
	private Integer opDay;
	/**
	 * 转科日期
	 */	
	private Date tranTime;
	/**
	 * 转科科别
	 */	
	private String tranDept;
	/**
	 * 转科病区
	 */	
	private String tranNation;
	/**
	 * 再转科别
	 */	
	private String againTranDept;
	/**
	 * 出院日期
	 */	
	private Date outTime;
	/**
	 * 出院科别
	 */	
	private String outDept;
	/**
	 * 出院病区
	 */	
	private String outNation;
	/**
	 * 实际住院天数
	 */	
	private Integer inpatientDay;
	/**
	 * 死亡日期
	 */	
	private Date deathTime;
	/**
	 * 死亡原因
	 */	
	private String deathReson;
	/**
	 * 科主任
	 */	
	private String deptHead;
	/**
	 * （副）主任医师
	 */
	private String chiefDoc;
	/**
	 * 主治医师
	 */
	private String attendingDoc;
	/**
	 * 住院医师
	 */
	private String inpatientDoc;
	/**
	 * 进修医生
	 */
	private String refresherDoc;
	/**
	 * 研究生实习医师
	 */
	private String graduateIntern;
	/**
	 * 实习医师
	 */
	private String intern;
	/**
	 * 质控医师
	 */
	private String qcDoc;
	/**
	 * 质控护士
	 */
	private String qcNur;
	/**
	 * 病案质量
	 */
	private Integer caseQuality;
	/**
	 * 质控时间
	 */
	private Date qcTime;
	/**
	 * 门急诊诊断
	 */
	private String outPatientDiag;
	/**
	 * 入院状态
	 */
	private Integer inState;
	/**
	 * 入院诊断
	 */
	private String inDiag;
	/**
	 * 入院后确诊日期
	 */
	private Date diagTime;
	/**
	 * 出院诊断1
	 */
	private String diag1;
	/**
	 * 出院诊断2
	 */
	private String diag2;
	/**
	 * 出院诊断3
	 */
	private String diag3;
	/**
	 * 评分
	 */
	private Double score;
	/**
	 * 评分等级
	 */
	private Integer level;
	/** 
	* @Fields inpatientNo : 患者住院流水号
	*/ 
	private String inpatientNo;
	/*———————————与数据库无关（渲染字段）字段—————————————*/
	/**
	 * 婚姻状况
	 */
	private String strMarriage;
	/**
	 * 职业
	 */
	private String strProfeesion;
	/**
	 * 民族
	 */
	private String strFolk;
	/**
	 * 国籍
	 */
	private String strNation;
	/**
	 * 联系人关系
	 */
	private String strLinkRelation;
	/**
	 * 入院科室
	 */
	private String strInDept;
    /**
     * 入院病区
     */
    private String strInNation;
    /**
     * 转科科室
     */
    private String strTranDept;
    /**
     * 转科病区
     */
    private String strTranNation;
    /**
     * 再转科室
     */
    private String strAgainTranDept;
    /**
     * 出院科室
     */
    private String strOutDept;
    /**
     * 出院病区
     */
    private String strOutNation;
	
    /**
	 * 科主任
	 */	
	private String strDeptHead;
	/**
	 * （副）主任医师
	 */
	private String strChiefDoc;
	/**
	 * 主治医师
	 */
	private String strAttendingDoc;
	/**
	 * 住院医师
	 */
	private String strInpatientDoc;
	/**
	 * 进修医生
	 */
	private String strRefresherDoc;
	/**
	 * 研究生实习医师
	 */
	private String strGraduateIntern;
	/**
	 * 实习医师
	 */
	private String strIntern;
	/**
	 * 质控医师
	 */
	private String strQcDoc;
	/**
	 * 质控护士
	 */
	private String strQcNur;
	
	/** 
	* @Fields strSex : 性别
	*/ 
	private String strSex;
	

	public String getEmrId() {
		return emrId;
	}
	public void setEmrId(String emrId) {
		this.emrId = emrId;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getSiNo() {
		return siNo;
	}
	public void setSiNo(String siNo) {
		this.siNo = siNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getProfeesion() {
		return profeesion;
	}
	public void setProfeesion(String profeesion) {
		this.profeesion = profeesion;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public String getFolk() {
		return folk;
	}
	public void setFolk(String folk) {
		this.folk = folk;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getJobAddress() {
		return jobAddress;
	}
	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}
	public String getJobPhone() {
		return jobPhone;
	}
	public void setJobPhone(String jobPhone) {
		this.jobPhone = jobPhone;
	}
	public String getJobPost() {
		return jobPost;
	}
	public void setJobPost(String jobPost) {
		this.jobPost = jobPost;
	}
	public String getHouseHold() {
		return houseHold;
	}
	public void setHouseHold(String houseHold) {
		this.houseHold = houseHold;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getHomePost() {
		return homePost;
	}
	public void setHomePost(String homePost) {
		this.homePost = homePost;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getLinkRelation() {
		return linkRelation;
	}
	public void setLinkRelation(Integer linkRelation) {
		this.linkRelation = linkRelation;
	}
	public String getLinkAddress() {
		return linkAddress;
	}
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public Date getInTime() {
		return inTime;
	}
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}
	public String getInDept() {
		return inDept;
	}
	public void setInDept(String inDept) {
		this.inDept = inDept;
	}
	public String getInNation() {
		return inNation;
	}
	public void setInNation(String inNation) {
		this.inNation = inNation;
	}
	public Integer getOpDay() {
		return opDay;
	}
	public void setOpDay(Integer opDay) {
		this.opDay = opDay;
	}
	public Date getTranTime() {
		return tranTime;
	}
	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}
	public String getTranDept() {
		return tranDept;
	}
	public void setTranDept(String tranDept) {
		this.tranDept = tranDept;
	}
	public String getTranNation() {
		return tranNation;
	}
	public void setTranNation(String tranNation) {
		this.tranNation = tranNation;
	}
	public String getAgainTranDept() {
		return againTranDept;
	}
	public void setAgainTranDept(String againTranDept) {
		this.againTranDept = againTranDept;
	}
	public Date getOutTime() {
		return outTime;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public String getOutDept() {
		return outDept;
	}
	public void setOutDept(String outDept) {
		this.outDept = outDept;
	}
	public String getOutNation() {
		return outNation;
	}
	public void setOutNation(String outNation) {
		this.outNation = outNation;
	}
	public Integer getInpatientDay() {
		return inpatientDay;
	}
	public void setInpatientDay(Integer inpatientDay) {
		this.inpatientDay = inpatientDay;
	}
	public Date getDeathTime() {
		return deathTime;
	}
	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}
	public String getDeathReson() {
		return deathReson;
	}
	public void setDeathReson(String deathReson) {
		this.deathReson = deathReson;
	}
	public String getDeptHead() {
		return deptHead;
	}
	public void setDeptHead(String deptHead) {
		this.deptHead = deptHead;
	}
	public String getChiefDoc() {
		return chiefDoc;
	}
	public void setChiefDoc(String chiefDoc) {
		this.chiefDoc = chiefDoc;
	}
	public String getAttendingDoc() {
		return attendingDoc;
	}
	public void setAttendingDoc(String attendingDoc) {
		this.attendingDoc = attendingDoc;
	}
	public String getInpatientDoc() {
		return inpatientDoc;
	}
	public void setInpatientDoc(String inpatientDoc) {
		this.inpatientDoc = inpatientDoc;
	}
	public String getRefresherDoc() {
		return refresherDoc;
	}
	public void setRefresherDoc(String refresherDoc) {
		this.refresherDoc = refresherDoc;
	}
	public String getGraduateIntern() {
		return graduateIntern;
	}
	public void setGraduateIntern(String graduateIntern) {
		this.graduateIntern = graduateIntern;
	}
	public String getIntern() {
		return intern;
	}
	public void setIntern(String intern) {
		this.intern = intern;
	}
	public String getQcDoc() {
		return qcDoc;
	}
	public void setQcDoc(String qcDoc) {
		this.qcDoc = qcDoc;
	}
	public String getQcNur() {
		return qcNur;
	}
	public void setQcNur(String qcNur) {
		this.qcNur = qcNur;
	}
	public Integer getCaseQuality() {
		return caseQuality;
	}
	public void setCaseQuality(Integer caseQuality) {
		this.caseQuality = caseQuality;
	}
	public Date getQcTime() {
		return qcTime;
	}
	public void setQcTime(Date qcTime) {
		this.qcTime = qcTime;
	}
	public String getOutPatientDiag() {
		return outPatientDiag;
	}
	public void setOutPatientDiag(String outPatientDiag) {
		this.outPatientDiag = outPatientDiag;
	}
	public Integer getInState() {
		return inState;
	}
	public void setInState(Integer inState) {
		this.inState = inState;
	}
	public String getInDiag() {
		return inDiag;
	}
	public void setInDiag(String inDiag) {
		this.inDiag = inDiag;
	}
	public Date getDiagTime() {
		return diagTime;
	}
	public void setDiagTime(Date diagTime) {
		this.diagTime = diagTime;
	}
	public String getDiag1() {
		return diag1;
	}
	public void setDiag1(String diag1) {
		this.diag1 = diag1;
	}
	public String getDiag2() {
		return diag2;
	}
	public void setDiag2(String diag2) {
		this.diag2 = diag2;
	}
	public String getDiag3() {
		return diag3;
	}
	public void setDiag3(String diag3) {
		this.diag3 = diag3;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	/*———————————渲染字段getter/setter—————————————*/
	public String getStrMarriage() {
		return strMarriage;
	}
	public void setStrMarriage(String strMarriage) {
		this.strMarriage = strMarriage;
	}
	public String getStrProfeesion() {
		return strProfeesion;
	}
	public void setStrProfeesion(String strProfeesion) {
		this.strProfeesion = strProfeesion;
	}
	public String getStrFolk() {
		return strFolk;
	}
	public void setStrFolk(String strFolk) {
		this.strFolk = strFolk;
	}
	public String getStrNation() {
		return strNation;
	}
	public void setStrNation(String strNation) {
		this.strNation = strNation;
	}
	public String getStrLinkRelation() {
		return strLinkRelation;
	}
	public void setStrLinkRelation(String strLinkRelation) {
		this.strLinkRelation = strLinkRelation;
	}
	public String getStrInDept() {
		return strInDept;
	}
	public void setStrInDept(String strInDept) {
		this.strInDept = strInDept;
	}
	public String getStrInNation() {
		return strInNation;
	}
	public void setStrInNation(String strInNation) {
		this.strInNation = strInNation;
	}
	public String getStrTranDept() {
		return strTranDept;
	}
	public void setStrTranDept(String strTranDept) {
		this.strTranDept = strTranDept;
	}
	public String getStrTranNation() {
		return strTranNation;
	}
	public void setStrTranNation(String strTranNation) {
		this.strTranNation = strTranNation;
	}
	public String getStrAgainTranDept() {
		return strAgainTranDept;
	}
	public void setStrAgainTranDept(String strAgainTranDept) {
		this.strAgainTranDept = strAgainTranDept;
	}
	public String getStrOutDept() {
		return strOutDept;
	}
	public void setStrOutDept(String strOutDept) {
		this.strOutDept = strOutDept;
	}
	public String getStrOutNation() {
		return strOutNation;
	}
	public void setStrOutNation(String strOutNation) {
		this.strOutNation = strOutNation;
	}
	public String getStrDeptHead() {
		return strDeptHead;
	}
	public void setStrDeptHead(String strDeptHead) {
		this.strDeptHead = strDeptHead;
	}
	public String getStrChiefDoc() {
		return strChiefDoc;
	}
	public void setStrChiefDoc(String strChiefDoc) {
		this.strChiefDoc = strChiefDoc;
	}
	public String getStrAttendingDoc() {
		return strAttendingDoc;
	}
	public void setStrAttendingDoc(String strAttendingDoc) {
		this.strAttendingDoc = strAttendingDoc;
	}
	public String getStrInpatientDoc() {
		return strInpatientDoc;
	}
	public void setStrInpatientDoc(String strInpatientDoc) {
		this.strInpatientDoc = strInpatientDoc;
	}
	public String getStrRefresherDoc() {
		return strRefresherDoc;
	}
	public void setStrRefresherDoc(String strRefresherDoc) {
		this.strRefresherDoc = strRefresherDoc;
	}
	public String getStrGraduateIntern() {
		return strGraduateIntern;
	}
	public void setStrGraduateIntern(String strGraduateIntern) {
		this.strGraduateIntern = strGraduateIntern;
	}
	public String getStrIntern() {
		return strIntern;
	}
	public void setStrIntern(String strIntern) {
		this.strIntern = strIntern;
	}
	public String getStrQcDoc() {
		return strQcDoc;
	}
	public void setStrQcDoc(String strQcDoc) {
		this.strQcDoc = strQcDoc;
	}
	public String getStrQcNur() {
		return strQcNur;
	}
	public void setStrQcNur(String strQcNur) {
		this.strQcNur = strQcNur;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getStrSex() {
		return strSex;
	}
	public void setStrSex(String strSex) {
		this.strSex = strSex;
	}
	
}