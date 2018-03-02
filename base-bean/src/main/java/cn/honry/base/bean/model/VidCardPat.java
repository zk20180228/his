package cn.honry.base.bean.model;

import java.util.Date;

public class VidCardPat {

	/**编号*/
	private String id;
	/**卡号*/
	private String idCardNo;
	/**卡类型1:磁卡2;IC卡3保障卡，从编码表里读取*/
	private String idCardType;
	/**病历号*/
	private String medicalrecordId;
	/**建卡时间*/
	private Date idCardCreatetime;
	/**金额*//*
	private Double idCardFee;*/
	/**操作人员*/
	private String idCardOperator;
	/**备注**/
	private String idCardRemark;
	/**建立人员**/
	private String idCardCreateUser;
	/**建立部门**/
	private String idCardCreateDept;
	/**建立时间**/
	private Date idCardCreateTime;
	/**修改人员**/
	private String idCardUpdateUser;
	/**修改时间**/
	private Date idCardUpdateTime;
	/**删除人员**/
	private String idCardDeleteUser;
	/**删除时间**/
	private Date idCardDeleteTime;	
	/**停用标志**/
	private Integer idCardStop_flg=0;
	/**删除标志**/
	private Integer idCardDel_flg=0;
	/** 患者ID**/
	private String patientId;
	/** 患者姓名  **/
	private String patientName;
	/** 拼音码  **/
	private String patientPinyin;
	/** 五笔码  **/
	private String patientWb;
	/** 自定义码	  **/
	private String patientInputcode;
	/** 性别  **/
	private String patientSex;
	/**年龄**/
	private Double patientAge;
	/** 出生日期  **/
	private Date patientBirthday;
	/** 家庭地址  **/
	private String patientAddress;
	/** 门牌号  **/
	private String patientDoorno;
	/** 电话  **/
	private String patientPhone;
	/**证件类型   **/
	private String patientCertificatestype;
	/** 证件号码  **/
	private String patientCertificatesno;
	/** 出生地  **/
	private String patientBirthplace;
	/** 籍贯  **/
	private String patientNativeplace;
	/**国籍   **/
	private String patientNationality;
	/** 民族  **/
	private String patientNation;
	/** 工作单位  **/
	private String patientWorkunit;
	/** 单位电话  **/
	private String patientWorkphone;
	/** 婚姻状况  **/
	private String patientWarriage;
	/** 职业  **/
	private String patientOccupation;
	/** 医保手册号  **/
	private String patientHandbook;
	/** 电子邮箱  **/
	private String patientEmail;
	/** 母亲姓名  **/
	private String patientMother;
	/** 联系人  **/
	private String patientLinkman;
	/** 联系人关系  **/
	private String patientLinkrelation;
	/** 联系人地址  **/
	private String patientLinkaddress;
	/** 联系人门牌号  **/
	private String patientLinkdoorno;
	/** 联系电话  **/
	private String patientLinkphone;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**删除人员**/
	private String deleteUser;
	/**删除时间**/
	private Date deleteTime;	
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	/**病历号*/
	private String patMedicalrecordId;
	/**就诊卡号*/
	private String patIdcardNo;
	/**住院流水号(6位日期+6位流水号)*/
	private String patInpatientNo;
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院*/
	private String patInState;
	/**住院创始时间*/
	private Date patcreateTime;
	
