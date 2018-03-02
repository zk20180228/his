package cn.honry.base.bean.model;

/**
 * BiRescue entity. @author MyEclipse Persistence Tools
 */

public class BiRescue implements java.io.Serializable {

	// Fields

	private BiRescueId id;

	// Constructors

	/** default constructor */
	public BiRescue() {
	}

	/** full constructor */
	public BiRescue(BiRescueId id) {
		this.id = id;
	}

	// Property accessors

	public BiRescueId getId() {
		return this.id;
	}

	public void setId(BiRescueId id) {
		this.id = id;
	}

}