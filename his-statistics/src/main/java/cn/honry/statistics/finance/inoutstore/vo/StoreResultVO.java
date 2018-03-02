package cn.honry.statistics.finance.inoutstore.vo;

import java.math.BigDecimal;
import java.util.Date;

/**  
 *  封装查询结果VO
 * @Author:luyanshou
 * @version 1.0
 */
public class StoreResultVO {

	/**药品编码**/
	private String drugCode;
	/**药品商品名**/
	private String tradeName;
	/**规格**/
	private String specs;
	/**入出库数**/
	private BigDecimal inoutNum;
	/**包装数**/
	private BigDecimal packQty;
	/**科室编码 0-全部部门**/
	private String drugDeptCode;
	/**科室名称**/
	private String drugDeptName;
	/**入出库日期**/
	private Date inoutDate;
	/**核准人**/
	private String approve;
	/**核准人姓名**/
	private String userName;
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public BigDecimal getInoutNum() {
		return inoutNum;
	}
	public void setInoutNum(BigDecimal inoutNum) {
		this.inoutNum = inoutNum;
	}
	public BigDecimal getPackQty() {
		return packQty;
	}
	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getDrugDeptName() {
		return drugDeptName;
	}
	public void setDrugDeptName(String drugDeptName) {
		this.drugDeptName = drugDeptName;
	}
	public Date getInoutDate() {
		return inoutDate;
	}
	public void setInoutDate(Date inoutDate) {
		this.inoutDate = inoutDate;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
