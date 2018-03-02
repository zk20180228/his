package cn.honry.statistics.bi.operation.operationCostList.vo;

public class OperationCostListvo {
	
	private Double personAvg;//人均费用
	private Double dateAvg;//日均费用
	private Double feeCost;//总费用
	private Integer personNum;//全院手术人次
	private Integer operationnNum;//总手术次
	private String kind;//手术分类
	/**时间维度 **/
	private String timeChose;
	private Double drugzyb;//药占比
	private Double feetb;//同比
	private Double feehb;//环比
	
	public Double getDateAvg() {
		return dateAvg;
	}
	public void setDateAvg(Double dateAvg) {
		this.dateAvg = dateAvg;
	}
	public Double getDrugzyb() {
		return drugzyb;
	}
	public void setDrugzyb(Double drugzyb) {
		this.drugzyb = drugzyb;
	}
	public Double getFeetb() {
		return feetb;
	}
	public void setFeetb(Double feetb) {
		this.feetb = feetb;
	}
	public Double getFeehb() {
		return feehb;
	}
	public void setFeehb(Double feehb) {
		this.feehb = feehb;
	}
	public String getTimeChose() {
		return timeChose;
	}
	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public Double getPersonAvg() {
		return personAvg;
	}
	public void setPersonAvg(Double personAvg) {
		this.personAvg = personAvg;
	}
	public Double getFeeCost() {
		return feeCost;
	}
	public void setFeeCost(Double feeCost) {
		this.feeCost = feeCost;
	}
	public Integer getPersonNum() {
		return personNum;
	}
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
	public Integer getOperationnNum() {
		return operationnNum;
	}
	public void setOperationnNum(Integer operationnNum) {
		this.operationnNum = operationnNum;
	}
	
}
