package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 
 * 
 * @author wujiao
 *预约挂号
 */
public class RegisterPreregister extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**排班编号**/
	private String scheduleId;
	/** 预约号 **/
	private String preregisterNo;
	/**是否网络预约**/
	private Integer preregisterIsnet=0;
	/**是否点话预约**/
	private Integer preregisterIsphone=0;
	/**挂号科室**/
	private String preregisterDept;
	/**挂号专家**/
	private String preregisterExpert;
	/**挂号级别**/
	private String preregisterGrade;
	/**预约日期**/
	private Date preregisterDate;
	/**预约开始时间   根据排班医生的排班记录回显时分**/
	private String preregisterStarttime;
	/**预约结束时间  根据排班医生的排班记录回显时分**/
	private String preregisterEndtime;
	/**病历号**/ 
    private  String  medicalrecordId;
    /**就诊卡号**/ 
    private  String  idcardId;
    /**证件类型**/
	private String  preregisterCertificatestype;
	/**证件号码**/
	private String  preregisterCertificatesno;
	/**姓名**/
	private String  preregisterName;
	/**性别**/
	private String  preregisterSex;
	/**年龄**/
	private  Integer preregisterAge;
	/**年龄单位**/
	private String  preregisterAgeunit;
	/**电话**/
	private String  preregisterPhone;
	/**地址**/
	private String  preregisterAddress;
	/**性别**/
	private Integer  sexS;
	/**午别**/
	private Integer  midday;
	/**看诊状态：1已看诊2未看**/
	private Integer  seeFlag=2;
	/**是否加号：1加号2非加号**/
	private Integer  appFlag=2;
	/**第三方订单号**/
	private String  orderNo;
	/**订单来源**/
	private String  sourceType;
	/**初复诊标志 1 初诊2复诊**/
	private Integer  isFirst=1;
	/**状态：1为有效,2为无效,3为取号,4爽约,0停诊**/
	private Integer  status=1;
	private Integer missNumber=0;//爽约次数；
	/**挂号科室名称**/
	private String preregisterDeptName;
	/**挂号专家名称**/
	private String preregisterExpertName;
	/**挂号级别名称**/
	private String preregisterGradeName;
	/**证件名称**/
	private String preregisterCertificatesname;
	/**午别名称**/
	private String preregisterMiddayname;
	
	/**虚拟字段**/
	/**挂号科室**/
	private String preregisterDeptname;
	/**挂号专家**/
	private String preregisterExpertname;
	/**挂号级别**/
	private String preregisterGradename;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;

	public String getPreregisterDeptname() {
		return preregisterDeptname;
	}
	public void setPreregisterDeptname(String preregisterDeptname) {
		this.preregisterDeptname = preregisterDeptname;
	}
	public String getPreregisterExpertname() {
		return preregisterExpertname;
	}
	public void setPreregisterExpertname(String preregisterExpertname) {
		this.preregisterExpertname = preregisterExpertname;
	}
	public String getPreregisterGradename() {
		return preregisterGradename;
	}
	public void setPreregisterGradename(String preregisterGradename) {
		this.preregisterGradename = preregisterGradename;
	}
	public Integer getMissNumber() {
		return missNumber;
	}
	public void setMissNumber(Integer missNumber) {
		this.missNumber = missNumber;
	}
	public String getIdcardId() {
		return idcardId;
	}
	public void setIdcardId(String idcardId) {
		this.idcardId = idcardId;
	}
	public Integer getSexS() {
		return sexS;
	}
	public void setSexS(Integer sexS) {
		this.sexS = sexS;
	}
	
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getPreregisterNo() {
		return preregisterNo;
	}
	public void setPreregisterNo(String preregisterNo) {
		this.preregisterNo = preregisterNo;
	}
	public Integer getPreregisterIsnet() {
		return preregisterIsnet;
	}
	public void setPreregisterIsnet(Integer preregisterIsnet) {
		this.preregisterIsnet = preregisterIsnet;
	}
	public Integer getPreregisterIsphone() {
		return preregisterIsphone;
	}
	public void setPreregisterIsphone(Integer preregisterIsphone) {
		this.preregisterIsphone = preregisterIsphone;
	}
	
	public String getPreregisterDept() {
		return preregisterDept;
	}
	public void setPreregisterDept(String preregisterDept) {
		this.preregisterDept = preregisterDept;
	}
	public String getPreregisterExpert() {
		return preregisterExpert;
	}
	public void setPreregisterExpert(String preregisterExpert) {
		this.preregisterExpert = preregisterExpert;
	}
	public String getPreregisterGrade() {
		return preregisterGrade;
	}
	public void setPreregisterGrade(String preregisterGrade) {
		this.preregisterGrade = preregisterGrade;
	}
	public Date getPreregisterDate() {
		return preregisterDate;
	}
	public void setPreregisterDate(Date preregisterDate) {
		this.preregisterDate = preregisterDate;
	}
	public String getPreregisterStarttime() {
		return preregisterStarttime;
	}
	public void setPreregisterStarttime(String preregisterStarttime) {
		this.preregisterStarttime = preregisterStarttime;
	}
	public String getPreregisterEndtime() {
		return preregisterEndtime;
	}
	public void setPreregisterEndtime(String preregisterEndtime) {
		this.preregisterEndtime = preregisterEndtime;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getPreregisterCertificatestype() {
		return preregisterCertificatestype;
	}
	public void setPreregisterCertificatestype(String preregisterCertificatestype) {
		this.preregisterCertificatestype = preregisterCertificatestype;
	}
	public String getPreregisterCertificatesno() {
		return preregisterCertificatesno;
	}
	public void setPreregisterCertificatesno(String preregisterCertificatesno) {
		this.preregisterCertificatesno = preregisterCertificatesno;
	}
	public String getPreregisterName() {
		return preregisterName;
	}
	public void setPreregisterName(String preregisterName) {
		this.preregisterName = preregisterName;
	}
	public String getPreregisterSex() {
		return preregisterSex;
	}
	public void setPreregisterSex(String preregisterSex) {
		this.preregisterSex = preregisterSex;
	}
	public Integer getPreregisterAge() {
		return preregisterAge;
	}
	public void setPreregisterAge(Integer preregisterAge) {
		this.preregisterAge = preregisterAge;
	}
	
	public String getPreregisterAgeunit() {
		return preregisterAgeunit;
	}
	public void setPreregisterAgeunit(String preregisterAgeunit) {
		this.preregisterAgeunit = preregisterAgeunit;
	}
	public String getPreregisterPhone() {
		return preregisterPhone;
	}
	public void setPreregisterPhone(String preregisterPhone) {
		this.preregisterPhone = preregisterPhone;
	}
	public String getPreregisterAddress() {
		return preregisterAddress;
	}
	public void setPreregisterAddress(String preregisterAddress) {
		this.preregisterAddress = preregisterAddress;
	}
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
	}
	public Integer getSeeFlag() {
		return seeFlag;
	}
	public void setSeeFlag(Integer seeFlag) {
		this.seeFlag = seeFlag;
	}
	public Integer getAppFlag() {
		return appFlag;
	}
	public void setAppFlag(Integer appFlag) {
		this.appFlag = appFlag;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getIsFirst() {
		return isFirst;
	}
	public void setIsFirst(Integer isFirst) {
		this.isFirst = isFirst;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPreregisterDeptName() {
		return preregisterDeptName;
	}
	public void setPreregisterDeptName(String preregisterDeptName) {
		this.preregisterDeptName = preregisterDeptName;
	}
	public String getPreregisterExpertName() {
		return preregisterExpertName;
	}
	public void setPreregisterExpertName(String preregisterExpertName) {
		this.preregisterExpertName = preregisterExpertName;
	}
	public String getPreregisterGradeName() {
		return preregisterGradeName;
	}
	public void setPreregisterGradeName(String preregisterGradeName) {
		this.preregisterGradeName = preregisterGradeName;
	}
	public String getPreregisterCertificatesname() {
		return preregisterCertificatesname;
	}
	public void setPreregisterCertificatesname(String preregisterCertificatesname) {
		this.preregisterCertificatesname = preregisterCertificatesname;
	}
	public String getPreregisterMiddayname() {
		return preregisterMiddayname;
	}
	public void setPreregisterMiddayname(String preregisterMiddayname) {
		this.preregisterMiddayname = preregisterMiddayname;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}
