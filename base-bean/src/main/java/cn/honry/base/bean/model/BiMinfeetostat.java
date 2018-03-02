package cn.honry.base.bean.model;

/**
 * BiMinfeetostat entity. @author MyEclipse Persistence Tools
 */

public class BiMinfeetostat implements java.io.Serializable {

	// Fields

	private BiMinfeetostatId id;

	// Constructors

	/** default constructor */
	public BiMinfeetostat() {
	}

	/** full constructor */
	public BiMinfeetostat(BiMinfeetostatId id) {
		this.id = id;
	}

	// Property accessors

	public BiMinfeetostatId getId() {
		return this.id;
	}

	public void setId(BiMinfeetostatId id) {
		this.id = id;
	}

}