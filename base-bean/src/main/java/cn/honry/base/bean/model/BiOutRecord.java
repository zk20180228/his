package cn.honry.base.bean.model;

/**
 * BiOutRecord entity. @author MyEclipse Persistence Tools
 */

public class BiOutRecord implements java.io.Serializable {

	// Fields

	private BiOutRecordId id;

	// Constructors

	/** default constructor */
	public BiOutRecord() {
	}

	/** full constructor */
	public BiOutRecord(BiOutRecordId id) {
		this.id = id;
	}

	// Property accessors

	public BiOutRecordId getId() {
		return this.id;
	}

	public void setId(BiOutRecordId id) {
		this.id = id;
	}

}