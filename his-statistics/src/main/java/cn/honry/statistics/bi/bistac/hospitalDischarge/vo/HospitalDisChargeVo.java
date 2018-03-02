package cn.honry.statistics.bi.bistac.hospitalDischarge.vo;
/**
 * 
 * 
 * <p>入出院人次统计vo </p>
 * @Author: XCL
 * @CreateDate: 2017年11月1日 下午5:51:44 
 * @Modifier: XCL
 * @ModifyDate: 2017年11月1日 下午5:51:44 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class HospitalDisChargeVo {
	private Integer inHost=0;//入院
	private Integer outHost=0;//出院
	private String operDate;//操作日期
	
	public String getOperDate() {
		return operDate;
	}
	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}
	public Integer getInHost() {
		return inHost;
	}
	public void setInHost(Integer inHost) {
		this.inHost = inHost;
	}
	public Integer getOutHost() {
		return outHost;
	}
	public void setOutHost(Integer outHost) {
		this.outHost = outHost;
	}
	@Override
	public String toString() {
		return "HospitalDisChargeVo [inHost=" + inHost + ", outHost=" + outHost
				+ ", operDate=" + operDate + "]";
	}
	
}
