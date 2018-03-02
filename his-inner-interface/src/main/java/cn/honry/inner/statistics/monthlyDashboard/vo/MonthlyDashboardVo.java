package cn.honry.inner.statistics.monthlyDashboard.vo;

import java.util.Date;

public class MonthlyDashboardVo {
	private Integer outTotal;
	private Date sTime;//最小时间
	private Date eTime;//最大时间
	private Integer mOperationApply;//当月手术例数
	private Integer operationApplys;//总手术例数
	private Date inDate;//入住时间
	private Date outDate;//出院时间
	private Integer useBed;//使用中床位
	private Integer totalBed;//总床位
	private Integer outState;//出院状态
	private Integer outStateTotal;//各种出院状态人数
	private Double totCost0;//住院费用0
	private Double totCost1;//住院费用1
	private Double totCost2;//住院费用2
	private Double totCost3;//住院费用3
	private Double totCost4;//住院费用4
	private Double totCost5;//住院费用5
	private Double totCost6;//住院费用6
	private Double totCost7;//住院费用7
	private Double totCost8;//住院费用8
	private Double totCost9;//住院费用9
	private Double totCost10;//住院费用10
	private Double totCost11;//住院费用11
	private Double totCost12;//上年同月住院费用
	private Double totCost13;//上月住院费用
	private String countLeave;//
	private String yearAndMonth;
	private String average;
	public Integer getOutTotal() {
		return outTotal;
	}
	public void setOutTotal(Integer outTotal) {
		this.outTotal = outTotal;
	}
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public Integer getmOperationApply() {
		return mOperationApply;
	}
	public void setmOperationApply(Integer mOperationApply) {
		this.mOperationApply = mOperationApply;
	}
	public Integer getOperationApplys() {
		return operationApplys;
	}
	public void setOperationApplys(Integer operationApplys) {
		this.operationApplys = operationApplys;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Integer getUseBed() {
		return useBed;
	}
	public void setUseBed(Integer useBed) {
		this.useBed = useBed;
	}
	public Integer getTotalBed() {
		return totalBed;
	}
	public void setTotalBed(Integer totalBed) {
		this.totalBed = totalBed;
	}
	public Integer getOutState() {
		return outState;
	}
	public void setOutState(Integer outState) {
		this.outState = outState;
	}
	public Integer getOutStateTotal() {
		return outStateTotal;
	}
	public void setOutStateTotal(Integer outStateTotal) {
		this.outStateTotal = outStateTotal;
	}
	public Double getTotCost0() {
		return totCost0;
	}
	public void setTotCost0(Double totCost0) {
		this.totCost0 = totCost0;
	}
	public Double getTotCost1() {
		return totCost1;
	}
	public void setTotCost1(Double totCost1) {
		this.totCost1 = totCost1;
	}
	public Double getTotCost2() {
		return totCost2;
	}
	public void setTotCost2(Double totCost2) {
		this.totCost2 = totCost2;
	}
	public Double getTotCost3() {
		return totCost3;
	}
	public void setTotCost3(Double totCost3) {
		this.totCost3 = totCost3;
	}
	public Double getTotCost4() {
		return totCost4;
	}
	public void setTotCost4(Double totCost4) {
		this.totCost4 = totCost4;
	}
	public Double getTotCost5() {
		return totCost5;
	}
	public void setTotCost5(Double totCost5) {
		this.totCost5 = totCost5;
	}
	public Double getTotCost6() {
		return totCost6;
	}
	public void setTotCost6(Double totCost6) {
		this.totCost6 = totCost6;
	}
	public Double getTotCost7() {
		return totCost7;
	}
	public void setTotCost7(Double totCost7) {
		this.totCost7 = totCost7;
	}
	public Double getTotCost8() {
		return totCost8;
	}
	public void setTotCost8(Double totCost8) {
		this.totCost8 = totCost8;
	}
	public Double getTotCost9() {
		return totCost9;
	}
	public void setTotCost9(Double totCost9) {
		this.totCost9 = totCost9;
	}
	public Double getTotCost10() {
		return totCost10;
	}
	public void setTotCost10(Double totCost10) {
		this.totCost10 = totCost10;
	}
	public Double getTotCost11() {
		return totCost11;
	}
	public void setTotCost11(Double totCost11) {
		this.totCost11 = totCost11;
	}
	public Double getTotCost12() {
		return totCost12;
	}
	public void setTotCost12(Double totCost12) {
		this.totCost12 = totCost12;
	}
	public Double getTotCost13() {
		return totCost13;
	}
	public void setTotCost13(Double totCost13) {
		this.totCost13 = totCost13;
	}
	public String getCountLeave() {
		return countLeave;
	}
	public void setCountLeave(String countLeave) {
		this.countLeave = countLeave;
	}
	public String getYearAndMonth() {
		return yearAndMonth;
	}
	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
	@Override
	public String toString() {
		return "MonthlyDashboardVo [outTotal=" + outTotal + ", sTime=" + sTime
				+ ", eTime=" + eTime + ", mOperationApply=" + mOperationApply
				+ ", operationApplys=" + operationApplys + ", inDate=" + inDate
				+ ", outDate=" + outDate + ", useBed=" + useBed + ", totalBed="
				+ totalBed + ", outState=" + outState + ", outStateTotal="
				+ outStateTotal + ", totCost0=" + totCost0 + ", totCost1="
				+ totCost1 + ", totCost2=" + totCost2 + ", totCost3="
				+ totCost3 + ", totCost4=" + totCost4 + ", totCost5="
				+ totCost5 + ", totCost6=" + totCost6 + ", totCost7="
				+ totCost7 + ", totCost8=" + totCost8 + ", totCost9="
				+ totCost9 + ", totCost10=" + totCost10 + ", totCost11="
				+ totCost11 + ", totCost12=" + totCost12 + ", totCost13="
				+ totCost13 + ", countLeave=" + countLeave + ", yearAndMonth="
				+ yearAndMonth + ", average=" + average + "]";
	}
	

}
