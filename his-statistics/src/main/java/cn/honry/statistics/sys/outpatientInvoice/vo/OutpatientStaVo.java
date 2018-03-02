package cn.honry.statistics.sys.outpatientInvoice.vo;

public class OutpatientStaVo {
	/**统计大类**/
	private String feeName;
	/**项目名称**/
	private String itemName;
	/**单价**/
	private Double unitPrice;
	/**数量**/
	private Double qty;
	/**金额**/
	private Double money;
	/**药品状态**/
	private Integer drugFlag;
	/**统计大类代码**/
	private String feeCode;
	/**收费人**/
	private String users;
	
	
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public Integer getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(Integer drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
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
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	
	
	
}
