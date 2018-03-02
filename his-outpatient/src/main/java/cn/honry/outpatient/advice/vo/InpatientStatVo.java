package cn.honry.outpatient.advice.vo;

import java.util.List;



public class InpatientStatVo{
	/**住院流水号**/
	private String inpatientNo;
	/**表名**/
	private String tab;
	/**患者姓名**/
	private String name;
	/**性别**/
	private String sex;
	/**年龄**/
	private String age;
	/**病历号**/
	private String recordNo;
	/**医嘱**/
	private List<InpatientInfoVo> list;

	
	/**getters and setters**/
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}
	public List<InpatientInfoVo> getList() {
		return list;
	}

	public void setList(List<InpatientInfoVo> list) {
		this.list = list;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	
}
