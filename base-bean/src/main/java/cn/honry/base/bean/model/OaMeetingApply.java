package cn.honry.base.bean.model;

import java.sql.Clob;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class OaMeetingApply extends Entity  {
	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年9月19日 下午2:08:19 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年9月19日 下午2:08:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**会议室ID**/
    private String meetId;
    /**会议室名称**/
    private String meetName;
    /**会议名称**/
    private String meetingName;
    /**会议主题**/
    private String meetingTheme;
    /**会议性质**/
    private String meetingNature;
    /**会议室管理员**/
    private String meetingAdmin;
    /**会议纪要员**/
    private String meetingMinutes;
    /**会议描述**/
    private Clob meetingDescribe;
    /**出席人员**/
    private String meetingAttendance;
    /**查看范围(部门)**/
    private String meetingDept;
    /**周期性会议申请:F:否,W:周,M:月**/
    private String meetingPeriodicity;
    /**开始时间(用于存放周期日期)**/
    private Date meetingStart;
    /**结束时间(用于存放周期日期)**/
    private Date meetingEnd;
    /**申请日期(星期)用","隔开**/
    private String meetingApplyweek;
    /**开始时间**/
    private Date meetingStarttime;
    /**结束时间**/
    private Date meetingEndtime;
    /**是否电子邮件提醒T:是,F:否**/
    private String meetingEmail;
    /**是否写入日程安排T:是,F:否**/
    private String meetingSchedule;
    /**是否提醒会议室管理员T:是,F:否**/
    private String meetingRemind;
    /**提醒时间**/
    private String meetingRemindTime;
    /**是否通知出席人员T:是,F:否**/
    private String meetingNotice;
    /**是否提醒查看范围T:是,F:否**/
    private String meetingNoticedept;
    /**提醒设置-小时**/
    private Integer meetingNoticehour;
    /**提醒设置-分钟**/
    private Integer meetingNoticeminute;
    /**提醒设置-次数**/
    private Integer meetingNoticefrequency;
    /**申请人**/
    private String meetingApplicant;
    /**申请人Code**/
    private String meetingApplicantCode;
    /**申请时间**/
    private Date meetingApplicanttime;
    /**审批人**/
    private String meetingApprover;
    /**审批人Code**/
    private String meetingApproverCode;
    /**审批时间**/
    private Date meetingApprovertime;
    /**申请状态0:申请状态,1:审核成功状态,2:申请拒绝状态,3:撤销状态**/
    private Integer meetingApptype;
    
    /**会议描述过渡字段**/
    private String meetingDescribeString;
    private String meetingFile;
    private String meetingFileName;
    /**提前签到时间**/
    private String signBeforeTime;
    
    /**会议内部人员code**/
    private String inSidePersonCode;
    /**查看范围code**/
    private String meetingDeptCode;
    /**会议纪要员Code**/
    private String meetingMinutesCode;
    /**会议室管理员Code**/
    private String meetingAdminCode;
    
    /**分页**/
	private String page;
	private String rows;
	
	
	/**预约的标志**/
	private String appointmentFLag;
	/**会议申请的标志	1--会议申请；2--会议审批；3--我的会议**/
	private String applyFLag;
	/**会议是否结束标志  1--未开始；2--已结束；**/
	private String isEnd;
	/**签到标记：0-成功，1-迟到，8-未到；**/
	private String signStatus;


	//用于回显时间，修改的时候时间多个0
	private String startTime;
	private String endTime;


	
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}
	public String getApplyFLag() {
		return applyFLag;
	}
	public void setApplyFLag(String applyFLag) {
		this.applyFLag = applyFLag;
	}
	public String getAppointmentFLag() {
		return appointmentFLag;
	}
	public void setAppointmentFLag(String appointmentFLag) {
		this.appointmentFLag = appointmentFLag;
	}
	public String getMeetingFile() {
		return meetingFile;
	}
	public void setMeetingFile(String meetingFile) {
		this.meetingFile = meetingFile;
	}
	public String getMeetingFileName() {
		return meetingFileName;
	}
	public void setMeetingFileName(String meetingFileName) {
		this.meetingFileName = meetingFileName;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getMeetId() {
		return meetId;
	}
	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}
	public String getMeetName() {
		return meetName;
	}
	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getMeetingTheme() {
		return meetingTheme;
	}
	public void setMeetingTheme(String meetingTheme) {
		this.meetingTheme = meetingTheme;
	}
	public String getMeetingNature() {
		return meetingNature;
	}
	public void setMeetingNature(String meetingNature) {
		this.meetingNature = meetingNature;
	}
	public String getMeetingAdmin() {
		return meetingAdmin;
	}
	public void setMeetingAdmin(String meetingAdmin) {
		this.meetingAdmin = meetingAdmin;
	}
	public String getMeetingMinutes() {
		return meetingMinutes;
	}
	public void setMeetingMinutes(String meetingMinutes) {
		this.meetingMinutes = meetingMinutes;
	}
	public Clob getMeetingDescribe() {
		return meetingDescribe;
	}
	public void setMeetingDescribe(Clob meetingDescribe) {
		this.meetingDescribe = meetingDescribe;
	}
	public String getMeetingAttendance() {
		return meetingAttendance;
	}
	public void setMeetingAttendance(String meetingAttendance) {
		this.meetingAttendance = meetingAttendance;
	}
	public String getMeetingDept() {
		return meetingDept;
	}
	public void setMeetingDept(String meetingDept) {
		this.meetingDept = meetingDept;
	}
	public String getMeetingPeriodicity() {
		return meetingPeriodicity;
	}
	public void setMeetingPeriodicity(String meetingPeriodicity) {
		this.meetingPeriodicity = meetingPeriodicity;
	}
	public Date getMeetingStart() {
		return meetingStart;
	}
	public void setMeetingStart(Date meetingStart) {
		this.meetingStart = meetingStart;
	}
	public Date getMeetingEnd() {
		return meetingEnd;
	}
	public void setMeetingEnd(Date meetingEnd) {
		this.meetingEnd = meetingEnd;
	}
	public String getMeetingApplyweek() {
		return meetingApplyweek;
	}
	public void setMeetingApplyweek(String meetingApplyweek) {
		this.meetingApplyweek = meetingApplyweek;
	}
	public Date getMeetingStarttime() {
		return meetingStarttime;
	}
	public void setMeetingStarttime(Date meetingStarttime) {
		this.meetingStarttime = meetingStarttime;
	}
	public Date getMeetingEndtime() {
		return meetingEndtime;
	}
	public void setMeetingEndtime(Date meetingEndtime) {
		this.meetingEndtime = meetingEndtime;
	}
	public String getMeetingEmail() {
		return meetingEmail;
	}
	public void setMeetingEmail(String meetingEmail) {
		this.meetingEmail = meetingEmail;
	}
	public String getMeetingSchedule() {
		return meetingSchedule;
	}
	public void setMeetingSchedule(String meetingSchedule) {
		this.meetingSchedule = meetingSchedule;
	}
	public String getMeetingRemind() {
		return meetingRemind;
	}
	public void setMeetingRemind(String meetingRemind) {
		this.meetingRemind = meetingRemind;
	}
	public String getMeetingRemindTime() {
		return meetingRemindTime;
	}
	public void setMeetingRemindTime(String meetingRemindTime) {
		this.meetingRemindTime = meetingRemindTime;
	}
	public String getMeetingNotice() {
		return meetingNotice;
	}
	public void setMeetingNotice(String meetingNotice) {
		this.meetingNotice = meetingNotice;
	}
	public String getMeetingNoticedept() {
		return meetingNoticedept;
	}
	public void setMeetingNoticedept(String meetingNoticedept) {
		this.meetingNoticedept = meetingNoticedept;
	}
	public Integer getMeetingNoticehour() {
		return meetingNoticehour;
	}
	public void setMeetingNoticehour(Integer meetingNoticehour) {
		this.meetingNoticehour = meetingNoticehour;
	}
	public Integer getMeetingNoticeminute() {
		return meetingNoticeminute;
	}
	public void setMeetingNoticeminute(Integer meetingNoticeminute) {
		this.meetingNoticeminute = meetingNoticeminute;
	}
	public Integer getMeetingNoticefrequency() {
		return meetingNoticefrequency;
	}
	public void setMeetingNoticefrequency(Integer meetingNoticefrequency) {
		this.meetingNoticefrequency = meetingNoticefrequency;
	}
	public String getMeetingApplicant() {
		return meetingApplicant;
	}
	public void setMeetingApplicant(String meetingApplicant) {
		this.meetingApplicant = meetingApplicant;
	}
	public Date getMeetingApplicanttime() {
		return meetingApplicanttime;
	}
	public void setMeetingApplicanttime(Date meetingApplicanttime) {
		this.meetingApplicanttime = meetingApplicanttime;
	}
	public String getMeetingApprover() {
		return meetingApprover;
	}
	public void setMeetingApprover(String meetingApprover) {
		this.meetingApprover = meetingApprover;
	}
	public Date getMeetingApprovertime() {
		return meetingApprovertime;
	}
	public void setMeetingApprovertime(Date meetingApprovertime) {
		this.meetingApprovertime = meetingApprovertime;
	}
	public Integer getMeetingApptype() {
		return meetingApptype;
	}
	public void setMeetingApptype(Integer meetingApptype) {
		this.meetingApptype = meetingApptype;
	}
	public String getMeetingDescribeString() {
		return meetingDescribeString;
	}
	public void setMeetingDescribeString(String meetingDescribeString) {
		this.meetingDescribeString = meetingDescribeString;
	}
	public String getInSidePersonCode() {
		return inSidePersonCode;
	}
	public void setInSidePersonCode(String inSidePersonCode) {
		this.inSidePersonCode = inSidePersonCode;
	}
	public String getMeetingDeptCode() {
		return meetingDeptCode;
	}
	public void setMeetingDeptCode(String meetingDeptCode) {
		this.meetingDeptCode = meetingDeptCode;
	}
	public String getMeetingMinutesCode() {
		return meetingMinutesCode;
	}
	public void setMeetingMinutesCode(String meetingMinutesCode) {
		this.meetingMinutesCode = meetingMinutesCode;
	}
	public String getMeetingAdminCode() {
		return meetingAdminCode;
	}
	public void setMeetingAdminCode(String meetingAdminCode) {
		this.meetingAdminCode = meetingAdminCode;
	}
	public String getSignBeforeTime() {
		return signBeforeTime;
	}
	public void setSignBeforeTime(String signBeforeTime) {
		this.signBeforeTime = signBeforeTime;
	}
	public String getMeetingApplicantCode() {
		return meetingApplicantCode;
	}
	public void setMeetingApplicantCode(String meetingApplicantCode) {
		this.meetingApplicantCode = meetingApplicantCode;
	}
	public String getMeetingApproverCode() {
		return meetingApproverCode;
	}
	public void setMeetingApproverCode(String meetingApproverCode) {
		this.meetingApproverCode = meetingApproverCode;
	}


	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
