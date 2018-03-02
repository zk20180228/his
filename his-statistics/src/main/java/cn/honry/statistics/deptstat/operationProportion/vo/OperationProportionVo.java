package cn.honry.statistics.deptstat.operationProportion.vo;

import java.util.Date;


public class OperationProportionVo{

	/**科室代码0-全部部门*/
	private String deptCode;
	/**药品编码*/
	private String deptName;
	/**批号(如果等于'全部',则表示所有批号的药品)*/
	private Integer outNum;
	/**商品名称*/
	private Integer exOutNum;
	/**规格*/
	private Integer operationNum;
	private Double proportion;
	/**出院人数*/
	private Integer total;
	/**转出人数*/
	private Integer total1;
	/**手术人数*/
	private Integer total2;
	private String workdate;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getTotal1() {
		return total1;
	}
	public void setTotal1(Integer total1) {
		this.total1 = total1;
	}
	public Integer getTotal2() {
		return total2;
	}
	public void setTotal2(Integer total2) {
		this.total2 = total2;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	/**创建人*/
	private String createuser;
	/**创建时间*/
	private Date createtime;
	/**人员姓名*/
	private String userName;
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
	public Integer getOutNum() {
		return outNum;
	}
	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}
	public Integer getExOutNum() {
		return exOutNum;
	}
	public void setExOutNum(Integer exOutNum) {
		this.exOutNum = exOutNum;
	}
	public Integer getOperationNum() {
		return operationNum;
	}
	public void setOperationNum(Integer operationNum) {
		this.operationNum = operationNum;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Double getProportion() {
		return proportion;
	}
	public void setProportion(Double proportion) {
		this.proportion = proportion;
	}
	@Override
	public String toString() {
		return "OperationProportionVo [deptCode=" + deptCode + ", deptName="
				+ deptName + ", outNum=" + outNum + ", exOutNum=" + exOutNum
				+ ", operationNum=" + operationNum + ", proportion="
				+ proportion + ", total=" + total + ", total1=" + total1
				+ ", total2=" + total2 + ", workdate=" + workdate
				+ ", createuser=" + createuser + ", createtime=" + createtime
				+ ", userName=" + userName + "]";
	}


}
