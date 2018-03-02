package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TMatCheckhead entity. @author MyEclipse Persistence Tools
 */

public class MatCheckhead extends Entity implements java.io.Serializable {

	// Fields

	private String checkCode;//盘点流水号
	private String storageCode;//仓库编码
	private Integer checkState;//盘点状态(0-封帐,1-结存,2-取消)
	private String checkName;//盘点单名称
	private String foperCode;//封帐人
	private Date foperTime;//封帐时间
	private String coperCode;//结存人
	private Date coperTime;//结存时间
	private Double inpriceLoss;//盘亏金额(入库价)
	private Double inpriceProfit;//盘盈金额(入库价)
	private Double salepriceLoss;//盘亏金额(零售价)
	private Double salepriceProfit;//盘盈金额(零售价)
	private String operCode;//操作员
	private Date operDate;//操作日期

	
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public Integer getCheckState() {
		return checkState;
	}
	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getFoperCode() {
		return foperCode;
	}
	public void setFoperCode(String foperCode) {
		this.foperCode = foperCode;
	}
	public Date getFoperTime() {
		return foperTime;
	}
	public void setFoperTime(Date foperTime) {
		this.foperTime = foperTime;
	}
	public String getCoperCode() {
		return coperCode;
	}
	public void setCoperCode(String coperCode) {
		this.coperCode = coperCode;
	}
	public Date getCoperTime() {
		return coperTime;
	}
	public void setCoperTime(Date coperTime) {
		this.coperTime = coperTime;
	}
	public Double getInpriceLoss() {
		return inpriceLoss;
	}
	public void setInpriceLoss(Double inpriceLoss) {
		this.inpriceLoss = inpriceLoss;
	}
	public Double getInpriceProfit() {
		return inpriceProfit;
	}
	public void setInpriceProfit(Double inpriceProfit) {
		this.inpriceProfit = inpriceProfit;
	}
	public Double getSalepriceLoss() {
		return salepriceLoss;
	}
	public void setSalepriceLoss(Double salepriceLoss) {
		this.salepriceLoss = salepriceLoss;
	}
	public Double getSalepriceProfit() {
		return salepriceProfit;
	}
	public void setSalepriceProfit(Double salepriceProfit) {
		this.salepriceProfit = salepriceProfit;
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



}