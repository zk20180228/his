package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 
 * 
 * <p>服务管理日志Vo </p>
 * @Author: XCL
 * @CreateDate: 2017年9月28日 上午9:37:40 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月28日 上午9:37:40 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class LogServiceVo {
	/**主键**/
	private String id;
	/**服务代码**/
	private String serviceCode;
	/**ip**/
	private String ip;
	/**最新心跳时间**/
	private Date heartNewTime;
	/**心跳频率**/
	private String heartRate;
	/**创建时间**/
	private Date createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getHeartNewTime() {
		return heartNewTime;
	}
	public void setHeartNewTime(Date heartNewTime) {
		this.heartNewTime = heartNewTime;
	}
	public String getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(String heartRate) {
		this.heartRate = heartRate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
