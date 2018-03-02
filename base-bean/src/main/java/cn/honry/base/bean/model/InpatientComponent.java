package cn.honry.base.bean.model;


import cn.honry.base.bean.business.Entity;

/**
 * 
 * 
 * <p>血液成分 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月3日 下午6:57:23 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月3日 下午6:57:23 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class InpatientComponent  extends Entity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//血液成分代码
	private String bloodTypeCode;
	//血液成分名称
	private String bloodTypeName;
	//拼音码
	private String spellCode;
	//五笔码
	private String wbCode;
	//扩展码
	private String customeCode;
	//是否需要配血
	private String matchFlag;
	//有效期
	private Integer useLife;
	//保存温度
	private String temperature;
	//最小单位
	private String stockUnit;
	//最小计费数量
	private Double minAmount;
	//购入价
	private Double tradePrice;
	//零售价
	private Double salePrice;
	//申请单有效天数
	private Integer validDays;
	//项目有效性
	private String validFlag;
	//血液成分序号
	private Integer sortId;
	//备注
	private String memo;
	//类别码
	private String typeCode;
	//需审批申请量-A级
	private Double approvalAmount;
	//需审批申请量-B级
	private Double approvalAmountB;
	//需审批申请量-C级
	private Double approvalAmountC;
	//新增字段 2017-06-12
   /**医院编码**/
   private Integer hospitalId;
   /**院区编码吗**/
   private String areaCode;
	public String getBloodTypeCode() {
		return bloodTypeCode;
	}
	public void setBloodTypeCode(String bloodTypeCode) {
		this.bloodTypeCode = bloodTypeCode;
	}
	public String getBloodTypeName() {
		return bloodTypeName;
	}
	public void setBloodTypeName(String bloodTypeName) {
		this.bloodTypeName = bloodTypeName;
	}
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public String getWbCode() {
		return wbCode;
	}
	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}
	public String getCustomeCode() {
		return customeCode;
	}
	public void setCustomeCode(String customeCode) {
		this.customeCode = customeCode;
	}
	public String getMatchFlag() {
		return matchFlag;
	}
	public void setMatchFlag(String matchFlag) {
		this.matchFlag = matchFlag;
	}
	public Integer getUseLife() {
		return useLife;
	}
	public void setUseLife(Integer useLife) {
		this.useLife = useLife;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getStockUnit() {
		return stockUnit;
	}
	public void setStockUnit(String stockUnit) {
		this.stockUnit = stockUnit;
	}
	public Double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}
	public Double getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Integer getValidDays() {
		return validDays;
	}
	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Double getApprovalAmount() {
		return approvalAmount;
	}
	public void setApprovalAmount(Double approvalAmount) {
		this.approvalAmount = approvalAmount;
	}
	public Double getApprovalAmountB() {
		return approvalAmountB;
	}
	public void setApprovalAmountB(Double approvalAmountB) {
		this.approvalAmountB = approvalAmountB;
	}
	public Double getApprovalAmountC() {
		return approvalAmountC;
	}
	public void setApprovalAmountC(Double approvalAmountC) {
		this.approvalAmountC = approvalAmountC;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	
	
}
