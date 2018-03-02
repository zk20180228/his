package cn.honry.statistics.deptstat.out_inpatient_work.vo;

import java.io.Serializable;

public class OutInpatientWorkVo implements Serializable{

	private static final long serialVersionUID = 2281339602651213071L;
	
	private String projectName;//项目
	private String num;//人次
	private String beginNum;//前人次，数量
	private String endNum;//后人次，数量
	private String increaseNum;//增减数
	private String increasePercent;//增减%
	/***********下面的字段是mongodb中要使用的字段***************/
	private String dateTime;//时间
	private String flag;//不同模块的标记
	private String vSum;//合计值
	/***********上面的字段是mongodb中要使用的字段***************/
	
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(String beginNum) {
		this.beginNum = beginNum;
	}
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	public String getIncreaseNum() {
		return increaseNum;
	}
	public void setIncreaseNum(String increaseNum) {
		this.increaseNum = increaseNum;
	}
	public String getIncreasePercent() {
		return increasePercent;
	}
	public void setIncreasePercent(String increasePercent) {
		this.increasePercent = increasePercent;
	}
	 
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getvSum() {
		return vSum;
	}
	public void setvSum(String vSum) {
		this.vSum = vSum;
	}
	
	
	
	
	
	

}
