package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MSchedule extends Entity{
	/** 日程编号-主键**/
	private String  schedule_codes;
	/** 标题**/
	private String schedule_titile;
	/** 是否全天（1：是，0：否）**/
	private Integer all_day_flg;
	/** 预约开始时间-全天预约时只精确到年月日，非全天预约时精确到分**/
	private Date schedule_start_time;
	/** 预约结束时间-全天预约时只精确到年月日，非全天预约时精确到分**/
	private Date schedule_end_time;
	/** 提醒时间差：提前预约开始时间多长时间开始提醒日程提醒**/
	private Integer schedule_time_minus;
	/** 时间单位-m:分钟，h:小时，d:天，w:周**/
	private String schedule_time_unit;
	/** 提醒时间**/
	private Date schedule_time;
	/** 备注**/
	private String schedule_remark;
	/** 用户编号**/
	private String user_id;
	/**用于全天自定义时的 时：分 的记录 如 23:15 **/
	private String time_minus_hh_mm;
	/** 是否自定义 0：否 1：是**/
	private String is_zdy;
	/**0（代表时间当天09:00）,1,2....存放默认非自定义时间标记，提醒标记**/
	private String schedule_flg;
	/** 数据库没有字段**/
	private String cur_year;
	private String account;
	
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTime_minus_hh_mm() {
		return time_minus_hh_mm;
	}
	public void setTime_minus_hh_mm(String time_minus_hh_mm) {
		this.time_minus_hh_mm = time_minus_hh_mm;
	}
	public String getIs_zdy() {
		return is_zdy;
	}
	public void setIs_zdy(String is_zdy) {
		this.is_zdy = is_zdy;
	}
	public String getSchedule_flg() {
		return schedule_flg;
	}
	public void setSchedule_flg(String schedule_flg) {
		this.schedule_flg = schedule_flg;
	}
	public String getCur_year() {
		return cur_year;
	}
	public void setCur_year(String cur_year) {
		this.cur_year = cur_year;
	}
	public String getSchedule_codes() {
		return schedule_codes;
	}
	public void setSchedule_codes(String schedule_codes) {
		this.schedule_codes = schedule_codes;
	}
	
	public String getSchedule_titile() {
		return schedule_titile;
	}
	public void setSchedule_titile(String schedule_titile) {
		this.schedule_titile = schedule_titile;
	}
	public Integer getAll_day_flg() {
		return all_day_flg;
	}
	public void setAll_day_flg(Integer all_day_flg) {
		this.all_day_flg = all_day_flg;
	}
	public Date getSchedule_start_time() {
		return schedule_start_time;
	}
	public void setSchedule_start_time(Date schedule_start_time) {
		this.schedule_start_time = schedule_start_time;
	}
	public Date getSchedule_end_time() {
		return schedule_end_time;
	}
	public void setSchedule_end_time(Date schedule_end_time) {
		this.schedule_end_time = schedule_end_time;
	}
	public Integer getSchedule_time_minus() {
		return schedule_time_minus;
	}
	public void setSchedule_time_minus(Integer schedule_time_minus) {
		this.schedule_time_minus = schedule_time_minus;
	}
	public String getSchedule_time_unit() {
		return schedule_time_unit;
	}
	public void setSchedule_time_unit(String schedule_time_unit) {
		this.schedule_time_unit = schedule_time_unit;
	}
	public Date getSchedule_time() {
		return schedule_time;
	}
	public void setSchedule_time(Date schedule_time) {
		this.schedule_time = schedule_time;
	}
	public String getSchedule_remark() {
		return schedule_remark;
	}
	public void setSchedule_remark(String schedule_remark) {
		this.schedule_remark = schedule_remark;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
