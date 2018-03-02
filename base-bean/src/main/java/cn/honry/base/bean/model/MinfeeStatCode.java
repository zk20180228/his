package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class MinfeeStatCode extends Entity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//报表代码 MZ01 门诊发票 ZY01 住院发票(归类维护编码取)
	private String reportCode;
	//报表类别
	private String reportType;
	//报表名称
	private String reportName;
	//最小费用代码（编码表中取）
	private String minfeeCode;
	//最小费用名称
	private String minfeeName;
	//统计费用代码（编码表中取）
	private String feeStatCode;
	//统计费用名称
	private String feeStatName;
	//医保中心统计费用代码（编码表中取）
	private String centerStatCode;
	//医保中心统计费用名称
	private String centerStatName;
	//打印顺序
	private Integer printOrder;
	//执行科室
	private String exeDeptId;
	
	
	//--虚拟报表名称
	private String reportNameVo;
	//--虚拟最小费用名称
	private String minfeeNameVo;
	//--虚拟统计费用名称
	private String feeStatNameVo;
	//--虚拟医保中心统计费用名称
	private String centerStatNameVo;
	//--虚拟执行科室
	private String exeDeptIdVo;
	
	
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getMinfeeCode() {
		return minfeeCode;
	}
	public void setMinfeeCode(String minfeeCode) {
		this.minfeeCode = minfeeCode;
	}
	public String getMinfeeName() {
		return minfeeName;
	}
	public void setMinfeeName(String minfeeName) {
		this.minfeeName = minfeeName;
	}
	public String getFeeStatCode() {
		return feeStatCode;
	}
	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}
	public String getFeeStatName() {
		return feeStatName;
	}
	public void setFeeStatName(String feeStatName) {
		this.feeStatName = feeStatName;
	}
	public String getCenterStatCode() {
		return centerStatCode;
	}
	public void setCenterStatCode(String centerStatCode) {
		this.centerStatCode = centerStatCode;
	}
	public String getCenterStatName() {
		return centerStatName;
	}
	public void setCenterStatName(String centerStatName) {
		this.centerStatName = centerStatName;
	}
	
	public Integer getPrintOrder() {
		return printOrder;
	}
	public void setPrintOrder(Integer printOrder) {
		this.printOrder = printOrder;
	}
	public String getExeDeptId() {
		return exeDeptId;
	}
	public void setExeDeptId(String exeDeptId) {
		this.exeDeptId = exeDeptId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getReportNameVo() {
		return reportNameVo;
	}
	public void setReportNameVo(String reportNameVo) {
		this.reportNameVo = reportNameVo;
	}
	public String getMinfeeNameVo() {
		return minfeeNameVo;
	}
	public void setMinfeeNameVo(String minfeeNameVo) {
		this.minfeeNameVo = minfeeNameVo;
	}
	
	public String getFeeStatNameVo() {
		return feeStatNameVo;
	}
	public void setFeeStatNameVo(String feeStatNameVo) {
		this.feeStatNameVo = feeStatNameVo;
	}
	public String getCenterStatNameVo() {
		return centerStatNameVo;
	}
	public void setCenterStatNameVo(String centerStatNameVo) {
		this.centerStatNameVo = centerStatNameVo;
	}
	public String getExeDeptIdVo() {
		return exeDeptIdVo;
	}
	public void setExeDeptIdVo(String exeDeptIdVo) {
		this.exeDeptIdVo = exeDeptIdVo;
	}
	
	
	

}
