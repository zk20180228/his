package cn.honry.inner.statistics.operationNum.vo;

public class InnerOperationNumsVo {

	private String finalDate;//时间
	private Integer nums;//例数
	private String pasource;//来源  住院 门诊
	private String deptName;//科室名称
	private String docName;//医生名称
	private String docCode;//医生code
	private String deptCode;//科室code
	private String opType;//手术类别
	private String opTypeName;//手术类别名称
	private String finalType;//在做，已完成
	private String name;//
	private String district;//院区code
	private String  districtname;//院区name
	
	
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getDistrictname() {
		return districtname;
	}
	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFinalType() {
		return finalType;
	}
	public void setFinalType(String finalType) {
		this.finalType = finalType;
	}
	public String getOpTypeName() {
		return opTypeName;
	}
	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}
	public Integer getNums() {
		return nums;
	}
	public void setNums(Integer nums) {
		this.nums = nums;
	}
	public String getPasource() {
		return pasource;
	}
	public void setPasource(String pasource) {
		this.pasource = pasource;
	}
	
	
}
