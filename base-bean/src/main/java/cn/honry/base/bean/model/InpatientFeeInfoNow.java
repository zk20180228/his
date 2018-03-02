package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 * @className：InpatientFeeInfo.java 
 * @Description：住院费用汇总表
 * @Author：hedong
 * @CreateDate：2015-08-12
 * @version 1.0
 */
public class InpatientFeeInfoNow extends Entity implements java.io.Serializable{
	  private static final long serialVersionUID = 1L;
	  private String recipeNo;//处方号        
	  private String feeCode;// 最小费用代码         
	  private Integer transType;//交易类型,1正交易，2反交易        
	  private String inpatientNo;//住院流水号     
	  private String name;// 姓名             VARCHAR2(20),
	  private String paykindCode;//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干      
	  private String pactCode;//合同单位      
	  private String inhosDeptcode;//在院科室代码    
	  private String nurseCellCode;//护士站代码   
	  private String recipeDeptcode;//开立科室代码   
	  private String executeDeptcode;//执行科室代码  
	  private String stockDeptcode;//扣库科室代码    
	  private String recipeDoccode;//开立医师代码    
	  private Double totCost;//费用金额         
	  private Double ownCost;//自费金额        
	  private Double payCost;//自付金额        
	  private Double pubCost;//公费金额         
	  private Double ecoCost;//优惠金额          
	  private String chargeOpercode;//划价人   
	  private Date chargeDate;//划价日期     
	  private String feeOpercode;//计费人     
	  private Date feeDate;//计费日期          
	  private String balanceOpercode;//结算人代码  
	  private Date balanceDate;//结算时间  
	  private String invoiceNo;//结算发票号       
	  private Integer balanceNo;//结算序号        
	  private Integer balanceState;//结算标志 0:未结算；1:已结算 2:已结转   
	  private String checkNo;// 审核序号      
	  private Integer babyFlag;//婴儿标记1：是，0：否(初始值为0)      
	  private Integer extFlag;//扩展标志(公费患者是否使用了自费的项目0否,1是)        
	  private String extCode;//扩展代码(中山一：保存退费原记录的处方号)         
	  private Date extDate;//扩展日期         
	  private String extOpercode;//扩展操作员      
	  private String feeoperDeptcode;//收费员科室 
	  private String extFlag1;//扩展标志1        
	  private Integer extFlag2 ;//扩展标志2   
	  private String operationId;//手术序号
	  
	  //新加字段
	  /**在院科室名称**/
	  private String inhosDeptname;
	  /**护士站名称**/
	  private String nurseCellName;
	  /**开立科室名称**/
	  private String recipeDeptname;
	  /**执行科室名称**/
	  private String executeDeptname;
	  /**扣库科室名称**/
	  private String stockDeptname;
	  /**开立医师名称**/
	  private String recipeDocname;
	//新增字段 2017-06-12
	   /**医院编码**/
	   private Integer hospitalId;
	   /**院区编码吗**/
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
	public String getInhosDeptname() {
		return inhosDeptname;
	}
	public void setInhosDeptname(String inhosDeptname) {
		this.inhosDeptname = inhosDeptname;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getRecipeDeptname() {
		return recipeDeptname;
	}
	public void setRecipeDeptname(String recipeDeptname) {
		this.recipeDeptname = recipeDeptname;
	}
	public String getExecuteDeptname() {
		return executeDeptname;
	}
	public void setExecuteDeptname(String executeDeptname) {
		this.executeDeptname = executeDeptname;
	}
	public String getStockDeptname() {
		return stockDeptname;
	}
	public void setStockDeptname(String stockDeptname) {
		this.stockDeptname = stockDeptname;
	}
	public String getRecipeDocname() {
		return recipeDocname;
	}
	public void setRecipeDocname(String recipeDocname) {
		this.recipeDocname = recipeDocname;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
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
	public String getInhosDeptcode() {
		return inhosDeptcode;
	}
	public void setInhosDeptcode(String inhosDeptcode) {
		this.inhosDeptcode = inhosDeptcode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getRecipeDeptcode() {
		return recipeDeptcode;
	}
	public void setRecipeDeptcode(String recipeDeptcode) {
		this.recipeDeptcode = recipeDeptcode;
	}
	public String getExecuteDeptcode() {
		return executeDeptcode;
	}
	public void setExecuteDeptcode(String executeDeptcode) {
		this.executeDeptcode = executeDeptcode;
	}
	public String getStockDeptcode() {
		return stockDeptcode;
	}
	public void setStockDeptcode(String stockDeptcode) {
		this.stockDeptcode = stockDeptcode;
	}
	public String getRecipeDoccode() {
		return recipeDoccode;
	}
	public void setRecipeDoccode(String recipeDoccode) {
		this.recipeDoccode = recipeDoccode;
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
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
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
	public String getChargeOpercode() {
		return chargeOpercode;
	}
	public void setChargeOpercode(String chargeOpercode) {
		this.chargeOpercode = chargeOpercode;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getFeeOpercode() {
		return feeOpercode;
	}
	public void setFeeOpercode(String feeOpercode) {
		this.feeOpercode = feeOpercode;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
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
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
	}
	public Integer getBalanceState() {
		return balanceState;
	}
	public void setBalanceState(Integer balanceState) {
		this.balanceState = balanceState;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
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
	public String getFeeoperDeptcode() {
		return feeoperDeptcode;
	}
	public void setFeeoperDeptcode(String feeoperDeptcode) {
		this.feeoperDeptcode = feeoperDeptcode;
	}
	public String getExtFlag1() {
		return extFlag1;
	}
	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}
	public Integer getExtFlag2() {
		return extFlag2;
	}
	public void setExtFlag2(Integer extFlag2) {
		this.extFlag2 = extFlag2;
	}
	  
	  
}
