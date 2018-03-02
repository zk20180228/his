package cn.honry.statistics.operation.operationArrange.vo;
/***
 * 手术安排统计vo
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.Date;
import java.util.List;

import cn.honry.inner.operation.record.vo.OpNameVo;
import cn.honry.inner.operation.record.vo.OperationUserVo;

public class OperationArrangeVo {
	
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
	//手术科室
	private String execDept;
	/**
	 *状态 
	 */
	private String status;
	
	/**
	 * 助手医生
	 */
	private List<OperationUserVo> zsDocList;
	/**
	 * 助手医生
	 */
	private List<OperationUserVo> xhlist;
	/**
	 * 助手医生
	 */
	private List<OperationUserVo> xslist;
	/**
	 * 手术名称
	 */
	private List<OpNameVo> mclist;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<OpNameVo> getMclist() {
		return mclist;
	}
	public void setMclist(List<OpNameVo> mclist) {
		this.mclist = mclist;
	}
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getExecDept() {
		return execDept;
	}
	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}
	public List<OperationUserVo> getZsDocList() {
		return zsDocList;
	}
	public void setZsDocList(List<OperationUserVo> zsDocList) {
		this.zsDocList = zsDocList;
	}
	public List<OperationUserVo> getXhlist() {
		return xhlist;
	}
	public void setXhlist(List<OperationUserVo> xhlist) {
		this.xhlist = xhlist;
	}
	public List<OperationUserVo> getXslist() {
		return xslist;
	}
	public void setXslist(List<OperationUserVo> xslist) {
		this.xslist = xslist;
	}
	
	
}
