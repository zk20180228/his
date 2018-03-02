package cn.honry.base.bean.model;

import java.util.Date;

/**
 * 权限-用户表
 * Date:2013/9/25 15:03
 * Name:sun
 */
public class UserLogin {
	/**唯一编号(主键)**/
	private String id;
	/**用户编号**/
	private String userId;
	/**IP**/
	private String ip;
	/**登录客户端**/
	private String http;
	/**会话编号**/
	private String sessionId;
	/**登录时间**/
	private Date loginTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHttp() {
		return http;
	}
	public void setHttp(String http) {
		this.http = http;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
}
