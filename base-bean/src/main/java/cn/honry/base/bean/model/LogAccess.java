package cn.honry.base.bean.model;

import java.util.Date;


/**
 * 日志查询实体
 * @Author: wangshujuan
 * @CreateDate: 2017年9月19日 下午4:09:43 
 */

public class LogAccess implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String serverCode;//服务代码
	private String interCode;//接口代码
	private String interName;//接口名称
	private String vendorCode;//厂商代码
	private String interPara;//传递参数
	private String accessIp;//客户端IP
	private Date accessTime;//调用时间
	private String authInfo;//认证信息
	private Integer result;//调用结果 0成功 1失败
	private String errorInfo;//失败原因
	private Date createtime;//创建时间	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServerCode() {
		return serverCode;
	}
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	public String getInterCode() {
		return interCode;
	}
	public void setInterCode(String interCode) {
		this.interCode = interCode;
	}
	public String getInterName() {
		return interName;
	}
	public void setInterName(String interName) {
		this.interName = interName;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getInterPara() {
		return interPara;
	}
	public void setInterPara(String interPara) {
		this.interPara = interPara;
	}
	public String getAccessIp() {
		return accessIp;
	}
	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}
	public String getAuthInfo() {
		return authInfo;
	}
	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public Date getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
}