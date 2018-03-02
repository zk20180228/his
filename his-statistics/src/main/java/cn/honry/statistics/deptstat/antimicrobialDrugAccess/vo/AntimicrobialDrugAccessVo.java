package cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo;

public class AntimicrobialDrugAccessVo {
  /** 员工姓名**/
  private String ename;
  /** 员工工号**/
  private String ecode;
  /**  员工级别**/
  private String elevel;
  /**  员工权限**/
  private String eaccess;
  /**  员工类型编码**/
  private String tcode;
  /**  员工类型名称**/
  private String tname;
  /**  总记录数 **/
  private Integer total;
  
public Integer getTotal() {
	return total;
}
public void setTotal(Integer total) {
	this.total = total;
}
public String getTcode() {
	return tcode;
}
public void setTcode(String tcode) {
	this.tcode = tcode;
}
public String getTname() {
	return tname;
}
public void setTname(String tname) {
	this.tname = tname;
}
public String getEname() {
	return ename;
}
public void setEname(String ename) {
	this.ename = ename;
}
public String getEcode() {
	return ecode;
}
public void setEcode(String ecode) {
	this.ecode = ecode;
}
public String getElevel() {
	return elevel;
}
public void setElevel(String elevel) {
	this.elevel = elevel;
}
public String getEaccess() {
	return eaccess;
}
public void setEaccess(String eaccess) {
	this.eaccess = eaccess;
}
  
}
