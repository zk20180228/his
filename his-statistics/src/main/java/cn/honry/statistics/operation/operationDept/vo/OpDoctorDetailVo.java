package cn.honry.statistics.operation.operationDept.vo;
/***
 * 手术医生明细vo
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.Date;

public class OpDoctorDetailVo {
	/**
	 * 医生科室
	 */
	private String opDoctorDept;
	
	/**
	 * 医生
	 */
	private String opDoctor;
	
	/**
	 * 手术时间
	 */
	private Date preDate;
	
	/**
	 * 住院号
	 */
	 private String inpatientNo;
	 
	 /**
	  * 姓名
	  */
	 private String name;
	 
	 /**
	  * 手术名称
	  */
	 private String itemName;
	 
	 /**
	  * 手术费
	  */
	 private Double totCost;
	 
	 /**
	  * 记账科室
	  */
	 private String execDept;
	 
	 /**
	  * 操作员
	  */
	 private String feeOperCode;

	public String getOpDoctorDept() {
		return opDoctorDept;
	}

	public void setOpDoctorDept(String opDoctorDept) {
		this.opDoctorDept = opDoctorDept;
	}

	public String getOpDoctor() {
		return opDoctor;
	}

	public void setOpDoctor(String opDoctor) {
		this.opDoctor = opDoctor;
	}
	
	public Date getPreDate() {
		return preDate;
	}

	public void setPreDate(Date preDate) {
		this.preDate = preDate;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getTotCost() {
		return totCost;
	}

	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}

	public String getExecDept() {
		return execDept;
	}

	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}

	public String getFeeOperCode() {
		return feeOperCode;
	}

	public void setFeeOperCode(String feeOperCode) {
		this.feeOperCode = feeOperCode;
	}
	 
}
