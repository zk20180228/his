package cn.honry.finance.registerDay.vo;

public class DayBalanceVO {
	private String payKindCode;//统计编码
	private String payKindName;//统计名称
	private Double totalCost;//总金额
	private Double sybOwnCost;//省医保个人负担--03
	private Double sybPubCost;//省医保医保支付--03
	private Double smxbOwnCost;//省慢性病个人负担
	private Double smxbPayCost;//省慢性病个人自付
	private Double smxbPubCost;//省慢性病医保支付
	private Double slxOwnCost;//省离休个人负担--02
	private Double slxPayCost;//省离休医保支付--02
	private Double shiybOwnCost;//市医保个人负担--05
	private Double shiybPubCost;//市医保医保支付--05
	private Double ownCost;//自费-- 01/15
	private Double ssyOwnCost;//省生育个人负担--07
	private Double ssyPayCost;//省生育个人自付--07
	private Double ssyPuvCost;//省生育医保支付--07
	private Double nhOwnCost;//农合个人负担--19
	private Double nhPayCost;//农合个人自付--19
	private Double nhPubCost;//农合医保支付--19
	private Double nyhgOwnCost;//能源化工个人负担--20
	private Double nyhgPayCost;//能源化工个人自付--20
	private Double nyhgPubCost;//能源化工医保支付--20
	private Double sbjjbxOwnCost;//省保健局保险个人负担 --10
	private Double sbjjbxPayCost;//省保健局保险个人自付--10
	private Double sbjjbxPubCost;//省保健局保险优惠--10
	private Double sjxnhptOwnCost;//省级新农合平台个人负担 --13
	private Double sjxnhptPayCost;//省级新农合平台个人自付 --13
	private Double sjxnhptPubCost;//省级新农合平台 医保支付 --13
	private Double ydjysdOwnCost;//异地就医(试点)个人负担 --90
	private Double ydjysdPayCost;//异地就医(试点)个人自付 --90
	private Double ydjysdPubCost;//异地就医(试点)医保支付 --90
	private Double stlylbxOwnCost;//郑州铁路医疗保险个人负担 --08
	private Double stlylbxPayCost;//郑州铁路医疗保险个人自付--08
	private Double stlylbxPubCost;//郑州铁路医疗保险医保支付--08
	
