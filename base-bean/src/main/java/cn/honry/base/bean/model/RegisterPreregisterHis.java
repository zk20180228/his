package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class RegisterPreregisterHis extends Entity{
	/**排班编号**/
	private RegisterSchedule scheduleId;
	/** 预约编号**/
	private String preregisterId;
	/** 预约号 **/
	private String preregisterNo;
	/**是否网络预约**/
	private Integer preregisterIsnet=0;
	/**是否点话预约**/
	private Integer preregisterIsphone=0;
	/**挂号科室**/
	private SysDepartment preregisterDept;
	/**挂号专家**/
	private SysEmployee preregisterExpert;
	/**挂号级别**/
	private RegisterGrade preregisterGrade;
	/**预约日期**/
	private Date preregisterDate;
	/**预约开始时间**/
	private Integer preregisterStarttime;
	/**预约结束时间**/
	private Integer preregisterEndtime;
	/**病历号**/ 
    private  String  medicalrecordId;
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
    /**合同单位**/
    private Integer  preregisterAgeunit=0;
    /**电话**/
    private String  preregisterPhone; 
    /**地址**/
    private String  preregisterAddress;
    
	public RegisterSchedule getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(RegisterSchedule scheduleId) {
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
	public SysDepartment getPreregisterDept() {
		return preregisterDept;
	}
	public void setPreregisterDept(SysDepartment preregisterDept) {
		this.preregisterDept = preregisterDept;
	}
	public SysEmployee getPreregisterExpert() {
		return preregisterExpert;
	}
	public void setPreregisterExpert(SysEmployee preregisterExpert) {
		this.preregisterExpert = preregisterExpert;
	}
	
	public Date getPreregisterDate() {
		return preregisterDate;
	}
	public void setPreregisterDate(Date preregisterDate) {
		this.preregisterDate = preregisterDate;
	}
	public Integer getPreregisterStarttime() {
		return preregisterStarttime;
	}
	public void setPreregisterStarttime(Integer preregisterStarttime) {
		this.preregisterStarttime = preregisterStarttime;
	}
	public Integer getPreregisterEndtime() {
		return preregisterEndtime;
	}
	public void setPreregisterEndtime(Integer preregisterEndtime) {
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
	public Integer getPreregisterAgeunit() {
		return preregisterAgeunit;
	}
	public void setPreregisterAgeunit(Integer preregisterAgeunit) {
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
	public String getPreregisterId() {
		return preregisterId;
	}
	public void setPreregisterId(String preregisterId) {
		this.preregisterId = preregisterId;
	}
	public RegisterGrade getPreregisterGrade() {
		return preregisterGrade;
	}
	public void setPreregisterGrade(RegisterGrade preregisterGrade) {
		this.preregisterGrade = preregisterGrade;
	}

}
