package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo;

import java.util.List;

public class DeptVoToIReport {
	private String deptString;
	private List<KidneyDiseaseWithDeptVo> list;

	public String getDeptString() {
		return deptString;
	}

	public void setDeptString(String deptString) {
		this.deptString = deptString;
	}

	public List<KidneyDiseaseWithDeptVo> getList() {
		return list;
	}

	public void setList(List<KidneyDiseaseWithDeptVo> list) {
		this.list = list;
	}
	
}
