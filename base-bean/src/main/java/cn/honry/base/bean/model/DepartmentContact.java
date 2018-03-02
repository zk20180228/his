package cn.honry.base.bean.model;

import java.io.Serializable;

import cn.honry.base.bean.business.Entity;
/**
 *  
 * @Description：  部门科室间关系
 * @Author：lyy
 * @CreateDate：2015-11-2 下午04:47:47  
 * @Modifier：lyy
 * @ModifyDate：2015-11-2 下午04:47:47  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class DepartmentContact extends Entity implements Serializable{

	private static final long serialVersionUID = 1L;
	/*父级编号*/
	private String pardeptId;
	/*部门/诊室编码*/
	private String deptId;
	/*部门表/诊室表中的部门代码，若为分类可通过程序（按一定规则）自动生成一个*/
	private String deptCode;
	/*部门名称（分类名称）*/
	private String deptName;
	/*1-分类，2-科室，3-终极科室(诊室/病区)*/
	private String deptCalss;
	/*C-门诊, I-住院,F-财务，L-后勤，PI-药库，T-医技，0-其它，D-机关，P-药房，N-护士站*/
	private String deptType;
	/*拼音码*/
	private String spellCode;
	/*五笔码*/
	private String wbCode;
	/*自定义码*/
	private String userDefinedCode;
	/*英文名称*/
	private String deptEnCode;
	/*节点的层级*/
	private Integer gradeCode;
	/*顺序号*/
	private Integer sortId ;
	/*1-在用，0-停用，2-废弃*/
	private Integer validState=1 ;
	/*扩展标志*/
	private Integer extFlag;
	/*备注*/
	private String mark;
	/*部门/诊室路径*/
	private String path;
	/*根据排序生成路径*/
	private Integer ordertopath;
	/*所有父级*/
	private String upperpath;
	/* 分类名称 */
	private String referenceType;
	
	
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getPardeptId() {
		return pardeptId;
	}
	public void setPardeptId(String pardeptId) {
		this.pardeptId = pardeptId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
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
	public String getDeptCalss() {
		return deptCalss;
	}
	public void setDeptCalss(String deptCalss) {
		this.deptCalss = deptCalss;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public String getWbCode() {
		return wbCode;
	}
	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}
	public String getUserDefinedCode() {
		return userDefinedCode;
	}
	public void setUserDefinedCode(String userDefinedCode) {
		this.userDefinedCode = userDefinedCode;
	}
	public String getDeptEnCode() {
		return deptEnCode;
	}
	public void setDeptEnCode(String deptEnCode) {
		this.deptEnCode = deptEnCode;
	}
	public Integer getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(Integer gradeCode) {
		this.gradeCode = gradeCode;
	}
	public Integer getSortId() {
		return sortId;
	}
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public Integer getExtFlag() {
		return extFlag;
	}
	public void setExtFlag(Integer extFlag) {
		this.extFlag = extFlag;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getOrdertopath() {
		return ordertopath;
	}
	public void setOrdertopath(Integer ordertopath) {
		this.ordertopath = ordertopath;
	}
	public String getUpperpath() {
		return upperpath;
	}
	public void setUpperpath(String upperpath) {
		this.upperpath = upperpath;
	}
	
}
