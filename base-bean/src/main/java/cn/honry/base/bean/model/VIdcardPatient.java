package cn.honry.base.bean.model;

import java.util.Date;

public class VIdcardPatient {

	/**编号*/
	private String idcardId;
	/**卡号*/
	private String idcardNo;
	/**卡类型1:磁卡2;IC卡3保障卡，从编码表里读取*/
	private String idcardType;
	/**病历号*/
	private String medicalrecordId;
	/**建卡时间*/
	private Date idcardCreatetime;
	/**金额*//*
	private Double idcardFee;*/
	/**操作人员*/
	private String idcardOperator;
	/**备注**/
	private String idcardRemark;
	/**建立人员**/
	private String idcardCreateUser;
	/**建立部门**/
	private String idcardCreateDept;
	/**建立时间**/
	private Date idcardCreateTime;
	/**修改人员**/
	private String idcardUpdateUser;
	/**修改时间**/
	private Date idcardUpdateTime;
	/**删除人员**/
	private String idcardDeleteUser;
	/**删除时间**/
	private Date idcardDeleteTime;	
	/**停用标志**/
	private Integer idcardStop_flg=0;
	/**删除标志**/
	private Integer idcardDel_flg=0;
	public String getIdcardCreateUser() {
		return idcardCreateUser;
	}
	public void setIdcardCreateUser(String idcardCreateUser) {
		this.idcardCreateUser = idcardCreateUser;
	}
	public String getIdcardCreateDept() {
		return idcardCreateDept;
	}
	public void setIdcardCreateDept(String idcardCreateDept) {
		this.idcardCreateDept = idcardCreateDept;
	}
	public Date getIdcardCreateTime() {
		return idcardCreateTime;
	}
	public void setIdcardCreateTime(Date idcardCreateTime) {
		this.idcardCreateTime = idcardCreateTime;
	}
	public String getIdcardUpdateUser() {
		return idcardUpdateUser;
	}
	public void setIdcardUpdateUser(String idcardUpdateUser) {
		this.idcardUpdateUser = idcardUpdateUser;
	}
	public Date getIdcardUpdateTime() {
		return idcardUpdateTime;
	}
	public void setIdcardUpdateTime(Date idcardUpdateTime) {
		this.idcardUpdateTime = idcardUpdateTime;
	}
	public String getIdcardDeleteUser() {
		return idcardDeleteUser;
	}
	public void setIdcardDeleteUser(String idcardDeleteUser) {
		this.idcardDeleteUser = idcardDeleteUser;
	}
	public Date getIdcardDeleteTime() {
		return idcardDeleteTime;
	}
	public void setIdcardDeleteTime(Date idcardDeleteTime) {
		this.idcardDeleteTime = idcardDeleteTime;
	}
	public Integer getIdcardStop_flg() {
		return idcardStop_flg;
	}
	public void setIdcardStop_flg(Integer idcardStop_flg) {
		this.idcardStop_flg = idcardStop_flg;
	}
	public Integer getIdcardDel_flg() {
		return idcardDel_flg;
	}
	public void setIdcardDel_flg(Integer idcardDel_flg) {
		this.idcardDel_flg = idcardDel_flg;
	}
	/**  患者编号 **/
	private String Id;
	
	
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getIdcardType() {
		return idcardType;
	}
	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public Date getIdcardCreatetime() {
		return idcardCreatetime;
	}
	public void setIdcardCreatetime(Date idcardCreatetime) {
		this.idcardCreatetime = idcardCreatetime;
	}
	public String getIdcardOperator() {
		return idcardOperator;
	}
	public void setIdcardOperator(String idcardOperator) {
		this.idcardOperator = idcardOperator;
	}
	public String getIdcardRemark() {
		return idcardRemark;
	}
	public void setIdcardRemark(String idcardRemark) {
		this.idcardRemark = idcardRemark;
	}
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
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
	/**住院流水号(6位日期+6位流水号)*/
	private String inpatientNo;
	/**医疗类别(从编码表里读取)*/
	private String medicalType;
	/**病历号*/
	private String fmedicalrecordId;
	/**就诊卡号*/
	private String fidcardNo;
	/**医疗证号*/
	private String mcardNo;
	/**姓名*/
	private String fpatientName;
	/**证件类型(从编码表里读取)*/
	private String certificatesType;
	/**证件号码*/
	private String certificatesNo;
	/**性别(从编码表里读取)*/
	private String reportSex;
	/**出生日期*/
	private Date reportBirthday;
	/**年龄*/
	private Integer reportAge;
	/**年龄单位(年月天)*/
	private String reportAgeunit;
	/**职业代码(从编码表里读取)*/
	private String profCode;
	/**工作单位*/
	private String workName;
	/**工作单位电话*/
	private String workTel;
	/**单位邮编*/
	private String workZip;
	/**户口或家庭地址*/
	private String home;
	/**家庭电话*/
	private String homeTel;
	/**户口或家庭邮编*/
	private String homeZip;
	/**籍贯(从城市表里读取)*/
	private String dist;
	/**出生地代码(从城市表里读取)*/
	private String birthArea;
	/**民族(从编码表里读取)*/
	private String nationCode;
	/**联系人姓名*/
	private String linkmanName;
	/**联系人电话*/
	private String linkmanTel;
	/**联系人地址*/
	private String linkmanAddress;
	/**关系(从编码表里读取)*/
	private String relaCode;
	/**婚姻状况(从编码表里读取)*/
	private String mari;
	/**国籍(从国家编码表里读取)*/
	private String counCode;
	/**身高*/
	private Double height;
	/**体重*/
	private Double weight;
	/**血压*/
	private String bloodDress;
	/**血型编码(从编码表里读取)*/
	private String bloodCode;
	/**重大疾病标志1:有  0:无*/
	private Integer hepatitisFlag;
	/**过敏标志1:有  0:无*/
	private Integer anaphyFlag;
	/**入院日期*/
	private Date inDate;
	/**科室代码*/
	private String deptCode;
	/**结算类别 1-自费  2-保险 3-公费在职 4-公费退休 5-公费高干 (从编码表里读取)*/
	private String paykindCode;
	/**合同单位代码 (从合同单位编码表里读取)*/
	private String pactCode;
	/**床号  (从病床维护表里读取)*/
	private String bedId;
	/**护理单元代码 (部门表里的护士站)*/
	private String nurseCellCode;
	/**医师代码(住院)*/
	private String houseDocCode;
	/**医师代码(主治)*/
	private String chargeDocCode;
	/**医师代码(主任)*/
	private String chiefDocCode;
	/**护士代码(责任)*/
	private String dutyNurseCode;
	/**入院情况*/
	private String inCircs;
	/**诊断名称（建议用此保存主诊断）*/
	private String diagName;
	/**入院途径*/
	private String inAvenue;
	/**入院来源 1:门诊，2:急诊，3:转科，4:转院 (从编码表里读取)*/
	private String inSource;
	/**住院次数*/
	private Integer inTimes;
	/**预交金额(未结)*/
	private Double prepayCost;
	/**转入预交金额（未结)*/
	private Double changePrepaycost;
	/**M 金额 D时间段*/
	private String alterType;
	/**警戒线开始时间*/
	private Date alterBegin;
	/**警戒线结束时间*/
	private Date alterEnd;
	/**警戒线*/
	private Double moneyAlert;
	/**费用金额(未结)*/
	private Double totCost;
	/**自费金额(未结)*/
	private Double ownCost;
	/**自付金额(未结)*/
	private Double payCost;
	/**公费金额(未结)*/
	private Double pubCost;
	/**优惠金额(未结)*/
	private Double ecoCost;
	/**余额(未结)*/
	private Double freeCost;
	/**转入费用金额(未结)*/
	private Double changeTotcost;
	/**待遇上限*/
	private Double upperLimit;
	/**固定费用间隔天数*/
	private Integer feeInterval;
	/**结算序号*/
	private String balanceNo;
	/**费用金额(已结)*/
	private Double balanceCost;
	/**预交金额(已结)*/
	private Double balancePrepay;
	/**结算日期(上次)*/
	private Date balanceDate;
	/**是否关帐*/
	private Integer stopAcount;
	/**婴儿标志 1:有婴儿；0:无婴儿*/
	private Integer babyFlag;
	/**病案状态: 0 无需病案 1 需要病案 2 医生站形成病案 3 病案室形成病案 4病案封存*/
	private Integer caseFlag;
	/**R-住院登记  I-病房接诊 B-出院登记 O-出院结算 P-预约出院,N-无费退院*/
	private String inState;
	/**是否请假 0 非 1是*/
	private Integer leaveFlag;
	/**出院日期(预约)*/
	private Date prepayOutdate;
	/**出院日期*/
	private Date outDate;
	/**转归代号*/
	private String zg;
	/**开据医师*/
	private String emplCode;
	/**是否在ICU 0 NO 1 YES*/
	private Integer inIcu;
	/**病案送入病案室否0未1送*/
	private Integer casesendFlag;
	/**护理级别(TEND):名称显示护理级别名称(一级护理，二级护理，三级护理) (从编码表里读取)*/
	private String tend;
	/**病危：0 普通 1 病重 2 病危*/
	private Integer criticalFlag;
	/**上次固定费用时间*/
	private Date prefixfeeDate;
	/**血滞纳金*/
	private Double bloodLatefee;
	/**公费患者日限额*/
	private Double dayLimit;
	/**公费患者日限额累计*/
	private Double limitTot;
	/**公费患者日限额超标部分金额*/
	private Double limitOvertop;
	/**生育保险患者电脑号*/
	private String procreatePcno;
	/**公费患者公费药品累计(日限额)*/
	private Double bursaryTotmedfee;
	/**备注*/
	private String remark;
	/**床位上限*/
	private Double bedLimit;
	/**空调上限*/
	private Double airLimit;
	/**床费超标处理 0超标不限 1超标自理 2超标不计*/
	private Integer bedoverdeal;
	/**（公医超日限额是否同意：0不同意，1同意）*/
	private Integer extFlag;
	/**扩展标记1*/
	private String extFlag1;
	/**扩展标记2*/
	private String extFlag2;
	/**膳食花费总额*/
	private Double boardCost;
	/**膳食预交金额*/
	private Double boardPrepay;
	/**膳食结算状态：0在院 1出院*/
	private Integer boardState;
	/**自费比例*/
	private Double ownRate;
	/**自付比例*/
	private Double payRate;
	/**扩展数值（中山一用作－剩余统筹金额）*/
	private Double extNumber;
	/**扩展编码（）*/
	private String extCode;
	//添加字段 证件类别名字
	private String patientCertificatestypeName;
	public String getPatientCertificatestypeName() {
		return patientCertificatestypeName;
	}
	public void setPatientCertificatestypeName(String patientCertificatestypeName) {
		this.patientCertificatestypeName = patientCertificatestypeName;
	}
}
