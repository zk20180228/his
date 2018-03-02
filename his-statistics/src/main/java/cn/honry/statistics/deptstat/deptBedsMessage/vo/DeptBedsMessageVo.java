package cn.honry.statistics.deptstat.deptBedsMessage.vo;

import java.util.List;

/**
 * 科室床位信息查询VO
 * 
 */
public class DeptBedsMessageVo {
	private String DEPT_CODE;//科室Code
	private String deptCode;//科室Code
	private String deptName;//科室名称
	private String bedName;//床位编号
	private String bedWardName;//房间号
	private String bedNum;//床位号
	private String bedLevel;//床位等级
	private String bedState;//床位状态
	private List<DeptBedsMessageVo> list;//报表打印
	
	public String getDEPT_CODE() {
		return DEPT_CODE;
	}
	public void setDEPT_CODE(String dEPT_CODE) {
		DEPT_CODE = dEPT_CODE;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getBedWardName() {
		return bedWardName;
	}
	public void setBedWardName(String bedWardName) {
		this.bedWardName = bedWardName;
	}
	public String getBedNum() {
		return bedNum;
	}
	public void setBedNum(String bedNum) {
		this.bedNum = bedNum;
	}
	public String getBedLevel() {
		return bedLevel;
	}
	public void setBedLevel(String bedLevel) {
		this.bedLevel = bedLevel;
	}
	public String getBedState() {
		return bedState;
	}
	public void setBedState(String bedState) {
		this.bedState = bedState;
	}
	public List<DeptBedsMessageVo> getList() {
		return list;
	}
	public void setList(List<DeptBedsMessageVo> list) {
		this.list = list;
	}
}
