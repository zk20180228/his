package cn.honry.base.bean.model;

import java.util.Date;


/**
 * 服务管理实体
 * @Author: wangshujuan
 * @CreateDate: 2017年9月19日 下午4:09:43 
 */

public class ServiceManagement implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String code;//服务编码
	private String name;//服务名称
	private int type;//服务类型  0同步服务1接口服务
	private String ip;//服务器ip
	private String port;//占用端口
	private int heartRate;//心跳频率
	private String heartUnit;//心跳频率单位
	private Date heartNewtime;//最新心跳时间
	private int state;//状态   0正常 1暂停 2停用
	private String move;//操作
	private Integer masterprePare;//1主;2备
	private String remarks;//备注
	private String system;
	
	public Integer getMasterprePare() {
		return masterprePare;
	}
	public void setMasterprePare(Integer masterprePare) {
		this.masterprePare = masterprePare;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}
	public String getHeartUnit() {
		return heartUnit;
	}
	public void setHeartUnit(String heartUnit) {
		this.heartUnit = heartUnit;
	}
	public Date getHeartNewtime() {
		return heartNewtime;
	}
	public void setHeartNewtime(Date heartNewtime) {
		this.heartNewtime = heartNewtime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMove() {
		return move;
	}
	public void setMove(String move) {
		this.move = move;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}