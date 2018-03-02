package cn.honry.inpatient.exitNoFee.vo;

import java.util.Date;

/**
 * 费用结算vo
 * @author yeguanqun
 *
 */
public class FeeVo {	
      private String  itemCode;//药品非药品项目编码    *
      private String  ty;//药品非药品标识1药品2非药品   *
	  private String recipeNo;// 处方号                  
	  private Integer sequenceNo;// 处方内项目流水号              
	  private Integer transType;//交易类型,1正交易，2反交易                 
	  private String inpatientNo ;//住院流水号   *
	  private String nurseCellCode;//护士站代码
	  private String recipeDeptCode;//开立科室代码             
	  private String executeDeptCode;//执行科室代码       *     
	  private String medicineDeptcode;//取药科室代码          
	  private String recipeDocCode;//开立医师代码                      
	  private String feeCode;// 最小费用代码                  
	  private String centerCode;//医疗中心项目代码                    
	  private Integer homeMadeFlag;//自制标识                           
	  private String currentUnit ;//当前单位          *                  
	  private Double qty ;// 数量          *           
	  private Integer days ;//天数                            
	  private Double ecoCost = 0.0  ;// 优惠金额               
	  private String updateSequenceno;//更新库存的流水号        
	  private String senddrugSequence ;//发药单序列号         
	  private Integer senddrugFlag;// 发药（发放）状态（0 划价 2摆药 1批费）             
	  private Integer babyFlag  ;//是否婴儿用药 0 不是 1 是                 
	  private Integer jzqjFlag  ;// 急诊抢救标志                
	  private Integer broughtFlag  ;//出院带药标记 0 否 1 是(Change as OrderType)                  
	  private Double nobackNum;//可退数量                     
	  private String apprno ;//  审批号(中山一：退费时保存退费申请单号)                              
	  private String execOpercode;//执行人代码               
	  private Date execDate  ;// 执行日期               
	  private String senddrugOpercode ;// 发药人         
	  private Date senddrugDate ;//发药日期            
	  private String checkOpercode ;//审核人            
	  private String checkNo ;//审核序号                   
	  private String moOrder ;//医嘱流水号                   
	  private String moExecSqn  ;//医嘱执行单流水号               
	  private Double  feeRate   ;//收费比率               
	  private Integer  uploadFlag;// 上传标志                
	  private Integer  extFlag2;//扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整)                          
//	  private String extFlag3  ;// 聊城市医保新增(记录凭单号)                
	  private String operationno ;//手术编码              
/*	  private String transactionSequenceNumber;//医保交易流水号 
	  private Date siTransactionDatetime ;//医保交易时间
	  private String hisRecipeNo  ;//HIS处方号            
	  private String siRecipeNo ;//医保处方号               
	  private String hisCancelRecipeNo  ;//HIS原处方号      
	  private String siCancelRecipeNo   ;//医保原处方号 
*/	  private String packageCode;//组套代码                
	  private String packageName;//组套名称   
	  private Double confirmNum  ;//已确认数     
	  private Integer itemFlag ;//  0非药品 2物资  
	  private Integer goon;//欠费之后,收费+医保处理继续标记 *欠费判断后必填
	  private Double changeTotcost;//转入费用金额(未结)
	  private String feeoperDeptcode;//收费员科室 
	  private String orderId;//医嘱主键,往页面传的，不保存到费用相关表（医嘱分解调用接口时用到）
	  private Integer  balanceNo  ;// 结算序号   
	  private String  paykindCode;//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
	  private String pactCode;//合同单位    
	  
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getTy() {
		return ty;
	}
	public void setTy(String ty) {
		this.ty = ty;
	}	
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getExecuteDeptCode() {
		return executeDeptCode;
	}
	public void setExecuteDeptCode(String executeDeptCode) {
		this.executeDeptCode = executeDeptCode;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}	
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getRecipeDeptCode() {
		return recipeDeptCode;
	}
	public void setRecipeDeptCode(String recipeDeptCode) {
		this.recipeDeptCode = recipeDeptCode;
	}
	public String getMedicineDeptcode() {
		return medicineDeptcode;
	}
	public void setMedicineDeptcode(String medicineDeptcode) {
		this.medicineDeptcode = medicineDeptcode;
	}
	public String getRecipeDocCode() {
		return recipeDocCode;
	}
	public void setRecipeDocCode(String recipeDocCode) {
		this.recipeDocCode = recipeDocCode;
	}
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public Integer getHomeMadeFlag() {
		return homeMadeFlag;
	}
	public void setHomeMadeFlag(Integer homeMadeFlag) {
		this.homeMadeFlag = homeMadeFlag;
	}
	public String getCurrentUnit() {
		return currentUnit;
	}
	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getUpdateSequenceno() {
		return updateSequenceno;
	}
	public void setUpdateSequenceno(String updateSequenceno) {
		this.updateSequenceno = updateSequenceno;
	}
	public String getSenddrugSequence() {
		return senddrugSequence;
	}
	public void setSenddrugSequence(String senddrugSequence) {
		this.senddrugSequence = senddrugSequence;
	}
	public Integer getSenddrugFlag() {
		return senddrugFlag;
	}
	public void setSenddrugFlag(Integer senddrugFlag) {
		this.senddrugFlag = senddrugFlag;
	}
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public Integer getJzqjFlag() {
		return jzqjFlag;
	}
	public void setJzqjFlag(Integer jzqjFlag) {
		this.jzqjFlag = jzqjFlag;
	}
	public Integer getBroughtFlag() {
		return broughtFlag;
	}
	public void setBroughtFlag(Integer broughtFlag) {
		this.broughtFlag = broughtFlag;
	}
	public Double getNobackNum() {
		return nobackNum;
	}
	public void setNobackNum(Double nobackNum) {
		this.nobackNum = nobackNum;
	}
	public String getApprno() {
		return apprno;
	}
	public void setApprno(String apprno) {
		this.apprno = apprno;
	}
	public String getExecOpercode() {
		return execOpercode;
	}
	public void setExecOpercode(String execOpercode) {
		this.execOpercode = execOpercode;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	public String getSenddrugOpercode() {
		return senddrugOpercode;
	}
	public void setSenddrugOpercode(String senddrugOpercode) {
		this.senddrugOpercode = senddrugOpercode;
	}
	public Date getSenddrugDate() {
		return senddrugDate;
	}
	public void setSenddrugDate(Date senddrugDate) {
		this.senddrugDate = senddrugDate;
	}
	public String getCheckOpercode() {
		return checkOpercode;
	}
	public void setCheckOpercode(String checkOpercode) {
		this.checkOpercode = checkOpercode;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getMoOrder() {
		return moOrder;
	}
	public void setMoOrder(String moOrder) {
		this.moOrder = moOrder;
	}
	public String getMoExecSqn() {
		return moExecSqn;
	}
	public void setMoExecSqn(String moExecSqn) {
		this.moExecSqn = moExecSqn;
	}
	public Double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	public Integer getUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}
	public Integer getExtFlag2() {
		return extFlag2;
	}
	public void setExtFlag2(Integer extFlag2) {
		this.extFlag2 = extFlag2;
	}
/*	public String getExtFlag3() {
		return extFlag3;
	}
	public void setExtFlag3(String extFlag3) {
		this.extFlag3 = extFlag3;
	}*/
	public String getOperationno() {
		return operationno;
	}
	public void setOperationno(String operationno) {
		this.operationno = operationno;
	}
/*	public String getTransactionSequenceNumber() {
		return transactionSequenceNumber;
	}
	public void setTransactionSequenceNumber(String transactionSequenceNumber) {
		this.transactionSequenceNumber = transactionSequenceNumber;
	}
	public Date getSiTransactionDatetime() {
		return siTransactionDatetime;
	}
	public void setSiTransactionDatetime(Date siTransactionDatetime) {
		this.siTransactionDatetime = siTransactionDatetime;
	}
	public String getHisRecipeNo() {
		return hisRecipeNo;
	}
	public void setHisRecipeNo(String hisRecipeNo) {
		this.hisRecipeNo = hisRecipeNo;
	}
	public String getSiRecipeNo() {
		return siRecipeNo;
	}
	public void setSiRecipeNo(String siRecipeNo) {
		this.siRecipeNo = siRecipeNo;
	}
	public String getHisCancelRecipeNo() {
		return hisCancelRecipeNo;
	}
	public void setHisCancelRecipeNo(String hisCancelRecipeNo) {
		this.hisCancelRecipeNo = hisCancelRecipeNo;
	}
	public String getSiCancelRecipeNo() {
		return siCancelRecipeNo;
	}
	public void setSiCancelRecipeNo(String siCancelRecipeNo) {
		this.siCancelRecipeNo = siCancelRecipeNo;
	}*/
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getConfirmNum() {
		return confirmNum;
	}
	public void setConfirmNum(Double confirmNum) {
		this.confirmNum = confirmNum;
	}
	public Integer getItemFlag() {
		return itemFlag;
	}
	public void setItemFlag(Integer itemFlag) {
		this.itemFlag = itemFlag;
	}
	public Double getEcoCost() {
		return ecoCost;
	}
	public void setEcoCost(Double ecoCost) {
		this.ecoCost = ecoCost;
	}
	public Integer getGoon() {
		return goon;
	}
	public void setGoon(Integer goon) {
		this.goon = goon;
	}
	public Double getChangeTotcost() {
		return changeTotcost;
	}
	public void setChangeTotcost(Double changeTotcost) {
		this.changeTotcost = changeTotcost;
	}
	public String getFeeoperDeptcode() {
		return feeoperDeptcode;
	}
	public void setFeeoperDeptcode(String feeoperDeptcode) {
		this.feeoperDeptcode = feeoperDeptcode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(Integer balanceNo) {
		this.balanceNo = balanceNo;
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
	
}
