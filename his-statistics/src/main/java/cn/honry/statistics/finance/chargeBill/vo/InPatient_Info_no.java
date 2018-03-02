package cn.honry.statistics.finance.chargeBill.vo;

import java.util.List;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;

public class InPatient_Info_no {
	//药品/非药品
	private String Flag;
	//项目名称
	private String itemName;
	//当前单位
	private String currentUnit;
	//数量
	private String qty;
	//单价
	private String unitPrice;
	//执行科室
	private String executeDeptname;
	//收费人
	private String chargeOpercode;
	//收费日期
	private String chargeDate;
	//费用金额
	private String toCost;
	//自费金额
	private String ownCost;
	//自付金额
	private String payCost;
	//公费金额
	private String pubCost;
	//优惠金额
	private String ecoCost;
	//动态链接数据 住院患者消费清单
	private List<ChargeBillVo> chargeBillVo;
	
	
	
	@Override
	public String toString() {
		return "InPatient_Info_no [Flag=" + Flag + ", itemName=" + itemName
				+ ", currentUnit=" + currentUnit + ", qty=" + qty
				+ ", executeDeptname=" + executeDeptname + ", chargeOpercode="
				+ chargeOpercode + ", chargeDate=" + chargeDate + ", toCost="
				+ toCost + ", ownCost=" + ownCost + ", payCost=" + payCost
				+ ", pubCost=" + pubCost + ", ecoCost=" + ecoCost + "]";
	}



	public String getFlag() {
		return Flag;
	}



	public void setFlag(String flag) {
		Flag = flag;
	}



	public String getItemName() {
		return itemName;
	}



	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUnitPrice() {
		return unitPrice;
	}



	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}



	public String getCurrentUnit() {
		return currentUnit;
	}



	public void setCurrentUnit(String currentUnit) {
		this.currentUnit = currentUnit;
	}



	public String getQty() {
		return qty;
	}



	public void setQty(String qty) {
		this.qty = qty;
	}



	public String getExecuteDeptname() {
		return executeDeptname;
	}



	public void setExecuteDeptname(String executeDeptname) {
		this.executeDeptname = executeDeptname;
	}



	public String getChargeOpercode() {
		return chargeOpercode;
	}



	public void setChargeOpercode(String chargeOpercode) {
		this.chargeOpercode = chargeOpercode;
	}



	public String getChargeDate() {
		return chargeDate;
	}



	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}



	public String getToCost() {
		return toCost;
	}



	public void setToCost(String toCost) {
		this.toCost = toCost;
	}



	public String getOwnCost() {
		return ownCost;
	}



	public void setOwnCost(String ownCost) {
		this.ownCost = ownCost;
	}



	public String getPayCost() {
		return payCost;
	}



	public void setPayCost(String payCost) {
		this.payCost = payCost;
	}



	public String getPubCost() {
		return pubCost;
	}



	public void setPubCost(String pubCost) {
		this.pubCost = pubCost;
	}



	public String getEcoCost() {
		return ecoCost;
	}



	public void setEcoCost(String ecoCost) {
		this.ecoCost = ecoCost;
	}



	public List<ChargeBillVo> getChargeBillVo() {
		return chargeBillVo;
	}



	public void setChargeBillVo(List<ChargeBillVo> chargeBillVo) {
		this.chargeBillVo = chargeBillVo;
	}
	
}
