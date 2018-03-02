package cn.honry.base.bean.model;

/**
 * BiBasePotype entity. @author MyEclipse Persistence Tools
 */

public class BiBasePotype implements java.io.Serializable {

	// Fields

	private String typeId;
	private String typeCode;
	private String typeName;
	private String fitExtent;
	private String decmpsState;
	private String chargeState;
	private String needDrug;
	private String prnExelist;
	private String needConfirm;
	private String totqtyFlag;
	private String prnMorlist;

	// Constructors

	/** default constructor */
	public BiBasePotype() {
	}

	/** full constructor */
	public BiBasePotype(String typeCode, String typeName, String fitExtent,
			String decmpsState, String chargeState, String needDrug,
			String prnExelist, String needConfirm, String totqtyFlag,
			String prnMorlist) {
		this.typeCode = typeCode;
		this.typeName = typeName;
		this.fitExtent = fitExtent;
		this.decmpsState = decmpsState;
		this.chargeState = chargeState;
		this.needDrug = needDrug;
		this.prnExelist = prnExelist;
		this.needConfirm = needConfirm;
		this.totqtyFlag = totqtyFlag;
		this.prnMorlist = prnMorlist;
	}

	// Property accessors

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getFitExtent() {
		return this.fitExtent;
	}

	public void setFitExtent(String fitExtent) {
		this.fitExtent = fitExtent;
	}

	public String getDecmpsState() {
		return this.decmpsState;
	}

	public void setDecmpsState(String decmpsState) {
		this.decmpsState = decmpsState;
	}

	public String getChargeState() {
		return this.chargeState;
	}

	public void setChargeState(String chargeState) {
		this.chargeState = chargeState;
	}

	public String getNeedDrug() {
		return this.needDrug;
	}

	public void setNeedDrug(String needDrug) {
		this.needDrug = needDrug;
	}

	public String getPrnExelist() {
		return this.prnExelist;
	}

	public void setPrnExelist(String prnExelist) {
		this.prnExelist = prnExelist;
	}

	public String getNeedConfirm() {
		return this.needConfirm;
	}

	public void setNeedConfirm(String needConfirm) {
		this.needConfirm = needConfirm;
	}

	public String getTotqtyFlag() {
		return this.totqtyFlag;
	}

	public void setTotqtyFlag(String totqtyFlag) {
		this.totqtyFlag = totqtyFlag;
	}

	public String getPrnMorlist() {
		return this.prnMorlist;
	}

	public void setPrnMorlist(String prnMorlist) {
		this.prnMorlist = prnMorlist;
	}

}