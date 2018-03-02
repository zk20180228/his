package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 申请退费实体类
 * 
 *
 */
public class InpatientCancelitem extends Entity implements java.io.Serializable {
	  private static final long serialVersionUID = 1L;
       
	  //申请单据号
	   private String billCode;
	   //申请归属标识 1门诊/2住院
	   private Integer applyFlag;
	   //住院流水号
	   private String inpatientNo;
	   //患者姓名
	   private String name;
	   //是否婴儿用药 0 不是 1 是
	   private Integer babyFlag;
	   //患者所在科室
	   private String deptCode;
	   //所在病区
	   private String cellCode;
	   //药品标志,1药品/2非药
	   private Integer drugFlag;
	   //项目编码
	   private String itemCode;
	   //项目名称
	   private String itemName;
	   //规格
	   private String specs; 
	   //零售价
	   private Double salePrice;
	   //申请退药数量（乘以付数后的总数量）
	   private Double quantity;
	   //付数
	   private Integer days;
	   //计价单位
	   private String priceUnit; 
	   //执行科室
	   private String execDpcd;
	   //操作员编码
	   private String operCode;
	   //操作时间
	   private Date operDate;
	   //操作员所在科室
	   private String operDpcd;
	   //对应收费明细处方号
	   private String recipeNo;
	   //对应处方内流水号
	   private Integer sequenceNo;
	   //确认单号
	   private String billNo;
	   //退药确认标识 0未确认/1确认
	   private Integer confirmFlag;
	   //确认科室代码
	   private String confirmDpcd;
	   //确认人编码
	   private String confirmCode;
	   //确认时间
	   private Date confirmDate;
	   //退费标识 0未退费/1退费/2作废
	   private Integer chargeFlag;
	   //退费确认人
	   private String chargeCode;
	   //退费确认时间
	   private Date chargeDate;
	   //1 包装 单位 0, 最小单位
	   private Integer extFlag;
	   //数量[22]
	   private Double qty;
	   //组套代码
	   private String packageCode;
	   //组套名称
	   private String packageName;
	   //病历卡号
	   private String cardNo;
	   //退费原因，需要统计
	   private String returnReason;
	   //新加字段
	   /**患者所在科室名称**/
	   private String deptName;
	   /**所在病区名称**/
	   private String nurseCellName;
	   /**执行科室名称**/
	   private String execDpcdName;
	   /**操作员名称**/
	   private String operName;
	   /**操作员所在科室名称**/
	   private String operDpcdName;
	   /**确认科室名称**/
	   private String confirmDpcdName;
	   /**确认人名称**/
	   private String confirmName;
	   /**退费确认人名称**/
	   private String chargeName;
	   //新增字段 2016-12-22 19:08:03
	   /** 发票科目代码（统计大类代码） */
	   private String invoCode;
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
	public String getInvoCode() {
		return invoCode;
	}
	public void setInvoCode(String invoCode) {
		this.invoCode = invoCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getExecDpcdName() {
		return execDpcdName;
	}
	public void setExecDpcdName(String execDpcdName) {
		this.execDpcdName = execDpcdName;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getOperDpcdName() {
		return operDpcdName;
	}
	public void setOperDpcdName(String operDpcdName) {
		this.operDpcdName = operDpcdName;
	}
	public String getConfirmDpcdName() {
		return confirmDpcdName;
	}
	public void setConfirmDpcdName(String confirmDpcdName) {
		this.confirmDpcdName = confirmDpcdName;
	}
	public String getConfirmName() {
		return confirmName;
	}
	public void setConfirmName(String confirmName) {
		this.confirmName = confirmName;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
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
	public Integer getBabyFlag() {
		return babyFlag;
	}
	public void setBabyFlag(Integer babyFlag) {
		this.babyFlag = babyFlag;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getCellCode() {
		return cellCode;
	}
	public void setCellCode(String cellCode) {
		this.cellCode = cellCode;
	}
	public Integer getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(Integer drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getOperDpcd() {
		return operDpcd;
	}
	public void setOperDpcd(String operDpcd) {
		this.operDpcd = operDpcd;
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
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Integer getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public String getConfirmDpcd() {
		return confirmDpcd;
	}
	public void setConfirmDpcd(String confirmDpcd) {
		this.confirmDpcd = confirmDpcd;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Integer getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
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
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getApplyFlag() {
		return applyFlag;
	}
	public void setApplyFlag(Integer applyFlag) {
		this.applyFlag = applyFlag;
	}
	
}


