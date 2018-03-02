package cn.honry.outpatient.preregister.vo;



/**  
 *  
 * @className：EmpScheduleVo
 * @Description：  
 * @Author：wujiao
 * @CreateDate：2015-11-19 下午06:22:16  
 * @Modifier：wujiao
 * @ModifyDate：2015-11-19 下午06:22:16
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class EmpScheduleVo {
	private String id;//专家id
	private String empName;//挂号专家
	private String scheduleId;//排班编号
	private String gradeName;//挂号级别
	private String deptId;//挂号科室
	private String deptName;//挂号科室
	private Integer midday;//午别
	private Integer preL=0;//预约总数;
	private Integer netL=0;//网络预约总数;
	private Integer nowL=0;//现场预约总数;
	private Integer phoneL=0;//电话预约总数;
	private Integer phoneA=0;//预约电话已预约数量;
	private Integer netA=0;//预约网络已预约数量;
	private Integer nowA=0;//预约现场已预约数量;
	private Integer preA=0;//已预约总数;
	private Integer phonesy=0;//电话预约剩余数量;
	private Integer netsy=0;//网络预约剩余数量;
	private Integer nowsy=0;//现场预约剩余数量;
	private String starttime;//开始时间
	private String enttime;//结束时间
	/**新加字段2016-02-17 lsy*/
	private Integer isNet;//是否为网络挂号(0-否,1-是)
	private Integer isPhone;//电话挂号(0-否,1-是)
	private String preDate;//预约挂号日期(2017-02-16)格式
	private String preId;//预约编号
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEnttime() {
		return enttime;
	}
	public void setEnttime(String enttime) {
		this.enttime = enttime;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getNowsy() {
		return nowsy;
	}
	public void setNowsy(Integer nowsy) {
		this.nowsy = nowsy;
	}
	public Integer getNowL() {
		return nowL;
	}
	public void setNowL(Integer nowL) {
		this.nowL = nowL;
	}
	public Integer getNowA() {
		return nowA;
	}
	public void setNowA(Integer nowA) {
		this.nowA = nowA;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public Integer getMidday() {
		return midday;
	}
	public void setMidday(Integer midday) {
		this.midday = midday;
	}
	public Integer getPreL() {
		return preL;
	}
	public void setPreL(Integer preL) {
		this.preL = preL;
	}
	public Integer getNetL() {
		return netL;
	}
	public void setNetL(Integer netL) {
		this.netL = netL;
	}
	public Integer getPhoneL() {
		return phoneL;
	}
	public void setPhoneL(Integer phoneL) {
		this.phoneL = phoneL;
	}
	public Integer getPhoneA() {
		return phoneA;
	}
	public void setPhoneA(Integer phoneA) {
		this.phoneA = phoneA;
	}
	public Integer getNetA() {
		return netA;
	}
	public void setNetA(Integer netA) {
		this.netA = netA;
	}
	public Integer getPreA() {
		return preA;
	}
	public void setPreA(Integer preA) {
		this.preA = preA;
	}
	public Integer getPhonesy() {
		return phonesy;
	}
	public void setPhonesy(Integer phonesy) {
		this.phonesy = phonesy;
	}
	public Integer getNetsy() {
		return netsy;
	}
	public void setNetsy(Integer netsy) {
		this.netsy = netsy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIsNet() {
		return isNet;
	}
	public void setIsNet(Integer isNet) {
		this.isNet = isNet;
	}
	public Integer getIsPhone() {
		return isPhone;
	}
	public void setIsPhone(Integer isPhone) {
		this.isPhone = isPhone;
	}
	
	public String getPreDate() {
		return preDate;
	}
	public void setPreDate(String preDate) {
		this.preDate = preDate;
	}
	public String getPreId() {
		return preId;
	}
	public void setPreId(String preId) {
		this.preId = preId;
	}
	
}
