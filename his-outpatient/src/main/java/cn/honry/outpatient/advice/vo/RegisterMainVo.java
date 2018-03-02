package cn.honry.outpatient.advice.vo;

import java.util.List;

public class RegisterMainVo {
	private String dept;//科室名称
	private String recipeNo;//处方号
	private String code;//合同号
	private String cont;//合同单位名称
	private String time;//操作时间
	private String mediNo;//病历号
	private String name;//姓名
	private String sex;//性别
	private String age;//患者年龄
	private String dct;//医师姓名
	private String dia;//主诉,诊断1
	private String pay;//药品金额
	
	private List<OutpatientVo> list;
	
	public List<OutpatientVo> getList() {
		return list;
	}
	public void setList(List<OutpatientVo> list) {
		this.list = list;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMediNo() {
		return mediNo;
	}
	public void setMediNo(String mediNo) {
		this.mediNo = mediNo;
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
	public String getDct() {
		return dct;
	}
	public void setDct(String dct) {
		this.dct = dct;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}

}
