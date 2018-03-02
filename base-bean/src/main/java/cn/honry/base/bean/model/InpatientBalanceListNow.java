package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：InpatientBalanceList.java 
 * @Description：住院结算明细表
 * @Author：hedong
 * @CreateDate：2015-08-17  
 * @version 1.0
 * 结算召回模块
 */
public class InpatientBalanceListNow extends Entity implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
	  private String invoiceNo;// 发票号码 结转时为流水号
	  private Integer transType;// 交易类型,1正交易，2反交易 
	  private String inpatientNo;//住院流水号
	  private String name;// 姓名          
	  private String paykindCode;// 结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干 
	  private String pactCode;// 合同单位
	  private String deptCode;// 在院科室 
	  private String statCode;// 统计大类 
	  private String statName;// 统计大类名称
	  private Integer sortId;//  打印顺序号  
	  private Double totCost;//  费用金额
	  private Double ownCost;//  自费金额 
	  private Double payCost;//  自付金额
	  private Double pubCost;//  公费金额
	  private Double ecoCost;//  优惠金额
	  private String balanceOpercode;// 结算人代码 
	  private Date balanceDate;//  结算时间
	  private Integer balanceType;// 结算类型 1:在院结算，2:转科结算，3:出院结算，4:重结算 5:结转 
	  private Integer balanceNo;// 结算序号
	  private Integer babyFlag;// 婴儿标志1：是，0：否 
	  private String checkNo;// 审核序号
	  private Integer extFlag;// 扩展标志 
	  private Integer ext1Flag;// 扩展标志1 
	  private Integer ext2Flag;// 扩展标志2 
	  private String extCode;// 扩展代码 
	  private Date extDate;// 扩展日期
	  private String extOpercode;//扩展操作员 
	  private String balanceoperDeptcode;//结算员科室 
	  
	  //新加字段
	  /**在院科室名称**/
	  private String deptName;
	  /**结算人名称**/
	  private String balanceOpername;
	  /**结算员科室名称**/
	  private String balanceoperDeptname;
	  /**医院编码**/
	  private Integer hospitalId;
	  /**院区编码**/
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
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBalanceOpername() {
		return balanceOpername;
	}
	public void setBalanceOpername(String balanceOpername) {
		this.balanceOpername = balanceOpername;
	}
	public String getBalanceoperDeptname() {
		return balanceoperDeptname;
	}
	public void setBalanceoperDeptname(String balanceoperDeptname) {
		this.balanceoperDeptname = balanceoperDeptname;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
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
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getStatCode() {
		return statCode;
	}
	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}
	public String getStatName() {
		return statName;
	}
	public void setStatName(String statName) {
		this.statName = statName;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public String getBalanceOpercode() {
		return balanceOpercode;
	}
	public void setBalanceOpercode(String balanceOpercode) {
		this.balanceOpercode = balanceOpercode;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Integer getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public Integer getExt1Flag() {
		return ext1Flag;
	}
	public void setExt1Flag(Integer ext1Flag) {
		this.ext1Flag = ext1Flag;
	}
	public Integer getExt2Flag() {
		return ext2Flag;
	}
	public void setExt2Flag(Integer ext2Flag) {
		this.ext2Flag = ext2Flag;
	}
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	public Date getExtDate() {
		return extDate;
	}
	public void setExtDate(Date extDate) {
		this.extDate = extDate;
	}
	public String getExtOpercode() {
		return extOpercode;
	}
	public void setExtOpercode(String extOpercode) {
		this.extOpercode = extOpercode;
	}
	public String getBalanceoperDeptcode() {
		return balanceoperDeptcode;
	}
	public void setBalanceoperDeptcode(String balanceoperDeptcode) {
		this.balanceoperDeptcode = balanceoperDeptcode;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	  
	  
}
