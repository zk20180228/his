package cn.honry.base.bean.model;

import java.util.Date;


/**
 * 离线管理实体类
 * @author zxl
 * Date:2017/7/15 15:00
 */
public class MOfoffline {
	private static final long serialVersionUID = 1L;
	/**用户账户**/
	private String userName;
	/**消息id**/
	private Integer messageId;
	/**创建时间**/
	private Date creationDate;
	/**消息长度**/
	private Integer messageSize;
	/**消息内容**/
	private String stanza;
	/**用户姓名**/
	private String uname;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getMessageId() {
		return messageId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	public Integer getMessageSize() {
		return messageSize;
	}
	public void setMessageSize(Integer messageSize) {
		this.messageSize = messageSize;
	}
	public String getStanza() {
		return stanza;
	}
	public void setStanza(String stanza) {
		this.stanza = stanza;
	}
	
}
