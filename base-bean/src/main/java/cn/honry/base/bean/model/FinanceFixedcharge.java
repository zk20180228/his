package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;


public class FinanceFixedcharge extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	/** 项目编号   **/
	private String drugUndrug;
	/** 床位等级   **/
	private String chargeBedlevel;
	/** 数量   **/
	private Double chargeAmount;
	/** 单价   **/
	private Double chargeUnitprice;
	/** 开始时间   **/
	private Date chargeStarttime;
	/** 结束时间   **/
	private Date chargeEndtime;
	/** 是否和婴儿相关   1相关 其他不相关**/
	private Integer chargeIsaboutchildren;
	/** 是否和时间相关   1相关 其他不相关**/
	private Integer chargeIsabouttime;
	/**  状态  **/
	private Integer chargeState;
	/** 排序   **/
	private Integer chargeOrder;
	/** 当前页数   **/
	private String page;
	/** 每页行数   **/
	private String rows;
	/** 存放开始事件（用于前台显示） **/
	private String sDate;
	/** 存放结束事件（用于前台显示） **/
	private String eDate;
	// Property accessors
	
	
	

	
	public Integer getChargeIsaboutchildren() {
		return chargeIsaboutchildren;
	}

	public void setChargeIsaboutchildren(Integer chargeIsaboutchildren) {
		this.chargeIsaboutchildren = chargeIsaboutchildren;
	}

	public Integer getChargeIsabouttime() {
		return chargeIsabouttime;
	}

	public void setChargeIsabouttime(Integer chargeIsabouttime) {
		this.chargeIsabouttime = chargeIsabouttime;
	}

	public Integer getChargeState() {
		return chargeState;
	}

	public void setChargeState(Integer chargeState) {
		this.chargeState = chargeState;
	}

	

	public String getDrugUndrug() {
		return drugUndrug;
	}

	public void setDrugUndrug(String drugUndrug) {
		this.drugUndrug = drugUndrug;
	}

	public String getChargeBedlevel() {
		return this.chargeBedlevel;
	}

	public void setChargeBedlevel(String chargeBedlevel) {
		this.chargeBedlevel = chargeBedlevel;
	}

	

	public Double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public Double getChargeUnitprice() {
		return this.chargeUnitprice;
	}

	public void setChargeUnitprice(Double chargeUnitprice) {
		this.chargeUnitprice = chargeUnitprice;
	}

	public Date getChargeStarttime() {
		return this.chargeStarttime;
	}

	public void setChargeStarttime(Date chargeStarttime) {
		this.chargeStarttime = chargeStarttime;
	}

	public Date getChargeEndtime() {
		return this.chargeEndtime;
	}

	public void setChargeEndtime(Date chargeEndtime) {
		this.chargeEndtime = chargeEndtime;
	}

	
	

	public Integer getChargeOrder() {
		return chargeOrder;
	}

	public void setChargeOrder(Integer chargeOrder) {
		this.chargeOrder = chargeOrder;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}


}