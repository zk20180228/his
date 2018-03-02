package cn.honry.inner.operation.billsearch.vo;
/***
 * 手术计费信息vo
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.Date;

public class OperationBillingInfoVo {
	
	/**
	 * 手术编号
	 */
	private String opId;
	
	/**
	 * 住院号
	 */
	private String patientNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 预约手术日期
	 */
	private Date preDate;
	/**
	 * 手术诊断
	 */
	private String diagName;
	
	/**
	 * 手术医生
	 */
	private String opDoctor;
	
	/**
	 * 手术医生科室
	 */
	private String opDoctordept;
	
	/**
	 * 在院状态
	 */
	private String inState;
	
	/**
	 * 手术状态
	 */
	private int opStatus;
	
	/**
	 * 计费状态
	 */
	private String feeState;

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPreDate() {
		return preDate;
	}

	public void setPreDate(Date preDate) {
		this.preDate = preDate;
	}

	public String getOpDoctor() {
		return opDoctor;
	}

	public void setOpDoctor(String opDoctor) {
		this.opDoctor = opDoctor;
	}

	public String getOpDoctordept() {
		return opDoctordept;
	}

	public void setOpDoctordept(String opDoctordept) {
		this.opDoctordept = opDoctordept;
	}

	public String getInState() {
		return inState;
	}

	public void setInState(String inState) {
		this.inState = inState;
	}

	
	public int getOpStatus() {
		return opStatus;
	}

	public void setOpStatus(int opStatus) {
		this.opStatus = opStatus;
	}

	public String getFeeState() {
		return feeState;
	}

	public void setFeeState(String feeState) {
		this.feeState = feeState;
	}

	public String getDiagName() {
		return diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	
	
}
