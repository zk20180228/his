package cn.honry.statistics.sys.reservationStatistics.vo;

import java.util.List;

public class ReservationStatistics {
	private String id;//id
	private String deptName;//部门名字
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
	
	public ReservationStatistics(String deptName,List<Integer> list) {
		super();
		this.deptName=deptName;
		this.commonNumber=list.get(0);
		this.countAllInfo=list.get(1);
		this.countDoctorVisits=list.get(2);
		this.firstVisit=list.get(3);
		this.furtherConsultation=list.get(4);
		this.furtherConsultationRe=list.get(5);
		this.firstVisitRe=list.get(6);
		this.netBooking=list.get(7);
		this.numberExpert=list.get(8);
		this.numberExpertRe=list.get(9);
		this.phoneBooking=list.get(10);
		this.total=list.get(11);
		
	}
	
	public ReservationStatistics() {
		super();
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
	
	

}
