package cn.honry.statistics.deptstat.out_inpatient_work.vo;

import java.util.List;

import cn.honry.statistics.sys.stop.vo.OutPatientVo;

public class OutInpatientWorkReport {
	
	private List<OutInpatientWorkVo> itemList;
	private String bTime;
	private String eTime;
	private String deptPerson;//科室负责人
	private String countPerson;//统计人
	private String dept;
	public List<OutInpatientWorkVo> getItemList() {
		return itemList;
	}
	public void setItemList(List<OutInpatientWorkVo> itemList) {
		this.itemList = itemList;
	}
	public String getbTime() {
		return bTime;
	}
	public void setbTime(String bTime) {
		this.bTime = bTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getDeptPerson() {
		return deptPerson;
	}
	public void setDeptPerson(String deptPerson) {
		this.deptPerson = deptPerson;
	}
	public String getCountPerson() {
		return countPerson;
	}
	public void setCountPerson(String countPerson) {
		this.countPerson = countPerson;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	

}