	public String getPatInState() {
		return patInState;
	}
	public void setPatInState(String patInState) {
		this.patInState = patInState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public Date getIdCardCreatetime() {
		return idCardCreatetime;
	}
	public void setIdCardCreatetime(Date idCardCreatetime) {
		this.idCardCreatetime = idCardCreatetime;
	}
	public String getIdCardOperator() {
		return idCardOperator;
	}
	public void setIdCardOperator(String idCardOperator) {
		this.idCardOperator = idCardOperator;
	}
	public String getIdCardRemark() {
		return idCardRemark;
	}
	public void setIdCardRemark(String idCardRemark) {
		this.idCardRemark = idCardRemark;
	}
	public String getIdCardCreateUser() {
		return idCardCreateUser;
	}
	public void setIdCardCreateUser(String idCardCreateUser) {
		this.idCardCreateUser = idCardCreateUser;
	}
	public String getIdCardCreateDept() {
		return idCardCreateDept;
	}
	public void setIdCardCreateDept(String idCardCreateDept) {
		this.idCardCreateDept = idCardCreateDept;
	}
	public Date getIdCardCreateTime() {
		return idCardCreateTime;
	}
	public void setIdCardCreateTime(Date idCardCreateTime) {
		this.idCardCreateTime = idCardCreateTime;
	}
	public String getIdCardUpdateUser() {
		return idCardUpdateUser;
	}
	public void setIdCardUpdateUser(String idCardUpdateUser) {
		this.idCardUpdateUser = idCardUpdateUser;
	}
	public Date getIdCardUpdateTime() {
		return idCardUpdateTime;
	}
	public void setIdCardUpdateTime(Date idCardUpdateTime) {
		this.idCardUpdateTime = idCardUpdateTime;
	}
	public String getIdCardDeleteUser() {
		return idCardDeleteUser;
	}
	public void setIdCardDeleteUser(String idCardDeleteUser) {
		this.idCardDeleteUser = idCardDeleteUser;
	}
	public Date getIdCardDeleteTime() {
		return idCardDeleteTime;
	}
	public void setIdCardDeleteTime(Date idCardDeleteTime) {
		this.idCardDeleteTime = idCardDeleteTime;
	}
	public Integer getIdCardStop_flg() {
		return idCardStop_flg;
	}
	public void setIdCardStop_flg(Integer idCardStop_flg) {
		this.idCardStop_flg = idCardStop_flg;
	}
	public Integer getIdCardDel_flg() {
		return idCardDel_flg;
	}
	public void setIdCardDel_flg(Integer idCardDel_flg) {
		this.idCardDel_flg = idCardDel_flg;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientPinyin() {
		return patientPinyin;
	}
	public void setPatientPinyin(String patientPinyin) {
		this.patientPinyin = patientPinyin;
	}
	public String getPatientWb() {
		return patientWb;
	}
	public void setPatientWb(String patientWb) {
		this.patientWb = patientWb;
	}
	public String getPatientInputcode() {
		return patientInputcode;
	}
	public void setPatientInputcode(String patientInputcode) {
		this.patientInputcode = patientInputcode;
	}
	public String getPatientSex() {
		return patientSex;
	}
	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}
	public Double getPatientAge() {
		return patientAge;
	}
	public void setPatientAge(Double patientAge) {
		this.patientAge = patientAge;
	}
	public Date getPatientBirthday() {
		return patientBirthday;
	}
	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}
	public String getPatientAddress() {
		return patientAddress;
	}
	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}
	public String getPatientDoorno() {
		return patientDoorno;
	}
	public void setPatientDoorno(String patientDoorno) {
		this.patientDoorno = patientDoorno;
	}
	public String getPatientPhone() {
		return patientPhone;
	}
	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}
	public String getPatientCertificatestype() {
		return patientCertificatestype;
	}
	public void setPatientCertificatestype(String patientCertificatestype) {
		this.patientCertificatestype = patientCertificatestype;
	}
	public String getPatientCertificatesno() {
		return patientCertificatesno;
	}
	public void setPatientCertificatesno(String patientCertificatesno) {
		this.patientCertificatesno = patientCertificatesno;
	}
	public String getPatientBirthplace() {
		return patientBirthplace;
	}
	public void setPatientBirthplace(String patientBirthplace) {
		this.patientBirthplace = patientBirthplace;
	}
	public String getPatientNativeplace() {
		return patientNativeplace;
	}
	public void setPatientNativeplace(String patientNativeplace) {
		this.patientNativeplace = patientNativeplace;
	}
	public String getPatientNationality() {
		return patientNationality;
	}
	public void setPatientNationality(String patientNationality) {
		this.patientNationality = patientNationality;
	}
	public String getPatientNation() {
		return patientNation;
	}
	public void setPatientNation(String patientNation) {
		this.patientNation = patientNation;
	}
	public String getPatientWorkunit() {
		return patientWorkunit;
	}
	public void setPatientWorkunit(String patientWorkunit) {
		this.patientWorkunit = patientWorkunit;
	}
	public String getPatientWorkphone() {
		return patientWorkphone;
	}
	public void setPatientWorkphone(String patientWorkphone) {
		this.patientWorkphone = patientWorkphone;
	}
	public String getPatientWarriage() {
		return patientWarriage;
	}
	public void setPatientWarriage(String patientWarriage) {
		this.patientWarriage = patientWarriage;
	}
	public String getPatientOccupation() {
		return patientOccupation;
	}
	public void setPatientOccupation(String patientOccupation) {
		this.patientOccupation = patientOccupation;
	}
	public String getPatientHandbook() {
		return patientHandbook;
	}
	public void setPatientHandbook(String patientHandbook) {
		this.patientHandbook = patientHandbook;
	}
	public String getPatientEmail() {
		return patientEmail;
	}
	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}
	public String getPatientMother() {
		return patientMother;
	}
	public void setPatientMother(String patientMother) {
		this.patientMother = patientMother;
	}
	public String getPatientLinkman() {
		return patientLinkman;
	}
	public void setPatientLinkman(String patientLinkman) {
		this.patientLinkman = patientLinkman;
	}
	public String getPatientLinkrelation() {
		return patientLinkrelation;
	}
	public void setPatientLinkrelation(String patientLinkrelation) {
		this.patientLinkrelation = patientLinkrelation;
	}
	public String getPatientLinkaddress() {
		return patientLinkaddress;
	}
	public void setPatientLinkaddress(String patientLinkaddress) {
		this.patientLinkaddress = patientLinkaddress;
	}
	public String getPatientLinkdoorno() {
		return patientLinkdoorno;
	}
	public void setPatientLinkdoorno(String patientLinkdoorno) {
		this.patientLinkdoorno = patientLinkdoorno;
	}
	public String getPatientLinkphone() {
		return patientLinkphone;
	}
	public void setPatientLinkphone(String patientLinkphone) {
		this.patientLinkphone = patientLinkphone;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
	public String getPatMedicalrecordId() {
		return patMedicalrecordId;
	}
	public void setPatMedicalrecordId(String patMedicalrecordId) {
		this.patMedicalrecordId = patMedicalrecordId;
	}
	public String getPatIdcardNo() {
		return patIdcardNo;
	}
	public void setPatIdcardNo(String patIdcardNo) {
		this.patIdcardNo = patIdcardNo;
	}
	public String getPatInpatientNo() {
		return patInpatientNo;
	}
	public void setPatInpatientNo(String patInpatientNo) {
		this.patInpatientNo = patInpatientNo;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public Date getPatcreateTime() {
		return patcreateTime;
	}
	public void setPatcreateTime(Date patcreateTime) {
		this.patcreateTime = patcreateTime;
	}
	
	
	
}