package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class RegisterBalancedetail extends Entity implements java.io.Serializable {
	
	
	/**日结编号（挂号日结档id）**/
	private RegisterDaybalance balance;
	/**支付类型**/
	private String payType;
	/**挂号数量**/
	private Integer regNum;
	/**退号数量**/
	private Integer quitNum;
	/**挂号数量-退号数量**/
	private Integer sumNum;
	/**挂号费用总额**/
	private Double regFee;
	/**挂号退费总额**/
	private Double quitFee;
	/**挂号费用总额-挂号退费总额**/
	private Double sumFee;
	/**个人承担的，自费的额度**/
	private Double ownFee;
	
	
	public RegisterDaybalance getBalance() {
		return balance;
	}
	public void setBalance(RegisterDaybalance balance) {
		this.balance = balance;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Integer getRegNum() {
		return regNum;
	}
	public void setRegNum(Integer regNum) {
		this.regNum = regNum;
	}
	public Integer getQuitNum() {
		return quitNum;
	}
	public void setQuitNum(Integer quitNum) {
		this.quitNum = quitNum;
	}
	public Integer getSumNum() {
		return sumNum;
	}
	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}
	public Double getRegFee() {
		return regFee;
	}
	public void setRegFee(Double regFee) {
		this.regFee = regFee;
	}
	public Double getQuitFee() {
		return quitFee;
	}
	public void setQuitFee(Double quitFee) {
		this.quitFee = quitFee;
	}
	public Double getSumFee() {
		return sumFee;
	}
	public void setSumFee(Double sumFee) {
		this.sumFee = sumFee;
	}
	public Double getOwnFee() {
		return ownFee;
	}
	public void setOwnFee(Double ownFee) {
		this.ownFee = ownFee;
	}

}