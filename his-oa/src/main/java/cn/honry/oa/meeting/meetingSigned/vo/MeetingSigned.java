package cn.honry.oa.meeting.meetingSigned.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>会议签到统计实体，注意:在数据层面上是往签到表中插入一条记录，但是这条记录的内容并不全，只做院长办公室页面新增后的列表显示，当会议结束后，这条记录是没用的</p>
 * @Author: zhangkui
 * @CreateDate: 2017年8月29日 下午6:39:51 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年8月29日 下午6:39:51 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class MeetingSigned implements Serializable{


	private static final long serialVersionUID = -3218348052825020123L;
	private String id;//主键
	private String meet_name;//会议室名称
	private String meeting_id;//会议ID
	private String meeting_name;//会议名称
	private Date meeting_tartTime;//开始时间
	private Date meeting_endTime;//结束时间

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMeet_name() {
		return meet_name;
	}
	public void setMeet_name(String meet_name) {
		this.meet_name = meet_name;
	}
	public String getMeeting_id() {
		return meeting_id;
	}
	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}
	public String getMeeting_name() {
		return meeting_name;
	}
	public void setMeeting_name(String meeting_name) {
		this.meeting_name = meeting_name;
	}
	public Date getMeeting_tartTime() {
		return meeting_tartTime;
	}
	public void setMeeting_tartTime(Date meeting_tartTime) {
		this.meeting_tartTime = meeting_tartTime;
	}
	public Date getMeeting_endTime() {
		return meeting_endTime;
	}
	public void setMeeting_endTime(Date meeting_endTime) {
		this.meeting_endTime = meeting_endTime;
	}

	
}
