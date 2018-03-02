package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientDayreportDetail  extends Entity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*统计日期*/
	private Date stateDate;
	/*病房代码*/
	private String deptCode;
	/*护士站代码*/
	private String nurseCellCode;
	/*住院流水号*/
	private String inpatientNo;
	/*床号*/
	private String bedNo;
	/*统计类型*/
	private String statType;
	/*备注*/
	private String mark;
	/*是否有效 0有效 1无效*/
	private Integer validState;
	/*对于出院患者表示转归*/
	private String extend;
	/*明细上报标志 0未提交 1提交未审核 2打回 3已审核*/
	private Integer upflag;
	/*取消操作人*/
	private String cancelOperCode;
	/*取消操作日期*/
	private Date cancelOperDate;
	//新增字段 2017-06-12
	   /**医院编码**/
	   private Integer hospitalId;
	   /**院区编码吗**/
	   private String areaCode;
	public Date getStateDate() {
		return stateDate;
	}
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getStatType() {
		return statType;
	}
	public void setStatType(String statType) {
		this.statType = statType;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public Integer getUpflag() {
		return upflag;
	}
	public void setUpflag(Integer upflag) {
		this.upflag = upflag;
	}
	public String getCancelOperCode() {
		return cancelOperCode;
	}
	public void setCancelOperCode(String cancelOperCode) {
		this.cancelOperCode = cancelOperCode;
	}
	public Date getCancelOperDate() {
		return cancelOperDate;
	}
	public void setCancelOperDate(Date cancelOperDate) {
		this.cancelOperDate = cancelOperDate;
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
