package cn.honry.statistics.finance.operationCost.vo;
/***
 * 手术费用汇总vo
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.Date;

public class OperationCostVo {
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 执行科室
	 */
	private String execDept;
	
	/**
	 * 手术费
	 */
	private Double totCost;
	
	/**
	 * 收费日期
	 */
	private Date feeDate;

	/**
	 * 病历号
	 */
	private String  medicalrecordId;
	
	

	public String getMedicalrecordId() {
		return medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecDept() {
		return execDept;
	}

	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}

	public Double getTotCost() {
		return totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	
	
}
