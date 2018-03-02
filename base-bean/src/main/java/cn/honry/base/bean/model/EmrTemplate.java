package cn.honry.base.bean.model;

import java.sql.Clob;

import cn.honry.base.bean.business.Entity;

/**
 * AbstractTEmrTemplate entity provides the base persistence definition of the
 * TEmrTemplate entity. @author MyEclipse Persistence Tools
 */

public class EmrTemplate extends Entity{

	// Fields
	/**
	 * 病历编码
	 */
	private String tempCode;
	/**
	 * 病历名称
	 */
	private String tempName;
	private String pingyin;
	private String wb;
	private String inputcode;
	/**
	 * 病历分类:0通用1科室2个人
	 */
	private Integer tempType;
	/**
	 * 病历内容
	 */
	private Clob tempContent;
	/**
	 * 病历文件（XML）
	 */
	private String tempFile;
	/**
	 * 病历对应的诊断编号
	 */
	private String tempDiagid;
	/**
	 * 病历对应的诊断名称
	 */
	private String tempDiag;
	/**
	 * 病历类型
	 */	
	private String tempErtype;
	/**
	 * 病历属性
	 */	
	private String tempAttrs;
	/**
	 * 病历书写类型
	 */	
	private Integer tempWritetype;
	/**
	 * 病历审核标志 0：未审核；1：通过审核；2：未通过审核
	 */
	private Integer tempChkflg;
	/**
	 * 病历使用标志 0：未使用；1：正在使用；
	 */
	private Integer tempUseflg;
	
	//与数据库无关字段
	/**
	 * 字符串类型内容
	 */	
	private String strContent;
	/**  分页  **/
	private String page;
	private String rows;
	

	// Constructors

	/** default constructor */
	public EmrTemplate() {
	}

	// Property accessors

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
	public String getTempCode() {
		return this.tempCode;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}

	public String getTempName() {
		return this.tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
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

	public Integer getTempType() {
		return this.tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public Clob getTempContent() {
		return this.tempContent;
	}

	public void setTempContent(Clob tempContent) {
		this.tempContent = tempContent;
	}

	public String getTempFile() {
		return this.tempFile;
	}

	public void setTempFile(String tempFile) {
		this.tempFile = tempFile;
	}

	public String getTempDiagid() {
		return this.tempDiagid;
	}

	public void setTempDiagid(String tempDiagid) {
		this.tempDiagid = tempDiagid;
	}

	public String getTempDiag() {
		return this.tempDiag;
	}

	public void setTempDiag(String tempDiag) {
		this.tempDiag = tempDiag;
	}

	public String getTempErtype() {
		return this.tempErtype;
	}

	public void setTempErtype(String tempErtype) {
		this.tempErtype = tempErtype;
	}

	public String getTempAttrs() {
		return this.tempAttrs;
	}

	public void setTempAttrs(String tempAttrs) {
		this.tempAttrs = tempAttrs;
	}

	public Integer getTempWritetype() {
		return this.tempWritetype;
	}

	public void setTempWritetype(Integer tempWritetype) {
		this.tempWritetype = tempWritetype;
	}

	public Integer getTempChkflg() {
		return this.tempChkflg;
	}

	public void setTempChkflg(Integer tempChkflg) {
		this.tempChkflg = tempChkflg;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}

	public Integer getTempUseflg() {
		return tempUseflg;
	}

	public void setTempUseflg(Integer tempUseflg) {
		this.tempUseflg = tempUseflg;
	}

}