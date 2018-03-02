package cn.honry.inner.statistics.hospitalday.vo;

/**  
 *  页面显示结果VO
 *  变量名后2位为统计费用代码
 * @Author:
 * @version 2.0
 */
public class ResultVo {

	private String inhosDeptcode;//在院科室代码
	private String deptName;//科室名称
	private Double totCost01=0d;//西药费
	private Double totCost02=0d;//中成药费
	private Double totCost03=0d;//中草药费
	private Double totCost04=0d;//床位费
	private Double totCost05=0d;//治疗费
	private Double totCost07=0d;//检查费
	private Double totCost08=0d;//放射费
	private Double totCost09=0d;//化验费
	private Double totCost10=0d;//手术费
	private Double totCost11=0d;//输血费
	private Double totCost12=0d;//输氧费
	private Double totCost13=0d;//材料费
	private Double totCost14=0d;//其他费
	private Double totCost15=0d;//护理费
	private Double totCost16=0d;//诊察费
	
	private Double totCost=0d;//总计
	
	public Double getTotCost() {
		return totCost;
	}
	public void setTotCost(Double totCost) {
		this.totCost = totCost;
	}
	public String getInhosDeptcode() {
		return inhosDeptcode;
	}
	public void setInhosDeptcode(String inhosDeptcode) {
		this.inhosDeptcode = inhosDeptcode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getTotCost01() {
		return totCost01;
	}
	public void setTotCost01(Double totCost01) {
		this.totCost01 = totCost01;
	}
	public Double getTotCost02() {
		return totCost02;
	}
	public void setTotCost02(Double totCost02) {
		this.totCost02 = totCost02;
	}
	public Double getTotCost03() {
		return totCost03;
	}
	public void setTotCost03(Double totCost03) {
		this.totCost03 = totCost03;
	}
	public Double getTotCost04() {
		return totCost04;
	}
	public void setTotCost04(Double totCost04) {
		this.totCost04 = totCost04;
	}
	public Double getTotCost05() {
		return totCost05;
	}
	public void setTotCost05(Double totCost05) {
		this.totCost05 = totCost05;
	}
	public Double getTotCost07() {
		return totCost07;
	}
	public void setTotCost07(Double totCost07) {
		this.totCost07 = totCost07;
	}
	public Double getTotCost08() {
		return totCost08;
	}
	public void setTotCost08(Double totCost08) {
		this.totCost08 = totCost08;
	}
	public Double getTotCost09() {
		return totCost09;
	}
	public void setTotCost09(Double totCost09) {
		this.totCost09 = totCost09;
	}
	public Double getTotCost10() {
		return totCost10;
	}
	public void setTotCost10(Double totCost10) {
		this.totCost10 = totCost10;
	}
	public Double getTotCost11() {
		return totCost11;
	}
	public void setTotCost11(Double totCost11) {
		this.totCost11 = totCost11;
	}
	public Double getTotCost12() {
		return totCost12;
	}
	public void setTotCost12(Double totCost12) {
		this.totCost12 = totCost12;
	}
	public Double getTotCost13() {
		return totCost13;
	}
	public void setTotCost13(Double totCost13) {
		this.totCost13 = totCost13;
	}
	public Double getTotCost14() {
		return totCost14;
	}
	public void setTotCost14(Double totCost14) {
		this.totCost14 = totCost14;
	}
	public Double getTotCost15() {
		return totCost15;
	}
	public void setTotCost15(Double totCost15) {
		this.totCost15 = totCost15;
	}
	public Double getTotCost16() {
		return totCost16;
	}
	public void setTotCost16(Double totCost16) {
		this.totCost16 = totCost16;
	}
	
}
