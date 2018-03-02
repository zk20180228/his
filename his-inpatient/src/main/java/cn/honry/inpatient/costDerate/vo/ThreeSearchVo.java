package cn.honry.inpatient.costDerate.vo;
/**
 * 费用汇总表、药品明细表、非药品明细表 部分字段Vo类
 * @author Administrator
 *
 */
public class ThreeSearchVo {
	  private String feeCode;// 最小费用代码 
	  private Double  totCost ;// 费用金额                 
	  private Double  ownCost ;//  自费金额                 
	  private Double  payCost ;// 自付金额                
	  private Double  pubCost ;//公费金额       
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
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
}
