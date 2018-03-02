package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class TecApply extends Entity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//门诊流水号
	private String clinicCode;
	//交易类型 1 正交易 ，2 负交易
	private Integer transType;
	//就诊卡编码
	private String cardNo;
	//姓名
	private String name;
	//年龄
	private Integer age;
	//项目代码
	private String itemCode;
	//项目名称
	private String itemName;
	//项目数量
	private Integer qty;
	//单位标识 1明细 2 组套
	private Integer unitFlag;
	//处方号
	private String rectpeNo;
	//处方内项目序号
	private Integer sequenceNo;
	//开单科室编码
	private String rectpeDeptCode; 
	//开单科室名称
	private String rectpeDeptName; 
	//执行科室编码
	private String deptCode;
	//执行科室名称
	private String deptName;
	//0 预约 1 生效 2 审核 3 作废
	private Integer status;
	//预约单号
	private String bookId;
	//预约时间
	private Date bookDate;
	//午别
	private String noonCode;
	//知情同意书
	private Integer reasonableFlag;
	//健康状态
	private String healthStatus;
	//执行地点
	private String executeLocate;
	//取报告时间
	private String  reportDate;
	//有创/无创
	private Integer hurtFlag;
	//标本或部位
	private String sampleKind;
	//采样方法
	private String sampleWay;
	//注意事项
	private String remark;
	//顺序号
	private Integer sortId;
	//操作员
	private String operCode;
	//操作科室
	private String operDeptCode;
	//操作日期
	private Date operDate;
	//医嘱流水号
	private String moOrder;
	//已预约的数量
	private Integer arrangenum;
	//执行设备
	private String execDevice;
	//执行人
	private String execOper;
	
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
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
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Integer getUnitFlag() {
		return unitFlag;
	}
	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}
	public String getRectpeNo() {
		return rectpeNo;
	}
	public void setRectpeNo(String rectpeNo) {
		this.rectpeNo = rectpeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getRectpeDeptName() {
		return rectpeDeptName;
	}
	public void setRectpeDeptName(String rectpeDeptName) {
		this.rectpeDeptName = rectpeDeptName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public Date getBookDate() {
		return bookDate;
	}
	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}
	public String getNoonCode() {
		return noonCode;
	}
	public void setNoonCode(String noonCode) {
		this.noonCode = noonCode;
	}
	public Integer getReasonableFlag() {
		return reasonableFlag;
	}
	public void setReasonableFlag(Integer reasonableFlag) {
		this.reasonableFlag = reasonableFlag;
	}
	public String getHealthStatus() {
		return healthStatus;
	}
	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getOperDeptCode() {
		return operDeptCode;
	}
	public void setOperDeptCode(String operDeptCode) {
		this.operDeptCode = operDeptCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public Integer getArrangenum() {
		return arrangenum;
	}
	public void setArrangenum(Integer arrangenum) {
		this.arrangenum = arrangenum;
	}
	public String getExecDevice() {
		return execDevice;
	}
	public void setExecDevice(String execDevice) {
		this.execDevice = execDevice;
	}
	public String getExecOper() {
		return execOper;
	}
	public void setExecOper(String execOper) {
		this.execOper = execOper;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRectpeDeptCode() {
		return rectpeDeptCode;
	}
	public void setRectpeDeptCode(String rectpeDeptCode) {
		this.rectpeDeptCode = rectpeDeptCode;
	}
	
	

}
