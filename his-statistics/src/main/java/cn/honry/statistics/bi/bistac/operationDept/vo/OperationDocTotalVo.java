package cn.honry.statistics.bi.bistac.operationDept.vo;

public class OperationDocTotalVo {
	private Double ysgzlcost;//手术金额
	private Integer ysgzlnum;//手术例数
	private String name;//名称
	private String inpatient_date;//日期
	public Double getYsgzlcost() {
		return ysgzlcost;
	}
	public void setYsgzlcost(Double ysgzlcost) {
		this.ysgzlcost = ysgzlcost;
	}
	public Integer getYsgzlnum() {
		return ysgzlnum;
	}
	public void setYsgzlnum(Integer ysgzlnum) {
		this.ysgzlnum = ysgzlnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInpatient_date() {
		return inpatient_date;
	}
	public void setInpatient_date(String inpatient_date) {
		this.inpatient_date = inpatient_date;
	}
	
}
