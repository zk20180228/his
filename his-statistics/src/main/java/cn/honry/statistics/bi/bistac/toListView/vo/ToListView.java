package cn.honry.statistics.bi.bistac.toListView.vo;

public class ToListView {
	private String index;//指标
	private Integer passengers;//人次
	private Integer addsLastM;//与上月比较增加量
	private String addPerLastM;//与上月比较增加百分比
	private Integer addsLastY;//与上年比较增加量
	private String addPerLastY;//与上年比较增加百分比
	private String ratio;//完成月目标比值
	
	private String adDate;
	private String amDate;
	private String ayDate;
	
	/**  以六为单位的统计  **/
	private String nowTime;       //当天时间
	private String nowMJNum;      //当天门急诊人数
	private String nowTimeB1;     //前一天时间   
	private String nowMJNumB1;    //前一天门急诊人数
	private String nowTimeB2;     //前二天时间   
	private String nowMJNumB2;    //前二天门急诊人数
	private String nowTimeB3;     //前三天时间   
	private String nowMJNumB3;    //前三天门急诊人数
	private String nowTimeB4;     //前四天时间   
	private String nowMJNumB4;    //前四天门急诊人数
	private String nowTimeB5;     //前五天时间   
	private String nowMJNumB5;    //前五天门急诊人数
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public Integer getPassengers() {
		return passengers;
	}
	public void setPassengers(Integer passengers) {
		this.passengers = passengers;
	}
	public Integer getAddsLastM() {
		return addsLastM;
	}
	public void setAddsLastM(Integer addsLastM) {
		this.addsLastM = addsLastM;
	}
	public String getAddPerLastM() {
		return addPerLastM;
	}
	public void setAddPerLastM(String addPerLastM) {
		this.addPerLastM = addPerLastM;
	}
	public Integer getAddsLastY() {
		return addsLastY;
	}
	public void setAddsLastY(Integer addsLastY) {
		this.addsLastY = addsLastY;
	}
	public String getAddPerLastY() {
		return addPerLastY;
	}
	public void setAddPerLastY(String addPerLastY) {
		this.addPerLastY = addPerLastY;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getAdDate() {
		return adDate;
	}
	public void setAdDate(String adDate) {
		this.adDate = adDate;
	}
	public String getAmDate() {
		return amDate;
	}
	public void setAmDate(String amDate) {
		this.amDate = amDate;
	}
	public String getAyDate() {
		return ayDate;
	}
	public void setAyDate(String ayDate) {
		this.ayDate = ayDate;
	}
	public String getNowTime() {
		return nowTime;
	}
	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	public String getNowMJNum() {
		return nowMJNum;
	}
	public void setNowMJNum(String nowMJNum) {
		this.nowMJNum = nowMJNum;
	}
	public String getNowTimeB1() {
		return nowTimeB1;
	}
	public void setNowTimeB1(String nowTimeB1) {
		this.nowTimeB1 = nowTimeB1;
	}
	public String getNowMJNumB1() {
		return nowMJNumB1;
	}
	public void setNowMJNumB1(String nowMJNumB1) {
		this.nowMJNumB1 = nowMJNumB1;
	}
	public String getNowTimeB2() {
		return nowTimeB2;
	}
	public void setNowTimeB2(String nowTimeB2) {
		this.nowTimeB2 = nowTimeB2;
	}
	public String getNowMJNumB2() {
		return nowMJNumB2;
	}
	public void setNowMJNumB2(String nowMJNumB2) {
		this.nowMJNumB2 = nowMJNumB2;
	}
	public String getNowTimeB3() {
		return nowTimeB3;
	}
	public void setNowTimeB3(String nowTimeB3) {
		this.nowTimeB3 = nowTimeB3;
	}
	public String getNowMJNumB3() {
		return nowMJNumB3;
	}
	public void setNowMJNumB3(String nowMJNumB3) {
		this.nowMJNumB3 = nowMJNumB3;
	}
	public String getNowTimeB4() {
		return nowTimeB4;
	}
	public void setNowTimeB4(String nowTimeB4) {
		this.nowTimeB4 = nowTimeB4;
	}
	public String getNowMJNumB4() {
		return nowMJNumB4;
	}
	public void setNowMJNumB4(String nowMJNumB4) {
		this.nowMJNumB4 = nowMJNumB4;
	}
	public String getNowTimeB5() {
		return nowTimeB5;
	}
	public void setNowTimeB5(String nowTimeB5) {
		this.nowTimeB5 = nowTimeB5;
	}
	public String getNowMJNumB5() {
		return nowMJNumB5;
	}
	public void setNowMJNumB5(String nowMJNumB5) {
		this.nowMJNumB5 = nowMJNumB5;
	}
	
}
