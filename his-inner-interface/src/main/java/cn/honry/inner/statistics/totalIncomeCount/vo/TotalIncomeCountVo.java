package cn.honry.inner.statistics.totalIncomeCount.vo;

import java.util.List;

public class TotalIncomeCountVo {
	
	private String TotCost;//某日/月/年的总收入
	private List<MapVo> feeOf;//某日/月/年费别集合
 	private List<MapVo> areaOf;//某日/月/年院区收入集合
 	
 	
	
	
	
	
	public String getTotCost() {
		return TotCost;
	}
	public void setTotCost(String totCost) {
		TotCost = totCost;
	}
	public List<MapVo> getFeeOf() {
		return feeOf;
	}
	public void setFeeOf(List<MapVo> feeOf) {
		this.feeOf = feeOf;
	}
	public List<MapVo> getAreaOf() {
		return areaOf;
	}
	public void setAreaOf(List<MapVo> areaOf) {
		this.areaOf = areaOf;
	}
 	
	
	
 	
 	
 	
 	
 	

}
