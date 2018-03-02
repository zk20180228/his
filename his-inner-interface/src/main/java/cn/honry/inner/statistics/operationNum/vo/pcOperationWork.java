package cn.honry.inner.statistics.operationNum.vo;


public class pcOperationWork {
	private String deptCode;
	private String deptName;
	private String docCode;
	private String docName;
	private String ownDeptCode;//所属科室code
	private String ownDeptName;//所属科室name
	private Integer opskindzq;//择期
	private Integer opskingpt;//普通
	private Integer opskingjz;//急诊
	private Integer opskinggr;//感染
	private Integer anesmz;//麻醉
	private Integer anesbmz;//不麻醉
	private Integer bigoperation;//大手术
	private Integer middleoperation;//中型手术
	private Integer smalloperation;//小手术
	private Integer consolept;//手术台普通
	private Integer consolejt;//加台
	private Integer consoledt;//电台
	private Integer consolejjt;//加急台
	private Integer operationNums;//手术例数
	
	public String getOwnDeptCode() {
		return ownDeptCode;
	}
	public void setOwnDeptCode(String ownDeptCode) {
		this.ownDeptCode = ownDeptCode;
	}
	public String getOwnDeptName() {
		return ownDeptName;
	}
	public void setOwnDeptName(String ownDeptName) {
		this.ownDeptName = ownDeptName;
	}
	public Integer getOperationNums() {
		return operationNums;
	}
	public void setOperationNums(Integer operationNums) {
		this.operationNums = operationNums;
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
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Integer getOpskindzq() {
		return opskindzq;
	}
	public void setOpskindzq(Integer opskindzq) {
		this.opskindzq = opskindzq;
	}
	public Integer getOpskingpt() {
		return opskingpt;
	}
	public void setOpskingpt(Integer opskingpt) {
		this.opskingpt = opskingpt;
	}
	public Integer getOpskingjz() {
		return opskingjz;
	}
	public void setOpskingjz(Integer opskingjz) {
		this.opskingjz = opskingjz;
	}
	public Integer getOpskinggr() {
		return opskinggr;
	}
	public void setOpskinggr(Integer opskinggr) {
		this.opskinggr = opskinggr;
	}
	public Integer getAnesmz() {
		return anesmz;
	}
	public void setAnesmz(Integer anesmz) {
		this.anesmz = anesmz;
	}
	public Integer getAnesbmz() {
		return anesbmz;
	}
	public void setAnesbmz(Integer anesbmz) {
		this.anesbmz = anesbmz;
	}
	public Integer getBigoperation() {
		return bigoperation;
	}
	public void setBigoperation(Integer bigoperation) {
		this.bigoperation = bigoperation;
	}
	public Integer getMiddleoperation() {
		return middleoperation;
	}
	public void setMiddleoperation(Integer middleoperation) {
		this.middleoperation = middleoperation;
	}
	public Integer getSmalloperation() {
		return smalloperation;
	}
	public void setSmalloperation(Integer smalloperation) {
		this.smalloperation = smalloperation;
	}
	public Integer getConsolept() {
		return consolept;
	}
	public void setConsolept(Integer consolept) {
		this.consolept = consolept;
	}
	public Integer getConsolejt() {
		return consolejt;
	}
	public void setConsolejt(Integer consolejt) {
		this.consolejt = consolejt;
	}
	public Integer getConsoledt() {
		return consoledt;
	}
	public void setConsoledt(Integer consoledt) {
		this.consoledt = consoledt;
	}
	public Integer getConsolejjt() {
		return consolejjt;
	}
	public void setConsolejjt(Integer consolejjt) {
		this.consolejjt = consolejjt;
	}
	

}
