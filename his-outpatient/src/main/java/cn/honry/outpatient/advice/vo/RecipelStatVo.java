package cn.honry.outpatient.advice.vo;



public class RecipelStatVo{
	
	/**处方号**/
	private String recipeNo;
	/**表名**/
	private String tab;
	/**患者姓名**/
	private String name;
	/**性别**/
	private String sex;
	/**年龄**/
	private String age;
	/**病历号**/
	private String recordNo;
	/**发票号**/
	private String invoiceNo;
	/**配药台**/
	private String disTable;
	/**配药人**/
	private String disUser;
	/**配药时间**/
	private String disTime;
	/**发药台**/
	private String medTable;
	/**发药人**/
	private String medUser;
	/**发药时间**/
	private String medTime;
	/**开方医生**/
	private String squareDoc;
	
	/**getters and setters**/
	
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getDisTable() {
		return disTable;
	}
	public void setDisTable(String disTable) {
		this.disTable = disTable;
	}
	public String getDisUser() {
		return disUser;
	}
	public void setDisUser(String disUser) {
		this.disUser = disUser;
	}
	public String getMedTable() {
		return medTable;
	}
	public void setMedTable(String medTable) {
		this.medTable = medTable;
	}
	public String getMedUser() {
		return medUser;
	}
	public void setMedUser(String medUser) {
		this.medUser = medUser;
	}
	public String getSquareDoc() {
		return squareDoc;
	}
	public void setSquareDoc(String squareDoc) {
		this.squareDoc = squareDoc;
	}
	public String getDisTime() {
		return disTime;
	}
	public void setDisTime(String disTime) {
		this.disTime = disTime;
	}
	public String getMedTime() {
		return medTime;
	}
	public void setMedTime(String medTime) {
		this.medTime = medTime;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
}
