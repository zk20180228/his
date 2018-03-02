package cn.honry.inner.statistics.hosIncomeCount.vo;

import java.util.List;

//住院收入的vo
public class ZyIncomeVo {
	
	private List<MapVo> feePies;//各项费别
	private List<MapVo> totCost;//总收入
	private List<MapVo> deptPies;//科室前10+其他
	private List<MapVo> huanbiBars;//6日/月/年环比
	private List<MapVo> tongbiBars;//6日/月环比
	
	public List<MapVo> getFeePies() {
		return feePies;
	}
	public void setFeePies(List<MapVo> feePies) {
		this.feePies = feePies;
	}
	public List<MapVo> getTotCost() {
		return totCost;
	}
	public void setTotCost(List<MapVo> totCost) {
		this.totCost = totCost;
	}
	public List<MapVo> getDeptPies() {
		return deptPies;
	}
	public void setDeptPies(List<MapVo> deptPies) {
		this.deptPies = deptPies;
	}
	public List<MapVo> getHuanbiBars() {
		return huanbiBars;
	}
	public void setHuanbiBars(List<MapVo> huanbiBars) {
		this.huanbiBars = huanbiBars;
	}
	public List<MapVo> getTongbiBars() {
		return tongbiBars;
	}
	public void setTongbiBars(List<MapVo> tongbiBars) {
		this.tongbiBars = tongbiBars;
	}
	
	
	
	
	
	

}
