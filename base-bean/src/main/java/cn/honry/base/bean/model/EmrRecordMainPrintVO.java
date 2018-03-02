package cn.honry.base.bean.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.honry.base.bean.model.EmrRecordMain;

public class EmrRecordMainPrintVO {
	//住院号
	private String inpatientNo;
	private String patientName;
	private String sex;
	private String birth;
	private String age;
	private String inDate;
	private String outDate;
	private String outDept;
	private String inpatientDoc;
	private String attendingDoc;
	private List<EmrRecordMain> list;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	public String getOutDept() {
		return outDept;
	}
	public void setOutDept(String outDept) {
		this.outDept = outDept;
	}
	public String getInpatientDoc() {
		return inpatientDoc;
	}
	public void setInpatientDoc(String inpatientDoc) {
		this.inpatientDoc = inpatientDoc;
	}
	public String getAttendingDoc() {
		return attendingDoc;
	}
	public void setAttendingDoc(String attendingDoc) {
		this.attendingDoc = attendingDoc;
	}
	public List<EmrRecordMain> getList() {
		return list;
	}
	public void setList(List<EmrRecordMain> list) {
		this.list = list;
	}
	
}
