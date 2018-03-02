package cn.honry.statistics.finance.inpatientFeeDetail.vo;

import java.util.List;

public class FeeDetailVo {
	/**
	 * 统计费用代码
	 */
	private String feeCode;
	/**
	 * 统计费用名称
	 */
	private String feeName;
	/**
	 * 金额/报表中表示总金额
	 */
	private double totcost;
	/**
	 * 报表用到的数据
	 */
	private List<FeeDetailVo> feeDetailvo;
	
	public List<FeeDetailVo> getFeeDetailvo() {
		return feeDetailvo;
	}
	public void setFeeDetailvo(List<FeeDetailVo> feeDetailvo) {
		this.feeDetailvo = feeDetailvo;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public double getTotcost() {
		return totcost;
	}
	public void setTotcost(double totcost) {
		this.totcost = totcost;
	}
	@Override
	public String toString() {
		return "FeeDetailVo [feeCode=" + feeCode + ", feeName=" + feeName
				+ ", totcost=" + totcost + ", feeDetailvo=" + feeDetailvo + "]";
	}
	
	
}
