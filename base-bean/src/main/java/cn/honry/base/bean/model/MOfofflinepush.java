package cn.honry.base.bean.model;

import java.util.Date;


/**
 * OA系统首页组件实体类
 * @author zpty
 * Date:2017/7/15 15:00
 */
public class MOfofflinepush {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**用户姓名**/
	private String userName;
	/**用户账户**/
	private String userAccount;
	/**消息类型**/
	private String  type;
	/**设备码**/
	private String deviceCode;
	/**消息内容**/
	private String content;
	/**创建时间**/
	private Date createTime;
	/**推送时间**/
	private Date pushTime;
	/**消息状态**/
	private Integer status;
	/**消息数量**/
	private Integer num;
	/**备注**/
	private String backup;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getPushTime() {
		return pushTime;
	}
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBackup() {
		return backup;
	}
	public void setBackup(String backup) {
		this.backup = backup;
	}
	
	
	
}
