package cn.honry.statistics.deptstat.outandinpatient.vo;

import java.io.Serializable;
import java.util.Date;

import cn.honry.utils.DateUtils;

public class GetOrOutPatient implements Serializable {

	private static final long serialVersionUID = 9017574129249892131L;
	
	/**住院号**/
	private String patientno;
	/**姓名**/
	private String name;
	/**性别**/
	private String sex;
	/**费别**/
	private String pact;
	/**转前科室**/
	private String beforeDept;
	/**转前床位**/
	private String beforeBedNo;
	/**转后科室**/
	private String afterDept;
	/**转后床位**/
	private String afterBedNo;
	/**入院日期**/
	private Date inDate;
	/**转科日期**/
	private Date turnDate;
	/**诊断**/
	private String clinic;
	public String getPatientno() {
		return patientno;
	}
	public void setPatientno(String patientno) {
		this.patientno = patientno;
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
	public String getPact() {
		return pact;
	}
	public void setPact(String pact) {
		this.pact = pact;
	}
	public String getBeforeDept() {
		return beforeDept;
	}
	public void setBeforeDept(String beforeDept) {
		this.beforeDept = beforeDept;
	}
	public String getBeforeBedNo() {
		return beforeBedNo;
	}
	public void setBeforeBedNo(String beforeBedNo) {
		this.beforeBedNo = beforeBedNo;
	}
	public String getAfterDept() {
		return afterDept;
	}
	public void setAfterDept(String afterDept) {
		this.afterDept = afterDept;
	}
	public String getAfterBedNo() {
		return afterBedNo;
	}
	public void setAfterBedNo(String afterBedNo) {
		this.afterBedNo = afterBedNo;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getTurnDate() {
		return turnDate;
	}
	public void setTurnDate(Date turnDate) {
		this.turnDate = turnDate;
	}
	public String getClinic() {
		return clinic;
	}
	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
	@Override
	public String toString() {
		return patientno + "," + (name==null?"":name)
				+ "," + (sex==null?"":sex) + "," + (pact==null?"":pact) + ","
				+ (beforeDept==null?"":beforeDept) + "," + (beforeBedNo==null?"":beforeBedNo) + ","
				+ (afterDept==null?"":afterDept) + "," + (afterBedNo==null?"":afterBedNo) + ","
				+ (inDate==null?"":DateUtils.formatDateY_M_D_H_M_S(inDate)) + "," + (turnDate==null?"":DateUtils.formatDateY_M_D_H_M_S(turnDate)) + "," + clinic+",";
	}
	
	
}
