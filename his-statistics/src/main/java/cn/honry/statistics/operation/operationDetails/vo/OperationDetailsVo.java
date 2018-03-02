package cn.honry.statistics.operation.operationDetails.vo;

import java.util.Date;
/***
 * 手术耗材统计明细报表VO 子类
 * @Description:
 * @author:  hedong
 * @CreateDate: 2017年02月28日 
 * @version 1.0
 */
public class OperationDetailsVo {
	private String undrugGbcode;
	private String itemName;
	private Double unitPrice;
	private Double qty;
	private Double totCost;
	private String name;
	private String recipeDeptcode;
	private String recipeDoccode;
	private String feeOpercode;
	private Date feeDate;
	private String itemCode;
	private String currentUnit;
	private String inpatientNo;
	private String spec;
	public String getUndrugGbcode() {
		return undrugGbcode;
	}
	public void setUndrugGbcode(String undrugGbcode) {
		this.undrugGbcode = undrugGbcode;
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
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRecipeDeptcode() {
		return recipeDeptcode;
	}
	public void setRecipeDeptcode(String recipeDeptcode) {
		this.recipeDeptcode = recipeDeptcode;
	}
	public String getRecipeDoccode() {
		return recipeDoccode;
	}
	public void setRecipeDoccode(String recipeDoccode) {
		this.recipeDoccode = recipeDoccode;
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
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getCurrentUnit() {
		return currentUnit;
	}
	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	};
	
	
}

