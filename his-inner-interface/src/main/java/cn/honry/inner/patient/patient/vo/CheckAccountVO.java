package cn.honry.inner.patient.patient.vo;
/**
 * 为了验证两个账户是否有余额和欠费情况
 * 及是否已办理出院登记
 * 
 * */
public class CheckAccountVO {
	/**住院状态*/
	String state;
	/**住院患者账户余额*/
	double ibalance;
	/**患者账户余额*/
	double balance;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getIbalance() {
		return ibalance;
	}
	public void setIbalance(double ibalance) {
		this.ibalance = ibalance;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
}
