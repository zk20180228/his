package cn.honry.statistics.bi.bistac.hospitalDischarge.vo;
/**
 * 住院出院人次VO
 * @author conglin
 *
 */
public class HospitalDisVo {
	private String date;//时间
	private String stat;// 住院(IN) 出院(OUT)表识
	private Integer num;//人次
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "HospitalDisVo [date=" + date + ", stat=" + stat + ", num="
				+ num + "]";
	}
	
}
