package cn.honry.base.bean.model;

import java.sql.Clob;
import java.util.Date;


import cn.honry.base.bean.business.Entity;


/**患者电子病历实体
 * @author dtl
 *
 */
public class EmrMain extends Entity{

	// Fields
	/**
	 * 模板编号
	 */
	private String tempId;
	/**
	 * 流水号
	 */
	private String emrSN;
	/**
	 * 患者病历号
	 */
	private String emrPatId;
	/**
	 * 病历分类
	 */
	private String emrType;
	/**
	 * 病历内容
	 */
	private Clob emrContent;
	/**
	 * 病历文件
	 */
	private String emrFile;
	/**
	 * 状态
	 */
	private Integer emrState;
	/**
	 * 评分
	 */
	private double emrScore;
	/**
	 * 评分等级
	 */
	private Integer emrLevel;
	/**
	 * 上级医师
	 */
	private String emrHigherDoc;
	/**
	 * 上级医师检验时间
	 */
	private Date emrHigherTime;
	/**
	 * 主任医师
	 */
	private String emrChiefDoc;
	/**
	 * 主任医师检验时间
	 */
	private Date emrChiefTime;
	/**
	 * 患者来源  1：门诊；2：住院
	 */
	private Integer emrSource;
	/**
	 * 患者科室
	 */
	private String deptCode;
	
	/** 
	* @Fields emrHigherAdvice : 审签意见
	*/ 
	private String emrHigherAdvice;
	
	/** 
	 * @Fields emrChiefAdvice : 审签意见
	 */ 
	private String emrChiefAdvice;
	
	/** 
	* @Fields emrClinic : 患者门诊号/住院号
	*/ 
	private String emrClinic;
	//与数据库无关字段
	/**
	 * 字符串类型内容
	 */	
	private String strContent;
	/**
	 * 患者姓名
	 */	
	private String patientName;
	/**
	 * 模板名称
	 */	
	private String tempName;
	/**
	 * 类别名称
	 */	
	private String typeName;
	
	
	/**
	 * 分页
	 */
	private String page;
	private String rows;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public String getEmrSN() {
		return emrSN;
	}
	public void setEmrSN(String emrSN) {
		this.emrSN = emrSN;
	}
	public String getEmrPatId() {
		return emrPatId;
	}
	public void setEmrPatId(String emrPatId) {
		this.emrPatId = emrPatId;
	}
	public String getEmrType() {
		return emrType;
	}
	public void setEmrType(String emrType) {
		this.emrType = emrType;
	}
	public Clob getEmrContent() {
		return emrContent;
	}
	public void setEmrContent(Clob emrContent) {
		this.emrContent = emrContent;
	}
	public String getEmrFile() {
		return emrFile;
	}
	public void setEmrFile(String emrFile) {
		this.emrFile = emrFile;
	}
	public Integer getEmrState() {
		return emrState;
	}
	public void setEmrState(Integer emrState) {
		this.emrState = emrState;
	}
	public double getEmrScore() {
		return emrScore;
	}
	public void setEmrScore(double emrScore) {
		this.emrScore = emrScore;
	}
	public Integer getEmrLevel() {
		return emrLevel;
	}
	public void setEmrLevel(Integer emrLevel) {
		this.emrLevel = emrLevel;
	}
	public String getEmrHigherDoc() {
		return emrHigherDoc;
	}
	public void setEmrHigherDoc(String emrHigherDoc) {
		this.emrHigherDoc = emrHigherDoc;
	}
	public Date getEmrHigherTime() {
		return emrHigherTime;
	}
	public void setEmrHigherTime(Date emrHigherTime) {
		this.emrHigherTime = emrHigherTime;
	}
	public String getEmrChiefDoc() {
		return emrChiefDoc;
	}
	public void setEmrChiefDoc(String emrChiefDoc) {
		this.emrChiefDoc = emrChiefDoc;
	}
	public Date getEmrChiefTime() {
		return emrChiefTime;
	}
	public void setEmrChiefTime(Date emrChiefTime) {
		this.emrChiefTime = emrChiefTime;
	}
	public Integer getEmrSource() {
		return emrSource;
	}
	public void setEmrSource(Integer emrSource) {
		this.emrSource = emrSource;
	}
	public String getStrContent() {
		return strContent;
	}
	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getEmrHigherAdvice() {
		return emrHigherAdvice;
	}
	public void setEmrHigherAdvice(String emrHigherAdvice) {
		this.emrHigherAdvice = emrHigherAdvice;
	}
	public String getEmrChiefAdvice() {
		return emrChiefAdvice;
	}
	public void setEmrChiefAdvice(String emrChiefAdvice) {
		this.emrChiefAdvice = emrChiefAdvice;
	}
	public String getEmrClinic() {
		return emrClinic;
	}
	public void setEmrClinic(String emrClinic) {
		this.emrClinic = emrClinic;
	}

}