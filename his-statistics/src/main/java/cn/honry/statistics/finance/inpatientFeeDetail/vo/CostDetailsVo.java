package cn.honry.statistics.finance.inpatientFeeDetail.vo;

import java.util.Date;
import java.util.List;
/**
 * 统计-费用明细Vo
* @ClassName: CostDetailsVo
* @Description: 
* @author yeguanqun
* @date 2016年6月13日 上午11:42:27
*
 */
public class CostDetailsVo {
	private String inpatientNo;//住院流水号
	private String recipeNo;// 处方号      
	private String itemCode ;//项目编号
	private String itemName;//项目名称  
	private Double unitPrice ;// 单价            
	private Double qty ;//数量
	private String currentUnit;//当前单位 
	private Double  totcost ;// 费用金额     
	private String feeOpercode  ;// 计费人              
	private Date feeDate  ;//  计费日期  
	private Integer itemType;//药品非药品标识
	private List<CostDetailsVo> costDetailsVo;//生成Javabean报表
	
	public List<CostDetailsVo> getCostDetailsVo() {
		return costDetailsVo;
	}
	public void setCostDetailsVo(List<CostDetailsVo> costDetailsVo) {
		this.costDetailsVo = costDetailsVo;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
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
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getCurrentUnit() {
		return currentUnit;
	}
	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}
	public Double getTotcost() {
		return totcost;
	}
	public void setTotcost(Double totcost) {
		this.totcost = totcost;
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
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}	
}
