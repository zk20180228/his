package cn.honry.finance.registerDay.vo;


public class InfoVo {
	/**发票号**/
	private String invoice;
	/**实收金额**/
	private Double realCost;
	/**记帐金额**/
	private Double pubCost;
	/**交易类型,1正，2反 **/
	private Integer transType;
	/**日结项目：'0'-正常/'1'-作废/'2'-重打/'3'-取消**/
	private Integer cancel;
	/**自费记帐特殊：1 自费 2 记帐 3 特殊**/
	private Integer extFlag;
	/**合计**/
	private Double totalNum;
	
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Double getRealCost() {
		return realCost;
	}
	public void setRealCost(Double realCost) {
		this.realCost = realCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Integer getCancel() {
		return cancel;
	}
	public void setCancel(Integer cancel) {
		this.cancel = cancel;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public Double getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Double totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	
}
