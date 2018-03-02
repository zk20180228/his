package cn.honry.statistics.sys.reportForms.vo;


public class DoctorWorkloadStatistics {
	
	
	private String name;//医生姓名
	private String nameId;//医生姓名
	private String dept;//部门名字
	private String deptId;//部门名字
	private Integer bookNo=0;//预约数
	private Integer regNo=0;//挂号数
	private Integer visNo=0;//就诊数
	private Integer helpLoc=0;//咨询（本地）
	private Integer helpNet=0;//咨询（外地）
	private Integer helpNo=0;//咨询总数
	private Integer telBook=0;//电话预约
	private Integer telReg=0;//电话挂号
	private Integer validHelpLoc=0;//有效咨询(本地)
	private Integer validHelpNet=0;//有效咨询(外地)
	private Integer validNo=0;//咨询总数
	private Integer netBook=0;//网络预约
	private String bookRat;//预约率
	private Integer netReg=0;//网络挂号
	private Integer arrRat=0;//到诊率
	private Integer validTot=0;//咨询总数
	private Integer bookTot=0;//预约总数
	private Integer regTot=0;//预约总数
	private Integer arrTot=0;//就诊总数
	public String getNameId() {
		return nameId;
	}
	public void setNameId(String nameId) {
		this.nameId = nameId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public Integer getBookNo() {
		return bookNo;
	}
	public void setBookNo(Integer bookNo) {
		this.bookNo = bookNo;
	}
	public Integer getRegNo() {
		return regNo;
	}
	public void setRegNo(Integer regNo) {
		this.regNo = regNo;
	}
	public Integer getVisNo() {
		return visNo;
	}
	public void setVisNo(Integer visNo) {
		this.visNo = visNo;
	}
	public Integer getHelpLoc() {
		return helpLoc;
	}
	public void setHelpLoc(Integer helpLoc) {
		this.helpLoc = helpLoc;
	}
	public Integer getHelpNet() {
		return helpNet;
	}
	public void setHelpNet(Integer helpNet) {
		this.helpNet = helpNet;
	}
	public Integer getHelpNo() {
		return helpNo;
	}
	public void setHelpNo(Integer helpNo) {
		this.helpNo = helpNo;
	}
	public Integer getTelBook() {
		return telBook;
	}
	public void setTelBook(Integer telBook) {
		this.telBook = telBook;
	}
	public Integer getTelReg() {
		return telReg;
	}
	public void setTelReg(Integer telReg) {
		this.telReg = telReg;
	}
	public Integer getValidHelpLoc() {
		return validHelpLoc;
	}
	public void setValidHelpLoc(Integer validHelpLoc) {
		this.validHelpLoc = validHelpLoc;
	}
	public Integer getValidHelpNet() {
		return validHelpNet;
	}
	public void setValidHelpNet(Integer validHelpNet) {
		this.validHelpNet = validHelpNet;
	}
	public Integer getValidNo() {
		return validNo;
	}
	public void setValidNo(Integer validNo) {
		this.validNo = validNo;
	}
	public Integer getNetBook() {
		return netBook;
	}
	public void setNetBook(Integer netBook) {
		this.netBook = netBook;
	}
	
	public String getBookRat() {
		return bookRat;
	}
	public void setBookRat(String bookRat) {
		this.bookRat = bookRat;
	}
	public Integer getNetReg() {
		return netReg;
	}
	public void setNetReg(Integer netReg) {
		this.netReg = netReg;
	}
	public Integer getArrRat() {
		return arrRat;
	}
	public void setArrRat(Integer arrRat) {
		this.arrRat = arrRat;
	}
	public Integer getValidTot() {
		return validTot;
	}
	public void setValidTot(Integer validTot) {
		this.validTot = validTot;
	}
	public Integer getBookTot() {
		return bookTot;
	}
	public void setBookTot(Integer bookTot) {
		this.bookTot = bookTot;
	}
	public Integer getRegTot() {
		return regTot;
	}
	public void setRegTot(Integer regTot) {
		this.regTot = regTot;
	}
	public Integer getArrTot() {
		return arrTot;
	}
	public void setArrTot(Integer arrTot) {
		this.arrTot = arrTot;
	}
	

}
