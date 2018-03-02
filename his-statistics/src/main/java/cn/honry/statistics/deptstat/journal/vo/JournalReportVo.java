package cn.honry.statistics.deptstat.journal.vo;

import java.util.List;

/** 住院日报打印VO
* @ClassName: JournalReportVo 住院日报打印VO
* @Description: 住院日报打印VO
* @author dtl
* @date 2017年6月6日
*  
*/
public class JournalReportVo{

	
	/** 
	* @Fields reportTitleName : 报表表头
	*/ 
	private String reportTitleName;
	/** 
	* @Fields sTime : 统计时间 
	*/ 
	private String sTime;
	/** 
	* @Fields count : 记录总数 
	*/ 
	private Integer count;
	/** 
	* @Fields journalVos : 日报子记录 
	*/ 
	private List<JournalVo> journalVos;
	public String getReportTitleName() {
		return reportTitleName;
	}
	public void setReportTitleName(String reportTitleName) {
		this.reportTitleName = reportTitleName;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<JournalVo> getJournalVos() {
		return journalVos;
	}
	public void setJournalVos(List<JournalVo> journalVos) {
		this.journalVos = journalVos;
	}
	public JournalReportVo(String reportTitleName, String sTime, Integer count,
			List<JournalVo> journalVos) {
		super();
		this.reportTitleName = reportTitleName;
		this.sTime = sTime;
		this.count = count;
		this.journalVos = journalVos;
	}
	
}
