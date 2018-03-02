package cn.honry.base.bean.model;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * TMatAddrate entity. @author MyEclipse Persistence Tools aizhonghua
 * 物资加价率维护
 */

public class MatAddrate extends Entity {
	
	/**序号**/
	private String seqNo;
	/**加价规则,用于入库自动加价**/
	private String addRate;
	/**物品科目编码**/
	private String kindCode;
	/**规格**/
	private String specs;
	/**起始价格**/
	private Double lowPrice;
	/**终止价格**/
	private Double highPrice;
	/**加价率**/
	private Double rate;
	/**附加费**/
	private Double addFee;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getAddRate() {
		return addRate;
	}
	public void setAddRate(String addRate) {
		this.addRate = addRate;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}
	public Double getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getAddFee() {
		return addFee;
	}
	public void setAddFee(Double addFee) {
		this.addFee = addFee;
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