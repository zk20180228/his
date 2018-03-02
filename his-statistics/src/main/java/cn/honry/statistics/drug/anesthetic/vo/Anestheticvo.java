package cn.honry.statistics.drug.anesthetic.vo;

import java.util.Date;

/**   
* 麻醉精神药品vo
* @modifier：zhangjin
* @modifyRmk：  
* @version 1.0
 */
public class Anestheticvo {
 
	private String deptName;//科室名字
	private String pno;//病历号
	private String patientName;//患者名字
	private String doctName;//员工姓名
	private String drugName;//通用名称
	private String drugSpec;//规格
	private String drugPack;//包装单位
	private Double num;//包装数量
	private Date drugedDate;//发药时间
	private String meark;//药品出库记录表备注
	private String name;//经手人（摆药单摆药人）
	private String doctCode;//医生编码（开方人）
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDoctName() {
		return doctName;
	}
	public void setDoctName(String doctName) {
		this.doctName = doctName;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugSpec() {
		return drugSpec;
	}
	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}
	public String getDrugPack() {
		return drugPack;
	}
	public void setDrugPack(String drugPack) {
		this.drugPack = drugPack;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Date getDrugedDate() {
		return drugedDate;
	}
	public void setDrugedDate(Date drugedDate) {
		this.drugedDate = drugedDate;
	}
	public String getMeark() {
		return meark;
	}
	public void setMeark(String meark) {
		this.meark = meark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}

	
}