	public String getPayKindCode() {
		return payKindCode;
	}
	public void setPayKindCode(String payKindCode) {
		this.payKindCode = payKindCode;
	}
	public String getPayKindName() {
		return payKindName;
	}
	public void setPayKindName(String payKindName) {
		this.payKindName = payKindName;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getSybOwnCost() {
		return sybOwnCost;
	}
	public void setSybOwnCost(Double sybOwnCost) {
		this.sybOwnCost = sybOwnCost;
	}
	public Double getSybPubCost() {
		return sybPubCost;
	}
	public void setSybPubCost(Double sybPubCost) {
		this.sybPubCost = sybPubCost;
	}
	public Double getSmxbOwnCost() {
		return smxbOwnCost;
	}
	public void setSmxbOwnCost(Double smxbOwnCost) {
		this.smxbOwnCost = smxbOwnCost;
	}
	public Double getSmxbPayCost() {
		return smxbPayCost;
	}
	public void setSmxbPayCost(Double smxbPayCost) {
		this.smxbPayCost = smxbPayCost;
	}
	public Double getSmxbPubCost() {
		return smxbPubCost;
	}
	public void setSmxbPubCost(Double smxbPubCost) {
		this.smxbPubCost = smxbPubCost;
	}
	public Double getSlxOwnCost() {
		return slxOwnCost;
	}
	public void setSlxOwnCost(Double slxOwnCost) {
		this.slxOwnCost = slxOwnCost;
	}
	public Double getSlxPayCost() {
		return slxPayCost;
	}
	public void setSlxPayCost(Double slxPayCost) {
		this.slxPayCost = slxPayCost;
	}
	public Double getShiybOwnCost() {
		return shiybOwnCost;
	}
	public void setShiybOwnCost(Double shiybOwnCost) {
		this.shiybOwnCost = shiybOwnCost;
	}
	public Double getShiybPubCost() {
		return shiybPubCost;
	}
	public void setShiybPubCost(Double shiybPubCost) {
		this.shiybPubCost = shiybPubCost;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getSsyOwnCost() {
		return ssyOwnCost;
	}
	public void setSsyOwnCost(Double ssyOwnCost) {
		this.ssyOwnCost = ssyOwnCost;
	}
	public Double getSsyPayCost() {
		return ssyPayCost;
	}
	public void setSsyPayCost(Double ssyPayCost) {
		this.ssyPayCost = ssyPayCost;
	}
	public Double getSsyPuvCost() {
		return ssyPuvCost;
	}
	public void setSsyPuvCost(Double ssyPuvCost) {
		this.ssyPuvCost = ssyPuvCost;
	}
	public Double getNhOwnCost() {
		return nhOwnCost;
	}
	public void setNhOwnCost(Double nhOwnCost) {
		this.nhOwnCost = nhOwnCost;
	}
	public Double getNhPayCost() {
		return nhPayCost;
	}
	public void setNhPayCost(Double nhPayCost) {
		this.nhPayCost = nhPayCost;
	}
	public Double getNhPubCost() {
		return nhPubCost;
	}
	public void setNhPubCost(Double nhPubCost) {
		this.nhPubCost = nhPubCost;
	}
	public Double getNyhgOwnCost() {
		return nyhgOwnCost;
	}
	public void setNyhgOwnCost(Double nyhgOwnCost) {
		this.nyhgOwnCost = nyhgOwnCost;
	}
	public Double getNyhgPayCost() {
		return nyhgPayCost;
	}
	public void setNyhgPayCost(Double nyhgPayCost) {
		this.nyhgPayCost = nyhgPayCost;
	}
	public Double getNyhgPubCost() {
		return nyhgPubCost;
	}
	public void setNyhgPubCost(Double nyhgPubCost) {
		this.nyhgPubCost = nyhgPubCost;
	}
	public Double getSbjjbxOwnCost() {
		return sbjjbxOwnCost;
	}
	public void setSbjjbxOwnCost(Double sbjjbxOwnCost) {
		this.sbjjbxOwnCost = sbjjbxOwnCost;
	}
	public Double getSbjjbxPayCost() {
		return sbjjbxPayCost;
	}
	public void setSbjjbxPayCost(Double sbjjbxPayCost) {
		this.sbjjbxPayCost = sbjjbxPayCost;
	}
	public Double getSbjjbxPubCost() {
		return sbjjbxPubCost;
	}
	public void setSbjjbxPubCost(Double sbjjbxPubCost) {
		this.sbjjbxPubCost = sbjjbxPubCost;
	}
	public Double getSjxnhptOwnCost() {
		return sjxnhptOwnCost;
	}
	public void setSjxnhptOwnCost(Double sjxnhptOwnCost) {
		this.sjxnhptOwnCost = sjxnhptOwnCost;
	}
	public Double getSjxnhptPayCost() {
		return sjxnhptPayCost;
	}
	public void setSjxnhptPayCost(Double sjxnhptPayCost) {
		this.sjxnhptPayCost = sjxnhptPayCost;
	}
	public Double getSjxnhptPubCost() {
		return sjxnhptPubCost;
	}
	public void setSjxnhptPubCost(Double sjxnhptPubCost) {
		this.sjxnhptPubCost = sjxnhptPubCost;
	}
	public Double getYdjysdOwnCost() {
		return ydjysdOwnCost;
	}
	public void setYdjysdOwnCost(Double ydjysdOwnCost) {
		this.ydjysdOwnCost = ydjysdOwnCost;
	}
	public Double getYdjysdPayCost() {
		return ydjysdPayCost;
	}
	public void setYdjysdPayCost(Double ydjysdPayCost) {
		this.ydjysdPayCost = ydjysdPayCost;
	}
	public Double getYdjysdPubCost() {
		return ydjysdPubCost;
	}
	public void setYdjysdPubCost(Double ydjysdPubCost) {
		this.ydjysdPubCost = ydjysdPubCost;
	}
	public Double getStlylbxOwnCost() {
		return stlylbxOwnCost;
	}
	public void setStlylbxOwnCost(Double stlylbxOwnCost) {
		this.stlylbxOwnCost = stlylbxOwnCost;
	}
	public Double getStlylbxPayCost() {
		return stlylbxPayCost;
	}
	public void setStlylbxPayCost(Double stlylbxPayCost) {
		this.stlylbxPayCost = stlylbxPayCost;
	}
	public Double getStlylbxPubCost() {
		return stlylbxPubCost;
	}
	public void setStlylbxPubCost(Double stlylbxPubCost) {
		this.stlylbxPubCost = stlylbxPubCost;
	}
}
