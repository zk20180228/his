package cn.honry.oa.meeting.meetingSigned.vo;

import java.io.Serializable;

/**
 * 
 * <p>会议签到统计vo </p>
 * @Author: zhangkui
 * @CreateDate: 2017年8月29日 下午6:39:51 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年8月29日 下午6:39:51 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class MeetingSignedVo implements Serializable{

	
	private static final long serialVersionUID = -8071195996760184450L;
	private String rid;//分页用的rownum，没意义
	
	private String id;//会议签到Id
	private String meetingName;//会议名称
	private Long totalNum;//总计
	private Long onTimeNum;//准时签到
	private Long isLateNum;//迟到
	private Long noComeNum;//未到
	private Long tempComeNum;//临时参加
	private String applyer;//申请人
	private String attendPersons;//出席人员
	
	private String meetingStartTime;//会议的开始时间
	private String meetingEndTime;//会议的结束时间
	
	private String meetingRoomName;//会议室的名字
	private String meetingStatusFlag;//会议的状态,在这里我用，0代表已结束，1代表进行中，2代表未开始
	
	
	
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public Long getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}
	public Long getOnTimeNum() {
		return onTimeNum;
	}
	public void setOnTimeNum(Long onTimeNum) {
		this.onTimeNum = onTimeNum;
	}
	public Long getIsLateNum() {
		return isLateNum;
	}
	public void setIsLateNum(Long isLateNum) {
		this.isLateNum = isLateNum;
	}
	public Long getNoComeNum() {
		return noComeNum;
	}
	public void setNoComeNum(Long noComeNum) {
		this.noComeNum = noComeNum;
	}
	public Long getTempComeNum() {
		return tempComeNum;
	}
	public void setTempComeNum(Long tempComeNum) {
		this.tempComeNum = tempComeNum;
	}
	public String getMeetingStartTime() {
		return meetingStartTime;
	}
	public void setMeetingStartTime(String meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}
	public String getMeetingEndTime() {
		return meetingEndTime;
	}
	public void setMeetingEndTime(String meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}
	public String getMeetingStatusFlag() {
		return meetingStatusFlag;
	}
	public void setMeetingStatusFlag(String meetingStatusFlag) {
		this.meetingStatusFlag = meetingStatusFlag;
	}
	public String getApplyer() {
		return applyer;
	}
	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}
	public String getAttendPersons() {
		return attendPersons;
	}
	public void setAttendPersons(String attendPersons) {
		this.attendPersons = attendPersons;
	}
	
	
	
	

	
	
	
}
