package cn.honry.inner.statistics.inOutPatient.vo;
/**
 * 
 * 
 * <p>住出院人次统计预处理vo </p>
 * @Author: XCL
 * @CreateDate: 2017年8月3日 下午3:58:21 
 * @Modifier: XCL
 * @ModifyDate: 2017年8月3日 下午3:58:21 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class InOutPatient {
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
		return "InOutPatient [date=" + date + ", stat=" + stat + ", num=" + num
				+ "]";
	}
	
}
