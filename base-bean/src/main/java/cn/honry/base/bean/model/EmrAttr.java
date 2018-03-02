package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * AbstractTEmrAttr entity provides the base persistence definition of the
 * TEmrAttr entity. @author MyEclipse Persistence Tools
 */

public class EmrAttr extends Entity{

	// Fields

	private String attrId;
	private String attrCode;
	private String attrName;
	private String pingyin;
	private String wb;
	private String inputcode;
	private Integer attrType;
	private Integer attrDisplaytype;
	private String attrCodetype;
	private String attrPrefix;
	private String attrSuffix;
	private Integer attrNotnull;
	private Integer attrMustSelect;
	private Integer attrLength;
	private Integer attrPrecision;
	private String attrDateformat;
	private String attrValidup;
	private String attrValiddown;
	private String attrDefaulttip;
	private Integer attrStatflg;
	private Integer attrPrintflg;
	private Integer attrKeywordflg;
	private Integer attrKind;

	// Constructors

	//分页
	private String page;
	private String rows;
	
	//选项
	/**
	 * 选项值
	 */
	private String options;
	/**
	 * 选项
	 */
	private String strOptions;

	// Property accessors

	public String getAttrId() {
		return this.attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getAttrCode() {
		return this.attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	public String getAttrName() {
		return this.attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getPingyin() {
		return this.pingyin;
	}

	public void setPingyin(String pingyin) {
		this.pingyin = pingyin;
	}

	public String getWb() {
		return this.wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getInputcode() {
		return this.inputcode;
	}

	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}

	public Integer getAttrType() {
		return this.attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}

	public Integer getAttrDisplaytype() {
		return this.attrDisplaytype;
	}

	public void setAttrDisplaytype(Integer attrDisplaytype) {
		this.attrDisplaytype = attrDisplaytype;
	}

	public String getAttrCodetype() {
		return this.attrCodetype;
	}

	public void setAttrCodetype(String attrCodetype) {
		this.attrCodetype = attrCodetype;
	}

	public String getAttrPrefix() {
		return this.attrPrefix;
	}

	public void setAttrPrefix(String attrPrefix) {
		this.attrPrefix = attrPrefix;
	}

	public String getAttrSuffix() {
		return this.attrSuffix;
	}

	public void setAttrSuffix(String attrSuffix) {
		this.attrSuffix = attrSuffix;
	}

	public Integer getAttrNotnull() {
		return this.attrNotnull;
	}

	public void setAttrNotnull(Integer attrNotnull) {
		this.attrNotnull = attrNotnull;
	}

	public Integer getAttrMustSelect() {
		return this.attrMustSelect;
	}

	public void setAttrMustSelect(Integer attrMustSelect) {
		this.attrMustSelect = attrMustSelect;
	}

	public Integer getAttrLength() {
		return this.attrLength;
	}

	public void setAttrLength(Integer attrLength) {
		this.attrLength = attrLength;
	}

	public Integer getAttrPrecision() {
		return this.attrPrecision;
	}

	public void setAttrPrecision(Integer attrPrecision) {
		this.attrPrecision = attrPrecision;
	}

	public String getAttrDateformat() {
		return this.attrDateformat;
	}

	public void setAttrDateformat(String attrDateformat) {
		this.attrDateformat = attrDateformat;
	}

	public String getAttrValidup() {
		return this.attrValidup;
	}

	public void setAttrValidup(String attrValidup) {
		this.attrValidup = attrValidup;
	}

	public String getAttrValiddown() {
		return this.attrValiddown;
	}

	public void setAttrValiddown(String attrValiddown) {
		this.attrValiddown = attrValiddown;
	}

	public String getAttrDefaulttip() {
		return this.attrDefaulttip;
	}

	public void setAttrDefaulttip(String attrDefaulttip) {
		this.attrDefaulttip = attrDefaulttip;
	}

	public Integer getAttrStatflg() {
		return this.attrStatflg;
	}

	public void setAttrStatflg(Integer attrStatflg) {
		this.attrStatflg = attrStatflg;
	}

	public Integer getAttrPrintflg() {
		return this.attrPrintflg;
	}

	public void setAttrPrintflg(Integer attrPrintflg) {
		this.attrPrintflg = attrPrintflg;
	}

	public Integer getAttrKeywordflg() {
		return this.attrKeywordflg;
	}

	public void setAttrKeywordflg(Integer attrKeywordflg) {
		this.attrKeywordflg = attrKeywordflg;
	}

	public Integer getAttrKind() {
		return attrKind;
	}

	public void setAttrKind(Integer attrKind) {
		this.attrKind = attrKind;
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

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getStrOptions() {
		return strOptions;
	}

	public void setStrOptions(String strOptions) {
		this.strOptions = strOptions;
	}


}