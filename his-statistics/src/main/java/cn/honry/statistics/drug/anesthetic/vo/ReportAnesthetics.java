package cn.honry.statistics.drug.anesthetic.vo;

import java.util.List;

/** 麻醉精神药品打印vo
* @ClassName: ReportAnesthetics 
* @author dtl
* @date 2017年3月27日
*  
*/
public class ReportAnesthetics {
 
	/** 
	* @Fields sTime : 开始时间
	*/ 
	private String sTime;
	/** 
	* @Fields eTime : 结束时间
	*/ 
	private String eTime;
	/** 
	* @Fields deptName : 科室名称
	*/ 
	private String deptName;
	/** 
	* @Fields drugTypt : 药品类型
	*/ 
	private String drugTypt;
	/** 
	* @Fields anestheticvos : 麻醉精神药品记录
	*/ 
	private List<Anestheticvo> anestheticvos;
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
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDrugTypt() {
		return drugTypt;
	}
	public void setDrugTypt(String drugTypt) {
		this.drugTypt = drugTypt;
	}
	public List<Anestheticvo> getAnestheticvos() {
		return anestheticvos;
	}
	public void setAnestheticvos(List<Anestheticvo> anestheticvos) {
		this.anestheticvos = anestheticvos;
	}
	public ReportAnesthetics(String sTime, String eTime, String deptName,
			String drugTypt, List<Anestheticvo> anestheticvos) {
		super();
		this.sTime = sTime;
		this.eTime = eTime;
		this.deptName = deptName;
		this.drugTypt = drugTypt;
		this.anestheticvos = anestheticvos;
	}
	
}
