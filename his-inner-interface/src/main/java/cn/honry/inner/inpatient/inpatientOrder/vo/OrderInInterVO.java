package cn.honry.inner.inpatient.inpatientOrder.vo;

import java.util.Date;

public class OrderInInterVO {
	/**id*/
	private String id;
	/**住院流水号*/
	private String inpatientNo;
	/**项目名称id*/
	private String itemId;
	/**项目名称*/
	private String itemName;
	/**价格*/
	private Double itemPrice;
	/**数量*/
	private Double packQty;
	/**单位*/
	private String priceUnit;
	/**频次id*/
	private String frequencyCode;
	/**频次*/
	private String frequencyName;
	/**开始时间*/
	private Date dateBgn;
	/**结束时间*/
	private Date dateEnd;
	/**备注*/
	private String moNote2;
	/**执行科室id*/
	private String execDpcd;
	/**执行科室*/
	private String execDpnm;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Double getPackQty() {
		return packQty;
	}
	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public Date getDateBgn() {
		return dateBgn;
	}
	public void setDateBgn(Date dateBgn) {
		this.dateBgn = dateBgn;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getMoNote2() {
		return moNote2;
	}
	public void setMoNote2(String moNote2) {
		this.moNote2 = moNote2;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public String getExecDpnm() {
		return execDpnm;
	}
	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
	}
	
}
