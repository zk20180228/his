package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class TecDeptItem extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//科室代码
	private String deptCode;
	//项目代码
	private String itemCode;
	//项目名称
	private String itemName;
	//系统类别
	private String classCode;
	//单位标识(1.明细   2.组套)
	private Integer unitFlag;
	//预约地
	private String bookLocate;
	//预约固定时间
	private String bookDate;
	//执行地点
	private String executeLocate;
	//取报告时间
	private String reportDate;
	//有创/无创  （0 有，1无）
	private Integer hurtFlag;
	//是否科内预约（ 0是，1否）
	private Integer selfbookFlag;
	//知情同意书  （0需要，1不需要）
	private Integer reasonableFlag;
	//所属专业
	private String speciality;
	//临床意义
	private String clinicMeaning;
	//标本
	private String sampleKind;
	//采样方法
	private String sampleWay;
	//标本单位
	private String sampleUnit;
	//标本量
	private Integer sampleQty;
	//容器
	private String sampleContainer;
	//正常值范围
	private String scope;
	//设备类型
	private String machineType;
	//注意事项
	private String remark;
	//操作员
	private String operCode;
	//操作日期
	private Date operDate;
	//抽血方式
	private String extFile;
	//备用1
	private String extFile1;
	//备用2
	private String extFile2;
	//备用3
	private String extFile3 ;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Integer getUnitFlag() {
		return unitFlag;
	}
	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}
	public String getBookLocate() {
		return bookLocate;
	}
	public void setBookLocate(String bookLocate) {
		this.bookLocate = bookLocate;
	}
	public String getBookDate() {
		return bookDate;
	}
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	public String getExecuteLocate() {
		return executeLocate;
	}
	public void setExecuteLocate(String executeLocate) {
		this.executeLocate = executeLocate;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public Integer getHurtFlag() {
		return hurtFlag;
	}
	public void setHurtFlag(Integer hurtFlag) {
		this.hurtFlag = hurtFlag;
	}
	public Integer getSelfbookFlag() {
		return selfbookFlag;
	}
	public void setSelfbookFlag(Integer selfbookFlag) {
		this.selfbookFlag = selfbookFlag;
	}
	public Integer getReasonableFlag() {
		return reasonableFlag;
	}
	public void setReasonableFlag(Integer reasonableFlag) {
		this.reasonableFlag = reasonableFlag;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getClinicMeaning() {
		return clinicMeaning;
	}
	public void setClinicMeaning(String clinicMeaning) {
		this.clinicMeaning = clinicMeaning;
	}
	public String getSampleKind() {
		return sampleKind;
	}
	public void setSampleKind(String sampleKind) {
		this.sampleKind = sampleKind;
	}
	public String getSampleWay() {
		return sampleWay;
	}
	public void setSampleWay(String sampleWay) {
		this.sampleWay = sampleWay;
	}
	public String getSampleUnit() {
		return sampleUnit;
	}
	public void setSampleUnit(String sampleUnit) {
		this.sampleUnit = sampleUnit;
	}
	public Integer getSampleQty() {
		return sampleQty;
	}
	public void setSampleQty(Integer sampleQty) {
		this.sampleQty = sampleQty;
	}
	public String getSampleContainer() {
		return sampleContainer;
	}
	public void setSampleContainer(String sampleContainer) {
		this.sampleContainer = sampleContainer;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getMachineType() {
		return machineType;
	}
	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getExtFile() {
		return extFile;
	}
	public void setExtFile(String extFile) {
		this.extFile = extFile;
	}
	public String getExtFile1() {
		return extFile1;
	}
	public void setExtFile1(String extFile1) {
		this.extFile1 = extFile1;
	}
	public String getExtFile2() {
		return extFile2;
	}
	public void setExtFile2(String extFile2) {
		this.extFile2 = extFile2;
	}
	public String getExtFile3() {
		return extFile3;
	}
	public void setExtFile3(String extFile3) {
		this.extFile3 = extFile3;
	}
	
	
}
