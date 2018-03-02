package cn.honry.statistics.deptstat.internalCompare1.vo;

import java.util.List;

public class ReportInternalCompare1Vo {
	
	/** 
	* @Fields title : 报表题目
	*/ 
	private String title;
	/** 
	* @Fields sTime : 上年时间
	*/ 
	private String sTime;
	/** 
	* @Fields eTime : 本年时间
	*/ 
	private String eTime;
	/** 
	* @Fields compare1Vos : 明细集合
	*/ 
	private List<InternalCompare1Vo> compare1Vos;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public List<InternalCompare1Vo> getCompare1Vos() {
		return compare1Vos;
	}
	public void setCompare1Vos(List<InternalCompare1Vo> compare1Vos) {
		this.compare1Vos = compare1Vos;
	}
	public ReportInternalCompare1Vo(String title, String sTime, String eTime,
			List<InternalCompare1Vo> compare1Vos) {
		super();
		this.title = title;
		this.sTime = sTime;
		this.eTime = eTime;
		this.compare1Vos = compare1Vos;
	}
	
	
	public ReportInternalCompare1Vo() {
		super();
	}
}

