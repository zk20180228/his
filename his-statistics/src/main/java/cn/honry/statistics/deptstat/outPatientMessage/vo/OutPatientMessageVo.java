package cn.honry.statistics.deptstat.outPatientMessage.vo;

import java.util.Date;
import java.util.List;

/**
 * 出院患者信息查询VO
 * 
 */
public class OutPatientMessageVo {
	
	private String inpatientNo;//住院流水号
	private String patientName;//患者姓名
	private String sex;//性别
	private Integer age;//年龄
	private String ageunit;//年龄单位
	private String bedName;//床位
	private String docName;//主治医师
	private String nurseName;//主管护士
	private Date inDate;//入院时间
	private Date outDate;//出院时间
	private Integer outState;//出院状态
	private String pactCode;//结算方式(费别)
	private String diagName;// 诊断名称(主诊断)
	private String deptCode;//科室名称
	private List<OutPatientMessageVo> list;//报表打印
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getAgeunit() {
		return ageunit;
	}
	public void setAgeunit(String ageunit) {
		this.ageunit = ageunit;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getNurseName() {
		return nurseName;
	}
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
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
	public Integer getOutState() {
		return outState;
	}
	public void setOutState(Integer outState) {
		this.outState = outState;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<OutPatientMessageVo> getList() {
		return list;
	}
	public void setList(List<OutPatientMessageVo> list) {
		this.list = list;
	}
	
}