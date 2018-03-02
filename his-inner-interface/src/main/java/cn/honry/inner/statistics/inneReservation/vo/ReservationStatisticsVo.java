package cn.honry.inner.statistics.inneReservation.vo;

/**
 * 
 * 
 * <p>预约统计预处理 </p>
 * @Author: XCL
 * @CreateDate: 2017年8月12日 上午10:07:33 
 * @Modifier: XCL
 * @ModifyDate: 2017年8月12日 上午10:07:33 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class ReservationStatisticsVo {
	private String id;//id
	private String deptName;//部门名字
	private String deptCode;//部门code
	private Integer countAllInfo=0;//全部号源合计
	private Integer commonNumber=0;//普通号
	private Integer numberExpert=0;//专家号
	private Integer countDoctorVisits=0;//就诊人次合计
	private Integer firstVisit=0;//初诊
	private Integer furtherConsultation=0;//复诊
	private Integer total=0;//合计
	private Integer commonNumberRe=0;//普通号
	private Integer numberExpertRe=0;//专家号
	private Integer windowBooking=0;//窗口预约
	private Integer phoneBooking=0;//电话预约
	private Integer netBooking=0;//网络预约
	private Integer otherBooking=0;//其他方式预约
	private Integer firstVisitRe=0;//初诊预约
	private Integer furtherConsultationRe=0;//复诊预约
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getCountAllInfo() {
		return countAllInfo;
	}
	public void setCountAllInfo(Integer countAllInfo) {
		this.countAllInfo = countAllInfo;
	}
	public Integer getCommonNumber() {
		return commonNumber;
	}
	public void setCommonNumber(Integer commonNumber) {
		this.commonNumber = commonNumber;
	}
	public Integer getNumberExpert() {
		return numberExpert;
	}
	public void setNumberExpert(Integer numberExpert) {
		this.numberExpert = numberExpert;
	}
	public Integer getCountDoctorVisits() {
		return countDoctorVisits;
	}
	public void setCountDoctorVisits(Integer countDoctorVisits) {
		this.countDoctorVisits = countDoctorVisits;
	}
	public Integer getFirstVisit() {
		return firstVisit;
	}
	public void setFirstVisit(Integer firstVisit) {
		this.firstVisit = firstVisit;
	}
	public Integer getFurtherConsultation() {
		return furtherConsultation;
	}
	public void setFurtherConsultation(Integer furtherConsultation) {
		this.furtherConsultation = furtherConsultation;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getCommonNumberRe() {
		return commonNumberRe;
	}
	public void setCommonNumberRe(Integer commonNumberRe) {
		this.commonNumberRe = commonNumberRe;
	}
	public Integer getNumberExpertRe() {
		return numberExpertRe;
	}
	public void setNumberExpertRe(Integer numberExpertRe) {
		this.numberExpertRe = numberExpertRe;
	}
	public Integer getWindowBooking() {
		return windowBooking;
	}
	public void setWindowBooking(Integer windowBooking) {
		this.windowBooking = windowBooking;
	}
	public Integer getPhoneBooking() {
		return phoneBooking;
	}
	public void setPhoneBooking(Integer phoneBooking) {
		this.phoneBooking = phoneBooking;
	}
	public Integer getNetBooking() {
		return netBooking;
	}
	public void setNetBooking(Integer netBooking) {
		this.netBooking = netBooking;
	}
	public Integer getOtherBooking() {
		return otherBooking;
	}
	public void setOtherBooking(Integer otherBooking) {
		this.otherBooking = otherBooking;
	}
	public Integer getFirstVisitRe() {
		return firstVisitRe;
	}
	public void setFirstVisitRe(Integer firstVisitRe) {
		this.firstVisitRe = firstVisitRe;
	}
	public Integer getFurtherConsultationRe() {
		return furtherConsultationRe;
	}
	public void setFurtherConsultationRe(Integer furtherConsultationRe) {
		this.furtherConsultationRe = furtherConsultationRe;
	}
	@Override
	public String toString() {
		return "ReservationStatisticsVo [id=" + id + ", deptName=" + deptName
				+ ", countAllInfo=" + countAllInfo + ", commonNumber="
				+ commonNumber + ", numberExpert=" + numberExpert
				+ ", countDoctorVisits=" + countDoctorVisits + ", firstVisit="
				+ firstVisit + ", furtherConsultation=" + furtherConsultation
				+ ", total=" + total + ", commonNumberRe=" + commonNumberRe
				+ ", numberExpertRe=" + numberExpertRe + ", windowBooking="
				+ windowBooking + ", phoneBooking=" + phoneBooking
				+ ", netBooking=" + netBooking + ", otherBooking="
				+ otherBooking + ", firstVisitRe=" + firstVisitRe
				+ ", furtherConsultationRe=" + furtherConsultationRe + "]";
	}
}
