package cn.honry.base.bean.model;
/**
 * 统计-组织机构代码
 */
public class BiBaseOrganization implements java.io.Serializable {

	// Fields
	private String orgCode;
	private String orgName;
	private String orgParentCode;
	private String orgParentName;
	private String orgKindCode;
	private String orgKind;
	private String orgMark;
	private String extFlag1;
	private String extFlag2;
	
	private Integer idKey;//代理主键
	private String id;//自然主键

	// Constructors

	/** default constructor */
	public BiBaseOrganization() {
	}

	/** minimal constructor */
	public BiBaseOrganization(String orgName) {
		this.orgName = orgName;
	}

	/** full constructor */
	public BiBaseOrganization(String orgName, String orgParentCode,
			String orgParentName, String orgKindCode, String orgKind,
			String orgMark, String extFlag1, String extFlag2) {
		this.orgName = orgName;
		this.orgParentCode = orgParentCode;
		this.orgParentName = orgParentName;
		this.orgKindCode = orgKindCode;
		this.orgKind = orgKind;
		this.orgMark = orgMark;
		this.extFlag1 = extFlag1;
		this.extFlag2 = extFlag2;
	}

	// Property accessors

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgParentCode() {
		return this.orgParentCode;
	}

	public void setOrgParentCode(String orgParentCode) {
		this.orgParentCode = orgParentCode;
	}

	public String getOrgParentName() {
		return this.orgParentName;
	}

	public void setOrgParentName(String orgParentName) {
		this.orgParentName = orgParentName;
	}

	public String getOrgKindCode() {
		return this.orgKindCode;
	}

	public void setOrgKindCode(String orgKindCode) {
		this.orgKindCode = orgKindCode;
	}

	public String getOrgKind() {
		return this.orgKind;
	}

	public void setOrgKind(String orgKind) {
		this.orgKind = orgKind;
	}

	public String getOrgMark() {
		return this.orgMark;
	}

	public void setOrgMark(String orgMark) {
		this.orgMark = orgMark;
	}

	public String getExtFlag1() {
		return this.extFlag1;
	}

	public void setExtFlag1(String extFlag1) {
		this.extFlag1 = extFlag1;
	}

	public String getExtFlag2() {
		return this.extFlag2;
	}

	public void setExtFlag2(String extFlag2) {
		this.extFlag2 = extFlag2;
	}

	public Integer getIdKey() {
		return idKey;
	}

	public void setIdKey(Integer idKey) {
		this.idKey = idKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}