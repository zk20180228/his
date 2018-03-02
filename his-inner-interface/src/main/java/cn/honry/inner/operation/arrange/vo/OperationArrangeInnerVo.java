package cn.honry.inner.operation.arrange.vo;
/***
 * 手术安排统计vo
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.Date;

public class OperationArrangeInnerVo {
	
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
	private String aneWay;
	
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
	private String zanesthesia;
	/**
	 * 麻醉助手
	 */
	private String fanesthesia;
	
	/**
	 *手术状态 
	 */
	private int opStates;
	
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
	public String getAneWay() {
		return aneWay;
	}
	public void setAneWay(String aneWay) {
		this.aneWay = aneWay;
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
	public String getZanesthesia() {
		return zanesthesia;
	}
	public void setZanesthesia(String zanesthesia) {
		this.zanesthesia = zanesthesia;
	}
	public String getFanesthesia() {
		return fanesthesia;
	}
	public void setFanesthesia(String fanesthesia) {
		this.fanesthesia = fanesthesia;
	}
	public int getOpStates() {
		return opStates;
	}
	public void setOpStates(int opStates) {
		this.opStates = opStates;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	
	
	
}
