package cn.honry.base.bean.model;

/**
 * BiBaseIcd10 entity. @author MyEclipse Persistence Tools
 */

public class BiBaseIcd10 implements java.io.Serializable {

	// Fields

	private String icdId;
	private String icdDiagnosticcode;
	private String icdDiagnosticname;
	private String icdAlias;
	private String icdAlias2;
	private String icdScode;
	private String icdType;
	private String icdDiereason;
	private Short icdStandarddate;
	private String icdInpgrade;
	private String icdDiseasetype;
	private String icdSex;
	private String icdIsthirty;
	private String icdIsinfect;
	private String icdIscancer;
	private String icdIstcm;

	// Constructors

	/** default constructor */
	public BiBaseIcd10() {
	}

	/** full constructor */
	public BiBaseIcd10(String icdDiagnosticcode, String icdDiagnosticname,
			String icdAlias, String icdAlias2, String icdScode, String icdType,
			String icdDiereason, Short icdStandarddate, String icdInpgrade,
			String icdDiseasetype, String icdSex, String icdIsthirty,
			String icdIsinfect, String icdIscancer, String icdIstcm) {
		this.icdDiagnosticcode = icdDiagnosticcode;
		this.icdDiagnosticname = icdDiagnosticname;
		this.icdAlias = icdAlias;
		this.icdAlias2 = icdAlias2;
		this.icdScode = icdScode;
		this.icdType = icdType;
		this.icdDiereason = icdDiereason;
		this.icdStandarddate = icdStandarddate;
		this.icdInpgrade = icdInpgrade;
		this.icdDiseasetype = icdDiseasetype;
		this.icdSex = icdSex;
		this.icdIsthirty = icdIsthirty;
		this.icdIsinfect = icdIsinfect;
		this.icdIscancer = icdIscancer;
		this.icdIstcm = icdIstcm;
	}

	// Property accessors

	public String getIcdId() {
		return this.icdId;
	}

	public void setIcdId(String icdId) {
		this.icdId = icdId;
	}

	public String getIcdDiagnosticcode() {
		return this.icdDiagnosticcode;
	}

	public void setIcdDiagnosticcode(String icdDiagnosticcode) {
		this.icdDiagnosticcode = icdDiagnosticcode;
	}

	public String getIcdDiagnosticname() {
		return this.icdDiagnosticname;
	}

	public void setIcdDiagnosticname(String icdDiagnosticname) {
		this.icdDiagnosticname = icdDiagnosticname;
	}

	public String getIcdAlias() {
		return this.icdAlias;
	}

	public void setIcdAlias(String icdAlias) {
		this.icdAlias = icdAlias;
	}

	public String getIcdAlias2() {
		return this.icdAlias2;
	}

	public void setIcdAlias2(String icdAlias2) {
		this.icdAlias2 = icdAlias2;
	}

	public String getIcdScode() {
		return this.icdScode;
	}

	public void setIcdScode(String icdScode) {
		this.icdScode = icdScode;
	}

	public String getIcdType() {
		return this.icdType;
	}

	public void setIcdType(String icdType) {
		this.icdType = icdType;
	}

	public String getIcdDiereason() {
		return this.icdDiereason;
	}

	public void setIcdDiereason(String icdDiereason) {
		this.icdDiereason = icdDiereason;
	}

	public Short getIcdStandarddate() {
		return this.icdStandarddate;
	}

	public void setIcdStandarddate(Short icdStandarddate) {
		this.icdStandarddate = icdStandarddate;
	}

	public String getIcdInpgrade() {
		return this.icdInpgrade;
	}

	public void setIcdInpgrade(String icdInpgrade) {
		this.icdInpgrade = icdInpgrade;
	}

	public String getIcdDiseasetype() {
		return this.icdDiseasetype;
	}

	public void setIcdDiseasetype(String icdDiseasetype) {
		this.icdDiseasetype = icdDiseasetype;
	}

	public String getIcdSex() {
		return this.icdSex;
	}

	public void setIcdSex(String icdSex) {
		this.icdSex = icdSex;
	}

	public String getIcdIsthirty() {
		return this.icdIsthirty;
	}

	public void setIcdIsthirty(String icdIsthirty) {
		this.icdIsthirty = icdIsthirty;
	}

	public String getIcdIsinfect() {
		return this.icdIsinfect;
	}

	public void setIcdIsinfect(String icdIsinfect) {
		this.icdIsinfect = icdIsinfect;
	}

	public String getIcdIscancer() {
		return this.icdIscancer;
	}

	public void setIcdIscancer(String icdIscancer) {
		this.icdIscancer = icdIscancer;
	}

	public String getIcdIstcm() {
		return this.icdIstcm;
	}

	public void setIcdIstcm(String icdIstcm) {
		this.icdIstcm = icdIstcm;
	}

}