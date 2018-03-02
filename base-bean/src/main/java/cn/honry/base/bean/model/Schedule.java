package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class Schedule extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**id**/
//	private String  id;
	/**标题**/
	private String  title;
	/**是否全天**/
	private Integer dayFlg;
	/**预约开始时间**/
	private Date    start;
	/**预约结束时间**/
	private Date    end;
	/**提醒时间差**/
	private Integer timeMinus;
	/**时间单位**/
	private String  timeUnit;
	/**提醒时间**/
	private Date    time;
	/**备注**/
	private String  remark;
	/**用户编号**/
	private String  userId;
	/**全天自定义时分**/
	private String  hhmm;
	/**是否自定义**/
	private String  isZDY;
	/**提醒标记**/
	private String  scheduleFlg;
	/**提醒标记**/
	private Integer  isFinish;
	
	public Integer getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}
	private String backgroundColor;
	private String textColor;
	private Boolean  allDay;
	private Boolean  editable;
	
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getDayFlg() {
		return dayFlg;
	}
	public void setDayFlg(Integer dayFlg) {
		this.dayFlg = dayFlg;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Boolean getAllDay() {
		return allDay;
	}
	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}
	public Integer getTimeMinus() {
		return timeMinus;
	}
	public void setTimeMinus(Integer timeMinus) {
		this.timeMinus = timeMinus;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHhmm() {
		return hhmm;
	}
	public void setHhmm(String hhmm) {
		this.hhmm = hhmm;
	}
	public String getIsZDY() {
		return isZDY;
	}
	public void setIsZDY(String isZDY) {
		this.isZDY = isZDY;
	}
	public String getScheduleFlg() {
		return scheduleFlg;
	}
	public void setScheduleFlg(String scheduleFlg) {
		this.scheduleFlg = scheduleFlg;
	}
	
}
