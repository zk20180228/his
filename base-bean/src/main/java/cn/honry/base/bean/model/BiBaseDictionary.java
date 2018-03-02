package cn.honry.base.bean.model;

import java.math.BigDecimal;

/**
 * BiBaseDictionary entity. @author MyEclipse Persistence Tools
 */

public class BiBaseDictionary implements java.io.Serializable {

	// Fields

	private String id;
	private String codeEncode;
	private String codeName;
	private String codeType;
	private String codeTypeName;
	private String codeParentid;
	private Boolean codeHaveson;
	private String codeMark;
	private BigDecimal codeSortId;
	private BigDecimal codeOrder;
	private String codePath;
	private BigDecimal codeLevel;
	private String codeUppath;
	private Boolean codeValidState;
	private String extC1;
	private String extC2;
	private String extC3;
	private String extC4;
	private String extC5;

	// Constructors

	/** default constructor */
	public BiBaseDictionary() {
	}

	/** minimal constructor */
	public BiBaseDictionary(String codeEncode, String codeName, String codeType) {
		this.codeEncode = codeEncode;
		this.codeName = codeName;
		this.codeType = codeType;
	}

	/** full constructor */
	public BiBaseDictionary(String codeEncode, String codeName,
			String codeType, String codeTypeName, String codeParentid,
			Boolean codeHaveson, String codeMark, BigDecimal codeSortId,
			BigDecimal codeOrder, String codePath, BigDecimal codeLevel,
			String codeUppath, Boolean codeValidState, String extC1,
			String extC2, String extC3, String extC4, String extC5) {
		this.codeEncode = codeEncode;
		this.codeName = codeName;
		this.codeType = codeType;
		this.codeTypeName = codeTypeName;
		this.codeParentid = codeParentid;
		this.codeHaveson = codeHaveson;
		this.codeMark = codeMark;
		this.codeSortId = codeSortId;
		this.codeOrder = codeOrder;
		this.codePath = codePath;
		this.codeLevel = codeLevel;
		this.codeUppath = codeUppath;
		this.codeValidState = codeValidState;
		this.extC1 = extC1;
		this.extC2 = extC2;
		this.extC3 = extC3;
		this.extC4 = extC4;
		this.extC5 = extC5;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeEncode() {
		return this.codeEncode;
	}

	public void setCodeEncode(String codeEncode) {
		this.codeEncode = codeEncode;
	}

	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeType() {
		return this.codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCodeTypeName() {
		return this.codeTypeName;
	}

	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}

	public String getCodeParentid() {
		return this.codeParentid;
	}

	public void setCodeParentid(String codeParentid) {
		this.codeParentid = codeParentid;
	}

	public Boolean getCodeHaveson() {
		return this.codeHaveson;
	}

	public void setCodeHaveson(Boolean codeHaveson) {
		this.codeHaveson = codeHaveson;
	}

	public String getCodeMark() {
		return this.codeMark;
	}

	public void setCodeMark(String codeMark) {
		this.codeMark = codeMark;
	}

	public BigDecimal getCodeSortId() {
		return this.codeSortId;
	}

	public void setCodeSortId(BigDecimal codeSortId) {
		this.codeSortId = codeSortId;
	}

	public BigDecimal getCodeOrder() {
		return this.codeOrder;
	}

	public void setCodeOrder(BigDecimal codeOrder) {
		this.codeOrder = codeOrder;
	}

	public String getCodePath() {
		return this.codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public BigDecimal getCodeLevel() {
		return this.codeLevel;
	}

	public void setCodeLevel(BigDecimal codeLevel) {
		this.codeLevel = codeLevel;
	}

	public String getCodeUppath() {
		return this.codeUppath;
	}

	public void setCodeUppath(String codeUppath) {
		this.codeUppath = codeUppath;
	}

	public Boolean getCodeValidState() {
		return this.codeValidState;
	}

	public void setCodeValidState(Boolean codeValidState) {
		this.codeValidState = codeValidState;
	}

	public String getExtC1() {
		return this.extC1;
	}

	public void setExtC1(String extC1) {
		this.extC1 = extC1;
	}

	public String getExtC2() {
		return this.extC2;
	}

	public void setExtC2(String extC2) {
		this.extC2 = extC2;
	}

	public String getExtC3() {
		return this.extC3;
	}

	public void setExtC3(String extC3) {
		this.extC3 = extC3;
	}

	public String getExtC4() {
		return this.extC4;
	}

	public void setExtC4(String extC4) {
		this.extC4 = extC4;
	}

	public String getExtC5() {
		return this.extC5;
	}

	public void setExtC5(String extC5) {
		this.extC5 = extC5;
	}

}