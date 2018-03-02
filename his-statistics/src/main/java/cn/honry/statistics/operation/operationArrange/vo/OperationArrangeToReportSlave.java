package cn.honry.statistics.operation.operationArrange.vo;
/***
 * 手术安排统计-用于报表打印子类
 * @Description:
 * @author: hedong
 * @CreateDate: 2017年3月1日 
 * @version 1.0
 */
import java.util.Date;

public class OperationArrangeToReportSlave {
	
	/**
	 * 预约时间
	 */
	private Date preDate; 
	
	/**
	 * 住院科室
	 */
	private String inDept;
	
	/**
	 * 住院号
	 */
	private String patientNo;
	
	/**
	 * 床号
	 */
	private String bedNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String sex;
	
	/**
	 * 年龄
	 */
	private String age;
	
	/**
	 * 手术间
	 */
	private String roomId;
	
	/**
	 * 手术ID
	 */
	private String opId;
	/**
	 * 手术名称
	 */
	private String itemName;
	/**
	 * 麻醉方式
	 */
	private String aneType;
	
	/**
	 * 手术医生
	 */
	private String opDoctor;
	
	/**
	 * 助手 thelper
	 */
	private String thelper;
	/**
	 * 洗手护士
	 */
	private String wash;
	/**
	 * 巡回护士
	 */
	private String tour;
	/**
	 * 麻醉医师
	 */
	private String anaeDocd;
	/**
	 * 麻醉助手
	 */
	private String anaeHelper;
	
	/**
	 *手术状态 
	 */
	private int opStates;
	
	/**
	 *手术科室 
	 */
	private String execDept;
	/**
	 *状态 
	 */
	private String status;
	
	/**
	 * 助手医生
	 */
	private String zsDocs;
	/**
	 * 助手医生
	 */
	private String xhs;
	/**
	 * 助手医生
	 */
	private String xss;
	/**
	 * 手术名称
	 */
	private String mcs;
	public Date getPreDate() {
		return preDate;
	}
	public void setPreDate(Date preDate) {
		this.preDate = preDate;
	}
	public String getInDept() {
		return inDept;
	}
	public void setInDept(String inDept) {
		this.inDept = inDept;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
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
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAneType() {
		return aneType;
	}
	public void setAneType(String aneType) {
		this.aneType = aneType;
	}
	public String getOpDoctor() {
		return opDoctor;
	}
	public void setOpDoctor(String opDoctor) {
		this.opDoctor = opDoctor;
	}
	public String getThelper() {
		return thelper;
	}
	public void setThelper(String thelper) {
		this.thelper = thelper;
	}
	public String getWash() {
		return wash;
	}
	public void setWash(String wash) {
		this.wash = wash;
	}
	public String getTour() {
		return tour;
	}
	public void setTour(String tour) {
		this.tour = tour;
	}
	public String getAnaeDocd() {
		return anaeDocd;
	}
	public void setAnaeDocd(String anaeDocd) {
		this.anaeDocd = anaeDocd;
	}
	public String getAnaeHelper() {
		return anaeHelper;
	}
	public void setAnaeHelper(String anaeHelper) {
		this.anaeHelper = anaeHelper;
	}
	public int getOpStates() {
		return opStates;
	}
	public void setOpStates(int opStates) {
		this.opStates = opStates;
	}
	public String getExecDept() {
		return execDept;
	}
	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getZsDocs() {
		return zsDocs;
	}
	public void setZsDocs(String zsDocs) {
		this.zsDocs = zsDocs;
	}
	public String getXhs() {
		return xhs;
	}
	public void setXhs(String xhs) {
		this.xhs = xhs;
	}
	public String getXss() {
		return xss;
	}
	public void setXss(String xss) {
		this.xss = xss;
	}
	public String getMcs() {
		return mcs;
	}
	public void setMcs(String mcs) {
		this.mcs = mcs;
	}
	
	
	
	
	
}
