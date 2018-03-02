package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ListTotalIncomeStaticVo {
	private Double cost1;//药费
	private Double cost2;//治疗费
	private Double cost3;//检查费
	private Double cost4;//床位费
	private Double cost5;//化验费
	private Double cost6;//手术费
	
	private Date eTime2;//费用汇总表的最小时间
	private Date sTime2;//费用汇总表的最大时间
	private Date eTime1;//处方明细表的最小时间
	private Date sTime1;//处方明细表的最大时间
	private Double value;
	private String name;
	private String date1;
	private String code;
	private String dept;
	private String classType;
	private String doctor;
	
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getName() {
		return name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date geteTime1() {
		return eTime1;
	}
	public void seteTime1(Date eTime1) {
		this.eTime1 = eTime1;
	}
	public Date getsTime1() {
		return sTime1;
	}
	public void setsTime1(Date sTime1) {
		this.sTime1 = sTime1;
	}
	public Date geteTime2() {
		return eTime2;
	}
	public void seteTime2(Date eTime2) {
		this.eTime2 = eTime2;
	}
	public Date getsTime2() {
		return sTime2;
	}
	public void setsTime2(Date sTime2) {
		this.sTime2 = sTime2;
	}
	public Double getCost1() {
		return cost1;
	}
	public void setCost1(Double cost1) {
		this.cost1 = cost1;
	}
	public Double getCost2() {
		return cost2;
	}
	public void setCost2(Double cost2) {
		this.cost2 = cost2;
	}
	public Double getCost3() {
		return cost3;
	}
	public void setCost3(Double cost3) {
		this.cost3 = cost3;
	}
	public Double getCost4() {
		return cost4;
	}
	public void setCost4(Double cost4) {
		this.cost4 = cost4;
	}
	public Double getCost5() {
		return cost5;
	}
	public void setCost5(Double cost5) {
		this.cost5 = cost5;
	}
	public Double getCost6() {
		return cost6;
	}
	public void setCost6(Double cost6) {
		this.cost6 = cost6;
	}
	@Override
	public String toString() {
		return "ListTotalIncomeStaticVo [cost1=" + cost1 + ", cost2=" + cost2
				+ ", cost3=" + cost3 + ", cost4=" + cost4 + ", cost5=" + cost5
				+ ", cost6=" + cost6 + ", eTime2=" + eTime2 + ", sTime2="
				+ sTime2 + ", eTime1=" + eTime1 + ", sTime1=" + sTime1
				+ ", value=" + value + ", name=" + name + "]";
	}
	
}
