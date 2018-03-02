package cn.honry.statistics.bi.outpatientcost.vo;


public class OutpatientcostVo {
	
	private Integer trips;//人次
	private Double  regFee;//挂号费
	private Double regRatio;//挂号比
	private Double undrugFee;//医疗费用
	private Double undrugRatio;//医疗比例
	private Double drugFee;//药品费用
	private Double drugRatio;//药品比例
	private Double totcost;//总费用
	private Double avg;//平均值
	private String deptDimensionality;//科室
	private String dat;//时间
	private Double cost;//年度费用
	private Double costold;//上一年度费用
	private String name;//（维度）
	private String timeChose; //时间
	
	private Double tothb;//环比费用
	private Double tottb;//同比费用
	private Double tripshb;//环比人次
	private Double tripstb;//同比人次
	private Double costgc;//构成
	private Double travgcost;//人均费用
	private Double travgcosttb;//人均费用同比
	private Double travgcosthb;//人均费用环比
	private String feecode;//费用
	private String regcode;//挂号级别
	
	
	public String getFeecode() {
		return feecode;
	}
	public void setFeecode(String feecode) {
		this.feecode = feecode;
	}
	public String getRegcode() {
		return regcode;
	}
	public void setRegcode(String regcode) {
		this.regcode = regcode;
	}
	public Double getTravgcost() {
		return travgcost;
	}
	public void setTravgcost(Double travgcost) {
		this.travgcost = travgcost;
	}
	public Double getTravgcosttb() {
		return travgcosttb;
	}
	public void setTravgcosttb(Double travgcosttb) {
		this.travgcosttb = travgcosttb;
	}
	public Double getTravgcosthb() {
		return travgcosthb;
	}
	public void setTravgcosthb(Double travgcosthb) {
		this.travgcosthb = travgcosthb;
	}
	public Double getCostgc() {
		return costgc;
	}
	public void setCostgc(Double costgc) {
		this.costgc = costgc;
	}
	public Double getTothb() {
		return tothb;
	}
	public void setTothb(Double tothb) {
		this.tothb = tothb;
	}
	public Double getTottb() {
		return tottb;
	}
	public void setTottb(Double tottb) {
		this.tottb = tottb;
	}
	public Double getTripshb() {
		return tripshb;
	}
	public void setTripshb(Double tripshb) {
		this.tripshb = tripshb;
	}
	public Double getTripstb() {
		return tripstb;
	}
	public void setTripstb(Double tripstb) {
		this.tripstb = tripstb;
	}
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getTrips() {
		return trips;
	}
	public void setTrips(Integer trips) {
		this.trips = trips;
	}
	public Double getRegFee() {
		return regFee;
	}
	public void setRegFee(Double regFee) {
		this.regFee = regFee;
	}
	public Double getRegRatio() {
		return regRatio;
	}
	public void setRegRatio(Double regRatio) {
		this.regRatio = regRatio;
	}
	public Double getUndrugFee() {
		return undrugFee;
	}
	public void setUndrugFee(Double undrugFee) {
		this.undrugFee = undrugFee;
	}
	public Double getUndrugRatio() {
		return undrugRatio;
	}
	public void setUndrugRatio(Double undrugRatio) {
		this.undrugRatio = undrugRatio;
	}
	public Double getDrugFee() {
		return drugFee;
	}
	public void setDrugFee(Double drugFee) {
		this.drugFee = drugFee;
	}
	public Double getDrugRatio() {
		return drugRatio;
	}
	public void setDrugRatio(Double drugRatio) {
		this.drugRatio = drugRatio;
	}
	public Double getTotcost() {
		return totcost;
	}
	public void setTotcost(Double totcost) {
		this.totcost = totcost;
	}
	public Double getAvg() {
		return avg;
	}
	public void setAvg(Double avg) {
		this.avg = avg;
	}
	public String getDeptDimensionality() {
		return deptDimensionality;
	}
	public void setDeptDimensionality(String deptDimensionality) {
		this.deptDimensionality = deptDimensionality;
	}
	public String getDat() {
		return dat;
	}
	public void setDat(String dat) {
		this.dat = dat;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getCostold() {
		return costold;
	}
	public void setCostold(Double costold) {
		this.costold = costold;
	}
	
}
