package cn.honry.base.bean.model;

/**
 * BiOperationRecord entity. @author MyEclipse Persistence Tools
 */

public class BiOperationRecord implements java.io.Serializable {

	// Fields

	private BiOperationRecordId id;

	// Constructors

	/** default constructor */
	public BiOperationRecord() {
	}

	/** full constructor */
	public BiOperationRecord(BiOperationRecordId id) {
		this.id = id;
	}

	// Property accessors

	public BiOperationRecordId getId() {
		return this.id;
	}

	public void setId(BiOperationRecordId id) {
		this.id = id;
	}

}