package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class BusinessOproom extends Entity {
	/**房间号,唯一的一个房间号，例如201 */
	 private String  roomId;
	 /**房间名称*/
	 private String roomName;
	 /**助记码*/
	 private String inputCode;
	 /**科室代码*/
	 private String deptCode;
	 /**
	  * 是否有效
	  * 1有效/0无效
	  */
	 private Integer validFlag;
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	

	 
}
