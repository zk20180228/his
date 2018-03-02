package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * AbstractTEmrAttr entity provides the base persistence definition of the
 * TEmrAttr entity. @author MyEclipse Persistence Tools
 */

public class EmrDeptScore extends Entity{

	// Fields
	/** 
	* @Fields deptsId : 评分编号
	*/ 
	private String deptsId;
	/** 
	* @Fields scoreId : 评分编码 
	*/ 
	private String scoreId;
	/** 
	* @Fields deptsPatId : 患者病历号
	*/ 
	private String deptsPatId;
	/** 
	* @Fields deptsType : 所属分类 
	*/ 
	private Integer deptsType;
	/** 
	* @Fields deptsCount : 次数
	*/ 
	private Integer deptsCount;
	/** 
	* @Fields deptsScore : 扣分 
	*/ 
	private Double deptsScore;
	/** 
	* @Fields deptsLevel : 评分等级
	*/ 
	private Integer deptsLevel;
	
	/** 
	* @Fields deptCode : 科室编码
	*/ 
	private String deptCode;

	// Constructors

	//分页
	private String page;
	private String rows;

	//数据库无关字段
	/** 
	* @Fields scoreDesc : 评分描述，对应评分标准中的描述
	*/ 
	private String scoreDesc;
	/** 
	* @Fields scoreValue : 每次扣分数
	*/ 
	private Double scoreValue;
	/** 
	* @Fields scoreBak : 评分标准 
	*/ 
	private String scoreBak;
	/** 
	* @Fields single : 是否重复扣分 
	*/ 
	private Integer single;
	/** 
	* @Fields scoreCode : 评分code
	*/ 
	private String scoreCode;
	// Property accessors
	
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}
	public String getDeptsPatId() {
		return deptsPatId;
	}
	public void setDeptsPatId(String deptsPatId) {
		this.deptsPatId = deptsPatId;
	}
	public Integer getDeptsType() {
		return deptsType;
	}
	public void setDeptsType(Integer deptsType) {
		this.deptsType = deptsType;
	}
	public Integer getDeptsCount() {
		return deptsCount;
	}
	public void setDeptsCount(Integer deptsCount) {
		this.deptsCount = deptsCount;
	}
	public Double getDeptsScore() {
		return deptsScore;
	}
	public void setDeptsScore(Double deptsScore) {
		this.deptsScore = deptsScore;
	}
	public Integer getDeptsLevel() {
		return deptsLevel;
	}
	public void setDeptsLevel(Integer deptsLevel) {
		this.deptsLevel = deptsLevel;
	}
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
	public String getDeptsId() {
		return deptsId;
	}
	public void setDeptsId(String deptsId) {
		this.deptsId = deptsId;
	}
	public String getScoreDesc() {
		return scoreDesc;
	}
	public void setScoreDesc(String scoreDesc) {
		this.scoreDesc = scoreDesc;
	}
	public Double getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(Double scoreValue) {
		this.scoreValue = scoreValue;
	}
	public String getScoreBak() {
		return scoreBak;
	}
	public void setScoreBak(String scoreBak) {
		this.scoreBak = scoreBak;
	}
	public Integer getSingle() {
		return single;
	}
	public void setSingle(Integer single) {
		this.single = single;
	}
	public String getScoreCode() {
		return scoreCode;
	}
	public void setScoreCode(String scoreCode) {
		this.scoreCode = scoreCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}


}