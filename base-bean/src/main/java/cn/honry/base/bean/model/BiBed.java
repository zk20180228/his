package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiBed entity. @author MyEclipse Persistence Tools
 */

public class BiBed implements java.io.Serializable {

	// Fields

	private String id;
	private String bedNo;
	private String wardCode;
	private String wardName;
	private String deptCode;
	private String deptName;
	private String roomCode;
	private String roomName;
	private String bedStatusCode;
	private String bedStatusName;
	private String createDate;
	private Date updateDate;

	// Constructors

	/** default constructor */
	public BiBed() {
	}

	/** full constructor */
	public BiBed(String bedNo, String wardCode, String wardName,
			String deptCode, String deptName, String roomCode, String roomName,
			String bedStatusCode, String bedStatusName, String createDate,
			Date updateDate) {
		this.bedNo = bedNo;
		this.wardCode = wardCode;
		this.wardName = wardName;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.roomCode = roomCode;
		this.roomName = roomName;
		this.bedStatusCode = bedStatusCode;
		this.bedStatusName = bedStatusName;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBedNo() {
		return this.bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getWardCode() {
		return this.wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getWardName() {
		return this.wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRoomCode() {
		return this.roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomName() {
		return this.roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getBedStatusCode() {
		return this.bedStatusCode;
	}

	public void setBedStatusCode(String bedStatusCode) {
		this.bedStatusCode = bedStatusCode;
	}

	public String getBedStatusName() {
		return this.bedStatusName;
	}

	public void setBedStatusName(String bedStatusName) {
		this.bedStatusName = bedStatusName;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}