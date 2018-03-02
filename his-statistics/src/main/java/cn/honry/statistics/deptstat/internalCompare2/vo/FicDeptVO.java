package cn.honry.statistics.deptstat.internalCompare2.vo;

public class FicDeptVO {
	private String id;
	private String name;
	/**类型**/
	private String type;
	/**部门科室code**/
	private String deptCode;
	/**部门科室那么**/
	private String deptName;
	/**虚拟科室code**/
	private String ficCode;
	/**虚拟科室那么**/
	private String ficName;
	private String wb;
	private String pinyin;
	private String inputCode;
	/**院区**/
	private Integer district;
	/**
	 * 院区名称
	 */
	private String districtName;
	
	
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getFicCode() {
		return ficCode;
	}
	public void setFicCode(String ficCode) {
		this.ficCode = ficCode;
	}
	public String getFicName() {
		return ficName;
	}
	public void setFicName(String ficName) {
		this.ficName = ficName;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
