package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class RegisterSchedulemodel extends Entity{

	private static final long serialVersionUID = 1L;
	/**1挂号排班2工作排班**/
	private Integer modelClass;
	/**工作科室**/
	private String modelWorkdept;
	/**医生**/
	private String modelDoctor;
	/**1周一2周二...7周日**/
	private Integer modelWeek;
	/**午别1上午2下午3晚上**/
	private Integer modelMidday;
	/**开始时间**/
	private String modelStartTime;
	/**结束时间**/
	private String modelEndTime;
	/**模板类型**/
	private Integer modeType;
	/**部门编号**/
	private String department;
	/**诊室编号**/
	private String clinic;
	/**挂号限额**/
	private Integer modelLimit;
	/**预约限额**/
	private Integer modelPrelimit;
	/**电话限额**/
	private Integer modelPhonelimit;
	/**网络限额**/
	private Integer modelNetlimit;
	/**特诊限额**/
	private Integer modelSpeciallimit;
	/**是否加号 1是 0否**/
	private Integer modelAppflag=0;
	/**挂号级别**/
	private String modelReggrade;
	/**备注**/
	private String modelRemark;
	
	/** 添加字段**/
	private String search;//用于查询
	/** *********************** vo数据*************************/
	/**科室name**/
	private String deptName;
	/**医生name**/
	private String empName;
	/**诊室name**/
	private String clinName;
	/**午别name**/
	private String codeName;
	/**医院编码*/
	private Integer hospitalId;
	/**院区编码*/
	private String areaCode;
	public String getModelWorkdept() {
		return modelWorkdept;
	}
	public String getModelDoctor() {
		return modelDoctor;
	}
	public void setModelDoctor(String modelDoctor) {
		this.modelDoctor = modelDoctor;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getClinic() {
		return clinic;
	}
	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
	public void setModelWorkdept(String modelWorkdept) {
		this.modelWorkdept = modelWorkdept;
	}
	public Integer getModelWeek() {
		return modelWeek;
	}
	public void setModelWeek(Integer modelWeek) {
		this.modelWeek = modelWeek;
	}
	public Integer getModelMidday() {
		return modelMidday;
	}
	public void setModelMidday(Integer modelMidday) {
		this.modelMidday = modelMidday;
	}
	public Integer getModelLimit() {
		return modelLimit;
	}
	public void setModelLimit(Integer modelLimit) {
		this.modelLimit = modelLimit;
	}
	public Integer getModelPrelimit() {
		return modelPrelimit;
	}
	public void setModelPrelimit(Integer modelPrelimit) {
		this.modelPrelimit = modelPrelimit;
	}
	public Integer getModelPhonelimit() {
		return modelPhonelimit;
	}
	public void setModelPhonelimit(Integer modelPhonelimit) {
		this.modelPhonelimit = modelPhonelimit;
	}
	public Integer getModelNetlimit() {
		return modelNetlimit;
	}
	public void setModelNetlimit(Integer modelNetlimit) {
		this.modelNetlimit = modelNetlimit;
	}
	public String getModelRemark() {
		return modelRemark;
	}
	public void setModelRemark(String modelRemark) {
		this.modelRemark = modelRemark;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getModelStartTime() {
		return modelStartTime;
	}
	public void setModelStartTime(String modelStartTime) {
		this.modelStartTime = modelStartTime;
	}
	public String getModelEndTime() {
		return modelEndTime;
	}
	public void setModelEndTime(String modelEndTime) {
		this.modelEndTime = modelEndTime;
	}
	public Integer getModeType() {
		return modeType;
	}
	public void setModeType(Integer modeType) {
		this.modeType = modeType;
	}
	public Integer getModelSpeciallimit() {
		return modelSpeciallimit;
	}
	public void setModelSpeciallimit(Integer modelSpeciallimit) {
		this.modelSpeciallimit = modelSpeciallimit;
	}
	public Integer getModelAppflag() {
		return modelAppflag;
	}
	public void setModelAppflag(Integer modelAppflag) {
		this.modelAppflag = modelAppflag;
	}
	public String getModelReggrade() {
		return modelReggrade;
	}
	public void setModelReggrade(String modelReggrade) {
		this.modelReggrade = modelReggrade;
	}
	public Integer getModelClass() {
		return modelClass;
	}
	public void setModelClass(Integer modelClass) {
		this.modelClass = modelClass;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getClinName() {
		return clinName;
	}
	public void setClinName(String clinName) {
		this.clinName = clinName;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
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