package cn.honry.statistics.finance.inpatientFee.vo;

import java.util.List;

public class FeeInfosVo {
	/**
	 * 费用名称
	 */
	public String feeCode;
	/**
	 * 金额
	 */
	public double tot;
	/**
	 * 自费
	 */
	public double own;
	/**
	 * 公费
	 */
	public double pub;
	/**
	 * 自负
	 */
	public double pay;
	/**
	 * 优惠金额
	 */
	public double eco;
	/**报表**/
	private List<FeeInfosVo> feeInfosVo;
	
	public List<FeeInfosVo> getFeeInfosVo() {
		return feeInfosVo;
	}
	public void setFeeInfosVo(List<FeeInfosVo> feeInfosVo) {
		this.feeInfosVo = feeInfosVo;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public double getTot() {
		return tot;
	}
	public void setTot(double tot) {
		this.tot = tot;
	}
	public double getOwn() {
		return own;
	}
	public void setOwn(double own) {
		this.own = own;
	}
	public double getPub() {
		return pub;
	}
	public void setPub(double pub) {
		this.pub = pub;
	}
	public double getPay() {
		return pay;
	}
	public void setPay(double pay) {
		this.pay = pay;
	}
	public double getEco() {
		return eco;
	}
	public void setEco(double eco) {
		this.eco = eco;
	}
			
}
