package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientPrepayin  extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//就诊卡号
	private String cardNo;
	//病历号
	private String medicalrecordId;
	//发生序号
	private Integer happenNo;
	//姓名
	private String name;
	//性别
	private String sexCode;
	//身份证号
	private String idenNo;
	//生日
	private Date birthday;
	//医疗证号
	private String mcardNo;
	//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
	private String paykindCode;
	//合同单位
	private String pactCode;
	//床号
	private String bedNo;
	//护士站代码
	private String nurseCellCode;
	//职务
	private String profCode;
	//工作单位
	private String workName;
	//工作单位电话
	private String workTel;
	//家庭所在县
	private String homeDistrict;
	//家庭住址
	private String home;
	//家庭电话
	private String homeTel;
	//籍贯
	private String dist;
	//出生地
	private String birthArea;
	//民族
	private String nationCode;
	//联系人
	private String linkmaName;
	//联系人电话
	private String linkmaTel;
	//联系人地址
	private String linkmaAdd;
	//联系人关系
	private String relaCode;
	//婚姻状况
	private String mari;
	//国籍
	private String counCode;
	//诊断代码
	private String diagCode;
	//预约科室
	private String deptCode;
	//预约医师
	private String predoctCode;
	//状态
	private Integer preState;
	//预约日期
	private Date preDate;
	/**
	 * 新添字段
	 */
	//预约科室名称
	private String deptCodeName;
	//病房ID
	private String bedwardID;
	//病房名称
	private String bedwardName;
	//病床ID
	private String bedId;
	//护士站名称
	private String nurseCellName;
	//预约医师名称
	private String predoctName;
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
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
	public String getIdenNo() {
		return idenNo;
	}
	public void setIdenNo(String idenNo) {
		this.idenNo = idenNo;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getMcardNo() {
		return mcardNo;
	}
	public void setMcardNo(String mcardNo) {
		this.mcardNo = mcardNo;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getProfCode() {
		return profCode;
	}
	public void setProfCode(String profCode) {
		this.profCode = profCode;
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
	public String getHomeDistrict() {
		return homeDistrict;
	}
	public void setHomeDistrict(String homeDistrict) {
		this.homeDistrict = homeDistrict;
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
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getBirthArea() {
		return birthArea;
	}
	public void setBirthArea(String birthArea) {
		this.birthArea = birthArea;
	}
	public String getNationCode() {
		return nationCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public String getLinkmaName() {
		return linkmaName;
	}
	public void setLinkmaName(String linkmaName) {
		this.linkmaName = linkmaName;
	}
	public String getLinkmaTel() {
		return linkmaTel;
	}
	public void setLinkmaTel(String linkmaTel) {
		this.linkmaTel = linkmaTel;
	}
	public String getLinkmaAdd() {
		return linkmaAdd;
	}
	public void setLinkmaAdd(String linkmaAdd) {
		this.linkmaAdd = linkmaAdd;
	}
	public String getRelaCode() {
		return relaCode;
	}
	public void setRelaCode(String relaCode) {
		this.relaCode = relaCode;
	}
	public String getMari() {
		return mari;
	}
	public void setMari(String mari) {
		this.mari = mari;
	}
	public String getCounCode() {
		return counCode;
	}
	public void setCounCode(String counCode) {
		this.counCode = counCode;
	}
	public String getDiagCode() {
		return diagCode;
	}
	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPredoctCode() {
		return predoctCode;
	}
	public void setPredoctCode(String predoctCode) {
		this.predoctCode = predoctCode;
	}
	public Integer getPreState() {
		return preState;
	}
	public void setPreState(Integer preState) {
		this.preState = preState;
	}
	public Date getPreDate() {
		return preDate;
	}
	public void setPreDate(Date preDate) {
		this.preDate = preDate;
	}
	public String getDeptCodeName() {
		return deptCodeName;
	}
	public void setDeptCodeName(String deptCodeName) {
		this.deptCodeName = deptCodeName;
	}
	public String getBedwardID() {
		return bedwardID;
	}
	public void setBedwardID(String bedwardID) {
		this.bedwardID = bedwardID;
	}
	public String getBedwardName() {
		return bedwardName;
	}
	public void setBedwardName(String bedwardName) {
		this.bedwardName = bedwardName;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getPredoctName() {
		return predoctName;
	}
	public void setPredoctName(String predoctName) {
		this.predoctName = predoctName;
	}
	
	
}
