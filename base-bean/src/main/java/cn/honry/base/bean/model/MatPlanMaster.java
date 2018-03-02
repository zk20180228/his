package cn.honry.base.bean.model;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  采购计划主表
 * @Author：zpty
 * @CreateDate：2017-11-20 上午09:35:05  
 *
 */
public class MatPlanMaster extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 采购流水号   **/
	private String procurementNo;
	/** 采购科室  **/
	private String procurementDept;
	/** 采购科室名称   **/
	private String procurementDeptName;
	/** 预算金额   **/
	private Double budgetMoney;
	/** 供货商代码   **/
	private String companyCode;
	/** 供货商名称   **/
	private String companyName;
	/** 审核人   **/
	private String checkUser;
	/** 审核人姓名   **/
	private String checkUserName;
	/** 审核状态 (0:通过,1未通过)  **/
	private Integer checkStatus;
	/** 暂存标志 (0:保存,1暂存)  **/
	private Integer saveTemp;
	
	/**所属医院   **/
	private Integer hospitalId;
	/** 所属院区   **/
	private String areaCode;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getProcurementNo() {
		return procurementNo;
	}
	public void setProcurementNo(String procurementNo) {
		this.procurementNo = procurementNo;
	}
	public String getProcurementDept() {
		return procurementDept;
	}
	public void setProcurementDept(String procurementDept) {
		this.procurementDept = procurementDept;
	}
	public String getProcurementDeptName() {
		return procurementDeptName;
	}
	public void setProcurementDeptName(String procurementDeptName) {
		this.procurementDeptName = procurementDeptName;
	}
	public Double getBudgetMoney() {
		return budgetMoney;
	}
	public void setBudgetMoney(Double budgetMoney) {
		this.budgetMoney = budgetMoney;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	public Integer getSaveTemp() {
		return saveTemp;
	}
	public void setSaveTemp(Integer saveTemp) {
		this.saveTemp = saveTemp;
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
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}