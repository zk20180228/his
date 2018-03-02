package cn.honry.statistics.finance.inpatientUDbalance.vo;


public class InpatientUDBVo {
	/**收款员**/
	private String name; 
	/**收入现金**/
	private Double icash;
	/**收入支票**/
	private Double icheck;
	/**收入其他**/
	private Double iother;
	/**支出现金**/
	private Double ocash;
	/**支出支票**/
	private Double ocheck;
	/**支出其他**/
	private Double oother;
	/**实收现金**/
	private Double scash;
	/**实收支票**/
	private Double scheck;
	/**实收其他**/
	private Double sother;
	/**日结时间**/
	private String time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getIcash() {
		return icash;
	}
	public void setIcash(Double icash) {
		this.icash = icash;
	}
	public Double getIcheck() {
		return icheck;
	}
	public void setIcheck(Double icheck) {
		this.icheck = icheck;
	}
	public Double getIother() {
		return iother;
	}
	public void setIother(Double iother) {
		this.iother = iother;
	}
	public Double getOcash() {
		return ocash;
	}
	public void setOcash(Double ocash) {
		this.ocash = ocash;
	}
	public Double getOcheck() {
		return ocheck;
	}
	public void setOcheck(Double ocheck) {
		this.ocheck = ocheck;
	}
	public Double getOother() {
		return oother;
	}
	public void setOother(Double oother) {
		this.oother = oother;
	}
	public Double getScash() {
		return scash;
	}
	public void setScash(Double scash) {
		this.scash = scash;
	}
	public Double getScheck() {
		return scheck;
	}
	public void setScheck(Double scheck) {
		this.scheck = scheck;
	}
	public Double getSother() {
		return sother;
	}
	public void setSother(Double sother) {
		this.sother = sother;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
