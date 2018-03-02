package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 转科申请实体类
 * @author zhenglin
 * @createTime 2015-12-17
 */
public class InpatientShiftApply extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	//住院流水号
	private String inpatientNo;
	//发生序号
	private Integer happenNo;
	//转往科室
	private String newDeptCode;
	//原来科室
	private String oldDeptCode;
	//转往护理站代码
	private String newNurseCellCode;
	//护士站代码
	private String nurseCellCode ;
	//当前状态,0未生效,1转科申请,2确认,3取消申请
	private String shiftState;
	//确认人
	private String confirmOpercode;
	//转科确认时间
	private Date confirmDate;
	//取消人
	private String cancelCode;
	//取消申请时间
	private Date cancelDate;
	//备注
	private String mark;
	//原病床号
	private String oldBedCode;
	//转往床号
	private String newBenCode;
	
	
	//新加字段
	/**转往科室名称**/
	private String newDeptName;
	/**原来科室名称**/
	private String oldDeptName;
	/**转往护理站名称**/
	private String newNurseCellName;
	/**护士站名称**/
	private String nurseCellName;
	/** 新添加字段 医院编码  **/
	private Integer hospitalId;
	/** 新添加字段 院区编码  **/
	private String areaCode;
	
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
	
	public String getNewDeptName() {
		return newDeptName;
	}
	public void setNewDeptName(String newDeptName) {
		this.newDeptName = newDeptName;
	}
	public String getOldDeptName() {
		return oldDeptName;
	}
	public void setOldDeptName(String oldDeptName) {
		this.oldDeptName = oldDeptName;
	}
	public String getNewNurseCellName() {
		return newNurseCellName;
	}
	public void setNewNurseCellName(String newNurseCellName) {
		this.newNurseCellName = newNurseCellName;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getNewDeptCode() {
		return newDeptCode;
	}
	public void setNewDeptCode(String newDeptCode) {
		this.newDeptCode = newDeptCode;
	}
	public String getOldDeptCode() {
		return oldDeptCode;
	}
	public void setOldDeptCode(String oldDeptCode) {
		this.oldDeptCode = oldDeptCode;
	}
	public String getNewNurseCellCode() {
		return newNurseCellCode;
	}
	public void setNewNurseCellCode(String newNurseCellCode) {
		this.newNurseCellCode = newNurseCellCode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getShiftState() {
		return shiftState;
	}
	public void setShiftState(String shiftState) {
		this.shiftState = shiftState;
	}
	public String getConfirmOpercode() {
		return confirmOpercode;
	}
	public void setConfirmOpercode(String confirmOpercode) {
		this.confirmOpercode = confirmOpercode;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getCancelCode() {
		return cancelCode;
	}
	public void setCancelCode(String cancelCode) {
		this.cancelCode = cancelCode;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getOldBedCode() {
		return oldBedCode;
	}
	public void setOldBedCode(String oldBedCode) {
		this.oldBedCode = oldBedCode;
	}
	public String getNewBenCode() {
		return newBenCode;
	}
	public void setNewBenCode(String newBenCode) {
		this.newBenCode = newBenCode;
	}
	
	
	
	
}
