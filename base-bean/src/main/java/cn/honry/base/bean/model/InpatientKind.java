package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 医嘱类型实体类
 * @author yeguanqun
 *
 */
public class InpatientKind extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/**医疗类别编码*/
	private String typeCode;
	/**医疗类别名称*/
	private String typeName;
	/**试用范围*/
	private Integer fitExtent;
	/**医嘱状态 1 2 3*/
	private Integer decmpsState;
	/**是否计费 1是0否*/
	private Integer chargeState;
	/**药房是否配药 1是 0否*/
	private Integer needDrug;
	/**是否打印执行单 1是 0否*/
	private Integer prnExelist;
	/**是否需要确认 1是 0否*/
	private Integer needConfirm;
	/**是否能开总量 1是 0否*/
	private Integer totqtyFlag;
	/**是否打印医嘱单 1是 0否*/
	private Integer prnMorlist;
	/** 
	* @Fields hospitalId : 医院编码 
	*/ 
	private Integer hospitalId;
	/** 
	* @Fields areaCode : 院区编码 
	*/ 
	private String areaCode;
	
	/** 分页用的page和rows*/
	private String page;
	private String rows;
	
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getFitExtent() {
		return fitExtent;
	}
	public void setFitExtent(Integer fitExtent) {
		this.fitExtent = fitExtent;
	}
	public Integer getDecmpsState() {
		return decmpsState;
	}
	public void setDecmpsState(Integer decmpsState) {
		this.decmpsState = decmpsState;
	}
	public Integer getChargeState() {
		return chargeState;
	}
	public void setChargeState(Integer chargeState) {
		this.chargeState = chargeState;
	}
	public Integer getNeedDrug() {
		return needDrug;
	}
	public void setNeedDrug(Integer needDrug) {
		this.needDrug = needDrug;
	}
	public Integer getPrnExelist() {
		return prnExelist;
	}
	public void setPrnExelist(Integer prnExelist) {
		this.prnExelist = prnExelist;
	}
	public Integer getNeedConfirm() {
		return needConfirm;
	}
	public void setNeedConfirm(Integer needConfirm) {
		this.needConfirm = needConfirm;
	}
	public Integer getTotqtyFlag() {
		return totqtyFlag;
	}
	public void setTotqtyFlag(Integer totqtyFlag) {
		this.totqtyFlag = totqtyFlag;
	}
	public Integer getPrnMorlist() {
		return prnMorlist;
	}
	public void setPrnMorlist(Integer prnMorlist) {
		this.prnMorlist = prnMorlist;
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
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}
