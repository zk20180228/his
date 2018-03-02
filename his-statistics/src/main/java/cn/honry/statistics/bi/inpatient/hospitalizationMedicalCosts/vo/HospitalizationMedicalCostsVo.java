package cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.vo;


public class HospitalizationMedicalCostsVo {

	//科室名称
	private String deptname;
	//总金额 
	private Double totcost;
	//缴费次数
	private Integer coun;
	//西药费 01
	private Double xy;
	//中成药 02
	private Double zchengy;
	//中草药 03
	private Double zcaoy;
	//每次缴费平均
	private String totAvg;
	//西药费占比
	private String xypro;
	//中成药占比
	private String zchengypro;
	//中草药占比
	private String zcaoypro;
	
	//金额类型
	private String feecode;
	
	
	public String getFeecode() {
		return feecode;
	}
	public void setFeecode(String feecode) {
		this.feecode = feecode;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getTotAvg() {
		return totAvg;
	}
	public void setTotAvg(String totAvg) {
		this.totAvg = totAvg;
	}
	public String getXypro() {
		return xypro;
	}
	public void setXypro(String xypro) {
		this.xypro = xypro;
	}
	public String getZchengypro() {
		return zchengypro;
	}
	public void setZchengypro(String zchengypro) {
		this.zchengypro = zchengypro;
	}
	public String getZcaoypro() {
		return zcaoypro;
	}
	public void setZcaoypro(String zcaoypro) {
		this.zcaoypro = zcaoypro;
	}
	public Integer getCoun() {
		return coun;
	}
	public void setCoun(Integer coun) {
		this.coun = coun;
	}
	public Double getTotcost() {
		return totcost;
	}
	public void setTotcost(Double totcost) {
		this.totcost = totcost;
	}
	public Double getXy() {
		return xy;
	}
	public void setXy(Double xy) {
		this.xy = xy;
	}
	public Double getZchengy() {
		return zchengy;
	}
	public void setZchengy(Double zchengy) {
		this.zchengy = zchengy;
	}
	public Double getZcaoy() {
		return zcaoy;
	}
	public void setZcaoy(Double zcaoy) {
		this.zcaoy = zcaoy;
	}
	
	
	
}
