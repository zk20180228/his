package cn.honry.base.bean.model;

import java.util.Date;
/** 
* @Description: 登录日志  
*  
*/
public class LoginLog {
	/**唯一编号(主键)**/
	private String id;
	/**用户编号**/
	private String userId;
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
