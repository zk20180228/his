package cn.honry.base.bean.model;

import java.util.Date;

/**
 * BiOperationItem entity. @author MyEclipse Persistence Tools
 */

public class BiOperationItem implements java.io.Serializable {

	// Fields

	private BiOperationItemId id;
	private String inpatientNo;
	private String cardNo;
	private String typeCode;
	private String itemId;
	private String itemName;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public BiOperationItem() {
	}

	/** minimal constructor */
	public BiOperationItem(BiOperationItemId id, String inpatientNo,
			String cardNo, String itemName) {
		this.id = id;
		this.inpatientNo = inpatientNo;
		this.cardNo = cardNo;
		this.itemName = itemName;
	}

	/** full constructor */
	public BiOperationItem(BiOperationItemId id, String inpatientNo,
			String cardNo, String typeCode, String itemId, String itemName,
			Date createTime, Date updateTime) {
		this.id = id;
		this.inpatientNo = inpatientNo;
		this.cardNo = cardNo;
		this.typeCode = typeCode;
		this.itemId = itemId;
		this.itemName = itemName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	// Property accessors

	public BiOperationItemId getId() {
		return this.id;
	}

	public void setId(BiOperationItemId id) {
		this.id = id;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}