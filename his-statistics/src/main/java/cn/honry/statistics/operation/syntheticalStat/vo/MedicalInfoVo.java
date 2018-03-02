package cn.honry.statistics.operation.syntheticalStat.vo;



public class MedicalInfoVo{
	
	/**系统类别**/
	private String sysType;
	/**项目名称**/
	private String itemName;
	/**频次**/
	private String freName;
	/**用法**/
	private String usage;
	/**数量**/
	private Integer qty;
	/**单位**/
	private String unit;
	/**开立时间**/
	private String openDate;
	/**作废时间**/
	private String cancelDate;
	
	/**getters and setters**/
	
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getFreName() {
		return freName;
	}
	public void setFreName(String freName) {
		this.freName = freName;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	
}
