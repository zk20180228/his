package cn.honry.inner.statistics.operationIncome.vo;

import java.math.BigDecimal;


public class InnerOperationIncomeVo {

	private Double outSum;//门诊收入
	private Double inSum;//住院收入
	
	private Double outInSum;//费用金额
	private String feeStatCode;//报表代码 MZ01 门诊发票 ZY01 住院发票
	private String minFeeName;//最小费用名称
	private String minFeeCode;//最小费用code
	private String finalDate;//费用时间
	private String classType;//门诊住院标记
	private String operationTypeCode;//手术类别code
	private String operationTypeName;//手术类别name
	private String deptName;//科室名称
	private String docName;//医生名称
	private String docCode;//医生名称
	private String deptCode;//科室code
	private String name;//
	
	
	private BigDecimal totalAmount;
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getOperationTypeCode() {
		return operationTypeCode;
	}
	public void setOperationTypeCode(String operationTypeCode) {
		this.operationTypeCode = operationTypeCode;
	}
	public String getOperationTypeName() {
		return operationTypeName;
	}
	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public Double getOutInSum() {
		return outInSum;
	}
	public void setOutInSum(Double outInSum) {
		this.outInSum = outInSum;
	}
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public String getMinFeeName() {
		return minFeeName;
	}
	public void setMinFeeName(String minFeeName) {
		this.minFeeName = minFeeName;
	}
	public String getMinFeeCode() {
		return minFeeCode;
	}
	public void setMinFeeCode(String minFeeCode) {
		this.minFeeCode = minFeeCode;
	}
	
	public Double getOutSum() {
		return outSum;
	}
	public void setOutSum(Double outSum) {
		this.outSum = outSum;
	}
	public Double getInSum() {
		return inSum;
	}
	public void setInSum(Double inSum) {
		this.inSum = inSum;
	}
	public String getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}
	
}
