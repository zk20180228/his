package cn.honry.statistics.bi.bistac.outpatientStac.vo;

public class BusinessContractunitVo {
	private String encode;//代码
	private String name;//名称
	private int total;//人数
	private String totalPer;//人数所占比例
	/**
	 * 急诊总数,出院数
	 */
	private int jiNum;
	/**
	 * 门诊总数,住院数
	 */
	private int mzNum;
	/**
	 * 急诊百分比,出院百分比
	 */
	private String jiPer;
	/**
	 * 门诊百分比,住院百分比
	 */
	private String mzPer;
	
	public String getJiPer() {
		return jiPer;
	}
	public void setJiPer(String jiPer) {
		this.jiPer = jiPer;
	}
	public String getMzPer() {
		return mzPer;
	}
	public void setMzPer(String mzPer) {
		this.mzPer = mzPer;
	}
	public int getJiNum() {
		return jiNum;
	}
	public void setJiNum(int jiNum) {
		this.jiNum = jiNum;
	}
	public int getMzNum() {
		return mzNum;
	}
	public void setMzNum(int mzNum) {
		this.mzNum = mzNum;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getTotalPer() {
		return totalPer;
	}
	public void setTotalPer(String totalPer) {
		this.totalPer = totalPer;
	}
}
