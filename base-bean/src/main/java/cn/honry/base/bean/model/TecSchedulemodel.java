package cn.honry.base.bean.model;


import cn.honry.base.bean.business.Entity;

/**
 * TTecSchedulemodel entity. @author MyEclipse Persistence Tools
 */
/**
 * 医技模板排版
 * @author Administrator
 *
 */
public class TecSchedulemodel extends Entity implements java.io.Serializable {

	// Fields

	private String modelItemCode;//项目/设备编码
	private String modelItemName;//项目/设备名称
	private Integer modelClass;//模板类型（1-项目2-设备）
	private Integer unitFlag;//单位标识1-.明细2-组套
	private Integer modelWeek;//星期
	private Integer modelMidday;//午别
	private String modelStarttime;//开始时间
	private String modelEndtime;//结束时间
	private String modelDeptid;//科室编号
	private String modelClinicid;//诊室编号
	private Integer modelPrelimit;//预约限额
	private Integer modelNetlimit;//网络限额
	private Integer modelSpeciallimit;//特诊限额
	private Integer modelAppflag;//是否加号
	private Integer validFlag;//有效性标志1  有效 2 无效
	private String modelAttentions;//注意事项

	// Constructors



	// Property accessors


	public String getModelItemCode() {
		return this.modelItemCode;
	}

	public void setModelItemCode(String modelItemCode) {
		this.modelItemCode = modelItemCode;
	}

	public String getModelItemName() {
		return this.modelItemName;
	}

	public void setModelItemName(String modelItemName) {
		this.modelItemName = modelItemName;
	}

	public Integer getModelClass() {
		return this.modelClass;
	}

	public void setModelClass(Integer modelClass) {
		this.modelClass = modelClass;
	}

	public Integer getUnitFlag() {
		return this.unitFlag;
	}

	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}

	public Integer getModelWeek() {
		return this.modelWeek;
	}

	public void setModelWeek(Integer modelWeek) {
		this.modelWeek = modelWeek;
	}

	public Integer getModelMidday() {
		return this.modelMidday;
	}

	public void setModelMidday(Integer modelMidday) {
		this.modelMidday = modelMidday;
	}

	public String getModelStarttime() {
		return this.modelStarttime;
	}

	public void setModelStarttime(String modelStarttime) {
		this.modelStarttime = modelStarttime;
	}

	public String getModelEndtime() {
		return this.modelEndtime;
	}

	public void setModelEndtime(String modelEndtime) {
		this.modelEndtime = modelEndtime;
	}

	public String getModelDeptid() {
		return this.modelDeptid;
	}

	public void setModelDeptid(String modelDeptid) {
		this.modelDeptid = modelDeptid;
	}

	public String getModelClinicid() {
		return this.modelClinicid;
	}

	public void setModelClinicid(String modelClinicid) {
		this.modelClinicid = modelClinicid;
	}

	public Integer getModelPrelimit() {
		return this.modelPrelimit;
	}

	public void setModelPrelimit(Integer modelPrelimit) {
		this.modelPrelimit = modelPrelimit;
	}

	public Integer getModelNetlimit() {
		return this.modelNetlimit;
	}

	public void setModelNetlimit(Integer modelNetlimit) {
		this.modelNetlimit = modelNetlimit;
	}

	public Integer getModelSpeciallimit() {
		return this.modelSpeciallimit;
	}

	public void setModelSpeciallimit(Integer modelSpeciallimit) {
		this.modelSpeciallimit = modelSpeciallimit;
	}

	public Integer getModelAppflag() {
		return this.modelAppflag;
	}

	public void setModelAppflag(Integer modelAppflag) {
		this.modelAppflag = modelAppflag;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public String getModelAttentions() {
		return this.modelAttentions;
	}

	public void setModelAttentions(String modelAttentions) {
		this.modelAttentions = modelAttentions;
	}

}